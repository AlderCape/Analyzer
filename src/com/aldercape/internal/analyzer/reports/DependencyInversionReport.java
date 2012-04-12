package com.aldercape.internal.analyzer.reports;

import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class DependencyInversionReport extends SingleClassReport {

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
