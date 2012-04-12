package com.aldercape.internal.analyzer.classmodel;

import java.util.HashSet;
import java.util.Set;


public class FieldInfo {

	private String type;
	private AttributeInfo attributeInfo = new AttributeInfo();

	public FieldInfo(int accessFlag, String name, String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public Set<PackageInfo> getDependentPackages() {
		Set<PackageInfo> result = new HashSet<>();
		result.add(getPackage(type));
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
		ClassInfo o = ClassRepository.getClass(type);
		Set<ClassInfo> result = new HashSet<>();
		result.add(o);
		result.addAll(attributeInfo.getDependentClasses());
		return result;
	}
}