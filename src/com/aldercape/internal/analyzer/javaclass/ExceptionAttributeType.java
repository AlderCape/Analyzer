package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ExceptionAttributeType extends AttributeTypeAdapter {

	private Set<PackageInfo> exceptions = new HashSet<>();
	private Set<ClassInfo> exceptionClasses = new HashSet<>();

	public ExceptionAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numExceptions = in.readUnsignedShort();
		for (int i = 0; i < numExceptions; i++) {
			int classConstantIndex = in.readUnsignedShort();
			String className = new TypeParser(builder).nextObjectFromIndex(classConstantIndex);
			exceptions.add(new PackageInfo(className.substring(0, className.lastIndexOf('.'))));
			exceptionClasses.add(ClassRepository.getClass(className));
		}
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return exceptions;
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return exceptionClasses;
	}

}
