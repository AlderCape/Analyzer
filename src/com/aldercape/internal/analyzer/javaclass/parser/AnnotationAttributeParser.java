package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.AnnotationAttributeType;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;

public class AnnotationAttributeParser implements AttributeTypeParser {

	private ConstantPoolInfo constantPool;

	public AnnotationAttributeParser(ConstantPoolInfo constantPool) {
		this.constantPool = constantPool;
	}

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		String className = null;
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numAnnotations = in.readUnsignedShort();
		for (int i = 0; i < numAnnotations; i++) {
			int annTypeIndex = in.readUnsignedShort();
			TypeParser parser = new TypeParser(constantPool);
			className = parser.parseTypeFromIndex(annTypeIndex);
			int numNameValuePairs = in.readUnsignedShort();
		}
		return new AnnotationAttributeType(ClassRepository.getClass(className));
	}

}
