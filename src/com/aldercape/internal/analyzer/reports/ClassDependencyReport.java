package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class ClassDependencyReport implements DependencyReport<ClassInfo> {

	private SortedSet<ClassInfo> classes = new TreeSet<>();

	public void addClass(ClassInfo classInfo) {
		classes.add(classInfo);
	}

	public SortedSet<ClassInfo> getClasses() {
		return Collections.unmodifiableSortedSet(classes);
	}

	@Override
	public SortedSet<ClassInfo> getChildrenFor(ClassInfo classInfo) {
		return new TreeSet<ClassInfo>(classInfo.getClassDependencies());
	}

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
	public SortedSet<ClassInfo> getIncludedTypes() {
		return getClasses();
	}

	@Override
	public List<DependencyReport.MetricPair> getMetricsPair(ClassInfo type) {
		return Collections.emptyList();
	}

}
