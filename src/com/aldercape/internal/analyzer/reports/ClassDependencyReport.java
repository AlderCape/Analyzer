package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class ClassDependencyReport {

	private SortedSet<ClassInfo> classes = new TreeSet<>();

	public void addClass(ClassInfo classInfo) {
		classes.add(classInfo);
	}

	public SortedSet<ClassInfo> getClasses() {
		return Collections.unmodifiableSortedSet(classes);
	}

	public Set<? extends ClassInfo> getChildrenFor(ClassInfo classInfo) {
		return classInfo.getClassDependencies();
	}

	public Set<ClassInfo> getParentsFor(ClassInfo classInfo) {
		Set<ClassInfo> result = new HashSet<>();
		for (ClassInfo otherClass : classes) {
			if (otherClass.getClassDependencies().contains(classInfo)) {
				result.add(otherClass);
			}
		}
		return result;
	}

}
