package com.aldercape.internal.analyzer.javaclass;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

abstract class ClassInfoBase implements ClassInfo {

	private String className;

	public ClassInfoBase(String className) {
		this.className = className;
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public PackageInfo getPackage() {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	@Override
	public int compareTo(ClassInfo o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		return getName().equals(((ClassInfo) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
