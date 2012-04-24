package com.aldercape.internal.analyzer.classmodel;

import java.util.Set;

public interface ClassInfo extends Comparable<ClassInfo>, TypeInfo {

	@Override
	public PackageInfo getPackage();

	public ClassInfo getEnclosingClass();

	public Set<PackageInfo> getPackageDependencies();

	public Set<ClassInfo> getClassDependencies();

	public boolean isAbstract();

	@Override
	public String getName();

	public boolean isInnerClass();

	public void addInnerClass(ClassInfo innerClass);

	public Set<MethodInfo> getMethods();

	public boolean isEnumeration();

}
