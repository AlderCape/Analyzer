package com.aldercape.internal.analyzer.javaclass;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

public class ClassFinder {

	public Set<File> getClassFilesIn(File file) {
		Set<File> result = new HashSet<>();
		File[] listFiles = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().endsWith(".class");
			}
		});
		for (File child : listFiles) {
			if (child.isDirectory()) {
				result.addAll(getClassFilesIn(child));
			} else {
				result.add(child);
			}
		}
		return result;
	}

}
