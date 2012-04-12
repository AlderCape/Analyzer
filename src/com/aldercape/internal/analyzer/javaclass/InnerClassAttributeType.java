package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;

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
			if (outerClassInfo > 0) {
				outerClass = ClassRepository.getClass(builder.getConstant(outerClassInfo).getName(builder.getConstants()).replace('/', '.'));
			} else {
				String innerClassName = builder.getConstant(innerClassInfo).getName(builder.getConstants());
				outerClass = ClassRepository.getClass(innerClassName.substring(0, innerClassName.indexOf('$')).replace('/', '.'));
			}
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
