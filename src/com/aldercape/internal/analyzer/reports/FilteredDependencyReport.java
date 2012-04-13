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

	public FilteredDependencyReport(DependencyReport<T> baseReport) {
		this.baseReport = baseReport;
	}

	public FilteredDependencyReport(DependencyReport<T> baseReport, Set<PackageInfo> ignoredPackages) {
		this.baseReport = baseReport;
		this.ignoredPackages.addAll(ignoredPackages);
	}

	public SortedSet<T> filter(SortedSet<? extends T> original) {
		SortedSet<T> result = new TreeSet<>();
		for (T info : original) {
			if (!ignoredPackages.contains(info.getPackage())) {
				result.add(info);
			}
		}
		return result;
	}

	public void ignorePackage(PackageInfo packageInfo) {
		ignoredPackages.add(packageInfo);
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
