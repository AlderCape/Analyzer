package com.aldercape.internal.analyzer.javaclass;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;

public class JavaClassParser {

	private DataInputStream in;

	public JavaClassParser() {
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
			createAndAddVersionInfo(in, builder);
			createAndAddConstantPool(in, builder);
			createAndAddAccessFlags(in, builder);
			createAndAddClassName(in, builder);
			createAndAddSuperclass(in, builder);
			createAndAddInterfaces(in, builder);
			createAndAddFields(in, builder);
			createAndAddMethods(in, builder);
			createAndAddAtrributes(in, builder);
			return builder.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void createAndAddAtrributes(DataInputStream in, JavaClassBuilder builder) throws IOException {
		builder.setAttributes(new AttributeParser(builder).createAttributes(in));
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
		builder.setClassNameIndex(in.readUnsignedShort());
	}

	protected void createAndAddSuperclass(DataInputStream in, JavaClassBuilder builder) throws IOException {
		builder.setSuperclassNameIndex(in.readUnsignedShort());
	}

	protected void createAndAddInterfaces(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int interfacesCount = in.readUnsignedShort();
		for (int i = 0; i < interfacesCount; i++) {
			int interfaceNameIndex = in.readUnsignedShort();
			Constant constant = builder.getConstant(interfaceNameIndex);
			builder.addInterfaceInfo(constant.getName(builder.getConstants()).replace('/', '.'));
		}
	}

	protected void createAndAddFields(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int fieldsCount = in.readUnsignedShort();
		for (int i = 0; i < fieldsCount; i++) {
			int accessFlag = in.readUnsignedShort();
			String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
			FieldInfo fieldInfo = new FieldInfo(accessFlag, methodName, new TypeParser(builder).parseTypeFromIndex(in.readUnsignedShort()));
			AttributeInfo attributeInfo = new AttributeParser(builder).createAttributes(in);
			fieldInfo.setAttribute(attributeInfo);
			builder.addFieldInfo(fieldInfo);

		}
	}

	protected void createAndAddMethods(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int methodsCount = in.readUnsignedShort();
		for (int i = 0; i < methodsCount; i++) {
			ParsedMethodInfo info = createMethodInfo(in, builder);
			info.setAttribute(new AttributeParser(builder).createAttributes(in));
			builder.addMethodInfo(info);
		}
	}

	protected ParsedMethodInfo createMethodInfo(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int accessFlag = in.readUnsignedShort();
		String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
		Constant constant = builder.getConstant(in.readUnsignedShort());
		return new ParsedMethodInfo(accessFlag, methodName, new TypeParser(builder).populateMethodParameters(constant));
	}
}
