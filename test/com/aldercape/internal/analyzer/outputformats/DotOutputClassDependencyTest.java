package com.aldercape.internal.analyzer.outputformats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;

public class DotOutputClassDependencyTest {

	private ClassDependencyReport report;

	@Before
	public void setUp() {
		report = new ClassDependencyReport("Report name") {
			@Override
			public SortedSet<ClassInfo> getChildrenFor(ClassInfo classInfo) {

				if (classInfo.getName().equals("FirstClass")) {
					return new TreeSet<ClassInfo>(Collections.singleton(new ClassInfoBase("SecondClass")));
				}
				return new TreeSet<ClassInfo>(Collections.singleton(new ClassInfoBase("FirstClass")));
			}

			@Override
			public SortedSet<ClassInfo> getIncludedTypes() {
				SortedSet<ClassInfo> result = new TreeSet<>();
				result.add(new ClassInfoBase("FirstClass"));
				result.add(new ClassInfoBase("SecondClass"));
				return result;
			}
		};

	}

	@Test
	public void simpleOutput() throws IOException {
		DotOutputFormat<ClassInfo> format = new DotOutputFormat<>();
		StringWriter writer = new StringWriter();
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"FirstClass\" [label=\"FirstClass\"];\n";
		expected += "\"SecondClass\" [label=\"SecondClass\"];\n";

		expected += "\"FirstClass\" -> \"SecondClass\";\n";
		expected += "\"SecondClass\" -> \"FirstClass\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}

	@Test
	public void metricOutput() throws IOException {
		DotOutputFormat<ClassInfo> format = new DotOutputFormat<>(true);
		StringWriter writer = new StringWriter();
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"FirstClass\" [label=\"FirstClass\\n(DI 1)\"];\n";
		expected += "\"SecondClass\" [label=\"SecondClass\\n(DI 1)\"];\n";

		expected += "\"FirstClass\" -> \"SecondClass\";\n";
		expected += "\"SecondClass\" -> \"FirstClass\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}

}
