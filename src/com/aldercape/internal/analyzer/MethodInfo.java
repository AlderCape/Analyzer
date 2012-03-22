package com.aldercape.internal.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodInfo {

	private String methodName;
	private List<String> parameters;

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
		return result;
	}

	protected PackageInfo getPackage(String className) {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

}
