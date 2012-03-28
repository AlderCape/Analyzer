package com.aldercape.internal.analyzer.javaclass;

import java.io.IOException;
import java.util.Set;

import com.aldercape.internal.analyzer.PackageInfo;

public interface AttributeType {

	void consume(byte[] values, JavaClassBuilder builder) throws IOException;

	Set<PackageInfo> getDependentPackages();

}
