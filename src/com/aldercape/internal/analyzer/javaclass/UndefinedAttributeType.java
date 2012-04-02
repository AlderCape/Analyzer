package com.aldercape.internal.analyzer.javaclass;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class UndefinedAttributeType implements AttributeType {

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.emptySet();
	}

	@Override
	public Collection<? extends ClassInfo> getDependentClasses() {
		return Collections.emptySet();
	}

}
