package com.aldercape.internal.analyzer.javaclass;

import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class CodeAttributeType extends AttributeTypeAdapter {

	private AttributeInfo attributes;

	public CodeAttributeType(AttributeInfo attributes) {
		this.attributes = attributes;
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return attributes.getDependentClasses();
	}

}
