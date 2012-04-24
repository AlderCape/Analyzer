package com.aldercape.internal.analyzer.reports;

import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassInfoStub implements ClassInfo {
	private boolean isAbstract;
	private Set<PackageInfo> dependencies = new HashSet<>();
	private String packageName;
	private Set<ClassInfo> classDependencies = new HashSet<>();
	private String className;
	private Set<MethodInfo> methods;

	public ClassInfoStub(boolean isAbstract) {
		this.isAbstract = isAbstract;
		packageName = "base";
	}

	public ClassInfoStub(String className) {
		this.className = className;
	}

	@Override
	public PackageInfo getPackage() {
		return new PackageInfo(packageName);
	}

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		return dependencies;
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		return classDependencies;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	public void setDependencies(Set<PackageInfo> dependencies) {
		this.dependencies = dependencies;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setClassDependency(Set<ClassInfo> classDependencies) {
		this.classDependencies = classDependencies;
	}

	public void addClassDependency(ClassInfo dependency) {
		this.classDependencies.add(dependency);
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public int compareTo(ClassInfo o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj) {
		ClassInfo other = (ClassInfo) obj;
		return getName().equals(other.getName());
	}

	@Override
	public String toString() {
		return className + " (ClassInfoStub)";
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public boolean isInnerClass() {
		return false;
	}

	@Override
	public ClassInfo getEnclosingClass() {
		return null;
	}

	@Override
	public void addInnerClass(ClassInfo result) {

	}

	public void setMethods(Set<MethodInfo> publicMethods) {
		this.methods = publicMethods;
	}

	@Override
	public Set<MethodInfo> getMethods() {
		return methods;
	}

	@Override
	public boolean isEnumeration() {
		return false;
	}
}