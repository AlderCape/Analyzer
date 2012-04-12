package com.aldercape.internal.analyzer.classmodel;


import java.util.Collections;
import java.util.Set;


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

		@Override
		public Set<MethodInfo> getMethods() {
			return Collections.emptySet();
		}

	};

	public boolean isInnerClass();

	public boolean isAbstract();

	public Set<ClassInfo> getClassDependencies(ClassInfo baseClass);

	public ClassInfo getEnclosingClass();

	public Set<MethodInfo> getMethods();
}
