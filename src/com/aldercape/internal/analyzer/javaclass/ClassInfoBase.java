package com.aldercape.internal.analyzer.javaclass;

import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassInfoBase implements ClassInfo {

	private String className;

	private ClassDetails details = ClassDetails.Unparsed;

	private Set<ClassInfo> innerClasses = new HashSet<>();

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
		Set<PackageInfo> result = new HashSet<>();

		for (ClassInfo classInfo : getClassDependencies()) {
			result.add(classInfo.getPackage());
		}
		result.remove(getPackage());
		return result;
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		Set<ClassInfo> result = new HashSet<>(details.getClassDependencies(this));
		for (ClassInfo innerClass : innerClasses) {
			result.addAll(innerClass.getClassDependencies());
		}
		return result;
	}

	@Override
	public boolean isAbstract() {
		return details.isAbstract();
	}

	@Override
	public boolean isInnerClass() {
		return details.isInnerClass() && !details.getEnclosingClass().equals(this);
	}

	public void setDetails(ParsedClassDetails details) {
		this.details = details;
	}

	@Override
	public ClassInfo getEnclosingClass() {
		return details.getEnclosingClass();
	}

	@Override
	public void addInnerClass(ClassInfo innerClass) {
		innerClasses.add(innerClass);
	}

	@Override
	public Set<MethodInfo> getMethods() {
		return details.getMethods();
	}

}
