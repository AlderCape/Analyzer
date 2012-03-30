package com.aldercape.internal.analyzer.outputformats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;

public class DotOutputClassDependencyFormat {

	public void write(ClassDependencyReport report, StringWriter outRaw) throws IOException {
		writeLine(outRaw, "digraph G {");
		Set<ClassInfo> packages = writeIncludedPackages(report, outRaw);
		for (ClassInfo packageInfo : packages) {
			writeDependencies(report, outRaw, packageInfo);
		}

		writeLine(outRaw, "}");

	}

	protected SortedSet<ClassInfo> writeIncludedPackages(ClassDependencyReport report, Writer outRaw) throws IOException {
		for (ClassInfo packageInfo : report.getClasses()) {
			writeLine(outRaw, makeQuotaded(packageInfo.getName()) + " [label=" + makeQuotaded(packageInfo.getName()) + "]" + ";");
		}
		return report.getClasses();
	}

	private void writeDependencies(ClassDependencyReport report, Writer outRaw, ClassInfo packageInfo) throws IOException {
		for (ClassInfo depPackage : report.getChildrenFor(packageInfo)) {
			writeLine(outRaw, makeQuotaded(packageInfo.getName()) + " -> " + makeQuotaded(depPackage.getName()) + ";");
		}
	}

	private String makeQuotaded(String value) {
		return "\"" + value + "\"";
	}

	private void writeLine(Writer outRaw, String value) throws IOException {
		outRaw.write(value + "\n");
	}

}
