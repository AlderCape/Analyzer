package com.aldercape.internal.analyzer.reports;

import com.aldercape.internal.analyzer.PackageInfo;

public class PackageDependencyInfo implements Comparable<PackageDependencyInfo> {

	private PackageInfo packageInfo;

	public PackageDependencyInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	@Override
	public int compareTo(PackageDependencyInfo o) {
		return packageInfo.compareTo(o.packageInfo);
	}

}
