package com.aldercape.internal.analyzer.javaclass;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.reports.ClassInfoStub;

public class JavaClassTest {

	private List<MethodInfo> methods;
	private List<FieldInfo> fields;
	private List<String> interfaces;

	@Before
	public void setUp() {
		methods = new ArrayList<>();
		fields = new ArrayList<>();
		interfaces = new ArrayList<>();
	}

	@Test
	public void noDependenciesTwoMethods() {
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", noParameters));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, "testpackage.A", interfaces, fields, methods, new AttributeInfo());
		JavaClass info = new JavaClass("testpackage.TestClass", new VersionInfo(0, 0, 0), classDetails);

		assertTrue(info.getPackageDependencies().isEmpty());
		assertEquals(Collections.singleton(new ClassInfoStub("testpackage.A")), info.getClassDependencies());
	}

	@Test
	public void oneMethodWithDependenciesTwoMethods() {

		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, "testpackage.A", interfaces, fields, methods, new AttributeInfo());
		JavaClass info = new JavaClass("testpackage.TestClass", new VersionInfo(0, 0, 0), classDetails);

		assertEquals(Collections.singleton(new PackageInfo("java.lang")), info.getPackageDependencies());
		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}

	@Test
	public void twoMethodsWithDependencies() {
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("java.util.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, "testpackage.A", interfaces, fields, methods, new AttributeInfo());
		JavaClass info = new JavaClass("testpackage.TestClass", new VersionInfo(0, 0, 0), classDetails);

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
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new MethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, "testpackage.A", interfaces, fields, methods, new AttributeInfo());
		JavaClass info = new JavaClass("testpackage.TestClass", new VersionInfo(0, 0, 0), classDetails);

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
		fields.add(new FieldInfo(0, "str", "java.lang.String"));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, "testpackage.A", interfaces, fields, methods, new AttributeInfo());
		JavaClass info = new JavaClass("testpackage.TestClass", new VersionInfo(0, 0, 0), classDetails);

		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		assertEquals(expected, info.getPackageDependencies());

		Set<ClassInfoStub> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoStub("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoStub("java.lang.String"));
		assertEquals(expectedClassDependencies, info.getClassDependencies());
	}
}
