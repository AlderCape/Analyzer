package com.aldercape.internal.analyzer.javaclass;

import java.util.HashMap;
import java.util.Map;

public class ClassRepository {

	private static ClassRepository instance = new ClassRepository();

	private Map<String, ClassInfoBase> loadedClasses = new HashMap<>();

	public static ClassInfoBase getClass(String className) {
		return instance.doGetClass(className);
	}

	private ClassInfoBase doGetClass(String className) {
		if (!loadedClasses.containsKey(className)) {
			loadedClasses.put(className, new ClassInfoBase(className));
		}
		return loadedClasses.get(className);
	}
}
