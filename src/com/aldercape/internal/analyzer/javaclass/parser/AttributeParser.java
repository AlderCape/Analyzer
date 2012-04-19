package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.javaclass.Constant;
import com.aldercape.internal.analyzer.javaclass.JavaClassBuilder;
import com.aldercape.internal.analyzer.javaclass.UndefinedAttributeType;

public class AttributeParser {

	private JavaClassBuilder builder;

	public AttributeParser(JavaClassBuilder builder) {
		this.builder = builder;
	}

	private AttributeType parseAttribute(DataInputStream in) throws IOException {
		AttributeType attrType = new UndefinedAttributeType();
		int nameIndex = in.readUnsignedShort();
		if (nameIndex != 0) {
			Constant type = builder.getConstant(nameIndex);
			int attrLength = in.readInt();
			byte[] values = new byte[attrLength];
			for (int b = 0; b < attrLength; b++) {
				values[b] = in.readByte();
			}
			AttributeTypeParser parser = getAttributeTypeParser(type);
			attrType = parser.parse(values);
		}
		return attrType;
	}

	public AttributeInfo createAttributes(DataInputStream in) throws IOException {
		AttributeParser attributeParser = this;
		int attributeCount = in.readUnsignedShort();
		AttributeInfo attributeInfo = new AttributeInfo();
		for (int j = 0; j < attributeCount; j++) {
			attributeInfo.add(attributeParser.parseAttribute(in));
		}
		return attributeInfo;
	}

	public AttributeTypeParser getAttributeTypeParser(Constant type) {
		if (type.isCode()) {
			return new CodeAttributeParser(new AttributeParser(builder), builder);
		} else if (type.isLocalVariableTable()) {
			return new LocalVariableTableParser(new TypeParser(builder));
		} else if (type.isInnerClass()) {
			return new InnerClassAttributeParser(builder);
		} else if (type.isException()) {
			return new ExceptionAttributeParser(builder);
		} else if (type.isAnnotation()) {
			return new AnnotationAttributeParser(builder);
		}
		return new NullAttributeTypeParser();
	}
}
