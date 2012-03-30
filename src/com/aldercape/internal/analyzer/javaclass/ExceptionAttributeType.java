package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ExceptionAttributeType implements AttributeType {

	private Set<PackageInfo> exceptions = new HashSet<>();

	public ExceptionAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numExceptions = in.readUnsignedShort();
		for (int i = 0; i < numExceptions; i++) {
			Constant classConstant = builder.getConstant(in.readUnsignedShort());
			String className = JavaClassParser.nextTypeFromDescriptor("L" + classConstant.getName(builder) + ";");
			exceptions.add(new PackageInfo(className.substring(0, className.lastIndexOf('.'))));
		}
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return exceptions;
	}

}
