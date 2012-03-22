package com.aldercape.internal.analyzer.javaclass;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ClassFinderTest {

	@Test
	public void shouldFindAllFilesInDirectoryEndingOnClass_SingleFile() {
		ClassFinder finder = new ClassFinder();
		Set<File> classFiles = finder.getClassFilesIn(new File("bin/testdata/filefinder/singleclasspackage"));
		assertEquals(Collections.singleton(new File("bin/testdata/filefinder/singleclasspackage/TheOnlyClass.class")), classFiles);
	}

	@Test
	public void shouldFindAllFilesInDirectoryEndingOnClass_MultipleFiles() {
		ClassFinder finder = new ClassFinder();
		Set<File> classFiles = finder.getClassFilesIn(new File("bin/testdata/filefinder/multiplefiles"));
		Set<File> expected = new HashSet<>();
		expected.add(new File("bin/testdata/filefinder/multiplefiles/FirstClass.class"));
		expected.add(new File("bin/testdata/filefinder/multiplefiles/SecondClass.class"));
		assertEquals(expected, classFiles);
	}

	@Test
	public void shouldFindAllFilesInDirectoryAndSubDirectoriesEndingOnClass() {
		ClassFinder finder = new ClassFinder();
		Set<File> classFiles = finder.getClassFilesIn(new File("bin/testdata/filefinder"));
		Set<File> expected = new HashSet<>();
		expected.add(new File("bin/testdata/filefinder/multiplefiles/FirstClass.class"));
		expected.add(new File("bin/testdata/filefinder/multiplefiles/SecondClass.class"));
		expected.add(new File("bin/testdata/filefinder/singleclasspackage/TheOnlyClass.class"));
		assertEquals(expected, classFiles);
	}
}
