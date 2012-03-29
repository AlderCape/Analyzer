package com.aldercape.internal.analyzer.javaclass;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.AttributeType;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;

public class JavaClassParser {

	public JavaClassParser() {
	}

	public JavaClass parse(String name) throws FileNotFoundException {
		return parse(convertToFile(name));
	}

	public JavaClass parse(File file) throws FileNotFoundException {
		return parse(new FileInputStream(file));
	}

	public JavaClass parse(InputStream in) {
		return parse(new DataInputStream(in));
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

	private JavaClass parse(DataInputStream in) {
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
		AttributeInfo attributeInfo = createAttributes(in, builder);
		builder.setAttributes(attributeInfo);
	}

	protected AttributeType parseAttribute(DataInputStream in, JavaClassBuilder builder) throws IOException {
		AttributeType attrType = new UndefinedAttributeType();
		int nameIndex = in.readUnsignedShort();
		if (nameIndex != 0) {
			Constant type = builder.getConstant(nameIndex);
			if (type.isAnnotation()) {
				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}

				attrType = new AnnotationAttributeType(values, builder);
			} else {

				int attrLength = in.readInt();
				byte[] values = new byte[attrLength];
				for (int b = 0; b < attrLength; b++) {
					values[b] = in.readByte();
				}
			}
		}
		return attrType;
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
			String interfaceName = ((String) builder.getConstant(constant.getNameIndex()).getObject()).replace('/', '.');
			builder.addInterfaceInfo(interfaceName);
		}
	}

	protected void createAndAddFields(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int fieldsCount = in.readUnsignedShort();
		for (int i = 0; i < fieldsCount; i++) {
			int accessFlag = in.readUnsignedShort();
			String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
			Constant constant = builder.getConstant(in.readUnsignedShort());
			FieldInfo fieldInfo = new FieldInfo(accessFlag, methodName, nextTypeFromDescriptor((String) constant.getObject()));
			AttributeInfo attributeInfo = createAttributes(in, builder);
			fieldInfo.setAttribute(attributeInfo);
			builder.addFieldInfo(fieldInfo);

		}
	}

	protected void createAndAddMethods(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int methodsCount = in.readUnsignedShort();
		for (int i = 0; i < methodsCount; i++) {
			MethodInfo info = createMethodInfo(in, builder);
			info.setAttribute(createAttributes(in, builder));
			builder.addMethodInfo(info);
		}
	}

	protected AttributeInfo createAttributes(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int attributeCount = in.readUnsignedShort();
		AttributeInfo attributeInfo = new AttributeInfo();
		for (int j = 0; j < attributeCount; j++) {
			attributeInfo.add(parseAttribute(in, builder));
		}
		return attributeInfo;
	}

	protected MethodInfo createMethodInfo(DataInputStream in, JavaClassBuilder builder) throws IOException {
		int accessFlag = in.readUnsignedShort();
		String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
		Constant constant = builder.getConstant(in.readUnsignedShort());
		return new MethodInfo(accessFlag, methodName, populateMethodParameters((String) constant.getObject()));
	}

	List<String> populateMethodParameters(String descriptor) {
		String parameterValue = descriptor.substring(descriptor.indexOf('(') + 1, descriptor.indexOf(')'));
		List<String> parameters = new ArrayList<>();
		while (!parameterValue.isEmpty()) {
			String result = nextTypeFromDescriptor(parameterValue);
			parameterValue = parameterValue.substring(isObject(parameterValue) ? 2 + result.length() : 1);
			parameters.add(result);
		}
		return parameters;
	}

	protected static String nextTypeFromDescriptor(String parameterValue) {
		// For see table 4.2 in the jvm specs
		switch (parameterValue.charAt(0)) {
		case 'B':
			return Byte.class.getName();
		case 'C':
			return Character.class.getName();
		case 'D':
			return Double.class.getName();
		case 'F':
			return Float.class.getName();
		case 'I':
			return Integer.class.getName();
		case 'J':
			return Long.class.getName();
		case 'L':
			return parameterValue.substring(1, parameterValue.indexOf(';')).replace('/', '.');
		case 'S':
			return Short.class.getName();
		case 'Z':
			return Boolean.class.getName();
		default:
			throw new RuntimeException("Unkown type: " + parameterValue);
		}
	}

	private boolean isObject(String parameterValue) {
		return parameterValue.charAt(0) == 'L';
	}

}
