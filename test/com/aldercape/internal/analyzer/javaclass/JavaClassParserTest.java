package com.aldercape.internal.analyzer.javaclass;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import testdata.ClassWithAllPrimitivesInOneConstructor;
import testdata.ClassWithAnInterface;
import testdata.ClassWithClassAnnotation;
import testdata.ClassWithFieldAnnotation;
import testdata.ClassWithOneField;
import testdata.ClassWithOneMethod;
import testdata.ClassWithRefInMethod;
import testdata.ClassWithTwoConstructors;
import testdata.EmptyClass;
import testdata.EmptyInterface;

import com.aldercape.internal.analyzer.PackageInfo;

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
		assertEquals(15, result.getConstantPoolSize());
		assertTrue(result.isPublic());
		assertEquals(EmptyClass.class.getName(), result.getClassName());
		assertEquals(EmptyClass.class.getSuperclass().getName(), result.getSuperclassName());
		assertEquals(0, result.getInterfaceCount());
		assertEquals(0, result.getFieldsCount());
		assertEquals(1, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals(1, result.getAttributesCount());
		assertFalse(result.isAbstract());
	}

	@Test
	public void parsesEmptyInterface() throws Exception {
		JavaClass result = new JavaClassParser().parse(EmptyInterface.class.getName());
		assertEquals(0xcafebabe, result.getMagic());
		assertEquals(0, result.getMinor());
		assertEquals(51, result.getMajor());
		assertEquals(6, result.getConstantPoolSize());
		assertTrue(result.isPublic());
		assertEquals(EmptyInterface.class.getName(), result.getClassName());
		assertEquals(Object.class.getName(), result.getSuperclassName());
		assertEquals(0, result.getInterfaceCount());
		assertEquals(0, result.getFieldsCount());
		assertEquals(0, result.getMethodsCount());
		assertEquals(1, result.getAttributesCount());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneMethod() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithOneMethod.class.getName());
		assertEquals(2, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals("test", result.getMethod(1).getName());
	}

	@Test
	public void parsesClassWithTwoConstructors() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithTwoConstructors.class.getName());
		assertEquals(2, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals(0, result.getMethod(0).getParameterCount());
		assertEquals("<init>", result.getMethod(1).getName());
		assertEquals(2, result.getMethod(1).getParameterCount());
		assertEquals("java.lang.String", result.getMethod(1).getParameterType(0));
		assertEquals("java.lang.Integer", result.getMethod(1).getParameterType(1));
	}

	@Test
	public void parsesClassWithAllPrimitivesInOneConstructor() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithAllPrimitivesInOneConstructor.class.getName());
		assertEquals(1, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals(8, result.getMethod(0).getParameterCount());
		assertEquals(Byte.class.getName(), result.getMethod(0).getParameterType(0));
		assertEquals(Short.class.getName(), result.getMethod(0).getParameterType(1));
		assertEquals(Integer.class.getName(), result.getMethod(0).getParameterType(2));
		assertEquals(Long.class.getName(), result.getMethod(0).getParameterType(3));
		assertEquals(Double.class.getName(), result.getMethod(0).getParameterType(4));
		assertEquals(Float.class.getName(), result.getMethod(0).getParameterType(5));
		assertEquals(Character.class.getName(), result.getMethod(0).getParameterType(6));
		assertEquals(Boolean.class.getName(), result.getMethod(0).getParameterType(7));
	}

	@Test
	public void parsesAbstractClassWithOneField() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithOneField.class.getName());
		assertEquals(1, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals(1, result.getFieldsCount());
		assertEquals(String.class.getName(), result.getField(0).getType());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneInterface() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithAnInterface.class.getName());
		assertEquals(1, result.getInterfaceCount());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
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
		assertEquals(2, result.getAttributesCount());
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseFieldWithAnnotation() throws Exception {
		JavaClass result = new JavaClassParser().parse(ClassWithFieldAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(1, result.getAttributesCount());
		assertEquals(expectedPackages, result.getPackageDependencies());
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
