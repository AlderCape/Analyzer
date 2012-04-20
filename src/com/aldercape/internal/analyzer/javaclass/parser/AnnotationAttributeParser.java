package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.AnnotationAttributeType;

public class AnnotationAttributeParser implements AttributeTypeParser {

	private TypeParser typeParser;

	public AnnotationAttributeParser(TypeParser typeParser) {
		this.typeParser = typeParser;
	}

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		String className = null;
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numAnnotations = in.readUnsignedShort();
		for (int i = 0; i < numAnnotations; i++) {
			int annTypeIndex = in.readUnsignedShort();
			className = typeParser.parseTypeFromIndex(annTypeIndex);
			int numNameValuePairs = in.readUnsignedShort();
		}
		return new AnnotationAttributeType(ClassRepository.getClass(className));
	}

}
