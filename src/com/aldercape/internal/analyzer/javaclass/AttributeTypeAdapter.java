package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

abstract class AttributeTypeAdapter implements AttributeType {

	@Override
	public boolean isInnerClass() {
		return false;
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.emptySet();
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return Collections.emptySet();
	}

	@Override
	public ClassInfo getEnclosingClass() {
		return null;
	}

}
