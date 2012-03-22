package com.aldercape.internal.analyzer.outputformats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.aldercape.internal.analyzer.PackageInfo;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class DotOutputFormatTest {

	@Test
	public void test() throws IOException {
		PackageDependencyReport report = new PackageDependencyReport() {
			@Override
			public SortedSet<PackageInfo> getPackages() {
				TreeSet<PackageInfo> result = new TreeSet<>();
				result.add(new PackageInfo("java.lang"));
				result.add(new PackageInfo("java.util"));
				return result;
			}

			@Override
			public SortedSet<PackageInfo> getEfferentFor(PackageInfo packageName) {
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
			public SortedSet<PackageInfo> getAfferentFor(PackageInfo packageName) {
				SortedSet<PackageInfo> result = new TreeSet<>();
				;
				if (packageName.equals(new PackageInfo("java.lang"))) {
					result.add(new PackageInfo("java.lang"));
				} else {
					result.add(new PackageInfo("java.util"));
				}
				return result;
			}
		};
		DotOutputFormat format = new DotOutputFormat();
		StringWriter writer = new StringWriter();
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"java.lang\";\n";
		expected += "\"java.util\";\n";

		expected += "\"java.lang\" -> \"java.util\";\n";
		expected += "\"java.util\" -> \"java.lang\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}
}
