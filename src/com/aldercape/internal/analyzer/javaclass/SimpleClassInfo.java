package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class SimpleClassInfo implements ClassInfo {

	private String className;

	public SimpleClassInfo(String className) {
		this.className = className;
	}

	@Override
	public int compareTo(ClassInfo o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public PackageInfo getPackage() {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		return Collections.emptySet();
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public String toString() {
		return getName() + " (SimpleClassInfo)";
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
