package com.aldercape.internal.analyzer.javaclass;

import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public interface ClassDetails {

	public static final ClassDetails Unparsed = new ClassDetails() {

		@Override
		public boolean isInnerClass() {
			return false;
		}

		@Override
		public boolean isAbstract() {
			return false;
		}

		@Override
		public Set<ClassInfo> getClassDependencies(ClassInfo baseClass) {
			return Collections.emptySet();
		}

		@Override
		public ClassInfo getEnclosingClass() {
			return null;
		}

	};

	public boolean isInnerClass();

	public boolean isAbstract();

	public Set<ClassInfo> getClassDependencies(ClassInfo baseClass);

	public ClassInfo getEnclosingClass();
}
