package com.aldercape.internal.analyzer.classmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassRepository {

	public static interface ClassRepositoryListener {

		public void classCreated(ClassInfo newClass);
	}

	private Set<ClassRepositoryListener> listeners = new HashSet<>();

	private Map<String, ClassInfoBase> loadedClasses = new HashMap<>();

	public ClassInfoBase getClass(String className) {
		if (!loadedClasses.containsKey(className)) {
			loadedClasses.put(className, new ClassInfoBase(className));
			notifyListeners(loadedClasses.get(className));
		}
		return loadedClasses.get(className);
	}

	private void notifyListeners(ClassInfoBase classInfoBase) {
		for (ClassRepositoryListener listener : listeners) {
			listener.classCreated(classInfoBase);
		}
	}

	public void addListener(ClassRepositoryListener l) {
		listeners.add(l);
	}
}
