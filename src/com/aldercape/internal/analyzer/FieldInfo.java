package com.aldercape.internal.analyzer;

public class FieldInfo {

	private String type;

	public FieldInfo(int accessFlag, String name, String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public PackageInfo getDependentPackage() {
		return getPackage(type);
	}

	protected PackageInfo getPackage(String className) {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}
}