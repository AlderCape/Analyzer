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

	@Before
	public void setUp() {
		abstractClass = new ClassInfoStub(true);
		concreteClass = new ClassInfoStub(false);
	}

	@Test
	public void noDependenciesOneClass() {
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(concreteClass);

		Set<PackageInfo> expected = Collections.singleton(new PackageInfo("base"));
		assertEquals(expected, report.getPackages());
		assertEquals(0, report.getInstability(new PackageInfo("base")), 0);
	}

	@Test
	public void dependsOnJavaLangOneClass() {
		PackageDependencyReport report = new PackageDependencyReport();
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
		PackageDependencyReport report = new PackageDependencyReport();
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
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(concreteClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(0f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void abstractnessOneAbstractClass() {
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(abstractClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(1f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void abstractnessOneConcreteOneAbstractClass() {
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(concreteClass);
		report.addClass(abstractClass);

		Set<PackageInfo> packages = report.getPackages();
		assertEquals(Collections.singleton(new PackageInfo("base")), packages);
		assertEquals(0.5f, report.getAbstractness(new PackageInfo("base")), 0);
	}

	@Test
	public void testDistanceStableConcrete() {
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(concreteClass);

		assertEquals(1f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceStableAbstract() {
		PackageDependencyReport report = new PackageDependencyReport();
		report.addClass(abstractClass);

		assertEquals(0f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceInstableConcrete() {
		PackageDependencyReport report = new PackageDependencyReport();
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(concreteClass);

		assertEquals(0f, report.getDistance(new PackageInfo("base")), 0);

	}

	@Test
	public void testDistanceInstableAbstract() {
		PackageDependencyReport report = new PackageDependencyReport();
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		report.addClass(abstractClass);

		assertEquals(1f, report.getDistance(new PackageInfo("base")), 0);
	}

	@Test
	public void ignoresSpecifiedPackages() {
		PackageDependencyReport baseReport = new PackageDependencyReport();
		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<PackageInfo>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("other"));
		concreteClass.setDependencies(Collections.singleton(new PackageInfo("java.lang")));
		baseReport.addClass(concreteClass);
		abstractClass.setPackageName("other");
		abstractClass.setDependencies(Collections.singleton(new PackageInfo("base")));
		baseReport.addClass(abstractClass);

		assertEquals(Collections.singleton(new PackageInfo("base")), report.getIncludedTypes());
		assertEquals(Collections.emptySet(), report.getChildrenFor(new PackageInfo("base")));
		assertEquals(Collections.emptySet(), report.getParentsFor(new PackageInfo("base")));
		assertEquals(0.5, baseReport.getInstability(new PackageInfo("base")), 0);

	}
}
