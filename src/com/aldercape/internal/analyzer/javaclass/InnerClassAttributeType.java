package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class InnerClassAttributeType extends AttributeTypeAdapter {

	public InnerClassAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int numClasses = in.readUnsignedShort();
		for (int i = 0; i < numClasses; i++) {
			int innerClassInfo = in.readUnsignedShort();
			int outerClassInfo = in.readUnsignedShort();
			int innerName = in.readUnsignedShort();
			int accessFlags = in.readUnsignedShort();
			System.out.println(builder.getConstant(innerClassInfo).getName(builder) + " " + builder.getConstant(outerClassInfo).getName(builder) + " " + builder.getConstant(innerName).getObject());
		}
	}

	@Override
	public boolean isInnerClass() {
		return true;
	}

}
