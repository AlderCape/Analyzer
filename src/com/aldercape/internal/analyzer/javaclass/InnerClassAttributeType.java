package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class InnerClassAttributeType extends AttributeTypeAdapter {

	private ClassInfoBase outerClass;

	public InnerClassAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numClasses = in.readUnsignedShort();
		for (int i = 0; i < numClasses; i++) {
			int innerClassInfo = in.readUnsignedShort();
			int outerClassInfo = in.readUnsignedShort();
			int innerName = in.readUnsignedShort();
			int accessFlags = in.readUnsignedShort();
			outerClass = ClassRepository.getClass(builder.getConstant(outerClassInfo).getName(builder.getConstants()).replace('/', '.'));
		}
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
