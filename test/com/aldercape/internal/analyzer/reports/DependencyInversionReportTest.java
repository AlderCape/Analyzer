package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class DependencyInversionReportTest {

	private DependencyInversionReport report;
	private ClassInfoStub classInfo1;
	private ClassInfo concreateClass;
	private ClassInfo abstractClass;

	@Before
	public void setUp() {
		report = new DependencyInversionReport();
		classInfo1 = new ClassInfoStub("FirstClass");
		abstractClass = new ClassInfoStub("Abstract");
		((ClassInfoStub) abstractClass).setAbstract(true);
		concreateClass = new ClassInfoStub("Concreate");
	}

	@Test
	public void noDependencies() {
		report.addClass(classInfo1);
		assertEquals(1, report.getDependencyInversionFor(classInfo1), 0);
	}

	@Test
	public void onlyAbstractDependencies() {
		classInfo1.setClassDependency(Collections.singleton(abstractClass));
		report.addClass(classInfo1);
		assertEquals(1, report.getDependencyInversionFor(classInfo1), 0);
	}

	@Test
	public void noAbstractDependencies() {
		classInfo1.setClassDependency(Collections.singleton(concreateClass));
		report.addClass(classInfo1);
		assertEquals(0, report.getDependencyInversionFor(classInfo1), 0);
	}

	@Test
	public void oneAbstractAndOneConcreteDependencies() {
		Set<ClassInfo> dependencies = new HashSet<>();
		dependencies.add(abstractClass);
		dependencies.add(concreateClass);
		classInfo1.setClassDependency(dependencies);
		report.addClass(classInfo1);
		assertEquals(0.5, report.getDependencyInversionFor(classInfo1), 0);
	}
}
