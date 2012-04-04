package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassDetails {

	public static final ClassDetails Unparsed = new ClassDetails();

	public boolean isInnerClass() {
		return false;
	}

	public boolean isAbstract() {
		return false;
	}

	public Set<ClassInfo> getClassDependencies() {
		return Collections.emptySet();
	}

	public Set<PackageInfo> getPackageDependencies() {
		return Collections.emptySet();
	}

}
