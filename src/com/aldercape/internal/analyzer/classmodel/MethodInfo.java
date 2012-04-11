package com.aldercape.internal.analyzer.classmodel;

import java.util.Set;

public interface MethodInfo {

	public enum AccessModifier {
		PUBLIC, PROTECTED, PRIVATE;
	}

	public String getName();

	public Set<ClassInfo> getDependentClasses();

	public AccessModifier getAccessModifier();

}