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
import com.aldercape.internal.analyzer.javaclass.ExceptionAttributeType;

public class ExceptionAttributeParser implements AttributeTypeParser {

	private TypeParser typeParser;
	private ClassRepository repository;

	public ExceptionAttributeParser(TypeParser typeParser, ClassRepository repository) {
		this.typeParser = typeParser;
		this.repository = repository;
	}

	@Override
	public AttributeType parse(byte[] values) throws IOException {
		Set<PackageInfo> exceptions = new HashSet<>();
		Set<ClassInfo> exceptionClasses = new HashSet<>();
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numExceptions = in.readUnsignedShort();
		for (int i = 0; i < numExceptions; i++) {
			int classConstantIndex = in.readUnsignedShort();
			String className = typeParser.nextObjectFromIndex(classConstantIndex);
			exceptions.add(new PackageInfo(className.substring(0, className.lastIndexOf('.'))));
			exceptionClasses.add(repository.getClass(className));
		}

		return new ExceptionAttributeType(exceptionClasses, exceptions);
	}

}
