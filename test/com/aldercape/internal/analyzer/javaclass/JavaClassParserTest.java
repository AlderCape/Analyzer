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

public class JavaClassParserTest {

	@Test
	public void convertToFile() {
		File result = JavaClassParser.convertToFile(EmptyClass.class.getName());
		assertTrue(result.exists());
		assertEquals(new File("bin/testdata/EmptyClass.class").getAbsolutePath(), result.getAbsolutePath());
	}

	@Test
	public void parsesEmptyClass() throws Exception {
		ClassInfo result = new JavaClassParser().parse(EmptyClass.class.getName());
		assertEquals(EmptyClass.class.getName(), result.getName());
		assertFalse(result.isAbstract());
		assertFalse(result.isInnerClass());
		assertEquals(Collections.singleton(new ClassInfoBase("java.lang.Object")), result.getClassDependencies());
		assertNull(result.getEnclosingClass());
	}

	@Test
	public void parsesEmptyInterface() throws Exception {
		ClassInfo result = new JavaClassParser().parse(EmptyInterface.class.getName());
		assertEquals(EmptyInterface.class.getName(), result.getName());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneMethod() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithOneMethod.class.getName());
		assertEquals(Collections.singleton(new ClassInfoBase("java.lang.Object")), result.getClassDependencies());
	}

	@Test
	public void parsesClassWithTwoConstructors() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithTwoConstructors.class.getName());

		Set<ClassInfo> expected = new HashSet<>();
		expected.add(new ClassInfoBase(String.class.getName()));
		expected.add(new ClassInfoBase(Object.class.getName()));
		expected.add(new ClassInfoBase(Integer.class.getName()));

		assertEquals(expected, result.getClassDependencies());
	}

	@Test
	public void parsesClassWithAllPrimitivesInOneConstructor() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithAllPrimitivesInOneConstructor.class.getName());
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(new ClassInfoBase(Byte.class.getName()));
		expected.add(new ClassInfoBase(Short.class.getName()));
		expected.add(new ClassInfoBase(Integer.class.getName()));
		expected.add(new ClassInfoBase(Long.class.getName()));
		expected.add(new ClassInfoBase(Double.class.getName()));
		expected.add(new ClassInfoBase(Float.class.getName()));
		expected.add(new ClassInfoBase(Character.class.getName()));
		expected.add(new ClassInfoBase(Boolean.class.getName()));
		expected.add(new ClassInfoBase(Object.class.getName()));
		assertEquals(expected, result.getClassDependencies());
	}

	@Test
	public void parsesAbstractClassWithOneField() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithOneField.class.getName());
		Set<ClassInfo> expectedClasses = new HashSet<>();
		expectedClasses.add(new ClassInfoBase("java.lang.Object"));
		expectedClasses.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClasses, result.getClassDependencies());
		assertTrue(result.isAbstract());
	}

	@Test
	public void parsesClassWithOneInterface() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithAnInterface.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
		Set<ClassInfo> expectedClasses = new HashSet<>();
		expectedClasses.add(new ClassInfoBase("java.lang.Object"));
		expectedClasses.add(new ClassInfoBase("java.util.Comparator"));
		assertEquals(expectedClasses, result.getClassDependencies());
	}

	@Test
	public void parseClassDependsOnSuperclassPackage() throws Exception {
		ClassInfo result = new JavaClassParser().parse(EmptyClass.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseClassWithAnnotation() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithClassAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseFieldWithAnnotation() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithFieldAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseMethodWithAnnotation() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithMethodAnnotation.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("org.junit"));
		expectedPackages.add(new PackageInfo("java.lang"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseFieldArray() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithArrays.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		expectedPackages.add(new PackageInfo("java.util.concurrent"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}

	@Test
	public void parseMethodException() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithException.class.getName());
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
		assertEquals(new ClassInfoBase(ClassWithInnerClass.class.getName()), result.getEnclosingClass());
	}

	@Test
	public void parseInnerClassAndOuter() throws Exception {
		ClassInfo innerResult = new JavaClassParser().parse(ClassWithInnerClass.InnerStatic.class.getName());
		ClassInfo outerResult = new JavaClassParser().parse(ClassWithInnerClass.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, innerResult.getPackageDependencies());
		Set<ClassInfo> expectedClasses = new HashSet<>();
		expectedClasses.add(new ClassInfoBase("java.lang.Object"));
		expectedClasses.add(new ClassInfoBase("java.util.List"));
		assertEquals(expectedClasses, innerResult.getClassDependencies());

		assertTrue(innerResult.isInnerClass());
		assertEquals(new ClassInfoBase(ClassWithInnerClass.class.getName()), innerResult.getEnclosingClass());

		assertEquals(expectedPackages, outerResult.getPackageDependencies());
		assertEquals(expectedClasses, outerResult.getClassDependencies());
	}

	@Test
	public void sameDependencyInTwoClassesShouldReturnSameInstance() throws Exception {
		ClassInfo first = new JavaClassParser().parse(ClassWithOneMethod.class.getName());
		ClassInfo second = new JavaClassParser().parse(EmptyClass.class.getName());
		assertEquals(Collections.singleton(new ClassInfoBase("java.lang.Object")), first.getClassDependencies());
		assertEquals(Collections.singleton(new ClassInfoBase("java.lang.Object")), second.getClassDependencies());
		assertSame(first.getClassDependencies().iterator().next(), second.getClassDependencies().iterator().next());
	}

	@Test
	@Ignore
	public void parseClassWithDependencyInMethod() throws Exception {
		ClassInfo result = new JavaClassParser().parse(ClassWithRefInMethod.class.getName());
		Set<PackageInfo> expectedPackages = new HashSet<>();
		expectedPackages.add(new PackageInfo("java.lang"));
		expectedPackages.add(new PackageInfo("java.util"));
		assertEquals(expectedPackages, result.getPackageDependencies());
	}
}
