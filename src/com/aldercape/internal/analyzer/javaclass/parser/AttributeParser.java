package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.Constant;
import com.aldercape.internal.analyzer.javaclass.Constant.ConstantAttributeType;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;
import com.aldercape.internal.analyzer.javaclass.UndefinedAttributeType;

public class AttributeParser {

	private ConstantPoolInfo constantPool;
	private Map<ConstantAttributeType, AttributeTypeParser> parsers = new HashMap<>();

	public AttributeParser(ConstantPoolInfo constantPool, TypeParser typeParser, ClassRepository repository) {
		this.constantPool = constantPool;

		parsers.put(ConstantAttributeType.Unkown, new NullAttributeTypeParser());
		parsers.put(ConstantAttributeType.Code, new CodeAttributeParser(this));
		parsers.put(ConstantAttributeType.LocalVariableTable, new LocalVariableTableParser(typeParser, repository));
		parsers.put(ConstantAttributeType.InnerClasses, new InnerClassAttributeParser(constantPool, repository));
		parsers.put(ConstantAttributeType.Exceptions, new ExceptionAttributeParser(typeParser, repository));
		parsers.put(ConstantAttributeType.RuntimeVisibleAnnotations, new AnnotationAttributeParser(typeParser, repository));
		parsers.put(ConstantAttributeType.SourceFile, new SourceFileAttributeTypeParser());
		parsers.put(ConstantAttributeType.LineNumberTable, new LineNumberTableAttributeTypeParser());
		parsers.put(ConstantAttributeType.Signature, new SignatureAttributeTypeParser());
		parsers.put(ConstantAttributeType.LocalVariableTypeTable, new LocalVariableTypeTableAttributeTypeParser());
		parsers.put(ConstantAttributeType.StackMapTable, new StackMapTableAttributeTypeParser());
		parsers.put(ConstantAttributeType.EnclosingMethod, new EnclosingMethodAttributeTypeParser());
		parsers.put(ConstantAttributeType.ConstantValue, new ConstantValueAttributeTypeParser());
	}

	private AttributeType parseAttribute(DataInputStream in) throws IOException {
		AttributeType attrType = new UndefinedAttributeType();
		int nameIndex = in.readUnsignedShort();
		if (nameIndex != 0) {
			Constant type = constantPool.get(nameIndex);
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
		return parsers.get(type.getAttributeType());
	}
}
