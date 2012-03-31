package com.aldercape.internal.analyzer.outputformats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;
import com.aldercape.internal.analyzer.reports.ClassInfoStub;

public class DotOutputClassDependencyTest {

	@Test
	public void simpleOutput() throws IOException {
		DotOutputFormat<ClassInfo> format = new DotOutputFormat<>();
		StringWriter writer = new StringWriter();
		ClassDependencyReport report = new ClassDependencyReport() {
			@Override
			public Set<? extends ClassInfo> getChildrenFor(ClassInfo classInfo) {
				if (classInfo.getName().equals("FirstClass")) {
					return Collections.singleton(new ClassInfoStub("SecondClass"));
				}
				return Collections.singleton(new ClassInfoStub("FirstClass"));
			}

			@Override
			public SortedSet<ClassInfo> getClasses() {
				SortedSet<ClassInfo> result = new TreeSet<>();
				result.add(new ClassInfoStub("FirstClass"));
				result.add(new ClassInfoStub("SecondClass"));
				return result;
			}
		};
		format.write(report, writer);
		String expected = "digraph G {\n";
		expected += "\"FirstClass\" [label=\"FirstClass\"];\n";
		expected += "\"SecondClass\" [label=\"SecondClass\"];\n";

		expected += "\"FirstClass\" -> \"SecondClass\";\n";
		expected += "\"SecondClass\" -> \"FirstClass\";\n";

		expected += "}\n";
		assertEquals(expected, writer.toString());
	}

}
