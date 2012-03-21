package com.aldercape.internal.classparser;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaClassParser {

	private String className;

	public JavaClassParser(String name) {
		className = name;
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

	public JavaClass parse() {
		try {
			return parse(new DataInputStream(new FileInputStream(convertToFile(className))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JavaClass parse(DataInputStream in) {
		try {
			JavaClassBuilder builder = new JavaClassBuilder();
			builder.setMagicNumber(in.readInt());
			builder.setMinorVersion(in.readUnsignedShort());
			builder.setMajorVersion(in.readUnsignedShort());
			int constantPoolSize = in.readUnsignedShort();
			for (int i = 0; i < constantPoolSize - 1; i++) {
				int tag = in.readUnsignedByte();
				ConstantPoolType type = ConstantPoolType.getFor(tag);
				builder.addConstant(type.consume(in));
			}
			builder.setAccessFlags(in.readUnsignedShort());
			builder.setClassNameIndex(in.readUnsignedShort());
			builder.setSuperclassNameIndex(in.readUnsignedShort());
			builder.setInterfaceCount(in.readUnsignedShort());
			builder.setFieldsCount(in.readUnsignedShort());
			int methodsCount = in.readUnsignedShort();
			builder.setMethodsCount(methodsCount);
			for (int i = 0; i < methodsCount; i++) {
				MethodInfo info = createMethodInfo(in, builder);
				int attributeCount = in.readUnsignedShort();
				for (int j = 0; j < attributeCount; j++) {
					in.readUnsignedShort();
					int attributeLength = in.readInt();
					for (int k = 0; k < attributeLength; k++) {
						in.readByte();
					}
				}
				builder.addMethodInfo(info);
			}
			builder.setAttributesCount(in.readUnsignedShort());

			return builder.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		// For see table 4.2 in the jvm specs
		String result;
		while (!parameterValue.isEmpty()) {
			int lengthToRemove = 1;
			switch (parameterValue.charAt(0)) {
			case 'B':
				result = Byte.class.getName();
				break;
			case 'C':
				result = Character.class.getName();
				break;
			case 'D':
				result = Double.class.getName();
				break;
			case 'F':
				result = Float.class.getName();
				break;
			case 'I':
				result = Integer.class.getName();
				break;
			case 'J':
				result = Long.class.getName();
				break;
			case 'L':
				result = parameterValue.substring(1, parameterValue.indexOf(';')).replace('/', '.');
				lengthToRemove = result.length() + 2;
				break;
			case 'S':
				result = Short.class.getName();
				break;
			case 'Z':
				result = Boolean.class.getName();
				break;
			default:
				throw new RuntimeException("Unkown type: " + parameterValue);
			}
			parameterValue = parameterValue.substring(lengthToRemove);
			parameters.add(result);
		}
		return parameters;
	}
}
