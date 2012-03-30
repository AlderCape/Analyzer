package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class ClassDependencyReportTest {

	@Test
	public void singleClass() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfoStub classInfo1 = new ClassInfoStub(true);
		report.addClass(classInfo1);
		assertEquals(Collections.singleton(classInfo1), report.getClasses());
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
	}

	@Test
	public void twoClassesNoDependencies() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfo classInfo1 = new ClassInfoStub(true);
		ClassInfo classInfo2 = new ClassInfoStub(true);
		report.addClass(classInfo1);
		report.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, report.getClasses());
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo2));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo2));
	}

	@Test
	public void twoClassesOneDependencies() {
		ClassDependencyReport report = new ClassDependencyReport();
		ClassInfoStub classInfo1 = new ClassInfoStub(true);
		ClassInfo classInfo2 = new ClassInfoStub(true);
		classInfo1.setClassDependency(Collections.singleton(classInfo2));
		report.addClass(classInfo1);
		report.addClass(classInfo2);
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(classInfo1);
		expected.add(classInfo2);
		assertEquals(expected, report.getClasses());
		assertEquals(Collections.singleton(classInfo2), report.getChildrenFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getParentsFor(classInfo1));
		assertEquals(Collections.emptySet(), report.getChildrenFor(classInfo2));
		assertEquals(Collections.singleton(classInfo1), report.getParentsFor(classInfo2));
	}

}