package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.ClassInfo;
import com.aldercape.internal.analyzer.PackageInfo;

public class PackageDependencyReport {

	private Map<PackageInfo, PackageDependencyInfo> packageDependencyInfos = new HashMap<>();

	public void addClass(ClassInfo info) {
		packageDependencyInfoFor(info.getPackage()).add(info);

		for (PackageInfo packageName : info.getPackageDependencies()) {
			packageDependencyInfoFor(packageName).addAfferentClass(info);
		}
	}

	protected PackageDependencyInfo packageDependencyInfoFor(PackageInfo packageInfo) {
		if (!packageDependencyInfos.containsKey(packageInfo)) {
			packageDependencyInfos.put(packageInfo, new PackageDependencyInfo(packageInfo));
		}
		return packageDependencyInfos.get(packageInfo);
	}

	public SortedSet<PackageInfo> getPackages() {
		return Collections.unmodifiableSortedSet(new TreeSet<>(packageDependencyInfos.keySet()));
	}

	public SortedSet<PackageInfo> getEfferentFor(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).efferentSet();
	}

	public SortedSet<PackageInfo> getAfferentFor(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).getAfferent();
	}

	public float getAbstractness(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).getAbstractness();
	}

	public float getInstability(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).getInstability();
	}

	public float getDistance(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).getDistance();
	}
}
