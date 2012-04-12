package com.aldercape.internal.analyzer;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.javaclass.ParsedClassDetails;
import com.aldercape.internal.analyzer.javaclass.ParsedMethodInfo;
import com.aldercape.internal.analyzer.javaclass.VersionInfo;

public class ClassInfoBaseTest {

	@Test
	public void classDependencies() {
		Set<MethodInfo> methods = new HashSet<>();
		List<String> interfaces = Collections.emptyList();
		List<FieldInfo> fields = Collections.emptyList();

		methods.add(new ParsedMethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new ParsedMethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));
		ClassInfoBase classInfo = new ClassInfoBase("testpackage.TestClass", classDetails);

		Set<ClassInfoBase> expectedClassDependencies = new HashSet<>();
		expectedClassDependencies.add(new ClassInfoBase("testpackage.A"));
		expectedClassDependencies.add(new ClassInfoBase("testpackage.List"));
		expectedClassDependencies.add(new ClassInfoBase("java.lang.String"));
		assertEquals(expectedClassDependencies, classInfo.getClassDependencies());
	}

	@Test
	public void packageDependencies() {
		Set<MethodInfo> methods = new HashSet<>();
		List<String> interfaces = Collections.emptyList();
		List<FieldInfo> fields = Collections.emptyList();

		methods.add(new ParsedMethodInfo(0, "method1", Collections.singletonList("testpackage.List")));
		methods.add(new ParsedMethodInfo(0, "method2", Collections.singletonList("java.lang.String")));

		ParsedClassDetails classDetails = new ParsedClassDetails(0, new ClassInfoBase("testpackage.A"), interfaces, fields, methods, new AttributeInfo(), new VersionInfo(0, 0, 0));
		ClassInfoBase classInfo = new ClassInfoBase("testpackage.TestClass", classDetails);

		Set<PackageInfo> expectedPackageDependencies = new HashSet<>();
		expectedPackageDependencies.add(new PackageInfo("java.lang"));
		assertEquals(new PackageInfo("testpackage"), classInfo.getPackage());
		assertEquals(expectedPackageDependencies, classInfo.getPackageDependencies());
	}

}
