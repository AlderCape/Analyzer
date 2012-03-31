package com.aldercape.internal.analyzer.classmodel;

import java.util.Set;

public interface ClassInfo extends Comparable<ClassInfo>, TypeInfo {

	public PackageInfo getPackage();

	public Set<PackageInfo> getPackageDependencies();

	public Set<ClassInfo> getClassDependencies();

	public boolean isAbstract();

	public String getName();

}
