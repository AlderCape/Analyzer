package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;
import com.aldercape.internal.analyzer.javaclass.InnerClassAttributeType;

public class InnerClassAttributeParser implements AttributeTypeParser {

	private ConstantPoolInfo constantPool;
	private ClassRepository repository;

	public InnerClassAttributeParser(ConstantPoolInfo constantPool, ClassRepository repository) {
		this.constantPool = constantPool;
		this.repository = repository;
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
				outerClass = repository.getClass(constantPool.get(outerClassInfo).getName(constantPool).replace('/', '.'));
			} else {
				String innerClassName = constantPool.get(innerClassInfo).getName(constantPool);
				outerClass = repository.getClass(innerClassName.substring(0, innerClassName.indexOf('$')).replace('/', '.'));
			}
		}

		return new InnerClassAttributeType(outerClass);
	}
}
