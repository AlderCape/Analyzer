package com.aldercape.internal.analyzer.javaclass;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.reports.ClassInfoStub;

public class JavaClassTest {

	@Test
	public void noDependenciesTwoMethods() {
		JavaClass info = new JavaClass(new VersionInfo(0, 0, 0));
		info.setClassName("testpackage.TestClass");
		info.setSuperclassName("testpackage.A");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", noParameters));
		info.setMethods(methods);
		assertTrue(info.getPackageDependencies().isEmpty());
		assertEquals(Collections.singleton(new ClassInfoStub("testpackage.A")), info.getClassDependencies());
	}

	@Test
	public void oneMethodWithDependenciesTwoMethods() {
		JavaClass info = new JavaClass(new VersionInfo(0, 0, 0));
		info.setClassName("testpackage.TestClass");
		info.setSuperclassName("testpackage.A");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), info.getPackageDependencies());
		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}

	@Test
	public void twoMethodsWithDependencies() {
		JavaClass info = new JavaClass(new VersionInfo(0, 0, 0));
		info.setClassName("testpackage.TestClass");
		info.setSuperclassName("testpackage.A");

		List<MethodInfo> methods = new ArrayList<>();
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("java.util.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("java.util"));
		assertEquals(expected, info.getPackageDependencies());
		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("java.util.List"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}

	@Test
	public void onSamePackageGetsFiltered() {
		JavaClass info = new JavaClass(new VersionInfo(0, 0, 0));
		info.setClassName("testpackage.TestClass");
		info.setSuperclassName("testpackage.A");

		List<MethodInfo> methods = new ArrayList<>();
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		assertEquals(expected, info.getPackageDependencies());

		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("testpackage.List"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}

	@Test
	public void fieldsAreIncluded() {
		JavaClass info = new JavaClass(new VersionInfo(0, 0, 0));
		info.setClassName("testpackage.TestClass");
		info.setSuperclassName("testpackage.A");

		List<FieldInfo> fields = new ArrayList<>();
		fields.add(new FieldInfo(0, "str", "java.lang.String"));
		info.setFields(fields);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		assertEquals(expected, info.getPackageDependencies());

		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}

}
