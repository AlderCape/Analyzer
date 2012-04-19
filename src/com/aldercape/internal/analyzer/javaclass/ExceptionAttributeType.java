package com.aldercape.internal.analyzer.javaclass;

import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ExceptionAttributeType extends AttributeTypeAdapter {

	private Set<PackageInfo> exceptions = new HashSet<>();
	private Set<ClassInfo> exceptionClasses = new HashSet<>();

	public ExceptionAttributeType(Set<ClassInfo> exceptionClasses, Set<PackageInfo> exceptions) {
		this.exceptionClasses = exceptionClasses;
		this.exceptions = exceptions;
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return exceptions;
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return exceptionClasses;
	}

}
