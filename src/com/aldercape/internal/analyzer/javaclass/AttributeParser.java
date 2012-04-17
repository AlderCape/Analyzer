package com.aldercape.internal.analyzer.javaclass;

import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.AttributeType;

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
			if (type.isAnnotation()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
				attrType = new AnnotationAttributeType(values, builder);
			} else if (type.isException()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
				attrType = new ExceptionAttributeType(values, builder);
			} else if (type.isInnerClass()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
				attrType = new InnerClassAttributeType(values, builder);
			} else if (type.isCode()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
				attrType = new CodeAttributeType(values, builder);
			} else if (type.isLocalVariableTable()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
				attrType = new LocalVariableTableAttributeType(values, builder);
			} else {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
			}
		}
		return attrType;
	}

	protected AttributeInfo createAttributes(DataInputStream in) throws IOException {
		AttributeParser attributeParser = this;
		int attributeCount = in.readUnsignedShort();
		AttributeInfo attributeInfo = new AttributeInfo();
		for (int j = 0; j < attributeCount; j++) {
			attributeInfo.add(attributeParser.parseAttribute(in));
		}
		return attributeInfo;
	}

}
