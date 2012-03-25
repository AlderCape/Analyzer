package com.aldercape.internal.analyzer;

import java.util.Set;

public interface ClassInfo {

	public PackageInfo getPackage();

	public Set<PackageInfo> getPackageDependencies();

	public boolean isAbstract();

}
