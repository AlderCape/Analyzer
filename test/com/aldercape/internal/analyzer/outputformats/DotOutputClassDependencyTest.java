package com.aldercape.internal.analyzer.outputformats;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.javaclass.ClassInfoBase;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;

public class DotOutputClassDependencyTest {

	@Test
	public void simpleOutput() throws IOException {
		DotOutputFormat<ClassInfo> format = new DotOutputFormat<>();
		StringWriter writer = new StringWriter();
		ClassDependencyReport report = new ClassDependencyReport() {
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
