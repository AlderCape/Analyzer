package com.aldercape.internal.analyzer.outputformats;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.PackageInfo;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class DotOutputFormat {

	private boolean withMetrics;

	public DotOutputFormat(boolean withMetrics) {
		this.withMetrics = withMetrics;
	}

	public DotOutputFormat() {
		this(false);
	}

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
			writeLine(outRaw, makeQuotaded(packageInfo.getName()) + " [label=" + createLabel(report, packageInfo) + "]" + ";");
		}
		return report.getPackages();
	}

	protected String createLabel(PackageDependencyReport report, PackageInfo packageInfo) {
		String name;
		if (withMetrics) {
			String ca = new MessageFormat("Ca {0}").format(new Object[] { report.getAfferentFor(packageInfo).size() });
			String ce = new MessageFormat("Ce {0}").format(new Object[] { report.getEfferentFor(packageInfo).size() });
			String a = new MessageFormat("A {0,number,0.0}").format(new Object[] { new Double(report.getAbstractness(packageInfo)) });
			String i = new MessageFormat("I {0,number,0.0}").format(new Object[] { new Double(report.getInstability(packageInfo)) });
			String d = new MessageFormat("D {0,number,0.0}").format(new Object[] { new Double(report.getDistance(packageInfo)) });
			name = makeQuotaded(packageInfo.getName() + "\\n(" + ca + ", " + ce + ", " + a + ", " + i + ", " + d + ")");
		} else {
			name = makeQuotaded(packageInfo.getName());
		}
		return name;
	}

	private String makeQuotaded(String value) {
		return "\"" + value + "\"";
	}

	private void writeLine(Writer outRaw, String value) throws IOException {
		outRaw.write(value + "\n");
	}

}
