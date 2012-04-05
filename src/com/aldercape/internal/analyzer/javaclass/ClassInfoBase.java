package com.aldercape.internal.analyzer.javaclass;

import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassInfoBase implements ClassInfo {

	private String className;

	private ClassDetails details = ClassDetails.Unparsed;

	public ClassInfoBase(String className) {
		this.className = className;
	}

	public ClassInfoBase(String className, ClassDetails details) {
		this.className = className;
		this.details = details;
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

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		return details.getPackageDependencies(this);
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		return details.getClassDependencies(this);
	}

	@Override
	public boolean isAbstract() {
		return details.isAbstract();
	}

	@Override
	public boolean isInnerClass() {
		return details.isInnerClass();
	}

}
