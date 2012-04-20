package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;
import com.aldercape.internal.analyzer.javaclass.ExceptionAttributeType;

public class ExceptionAttributeParser implements AttributeTypeParser {

	private ConstantPoolInfo constantPool;

	public ExceptionAttributeParser(ConstantPoolInfo constantPool) {
		this.constantPool = constantPool;
	}

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		Set<PackageInfo> exceptions = new HashSet<>();
		Set<ClassInfo> exceptionClasses = new HashSet<>();
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numExceptions = in.readUnsignedShort();
		for (int i = 0; i < numExceptions; i++) {
			int classConstantIndex = in.readUnsignedShort();
			String className = new TypeParser(constantPool).nextObjectFromIndex(classConstantIndex);
			exceptions.add(new PackageInfo(className.substring(0, className.lastIndexOf('.'))));
			exceptionClasses.add(ClassRepository.getClass(className));
		}

		return new ExceptionAttributeType(exceptionClasses, exceptions);
	}

}
