package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.LocalVariableTableAttributeType;

public class LocalVariableTableParser implements AttributeTypeParser {

	private TypeParser typeParser;
	private ClassRepository repository;

	public LocalVariableTableParser(TypeParser typeParser, ClassRepository repository) {
		this.typeParser = typeParser;
		this.repository = repository;
	}

	@Override
	public LocalVariableTableAttributeType parse(byte[] values) throws IOException {
		Set<ClassInfo> classes = new HashSet<>();
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(values));
		int local_variable_table_length = in.readUnsignedShort();
		for (int i = 0; i < local_variable_table_length; i++) {
			int start_pc = in.readUnsignedShort();
			int length = in.readUnsignedShort();
			int name_index = in.readUnsignedShort();
			int descriptor_index = in.readUnsignedShort();
			int index = in.readUnsignedShort();
			String type = typeParser.parseTypeFromIndex(descriptor_index);
			classes.add(repository.getClass(type));
		}
		return new LocalVariableTableAttributeType(classes);
	}

}
