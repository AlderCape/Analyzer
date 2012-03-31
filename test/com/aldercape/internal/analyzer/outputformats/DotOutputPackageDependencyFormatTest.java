package com.aldercape.internal.analyzer.outputformats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class DotOutputPackageDependencyFormatTest {

	private PackageDependencyReport report;

	@Before
	public void setUp() {
		report = new PackageDependencyReport() {
			@Override
			public SortedSet<PackageInfo> getPackages() {
				TreeSet<PackageInfo> result = new TreeSet<>();
				result.add(new PackageInfo("java.lang"));
				result.add(new PackageInfo("java.util"));
				return result;
			}

			@Override
			public SortedSet<PackageInfo> getChildrenFor(PackageInfo packageName) {
				SortedSet<PackageInfo> result = new TreeSet<>();
				;
				if (packageName.equals(new PackageInfo("java.lang"))) {
					result.add(new PackageInfo("java.util"));
				} else {
					result.add(new PackageInfo("java.lang"));
				}
				return result;
			}

			@Override
			public SortedSet<PackageInfo> getParentsFor(PackageInfo packageName) {
				SortedSet<PackageInfo> result = new TreeSet<>();
				if (packageName.equals(new PackageInfo("java.lang"))) {
					result.add(new PackageInfo("java.lang"));
				} else {
					result.add(new PackageInfo("java.util"));
				}
				return result;
			}

			@Override
			public float getAbstractness(PackageInfo packageInfo) {
				if (packageInfo.equals(new PackageInfo("java.lang"))) {
					return 0.3f;
				} else {
					return 0.3f;
				}
			}

			@Override
			public float getDistance(PackageInfo packageInfo) {
				if (packageInfo.equals(new PackageInfo("java.lang"))) {
					return 0.5f;
				} else {
					return 0.1f;
				}
			}

			@Override
			public float getInstability(PackageInfo packageInfo) {
				if (packageInfo.equals(new PackageInfo("java.lang"))) {
					return 0.4f;
				} else {
					return 0.2f;
				}
			}
		};
	}

	@Test
	public void simpleOutput() throws IOException {
		DotOutputFormat<PackageInfo> format = new DotOutputFormat<>();
		StringWriter writer = new StringWriter();
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"java.lang\" [label=\"java.lang\"];\n";
		expected += "\"java.util\" [label=\"java.util\"];\n";

		expected += "\"java.lang\" -> \"java.util\";\n";
		expected += "\"java.util\" -> \"java.lang\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}

	@Test
	public void metricOutput() throws IOException {
		DotOutputFormat<PackageInfo> format = new DotOutputFormat<>(true);
		StringWriter writer = new StringWriter();
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"java.lang\" [label=\"java.lang\\n(Ca 1, Ce 1, A 0.3, I 0.4, D 0.5)\"];\n";
		expected += "\"java.util\" [label=\"java.util\\n(Ca 1, Ce 1, A 0.3, I 0.2, D 0.1)\"];\n";

		expected += "\"java.lang\" -> \"java.util\";\n";
		expected += "\"java.util\" -> \"java.lang\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}
}
