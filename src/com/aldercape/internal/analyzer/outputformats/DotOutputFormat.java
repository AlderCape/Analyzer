package com.aldercape.internal.analyzer.outputformats;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.classmodel.TypeInfo;
import com.aldercape.internal.analyzer.reports.DependencyReport;

public class DotOutputFormat<T extends TypeInfo> {

	protected boolean withMetrics;

	public DotOutputFormat() {
		this(false);
	}

	public DotOutputFormat(boolean withMetrics) {
		this.withMetrics = withMetrics;
	}

	public void write(DependencyReport<T> report, Writer outRaw) throws IOException {
		writeLine(outRaw, "digraph G {");
		writeIncludedTypes(outRaw, report.getIncludedTypes(), report);

		for (T packageInfo : report.getIncludedTypes()) {
			writeDependencies(report, outRaw, packageInfo);
		}
		writeLine(outRaw, "}");
	}

	protected final void writeIncludedTypes(Writer outRaw, SortedSet<T> includedTypes, DependencyReport<T> report) throws IOException {
		for (T packageInfo : includedTypes) {
			writeLine(outRaw, makeQuotaded(createRefName(packageInfo)) + " [label=" + makeQuotaded(createLabel(packageInfo, report)) + "]" + ";");
		}
	}

	protected final void writeDependencies(DependencyReport<T> report, Writer outRaw, T packageInfo) throws IOException {
		for (T depPackage : report.getChildrenFor(packageInfo)) {
			writeLine(outRaw, makeQuotaded(createRefName(packageInfo)) + " -> " + makeQuotaded(createRefName(depPackage)) + ";");
		}
	}

	protected String createLabel(T packageInfo, DependencyReport<T> report) {
		if (withMetrics) {
			return detailedLabel(packageInfo, report);
		} else {
			return createRefName(packageInfo);
		}
	}

	protected final String detailedLabel(T packageInfo, DependencyReport<T> report) {
		String metricsStr = "";
		for (DependencyReport.MetricPair metric : report.getMetricsPair(packageInfo)) {
			metricsStr += ", " + new MessageFormat(metric.getMessage()).format(new Object[] { metric.getValue() });
		}
		if (metricsStr.length() > 0) {
			metricsStr = metricsStr.substring(2);
			return createRefName(packageInfo) + "\\n(" + metricsStr + ")";
		}
		return createRefName(packageInfo);
	}

	protected String createRefName(T packageInfo) {
		return packageInfo.getName();
	}

	protected String makeQuotaded(String value) {
		return "\"" + value + "\"";
	}

	protected void writeLine(Writer outRaw, String value) throws IOException {
		outRaw.write(value + "\n");
	}

}
