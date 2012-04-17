package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class CodeAttributeType extends AttributeTypeAdapter {

	private AttributeInfo attributes;

	public CodeAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int maxStack = in.readUnsignedShort();
		int maxLocals = in.readUnsignedShort();
		int codeLength = in.readInt();
		byte[] code = new byte[codeLength];
		in.read(code);
		int exceptionTableLength = in.readUnsignedShort();
		for (int i = 0; i < exceptionTableLength; i++) {
			int startPc = in.readUnsignedShort();
			int endPc = in.readUnsignedShort();
			int handlerPc = in.readUnsignedShort();
			int catchType = in.readUnsignedShort();
		}
		attributes = createAttributes(in, builder);
	}

	protected AttributeInfo createAttributes(DataInputStream in, JavaClassBuilder builder) throws IOException {
		return new AttributeParser(builder).createAttributes(in);
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return attributes.getDependentClasses();
	}

}
