package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ClassDependencyReportTest {

	@Test
	public void singleClass() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfo classInfo1 = new ClassInfoStub("FirstClass");
		report.addClass(classInfo1);
		assertEquals(Collections.singleton(classInfo1), report.getIncludedTypes());
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
	}

	@Test
	public void twoClassesNoDependencies() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfo classInfo1 = new ClassInfoStub("FirstClass");
		ClassInfo classInfo2 = new ClassInfoStub("SecondClass");
		report.addClass(classInfo1);
		report.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, report.getIncludedTypes());
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo2));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo2));
	}

	@Test
	public void twoClassesOneDependencies() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfoStub classInfo1 = new ClassInfoStub("FirstClass");
		ClassInfo classInfo2 = new ClassInfoStub("SecondClass");
		classInfo1.setClassDependency(Collections.singleton(classInfo2));
		report.addClass(classInfo1);
		report.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, report.getIncludedTypes());
		assertEquals(Collections.singleton(classInfo2), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo2));
		assertEquals(Collections.singleton(classInfo1), report.getParentsFor(classInfo2));
	}

	@Test
	public void ignoresSpecifiedPackages() {
		ClassDependencyReport baseReport = new ClassDependencyReport();
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
