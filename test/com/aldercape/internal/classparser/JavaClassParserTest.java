package com.aldercape.internal.classparser;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import testdata.ClassWithAllPrimitivesInOneConstructor;
import testdata.ClassWithOneMethod;
import testdata.ClassWithTwoConstructors;
import testdata.EmptyClass;
import testdata.EmptyInterface;

public class JavaClassParserTest {

	@Test
	public void convertToFile() {
		File result = JavaClassParser.convertToFile(EmptyClass.class.getName());
		assertTrue(result.exists());
		assertEquals(new File("bin/testdata/EmptyClass.class").getAbsolutePath(), result.getAbsolutePath());
	}

	@Test
	public void parsesEmptyClass() {
		JavaClass result = new JavaClassParser(EmptyClass.class.getName()).parse();
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
	}

	@Test
	public void parsesEmptyInterface() {
		JavaClass result = new JavaClassParser(EmptyInterface.class.getName()).parse();
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
	}

	@Test
	public void parsesClassWithOneMethod() {
		JavaClass result = new JavaClassParser(ClassWithOneMethod.class.getName()).parse();
		assertEquals(2, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals("test", result.getMethod(1).getName());
	}

	@Test
	public void parsesClassWithTwoConstructors() {
		JavaClass result = new JavaClassParser(ClassWithTwoConstructors.class.getName()).parse();
		assertEquals(2, result.getMethodsCount());
		assertEquals("<init>", result.getMethod(0).getName());
		assertEquals(0, result.getMethod(0).getParameterCount());
		assertEquals("<init>", result.getMethod(1).getName());
		assertEquals(2, result.getMethod(1).getParameterCount());
		assertEquals("java.lang.String", result.getMethod(1).getParameterType(0));
		assertEquals("java.lang.Integer", result.getMethod(1).getParameterType(1));
	}

	@Test
	public void parsesClassWithAllPrimitivesInOneConstructor() {
		JavaClass result = new JavaClassParser(ClassWithAllPrimitivesInOneConstructor.class.getName()).parse();
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
}
