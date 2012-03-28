package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import com.aldercape.internal.analyzer.PackageInfo;

public class AnnotationAttributeType implements AttributeType {

	private String className;

	@Override
	public void consume(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numAnnotations = in.readUnsignedShort();
		for (int i = 0; i < numAnnotations; i++) {
			int annTypeIndex = in.readUnsignedShort();
			className = JavaClassParser.nextTypeFromDescriptor((String) builder.getConstant(annTypeIndex).getObject());
			int numNameValuePairs = in.readUnsignedShort();
		}
	}

	public String getClassName() {
		return className;
	}

	@Override
	public Set<PackageInfo> getDependentPackages() {
		return Collections.singleton(new PackageInfo(className.substring(0, className.lastIndexOf('.'))));
	}
}
