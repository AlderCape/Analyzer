package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class PackageDependencyReportTest {

	private ClassInfoStub abstractClass;
	private ClassInfoStub concreteClass;
	private PackageDependencyReport report;

	@Before
	public void setUp() {
		abstractClass = new ClassInfoStub(true);
		concreteClass = new ClassInfoStub(false);
		report = new PackageDependencyReport("Report name");
	}

	@Test
	public void noDependenciesOneClass() {
		report.addClass(concreteClass);

		Set<PackageInfo> expected = Collections.singleton(new PackageInfo("base"));
		assertEquals("Report name", report.getReportName());
		assertEquals(expected, report.getPackages());
		assertEquals(0, report.getInstability(new PackageInfo("base")), 0);
	}

	@Test
	public void dependsOnJavaLangOneClass() {
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(concreteClass);

		SortedSet<PackageInfo> packages = report.getPackages();
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("base"));
		assertEquals(expected, packages);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), report.getChildrenFor(new PackageInfo("base")));
		assertEquals(1, report.getInstability(new PackageInfo("base")), 0);

		assertEquals(Collections.singleton(new PackageInfo("base")), report.getParentsFor(new PackageInfo("java.lang")));
	}

	@Test
	public void oneEfferentAndOneAfferent() {
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(concreteClass);
		abstractClass.setPackageName("other");
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("base")));
		report.addClass(abstractClass);

		SortedSet<PackageInfo> packages = report.getPackages();
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("base"));
		expected.add(new PackageInfo("other"));
		assertEquals(expected, packages);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), report.getChildrenFor(new PackageInfo("base")));
		assertEquals(Collections.singleton(new PackageInfo("other")), report.getParentsFor(new PackageInfo("base")));
		assertEquals(0.5, report.getInstability(new PackageInfo("base")), 0);
	}

	@Test
	public void abstractnessOneConcreteClass() {
		report.addClass(concreteClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(0f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void abstractnessOneAbstractClass() {
		report.addClass(abstractClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(1f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void abstractnessOneConcreteOneAbstractClass() {
		report.addClass(concreteClass);
		report.addClass(abstractClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(0.5f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void testDistanceStableConcrete() {
		report.addClass(concreteClass);

		assertEquals(1f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceStableAbstract() {
		report.addClass(abstractClass);

		assertEquals(0f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceInstableConcrete() {
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(concreteClass);

		assertEquals(0f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceInstableAbstract() {
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(abstractClass);

		assertEquals(1f, report.getDistance(new PackageInfo("base")), 0);
	}

	@Test
	public void ignoresSpecifiedPackages() {
		FilteredDependencyReport<PackageInfo> filteredReport = new FilteredDependencyReport<PackageInfo>(report);
		filteredReport.ignorePackage(new PackageInfo("java.lang"));
		filteredReport.ignorePackage(new PackageInfo("other"));
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(concreteClass);
		abstractClass.setPackageName("other");
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("base")));
		report.addClass(abstractClass);

		assertEquals(Collections.singleton(new PackageInfo("base")), filteredReport.getIncludedTypes());
		assertEquals(Collections.emptySet(), filteredReport.getChildrenFor(new PackageInfo("base")));
		assertEquals(Collections.emptySet(), filteredReport.getParentsFor(new PackageInfo("base")));
		assertEquals(0.5, report.getInstability(new PackageInfo("base")), 0);

	}
}
