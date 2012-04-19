package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.javaclass.UndefinedAttributeType;

public class NullAttributeTypeParser implements AttributeTypeParser {

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		return new UndefinedAttributeType();
	}

}
