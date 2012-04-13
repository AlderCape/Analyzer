package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class ClassDependencyReport extends SingleClassReport implements DependencyReport<ClassInfo> {

	private String reportName;

	public ClassDependencyReport(String reportName) {
		this.reportName = reportName;
	}

	@Override
	protected boolean isValidClass(ClassInfo classInfo) {
		return !classInfo.isInnerClass();
	}

	@Override
	public SortedSet<ClassInfo> getChildrenFor(ClassInfo classInfo) {
		return new TreeSet<ClassInfo>(classInfo.getClassDependencies());
	}

	@Override
	public SortedSet<ClassInfo> getParentsFor(ClassInfo classInfo) {
		SortedSet<ClassInfo> result = new TreeSet<>();
		for (ClassInfo otherClass : classes) {
			if (otherClass.getClassDependencies().contains(classInfo)) {
				result.add(otherClass);
			}
		}
		return result;
	}

	@Override
	public List<DependencyReport.MetricPair> getMetricsPair(ClassInfo type) {
		return Collections.emptyList();
	}

	@Override
	public String getReportName() {
		return reportName;
	}

}
