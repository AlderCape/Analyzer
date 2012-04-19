package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;

public interface AttributeTypeParser {

	public AttributeType parse(byte[] values) throws IOException;
}
