package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.PackageInfo;

public class UndefinedAttributeType implements AttributeType {

	@Override
	public void consume(byte[] values, JavaClassBuilder builder) {
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.emptySet();
	}

}
