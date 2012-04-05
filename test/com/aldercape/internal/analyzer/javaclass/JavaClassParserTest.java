package com.aldercape.internal.analyzer.javaclass;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import testdata.ClassWithAllPrimitivesInOneConstructor;
import testdata.ClassWithAnInterface;
import testdata.ClassWithArrays;
import testdata.ClassWithClassAnnotation;
import testdata.ClassWithException;
import testdata.ClassWithFieldAnnotation;
import testdata.ClassWithInnerClass;
import testdata.ClassWithMethodAnnotation;
import testdata.ClassWithOneField;
import testdata.ClassWithOneMethod;
import testdata.ClassWithRefInMethod;
import testdata.ClassWithTwoConstructors;
import testdata.EmptyClass;
import testdata.EmptyInterface;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.reports.ClassInfoStub;

public class JavaClassParserTest {

	@Test
	public void convertToFile() {
		File result = JavaClassParser.convertToFile(EmptyClass.class.getName());
		assertTrue(result.exists());
		assertEquals(new File("bin/testdata/EmptyClass.class").getAbsolutePath(), result.getAbsolutePath());
	}

	@Test
	public void parsesEmptyClass() throws Exception {
		JavaClass result = new JavaClassParser().parse(EmptyClass.class.getName());
		assertEquals(0xcafebabe, result.getMagic());
		assertEquals(0, result.getMinor());
		assertEquals(51, result.getMajor());
		assertTrue(result.isPublic());
		assertEquals(EmptyClass.class.getName(), result.getName());
		assertFalse(result.isAbstract());
		assertFalse(result.isInnerClass());
		assertEquals(Collections.singleton(new ClassInfoStub("java.lang.Object")), result.getClassDependencies());
	}

	@Test
	public void parsesEmptyInterface() throws Exception {
		JavaClass result = new JavaClassParser().parse(EmptyInterface.class.getName());
		assertEquals(0xcafebabe, result.getMagic());
		assertEquals(0, result.getMinor());
		assertEquals(51, result.getMajor());
		assertTrue(result.isPublic());
		assertEquals(EmptyInterface.class.getName(), result.getName());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneMethod() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithOneMethod.class.getName());
		assertEquals(Collections.singleton(new ClassInfoStub("java.lang.Object")), result.getClassDependencies());
	}

	@Test
	public void parsesClassWithTwoConstructors() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithTwoConstructors.class.getName());

		Set<ClassInfo> expected = new HashSet<>();
		expected.add(new ClassInfoStub(String.class.getName()));
		expected.add(new ClassInfoStub(Object.class.getName()));
		expected.add(new ClassInfoStub(Integer.class.getName()));

		assertEquals(expected, result.getClassDependencies());
	}

	@Test
	public void parsesClassWithAllPrimitivesInOneConstructor() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithAllPrimitivesInOneConstructor.class.getName());
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(new ClassInfoStub(Byte.class.getName()));
		expected.add(new ClassInfoStub(Short.class.getName()));
		expected.add(new ClassInfoStub(Integer.class.getName()));
		expected.add(new ClassInfoStub(Long.class.getName()));
		expected.add(new ClassInfoStub(Double.class.getName()));
		expected.add(new ClassInfoStub(Float.class.getName()));
		expected.add(new ClassInfoStub(Character.class.getName()));
		expected.add(new ClassInfoStub(Boolean.class.getName()));
		expected.add(new ClassInfoStub(Object.class.getName()));
		assertEquals(expected, result.getClassDependencies());
	}

	@Test
	public void parsesAbstractClassWithOneField() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithOneField.class.getName());
		Set<ClassInfo> expectedClasses = new HashSet<>();
		expectedClasses.add(new ClassInfoStub("java.lang.Object"));
		expectedClasses.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClasses, result.getClassDependencies());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneInterface() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithAnInterface.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
		Set<ClassInfo> expectedClasses = new HashSet<>();
		expectedClasses.add(new ClassInfoStub("java.lang.Object"));
		expectedClasses.add(new ClassInfoStub("java.util.Comparator"));
		assertEquals(expectedClasses, result.getClassDependencies());
	}

	@Test
	public void parseClassDependsOnSuperclassPackage() throws Exception {
		JavaClass result = new JavaClassParser().parse(EmptyClass.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseClassWithAnnotation() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithClassAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseFieldWithAnnotation() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithFieldAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseMethodWithAnnotation() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithMethodAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseFieldArray() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithArrays.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		expectedPackages.add(new PackageInfo("java.util.concurrent"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseMethodException() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithException.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		expectedPackages.add(new PackageInfo("java.util.concurrent"));

		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseInnerClass() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithInnerClass.InnerStatic.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
		assertTrue(result.isInnerClass());
	}

	@Test
	@Ignore
	public void parseClassWithDependencyInMethod() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithRefInMethod.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}
}
