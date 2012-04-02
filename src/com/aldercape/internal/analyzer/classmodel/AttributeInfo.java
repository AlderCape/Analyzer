package com.aldercape.internal.analyzer.classmodel;

import java.util.HashSet;
import java.util.Set;

public class AttributeInfo {

	private static AttributeInfo empty = new AttributeInfo();

	private Set<AttributeType> attributes = new HashSet<>();

	public AttributeInfo(AttributeType attributeType) {
		this.attributes.add(attributeType);
	}

	public AttributeInfo() {
	}

	public Set<PackageInfo> getDependentPackages() {
		Set<PackageInfo> result = new HashSet<>();
		for (AttributeType attr : attributes) {
			result.addAll(attr.getDependentPackages());
		}
		return result;
	}

	public static AttributeInfo empty() {
		return empty;
	}

	public void add(AttributeType attributeType) {
		attributes.add(attributeType);
	}

	public int size() {
		return attributes.size();
	}

	public Set<? extends ClassInfo> getDependentClasses() {
		Set<ClassInfo> result = new HashSet<>();
		for (AttributeType attr : attributes) {
			result.addAll(attr.getDependentClasses());
		}
		return result;
	}

}
