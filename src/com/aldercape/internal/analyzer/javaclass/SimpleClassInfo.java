package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class SimpleClassInfo extends ClassInfoBase implements ClassInfo {

	private String className;

	public SimpleClassInfo(String className) {
		super(className);
		this.className = className;
	}

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		return Collections.emptySet();
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

}
