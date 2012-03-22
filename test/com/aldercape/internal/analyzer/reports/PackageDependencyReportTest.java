package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Test;

import com.aldercape.internal.analyzer.MethodInfo;
import com.aldercape.internal.analyzer.PackageInfo;
import com.aldercape.internal.analyzer.javaclass.JavaClass;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class PackageDependencyReportTest {

	@Test
	public void noDependenciesOneClass() {
		PackageDependencyReport report = new PackageDependencyReport();
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", noParameters));
		methods.add(new MethodInfo(0, "method2", noParameters));
		info.setMethods(methods);

		report.addClass(info);

		Set<PackageInfo> expected = Collections.singleton(new PackageInfo("testpackage"));
		assertEquals(expected, report.getPackages());
	}

	@Test
	public void dependsOnJavaLangOneClass() {
		PackageDependencyReport report = new PackageDependencyReport();
		JavaClass info = new JavaClass(0, 0, 0);
		info.setClassName("testpackage.TestClass");

		List<MethodInfo> methods = new ArrayList<>();
		List<String> noParameters = Collections.emptyList();
		methods.add(new MethodInfo(0, "method1", Collections.singletonList("java.lang.String")));
		methods.add(new MethodInfo(0, "method2", noParameters));
		info.setMethods(methods);

		report.addClass(info);

		SortedSet<PackageInfo> packages = report.getPackages();
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("testpackage"));
		assertEquals(expected, packages);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), report.getEfferentFor(new PackageInfo("testpackage")));
		assertEquals(Collections.singleton(new PackageInfo("testpackage")), report.getAfferentFor(new PackageInfo("java.lang")));
	}
}
