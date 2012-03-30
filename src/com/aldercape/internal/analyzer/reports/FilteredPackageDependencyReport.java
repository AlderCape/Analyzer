package com.aldercape.internal.analyzer.reports;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class FilteredPackageDependencyReport extends PackageDependencyReport {

	private Set<PackageInfo> ignoredPackages = new HashSet<>();

	@Override
	public SortedSet<PackageInfo> getPackages() {
		return filter(super.getPackages());
	}

	@Override
	public SortedSet<PackageInfo> getChildrenFor(PackageInfo packageInfo) {
		return filter(super.getChildrenFor(packageInfo));
	}

	@Override
	public SortedSet<PackageInfo> getParentsFor(PackageInfo packageInfo) {
		return filter(super.getParentsFor(packageInfo));
	}

	public void ignorePackage(PackageInfo packageInfo) {
		ignoredPackages.add(packageInfo);
	}

	private SortedSet<PackageInfo> filter(SortedSet<PackageInfo> original) {
		SortedSet<PackageInfo> result = original;
		result.removeAll(ignoredPackages);
		return result;
	}

}
