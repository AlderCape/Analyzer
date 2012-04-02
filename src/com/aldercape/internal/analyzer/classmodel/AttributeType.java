package com.aldercape.internal.analyzer.classmodel;

import java.util.Collection;
import java.util.Set;

public interface AttributeType {

	Set<PackageInfo> getDependentPackages();

	Collection<? extends ClassInfo> getDependentClasses();

}
