package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class ClassDependencyReport {

	private Set<ClassInfo> classes = new LinkedHashSet<>();

	public void addClass(ClassInfo classInfo) {
		classes.add(classInfo);
	}

	public Set<ClassInfo> getClasses() {
		return Collections.unmodifiableSet(classes);
	}

	public Set<ClassInfo> getChildrenFor(ClassInfo classInfo) {
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
