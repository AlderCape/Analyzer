package com.aldercape.internal.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.ClassRepository.ClassRepositoryListener;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.classmodel.TypeInfo;
import com.aldercape.internal.analyzer.javaclass.ClassFinder;
import com.aldercape.internal.analyzer.javaclass.ConstantPoolInfo;
import com.aldercape.internal.analyzer.javaclass.parser.JavaClassParser;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;
import com.aldercape.internal.analyzer.reports.DependencyInversionReport;
import com.aldercape.internal.analyzer.reports.DependencyReport;
import com.aldercape.internal.analyzer.reports.FilteredDependencyReport;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {
	private List<DependencyReport<? extends TypeInfo>> dependencyReports = new ArrayList<>();
	private Set<PackageInfo> baseIgnoredPackages;
	private DependencyInversionReport classDependencyInversion;

	public static void main(String[] args) throws IOException {
		new Dependencies().run();
	}

	public Dependencies() {
		baseIgnoredPackages = new HashSet<>();
		baseIgnoredPackages.add(new PackageInfo("java.lang"));
		baseIgnoredPackages.add(new PackageInfo("java.util"));
		baseIgnoredPackages.add(new PackageInfo("org.junit"));
	}

	public Set<PackageInfo> getBaseIgnoredPackages() {
		return baseIgnoredPackages;
	}

	private void run() throws IOException {
		createReports();
		parseClasses("bin");
		writeReports();
	}

	protected void writeReports() throws IOException {
		writeDotFiles();
		writeMetrics();
	}

	protected void writeMetrics() {
		writeContentToConsole(classDependencyInversion);
	}

	protected void writeDotFiles() throws IOException {
		for (DependencyReport<? extends TypeInfo> report : dependencyReports) {
			writeContentToDotFile(report);
		}
	}

	protected void createReports() throws IOException {
		dependencyReports.add(createPackageDependencyReport("project"));
		dependencyReports.add(createClassDependencyReport(getPackageName(ClassInfo.class), getBaseIgnoredPackages()));
		dependencyReports.add(createClassDependencyReport(getPackageName(ConstantPoolInfo.class), getBaseIgnoredPackages()));
		dependencyReports.add(createClassDependencyReport(getPackageName(JavaClassParser.class), getBaseIgnoredPackages()));
		classDependencyInversion = createClassDependencyInversion(getPackageName(JavaClassParser.class));
	}

	protected String getPackageName(Class<?> clazz) {
		return clazz.getPackage().getName();
	}

	private DependencyInversionReport createClassDependencyInversion(final String packageName) {
		final DependencyInversionReport baseReport = new DependencyInversionReport();
		ClassRepository.addListener(new ClassRepositoryListener() {

			@Override
			public void classCreated(ClassInfo newClass) {
				if (newClass.getPackage().equals(new PackageInfo(packageName))) {
					baseReport.addClass(newClass);
				}
			}
		});
		return baseReport;
	}

	private void writeContentToConsole(DependencyInversionReport report) {
		SortedSet<ClassInfo> classes = report.getIncludedTypes();
		for (ClassInfo info : classes) {
			System.out.println(info.getName() + " " + report.getDependencyInversionFor(info));
		}
	}

	protected DependencyReport<ClassInfo> createClassDependencyReport(final String packageName, Set<PackageInfo> ignoredPackages) throws IOException {
		final ClassDependencyReport baseReport = new ClassDependencyReport(packageName);
		ClassRepository.addListener(new ClassRepositoryListener() {

			@Override
			public void classCreated(ClassInfo newClass) {
				if (newClass.getPackage().equals(new PackageInfo(packageName))) {
					baseReport.addClass(newClass);
				}
			}
		});

		return new FilteredDependencyReport<>(baseReport, ignoredPackages);
	}

	protected DependencyReport<PackageInfo> createPackageDependencyReport(String reportName) throws IOException {
		final PackageDependencyReport baseReport = new PackageDependencyReport(reportName);
		ClassRepository.addListener(new ClassRepositoryListener() {
			@Override
			public void classCreated(ClassInfo newClass) {
				baseReport.addClass(newClass);
			}
		});
		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("java.util"));
		report.ignorePackage(new PackageInfo("org.junit"));
		report.ignorePackage(new PackageInfo("java.io"));
		report.ignorePackage(new PackageInfo("java.util.concurrent"));
		report.ignorePackage(new PackageInfo("testdata"));
		report.ignorePackage(new PackageInfo("testdata.filefinder"));
		report.ignorePackage(new PackageInfo("testdata.filefinder.multiplefiles"));
		report.ignorePackage(new PackageInfo("testdata.filefinder.singleclasspackage"));
		return report;
	}

	protected <T extends TypeInfo> void writeContentToDotFile(DependencyReport<T> report) throws IOException {
		DotOutputFormat<T> output = new DotOutputFormat<>(true);
		FileWriter writer = new FileWriter(report.getReportName() + ".dot");
		output.write(report, writer);
		writer.flush();
		writer.close();
	}

	private void parseClasses(String baseFolder) {
		ClassFinder classFinder = new ClassFinder();
		for (File file : classFinder.getClassFilesIn(new File(baseFolder))) {
			parseFile(file);
		}

	}

	protected void parseFile(File file) {
		try {
			JavaClassParser parser = new JavaClassParser();
			parser.parse(file);
		} catch (Exception e) {
			System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
}
