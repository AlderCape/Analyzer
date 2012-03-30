package com.aldercape.internal.analyzer.classmodel;

import java.util.Set;

public interface ClassInfo {

	public PackageInfo getPackage();

	public Set<PackageInfo> getPackageDependencies();

	public Set<ClassInfo> getClassDependencies();

	public boolean isAbstract();

}
