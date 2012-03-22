package com.aldercape.internal.analyzer.javaclass;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.FieldInfo;
import com.aldercape.internal.analyzer.MethodInfo;

public class JavaClassParser {

	private String className;

	public JavaClassParser(String name) {
		className = name;
	}

	public JavaClassParser(File file) throws FileNotFoundException {
		parse(new FileInputStream(file));
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
			FileInputStream in = new FileInputStream(convertToFile(className));
			return parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JavaClass parse(FileInputStream in) {
		return parse(new DataInputStream(in));
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
			int fieldsCount = in.readUnsignedShort();
			builder.setFieldsCount(fieldsCount);
			for (int i = 0; i < fieldsCount; i++) {
				int accessFlag = in.readUnsignedShort();
				String methodName = (String) builder.getConstant(in.readUnsignedShort()).getObject();
				Constant constant = builder.getConstant(in.readUnsignedShort());
				FieldInfo fieldInfo = new FieldInfo(accessFlag, methodName, nextTypeFromDescriptor((String) constant.getObject()));
				int attributesCount = in.readUnsignedShort();
				for (int j = 0; j < attributesCount; j++) {
					in.readUnsignedShort();
					int attributeLength = in.readInt();
					for (int k = 0; k < attributeLength; k++) {
						in.readByte();
					}
				}
				builder.addFieldInfo(fieldInfo);

			}

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
		while (!parameterValue.isEmpty()) {
			String result = nextTypeFromDescriptor(parameterValue);
			parameterValue = parameterValue.substring(isObject(parameterValue) ? 2 + result.length() : 1);
			parameters.add(result);
		}
		return parameters;
	}

	protected String nextTypeFromDescriptor(String parameterValue) {
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
