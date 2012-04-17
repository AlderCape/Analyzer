package com.aldercape.internal.analyzer.javaclass;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;

public class LocalVariableTableAttributeType extends AttributeTypeAdapter {

	private Set<ClassInfo> types = new HashSet<>();

	public LocalVariableTableAttributeType(byte[] values, JavaClassBuilder builder) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int local_variable_table_length = in.readUnsignedShort();
		for (int i = 0; i < local_variable_table_length; i++) {
			int start_pc = in.readUnsignedShort();
			int length = in.readUnsignedShort();
			int name_index = in.readUnsignedShort();
			int descriptor_index = in.readUnsignedShort();
			int index = in.readUnsignedShort();
			String type = new TypeParser(builder).parseTypeFromIndex(descriptor_index);
			types.add(ClassRepository.getClass(type));
		}
	}

	@Override
	public Set<? extends ClassInfo> getDependentClasses() {
		return types;
	}
}
