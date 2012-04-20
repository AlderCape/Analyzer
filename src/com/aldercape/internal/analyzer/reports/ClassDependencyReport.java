package com.aldercape.internal.analyzer.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
		List<MetricPair> result = new ArrayList<>();
		result.add(new MetricPair("DI {0}", getDependencyInversionFor(type)));
		return result;
	}

	@Override
	public String getReportName() {
		return reportName;
	}

	public float getDependencyInversionFor(ClassInfo info) {
		Set<ClassInfo> classDependencies = info.getClassDependencies();
		if (classDependencies.size() == 0) {
			return 1;
		}
		int abstractDependencyCount = 0;
		for (ClassInfo classInfo : classDependencies) {
			if (classInfo.isAbstract()) {
				abstractDependencyCount++;
			}
		}
		return (float) abstractDependencyCount / classDependencies.size();
	}

}
