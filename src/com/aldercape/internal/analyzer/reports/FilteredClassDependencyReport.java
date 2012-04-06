package com.aldercape.internal.analyzer.reports;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class FilteredClassDependencyReport extends ClassDependencyReport {

	private Set<PackageInfo> ignoredPackages = new HashSet<>();

	@Override
	public SortedSet<ClassInfo> getChildrenFor(ClassInfo packageInfo) {
		return filter(super.getChildrenFor(packageInfo));
	}

	@Override
	public SortedSet<ClassInfo> getParentsFor(ClassInfo packageInfo) {
		return filter(super.getParentsFor(packageInfo));
	}

	public void ignorePackage(PackageInfo packageInfo) {
		ignoredPackages.add(packageInfo);
	}

	@Override
	public SortedSet<ClassInfo> getClasses() {
		return filter(super.getClasses());
	}

	private SortedSet<ClassInfo> filter(SortedSet<ClassInfo> original) {
		SortedSet<ClassInfo> result = new TreeSet<>();
		for (ClassInfo info : original) {

			System.out.println(info.getPackage());
			System.out.println(ignoredPackages);
			if (!ignoredPackages.contains(info.getPackage())) {
				result.add(info);
			}
		}
		return result;
	}
}
