package com.aldercape.internal.analyzer.reports;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.classmodel.TypeInfo;

public class FilteredDependencyReport<T extends TypeInfo> implements DependencyReport<T> {

	private DependencyReport<T> baseReport;
	private Set<PackageInfo> ignoredPackages = new HashSet<>();
	private Set<PackageInfo> includedPackages = new HashSet<>();
	private Set<PackageInfo> ignoredParentPackages = new HashSet<>();

	public FilteredDependencyReport(DependencyReport<T> baseReport) {
		this.baseReport = baseReport;
	}

	public FilteredDependencyReport(DependencyReport<T> baseReport, Set<PackageInfo> ignoredPackages) {
		this.baseReport = baseReport;
		this.ignoredPackages.addAll(ignoredPackages);
	}

	private SortedSet<T> filter(SortedSet<? extends T> original) {
		SortedSet<T> result = new TreeSet<>();
		for (T info : original) {
			if (isIncluded(info)) {
				result.add(info);
			}
		}
		return result;
	}

	private boolean isIncluded(T info) {
		if (includedPackages.contains(info.getPackage())) {
			return true;
		}
		if (ignoredPackages.contains(info.getPackage())) {
			return false;
		}
		if (ignoredParentPackages.contains(info.getPackage())) {
			return false;
		}
		return isParentPackageIncluded(info.getPackage());
	}

	private boolean isParentPackageIncluded(PackageInfo info) {
		for (PackageInfo packageInfo : ignoredParentPackages) {

			if (packageInfo.isParentTo(info)) {
				return false;
			}
		}
		return true;
	}

	public void ignorePackage(PackageInfo packageInfo) {
		ignoredPackages.add(packageInfo);
	}

	public void includePackage(PackageInfo packageInfo) {
		includedPackages.add(packageInfo);
	}

	public void ignoreParentPackage(PackageInfo packageInfo) {
		ignoredParentPackages.add(packageInfo);
	}

	@Override
	public SortedSet<T> getChildrenFor(T type) {
		return filter(baseReport.getChildrenFor(type));
	}

	@Override
	public SortedSet<T> getIncludedTypes() {
		return filter(baseReport.getIncludedTypes());
	}

	@Override
	public SortedSet<T> getParentsFor(T type) {
		return filter(baseReport.getParentsFor(type));
	}

	@Override
	public List<DependencyReport.MetricPair> getMetricsPair(T type) {
		return baseReport.getMetricsPair(type);
	}

	@Override
	public String getReportName() {
		return baseReport.getReportName();
	}

}
