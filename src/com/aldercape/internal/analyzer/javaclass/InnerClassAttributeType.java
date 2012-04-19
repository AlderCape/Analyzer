package com.aldercape.internal.analyzer.javaclass;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class InnerClassAttributeType extends AttributeTypeAdapter {

	private ClassInfo outerClass;

	public InnerClassAttributeType(ClassInfo outerClass) {
		this.outerClass = outerClass;
	}

	@Override
	public boolean isInnerClass() {
		return true;
	}

	@Override
	public ClassInfo getEnclosingClass() {
		return outerClass;
	}

}
