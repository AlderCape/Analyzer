package com.aldercape.internal.analyzer.classmodel;

import java.util.Set;

public interface AttributeType {

	Set<PackageInfo> getDependentPackages();

	Set<? extends ClassInfo> getDependentClasses();

	boolean isInnerClass();

	ClassInfo getEnclosingClass();

}
