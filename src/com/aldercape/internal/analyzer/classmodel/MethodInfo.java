package com.aldercape.internal.analyzer.classmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.javaclass.ClassInfoBase;

public class MethodInfo {

	private String methodName;
	private List<String> parameters;
	private AttributeInfo attributeInfo = new AttributeInfo();

	public MethodInfo(int accessFlag, String methodName, List<String> parameters) {
		this.methodName = methodName;
		this.parameters = parameters;
	}

	public String getName() {
		return methodName;
	}

	public int getParameterCount() {
		return parameters.size();
	}

	public String getParameterType(int i) {
		return parameters.get(i);
	}

	@Override
	public String toString() {
		return "MethodInfo [methodName=" + methodName + ", parameters=" + parameters + "]";
	}

	public Set<PackageInfo> getDependentPackages() {
		Set<PackageInfo> result = new HashSet<>();
		for (String dependency : parameters) {
			result.add(getPackage(dependency));
		}
		result.addAll(attributeInfo.getDependentPackages());
		return result;
	}

	protected PackageInfo getPackage(String className) {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	public void setAttribute(AttributeInfo attributeInfo) {
		this.attributeInfo = attributeInfo;
	}

	public Set<ClassInfo> getDependentClasses() {
		Set<ClassInfo> result = new HashSet<>();
		for (String dependency : parameters) {
			result.add(new ClassInfoBase(dependency));
		}
		result.addAll(attributeInfo.getDependentClasses());
		return result;
	}

}
