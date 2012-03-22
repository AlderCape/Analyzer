package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.ClassInfo;
import com.aldercape.internal.analyzer.PackageInfo;

public class PackageDependencyReport {

	private SortedSet<PackageInfo> packages = new TreeSet<>();
	private Map<PackageInfo, SortedSet<PackageInfo>> efferent = new HashMap<>();
	private Map<PackageInfo, SortedSet<PackageInfo>> afferent = new HashMap<>();

	public void addClass(ClassInfo info) {
		packages.add(info.getPackage());
		Set<PackageInfo> packageDependencies = info.getPackageDependencies();
		for (PackageInfo packageInfo : packageDependencies) {
			packages.add(packageInfo);
		}
		efferentFor(info.getPackage()).addAll(packageDependencies);

		for (PackageInfo packageName : packageDependencies) {
			afferentFor(packageName).add(info.getPackage());
		}
	}

	protected SortedSet<PackageInfo> afferentFor(PackageInfo packageName) {
		if (!afferent.containsKey(packageName.getName())) {
			afferent.put(packageName, new TreeSet<PackageInfo>());
		}
		return afferent.get(packageName);
	}

	protected SortedSet<PackageInfo> efferentFor(PackageInfo packageInfo) {
		if (!efferent.containsKey(packageInfo)) {
			efferent.put(packageInfo, new TreeSet<PackageInfo>());
		}
		return efferent.get(packageInfo);
	}

	public SortedSet<PackageInfo> getPackages() {
		return Collections.unmodifiableSortedSet(packages);
	}

	public SortedSet<PackageInfo> getEfferentFor(PackageInfo packageName) {
		return Collections.unmodifiableSortedSet(efferentFor(packageName));
	}

	public SortedSet<PackageInfo> getAfferentFor(PackageInfo packageName) {
		return Collections.unmodifiableSortedSet(afferent.get(packageName));
	}

}
