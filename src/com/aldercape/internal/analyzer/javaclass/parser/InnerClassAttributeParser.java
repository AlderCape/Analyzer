package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.InnerClassAttributeType;
import com.aldercape.internal.analyzer.javaclass.JavaClassBuilder;

public class InnerClassAttributeParser implements AttributeTypeParser {

	private JavaClassBuilder builder;

	public InnerClassAttributeParser(JavaClassBuilder builder) {
		this.builder = builder;
	}

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numClasses = in.readUnsignedShort();
		ClassInfo outerClass = null;
		for (int i = 0; i < numClasses; i++) {
			int innerClassInfo = in.readUnsignedShort();
			int outerClassInfo = in.readUnsignedShort();
			int innerName = in.readUnsignedShort();
			int accessFlags = in.readUnsignedShort();
			if (outerClassInfo > 0) {
				outerClass = ClassRepository.getClass(builder.getConstant(outerClassInfo).getName(builder.getConstants()).replace('/', '.'));
			} else {
				String innerClassName = builder.getConstant(innerClassInfo).getName(builder.getConstants());
				outerClass = ClassRepository.getClass(innerClassName.substring(0, innerClassName.indexOf('$')).replace('/', '.'));
			}
		}

		return new InnerClassAttributeType(outerClass);
	}

}
