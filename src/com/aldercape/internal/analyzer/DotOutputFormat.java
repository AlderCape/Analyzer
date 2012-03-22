package com.aldercape.internal.analyzer;

import java.io.IOException;
import java.io.Writer;
import java.util.SortedSet;

public class DotOutputFormat {

	public void write(PackageDependencyReport report, Writer outRaw) throws IOException {
		writeLine(outRaw, "digraph G {");
		SortedSet<PackageInfo> packages = writeIncludedPackages(report, outRaw);

		for (PackageInfo packageInfo : packages) {
			writePackageDependencies(report, outRaw, packageInfo);
		}

		writeLine(outRaw, "}");
	}

	protected void writePackageDependencies(PackageDependencyReport report, Writer outRaw, PackageInfo packageInfo) throws IOException {
		for (PackageInfo depPackage : report.getEfferentFor(packageInfo)) {
			writeLine(outRaw, makeQuotaded(packageInfo.getName()) + " -> " + makeQuotaded(depPackage.getName()) + ";");
		}
	}

	protected SortedSet<PackageInfo> writeIncludedPackages(PackageDependencyReport report, Writer outRaw) throws IOException {
		for (PackageInfo packageInfo : report.getPackages()) {
			writeLine(outRaw, makeQuotaded(packageInfo.getName()) + ";");
		}
		return report.getPackages();
	}

	private String makeQuotaded(String value) {
		return "\"" + value + "\"";
	}

	private void writeLine(Writer outRaw, String value) throws IOException {
		outRaw.write(value + "\n");
	}

}
