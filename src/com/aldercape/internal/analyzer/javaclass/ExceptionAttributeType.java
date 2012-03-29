package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ExceptionAttributeType implements AttributeType {

	public ExceptionAttributeType(byte[] values, JavaClassBuilder builder) {
		System.out.println("Exception");
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.emptySet();
	}

}
