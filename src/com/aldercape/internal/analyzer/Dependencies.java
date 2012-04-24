package com.aldercape.internal.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.aldercape.internal.analyzer.reports.DependencyReport;
import com.aldercape.internal.analyzer.reports.FilteredDependencyReport;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {
	private List<DependencyReport<? extends TypeInfo>> dependencyReports = new ArrayList<>();
	private Set<PackageInfo> baseIgnoredPackages;
	ClassRepository repository = new ClassRepository();

	public static void main(String[] args) throws IOException {
		new Dependencies().run();
	}

	public Dependencies() {
		baseIgnoredPackages = new HashSet<>();
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
	}

	protected void writeDotFiles() throws IOException {
		for (DependencyReport<? extends TypeInfo> report : dependencyReports) {
			writeContentToDotFile(report);
		}
	}

	protected void createReports() throws IOException {
		dependencyReports.add(createPackageDependencyReport("project"));
		dependencyReports.add(createClassDependencyReport(getPackageName(ClassInfo.class)));
		dependencyReports.add(createClassDependencyReport(getPackageName(ConstantPoolInfo.class)));
		dependencyReports.add(createClassDependencyReport(getPackageName(JavaClassParser.class)));
	}

	protected String getPackageName(Class<?> clazz) {
		return clazz.getPackage().getName();
	}

	protected DependencyReport<ClassInfo> createClassDependencyReport(final String packageName) throws IOException {
		final ClassDependencyReport baseReport = new ClassDependencyReport(packageName);
		repository.addListener(new ClassRepositoryListener() {

			@Override
			public void classCreated(ClassInfo newClass) {
				if (newClass.getPackage().equals(new PackageInfo(packageName))) {
					baseReport.addClass(newClass);
				}
			}
		});

		FilteredDependencyReport<ClassInfo> result = new FilteredDependencyReport<>(baseReport, getBaseIgnoredPackages());

		result.ignoreParentPackage(new PackageInfo("java"));
		return result;
	}

	protected DependencyReport<PackageInfo> createPackageDependencyReport(String reportName) throws IOException {
		final PackageDependencyReport baseReport = new PackageDependencyReport(reportName);
		repository.addListener(new ClassRepositoryListener() {
			@Override
			public void classCreated(ClassInfo newClass) {
				baseReport.addClass(newClass);
			}
		});
		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignoreParentPackage(new PackageInfo("java.lang"));
		report.ignoreParentPackage(new PackageInfo("java.util"));
		report.ignoreParentPackage(new PackageInfo("org.junit"));
		report.ignoreParentPackage(new PackageInfo("java.io"));
		report.ignoreParentPackage(new PackageInfo("testdata"));
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
			JavaClassParser parser = new JavaClassParser(repository);
			parser.parse(file);
		} catch (Exception e) {
			System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
}
