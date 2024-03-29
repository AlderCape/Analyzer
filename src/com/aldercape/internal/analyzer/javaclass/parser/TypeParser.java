package com.aldercape.internal.analyzer.javaclass.parser;

import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.javaclass.Constant;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;

public class TypeParser {

	private ConstantPoolInfo constantPool;

	public TypeParser(ConstantPoolInfo constantPool) {
		this.constantPool = constantPool;
	}

	public String nextTypeFromString(String parameterValue) {
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
		case '[':
			return this.nextTypeFromString(parameterValue.substring(1));
		case 'S':
			return Short.class.getName();
		case 'Z':
			return Boolean.class.getName();
		default:
			throw new RuntimeException("Unkown type: " + parameterValue);
		}
	}

	public String parseTypeFromIndex(int index) {
		return nextTypeFromString((String) constantPool.get(index).getObject());
	}

	List<String> populateMethodParameters(Constant constant) {
		String descriptor = (String) constant.getObject();
		String parameterValue = descriptor.substring(descriptor.indexOf('(') + 1, descriptor.indexOf(')'));
		List<String> parameters = new ArrayList<>();
		while (!parameterValue.isEmpty()) {
			String result = nextTypeFromString(parameterValue);
			parameterValue = parameterValue.substring(isObject(parameterValue) ? 2 + result.length() : 1);
			parameters.add(result);
		}
		return parameters;
	}

	boolean isObject(String parameterValue) {
		return parameterValue.charAt(0) == 'L';
	}

	public String nextObjectFromIndex(int classConstantIndex) {
		return nextTypeFromString("L" + constantPool.get(classConstantIndex).getName(constantPool) + ";");
	}

}
