package com.aldercape.internal.analyzer.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class PackageDependencyReport implements DependencyReport<PackageInfo>, ClassConsumer {

	private Map<PackageInfo, PackageDependencyInfo> packageDependencyInfos = new HashMap<>();
	private String reportName;

	public PackageDependencyReport(String reportName) {
		this.reportName = reportName;
	}

	@Override
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
		return new TreeSet<>(packageDependencyInfos.keySet());
	}

	@Override
	public SortedSet<PackageInfo> getChildrenFor(PackageInfo packageInfo) {
		return getEfferent(packageInfo);
	}

	protected SortedSet<PackageInfo> getEfferent(PackageInfo packageInfo) {
		return packageDependencyInfoFor(packageInfo).efferentSet();
	}

	@Override
	public SortedSet<PackageInfo> getParentsFor(PackageInfo packageInfo) {
		return getAfferent(packageInfo);
	}

	protected TreeSet<PackageInfo> getAfferent(PackageInfo packageInfo) {
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

	@Override
	public SortedSet<PackageInfo> getIncludedTypes() {
		return getPackages();
	}

	@Override
	public List<DependencyReport.MetricPair> getMetricsPair(PackageInfo packageInfo) {
		List<MetricPair> metrics = new ArrayList<>();
		metrics.add(new MetricPair("Ca {0}", getParentsFor(packageInfo).size()));
		metrics.add(new MetricPair("Ce {0}", getChildrenFor(packageInfo).size()));
		metrics.add(new MetricPair("A {0,number,0.0}", getAbstractness(packageInfo)));
		metrics.add(new MetricPair("I {0,number,0.0}", getInstability(packageInfo)));
		metrics.add(new MetricPair("D {0,number,0.0}", getDistance(packageInfo)));
		return metrics;
	}

	public String getReportName() {
		return reportName;
	}
}
