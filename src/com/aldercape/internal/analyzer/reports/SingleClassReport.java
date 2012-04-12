package com.aldercape.internal.analyzer.reports;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class SingleClassReport implements ClassConsumer {

	protected SortedSet<ClassInfo> classes = new TreeSet<>();

	public SortedSet<ClassInfo> getIncludedTypes() {
		return Collections.unmodifiableSortedSet(classes);
	}

	@Override
	public void addClass(ClassInfo classInfo) {
		if (isValidClass(classInfo)) {
			classes.add(classInfo);
		}
	}

	protected boolean isValidClass(ClassInfo classInfo) {
		return true;
	}

}