package com.aldercape.internal.analyzer.reports;

import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class SingleClassReport implements ClassConsumer {

	protected SortedSet<ClassInfo> classes = new TreeSet<>();

	public SortedSet<ClassInfo> getIncludedTypes() {
		SortedSet<ClassInfo> result = new TreeSet<>();
		for (ClassInfo info : classes) {
			if (isValidClass(info)) {
				result.add(info);
			}
		}
		return result;
	}

	@Override
	public void addClass(ClassInfo classInfo) {
		classes.add(classInfo);
	}

	protected boolean isValidClass(ClassInfo classInfo) {
		return true;
	}

}