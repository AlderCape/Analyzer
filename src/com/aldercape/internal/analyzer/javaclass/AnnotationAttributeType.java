package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class AnnotationAttributeType extends AttributeTypeAdapter {

	private ClassInfo className;

	public AnnotationAttributeType(ClassInfo annotation) {
		this.className = annotation;
	}

	protected String getConstantAsString(JavaClassBuilder builder, int annTypeIndex) {
		return (String) builder.getConstant(annTypeIndex).getObject();
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.singleton(className.getPackage());
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return Collections.singleton(className);
	}
}
