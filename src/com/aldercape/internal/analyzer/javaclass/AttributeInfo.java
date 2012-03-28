package com.aldercape.internal.analyzer.javaclass;

import java.util.Set;

import com.aldercape.internal.analyzer.PackageInfo;

public class AttributeInfo {

	private static AttributeInfo empty = new AttributeInfo(new UndefinedAttributeType());

	private AttributeType attributeType;

	public AttributeInfo(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Set<PackageInfo> getDependentPackages() {
		return attributeType.getDependentPackages();
	}

	public static AttributeInfo empty() {
		return empty;
	}

}
