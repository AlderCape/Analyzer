package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class UndefinedAttributeType implements AttributeType {

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.emptySet();
	}

}
