package com.aldercape.internal.classparser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PackageDependencyReport {

	private SortedSet<PackageInfo> packages = new TreeSet<>();
	private Map<PackageInfo, SortedSet<PackageInfo>> efferent = new HashMap<>();
	private Map<PackageInfo, SortedSet<PackageInfo>> afferent = new HashMap<>();

	public void addClass(JavaClass info) {
		packages.add(info.getPackage());
		Set<PackageInfo> packageDependencies = info.getPackageDependencies();
		for (PackageInfo packageInfo : packageDependencies) {
			packages.add(packageInfo);
		}
		if (!efferent.containsKey(info.getPackage())) {
			efferent.put(info.getPackage(), new TreeSet<PackageInfo>());
		}
		efferent.get(info.getPackage()).addAll(packageDependencies);

		for (PackageInfo packageName : packageDependencies) {
			if (!afferent.containsKey(packageName.getName())) {
				afferent.put(packageName, new TreeSet<PackageInfo>());
			}
			afferent.get(packageName).add(info.getPackage());
		}
	}

	public SortedSet<PackageInfo> getPackages() {
		return Collections.unmodifiableSortedSet(packages);
	}

	public SortedSet<PackageInfo> getEfferentFor(PackageInfo packageName) {
		return Collections.unmodifiableSortedSet(efferent.get(packageName));
	}

	public SortedSet<PackageInfo> getAfferentFor(PackageInfo packageName) {
		return Collections.unmodifiableSortedSet(afferent.get(packageName));
	}

}
