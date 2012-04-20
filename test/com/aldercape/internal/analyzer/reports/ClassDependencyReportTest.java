package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassDependencyReportTest {

	private ClassDependencyReport baseReport;

	@Before
	public void setUp() {
		baseReport = new ClassDependencyReport("Report name");
	}

	@Test
	public void singleClass() {
		ClassInfo classInfo1 = new ClassInfoStub("FirstClass");
		baseReport.addClass(classInfo1);
		assertEquals("Report name", baseReport.getReportName());
		assertEquals(Collections.singleton(classInfo1), baseReport.getIncludedTypes());
		assertEquals(Collections.emptySet(), baseReport.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), baseReport.getParentsFor(classInfo1));
	}

	@Test
	public void twoClassesNoDependencies() {
		ClassInfo classInfo1 = new ClassInfoStub("FirstClass");
		ClassInfo classInfo2 = new ClassInfoStub("SecondClass");
		baseReport.addClass(classInfo1);
		baseReport.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, baseReport.getIncludedTypes());
		assertEquals(Collections.emptySet(), baseReport.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), baseReport.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), baseReport.getChildrenFor(classInfo2));
		assertEquals(Collections.emptySet(), baseReport.getParentsFor(classInfo2));
	}

	@Test
	public void twoClassesOneDependencies() {
		ClassInfoStub classInfo1 = new ClassInfoStub("FirstClass");
		ClassInfo classInfo2 = new ClassInfoStub("SecondClass");
		classInfo1.setClassDependency(Collections.singleton(classInfo2));
		baseReport.addClass(classInfo1);
		baseReport.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, baseReport.getIncludedTypes());
		assertEquals(Collections.singleton(classInfo2), baseReport.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), baseReport.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), baseReport.getChildrenFor(classInfo2));
		assertEquals(Collections.singleton(classInfo1), baseReport.getParentsFor(classInfo2));
	}

	@Test
	public void ignoresSpecifiedPackages() {
		FilteredDependencyReport<ClassInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("other"));
		ClassInfoStub concreteClass = new ClassInfoStub("test.FirstClass");
		concreteClass.setPackageName("test");
		ClassInfoStub abstractClass = new ClassInfoStub("other.SecondClass");
		abstractClass.setPackageName("other");

		concreteClass.addClassDependency(new ClassInfoBase("java.lang.String"));
		baseReport.addClass(concreteClass);
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("base")));
		baseReport.addClass(abstractClass);

		assertEquals(Collections.singleton(new ClassInfoBase("test.FirstClass")), report.getIncludedTypes());
		assertEquals(Collections.emptySet(), report.getChildrenFor(concreteClass));
		assertEquals(Collections.emptySet(), report.getParentsFor(concreteClass));
	}
}
