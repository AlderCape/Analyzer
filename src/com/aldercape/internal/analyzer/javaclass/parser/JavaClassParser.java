package com.aldercape.internal.analyzer.javaclass.parser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.javaclass.Constant;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolType;
import com.aldercape.internal.analyzer.javaclass.JavaClassBuilder;
import com.aldercape.internal.analyzer.javaclass.ParsedMethodInfo;
import com.aldercape.internal.analyzer.javaclass.VersionInfo;

public class JavaClassParser {

	private DataInputStream in;
	private AttributeParser attributeParser;
	private TypeParser typeParser;
	private ClassRepository repository;

	public JavaClassParser(ClassRepository repository) {
		this.repository = repository;
	}

	public ClassInfoBase parse(String name) throws FileNotFoundException {
		return parse(convertToFile(name));
	}

	public ClassInfoBase parse(File file) throws FileNotFoundException {
		in = new DataInputStream(new FileInputStream(file));
		return parse();
	}

	public static File convertToFile(String name) {
		String fileName = name.replace(".", "/") + ".class";
		String[] paths = System.getProperty("java.class.path").split("\\:");
		for (String path : paths) {
			File file = new File(path, fileName);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	private ClassInfoBase parse() {
		try {
			JavaClassBuilder builder = new JavaClassBuilder();
			typeParser = new TypeParser(builder.getConstants());
			attributeParser = new AttributeParser(builder.getConstants(), typeParser, repository);

			createAndAddVersionInfo(in, builder);
			createAndAddConstantPool(in, builder);
			createAndAddAccessFlags(in, builder);
			createAndAddClassName(in, builder);
			createAndAddSuperclass(in, builder);
			createAndAddInterfaces(in, builder);
			createAndAddFields(in, builder);
			createAndAddMethods(in, builder);
			createAndAddAtrributes(in, builder);
			return builder.create(repository);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void createAndAddAtrributes(DataInputStream in, JavaClassBuilder builder) throws IOException {
		builder.setAttributes(attributeParser.createAttributes(in));
	}

	protected void createAndAddVersionInfo(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int magicNumber = in.readInt();
		int minorVersion = in.readUnsignedShort();
		int majorVersion = in.readUnsignedShort();
		builder.setVersionInfo(new VersionInfo(magicNumber, minorVersion, majorVersion));
	}

	protected void createAndAddConstantPool(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int constantPoolSize = in.readUnsignedShort();
		for (int i = 0; i < constantPoolSize - 1; i++) {
			int tag = in.readUnsignedByte();
			ConstantPoolType type = ConstantPoolType.getFor(tag);
			builder.addConstant(type.consume(in));
		}
	}

	protected void createAndAddAccessFlags(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int accessFlags = in.readUnsignedShort();
		builder.setAccessFlags(accessFlags);
	}

	protected void createAndAddClassName(DataInputStream in, JavaClassBuilder builder) throws IOException {
		String className = builder.getConstantClassName(in.readUnsignedShort());
		builder.setClassNameIndex(className);
	}

	protected void createAndAddSuperclass(DataInputStream in, JavaClassBuilder builder) throws IOException {
		ClassInfoBase superClass = repository.getClass(builder.getConstantClassName(in.readUnsignedShort()));
		builder.setSuperclassNameIndex(superClass);
	}

	protected void createAndAddInterfaces(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int interfacesCount = in.readUnsignedShort();
		for (int i = 0; i < interfacesCount; i++) {
			int interfaceNameIndex = in.readUnsignedShort();
			Constant constant = builder.getConstant(interfaceNameIndex);
			builder.addInterfaceInfo(repository.getClass(constant.getName(builder.getConstants()).replace('/', '.')));
		}
	}

	protected void createAndAddFields(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int fieldsCount = in.readUnsignedShort();
		for (int i = 0; i < fieldsCount; i++) {
			int accessFlag = in.readUnsignedShort();
			String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
			FieldInfo fieldInfo = new FieldInfo(accessFlag, methodName, typeParser.parseTypeFromIndex(in.readUnsignedShort()), repository);
			AttributeInfo attributeInfo = attributeParser.createAttributes(in);
			fieldInfo.setAttribute(attributeInfo);
			builder.addFieldInfo(fieldInfo);

		}
	}

	protected void createAndAddMethods(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int methodsCount = in.readUnsignedShort();
		for (int i = 0; i < methodsCount; i++) {
			ParsedMethodInfo info = createMethodInfo(in, builder);
			info.setAttribute(attributeParser.createAttributes(in));
			builder.addMethodInfo(info);
		}
	}

	protected ParsedMethodInfo createMethodInfo(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int accessFlag = in.readUnsignedShort();
		String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
		Constant constant = builder.getConstant(in.readUnsignedShort());
		return new ParsedMethodInfo(accessFlag, methodName, typeParser.populateMethodParameters(constant), repository);
	}
}
