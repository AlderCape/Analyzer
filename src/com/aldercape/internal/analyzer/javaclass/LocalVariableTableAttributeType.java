package com.aldercape.internal.analyzer.javaclass;

import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class LocalVariableTableAttributeType extends AttributeTypeAdapter {

	public Set<ClassInfo> types = new HashSet<>();

	public LocalVariableTableAttributeType(Set<ClassInfo> classes) {
		types.addAll(classes);
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return types;
	}
}
