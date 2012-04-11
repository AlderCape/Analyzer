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
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;

public class ParsedClassDetailsTest {

	private Set<MethodInfo> methods;
	private List<FieldInfo> fields;
	private List<String> interfaces;
	private ClassInfo currentClass;

	@Before
	public void setUp() {
		methods = new HashSet<>();
		fields = new ArrayList<>();
		interfaces = new ArrayList<>();
		currentClass = new ClassInfoBase("testpackage.TestClass");
	}

	@Test
	public void noDependenciesTwoMethods() {
		List<String> noParameters = Collections.emptyList();
		methods.add(new ParsedMethodInfo(0, "method1", noParameters));
		methods.add(new ParsedMethodInfo(0, "method2", noParameters));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));
		assertEquals(Collections.singleton(new ClassInfoBase("testpackage.A")), classDetails.getClassDependencies(currentClass));
	}

	@Test
	public void oneMethodWithDependenciesTwoMethods() {

		List<String> noParameters = Collections.emptyList();
		methods.add(new ParsedMethodInfo(0, "method1", noParameters));
		methods.add(new ParsedMethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));

		Set<ClassInfoBase> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoBase("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClassDependencies, classDetails.getClassDependencies(currentClass));
	}

	@Test
	public void twoMethodsWithDependencies() {
		methods.add(new ParsedMethodInfo(0, "method1", Collections.singletonList("java.util.List")));
		methods.add(new ParsedMethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));

		Set<ClassInfoBase> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoBase("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoBase("java.util.List"));
		expectedClassDependencies.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClassDependencies, classDetails.getClassDependencies(currentClass));
	}

	@Test
	public void onSamePackageGetsFiltered() {
		methods.add(new ParsedMethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new ParsedMethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));

		Set<ClassInfoBase> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoBase("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoBase("testpackage.List"));
		expectedClassDependencies.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClassDependencies, classDetails.getClassDependencies(new ClassInfoBase("testpackage.TestClass")));
	}

	@Test
	public void fieldsAreIncluded() {
		fields.add(new FieldInfo(0, "str", "java.lang.String"));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));

		Set<ClassInfoBase> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoBase("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClassDependencies, classDetails.getClassDependencies(currentClass));
	}

	@Test
	public void methodsAreIncluded() {
		MethodInfo publicMethod = new ParsedMethodInfo(0x1, "publicMethod", emptyStringList());
		MethodInfo privateMethod = new ParsedMethodInfo(0x2, "privateMethod", emptyStringList());
		MethodInfo protectedMethod = new ParsedMethodInfo(0x4, "protectedMethod", emptyStringList());
		methods.add(publicMethod);
		methods.add(privateMethod);
		methods.add(protectedMethod);
		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));
		assertEquals(methods, classDetails.getMethods());
		assertEquals(Collections.singletonList(publicMethod), classDetails.getPublicMethods());
		assertEquals(Collections.singletonList(privateMethod), classDetails.getPrivateMethods());
		assertEquals(Collections.singletonList(protectedMethod), classDetails.getProtectedMethods());
		try {
			classDetails.getMethods().clear();
			fail("Should be unmodifiable");
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void constructorsAreNotMethods() {
		MethodInfo publicMethod = new ParsedMethodInfo(0x1, "<init>", emptyStringList());
		methods.add(publicMethod);
		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));
		assertEquals(Collections.emptySet(), classDetails.getMethods());
	}

	protected List<String> emptyStringList() {
		return Collections.emptyList();
	}
}
