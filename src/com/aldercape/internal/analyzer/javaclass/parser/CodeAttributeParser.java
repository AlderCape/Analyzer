package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.javaclass.CodeAttributeType;

public class CodeAttributeParser implements AttributeTypeParser {

	private AttributeParser attributeParser;

	public CodeAttributeParser(AttributeParser attributeParser) {
		this.attributeParser = attributeParser;
	}

	public AttributeParser getAttributeParser() {
		return attributeParser;
	}

	@Override
	public CodeAttributeType parse(byte[] values) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int maxStack = in.readUnsignedShort();
		int maxLocals = in.readUnsignedShort();
		int codeLength = in.readInt();
		byte[] code = new byte[codeLength];
		in.read(code);
		int exceptionTableLength = in.readUnsignedShort();
		for (int i = 0; i < exceptionTableLength; i++) {
			int startPc = in.readUnsignedShort();
			int endPc = in.readUnsignedShort();
			int handlerPc = in.readUnsignedShort();
			int catchType = in.readUnsignedShort();
		}
		return new CodeAttributeType(getAttributeParser().createAttributes(in));

	}

}
