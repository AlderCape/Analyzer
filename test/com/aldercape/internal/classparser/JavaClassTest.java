package com.aldercape.internal.classparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class JavaClassTest {

	@Test
	public void noDependenciesTwoMethods() {
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", noParameters));
		info.setMethods(methods);
		assertTrue(info.getPackageDependencies().isEmpty());
	}

	@Test
	public void oneMethodWithDependenciesTwoMethods() {
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), info.getPackageDependencies());
	}

	@Test
	public void twoMethodsWithDependencies() {
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("java.util.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("java.util"));
		assertEquals(expected, info.getPackageDependencies());
	}

	@Test
	public void onSamePackageGetsFiltered() {
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));
		info.setMethods(methods);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		assertEquals(expected, info.getPackageDependencies());
	}

	@Test
	public void fieldsAreIncluded() {
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<FieldInfo> fields = new ArrayList<>();
		fields.add(new FieldInfo(0, "str", "java.lang.String"));
		info.setFields(fields);
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		assertEquals(expected, info.getPackageDependencies());
	}

}
