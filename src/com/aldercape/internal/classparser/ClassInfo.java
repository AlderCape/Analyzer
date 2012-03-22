package com.aldercape.internal.classparser;

import java.util.Set;

public interface ClassInfo {

	public PackageInfo getPackage();

	public Set<PackageInfo> getPackageDependencies();

}
