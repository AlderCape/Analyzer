package com.aldercape.internal.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.ClassRepository.ClassRepositoryListener;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.classmodel.TypeInfo;
import com.aldercape.internal.analyzer.javaclass.ClassFinder;
import com.aldercape.internal.analyzer.javaclass.JavaClassParser;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;
import com.aldercape.internal.analyzer.reports.DependencyInversionReport;
import com.aldercape.internal.analyzer.reports.DependencyReport;
import com.aldercape.internal.analyzer.reports.FilteredDependencyReport;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {

	public static void main(String[] args) throws IOException {
		ClassFinder classFinder = new ClassFinder();
		Set<PackageInfo> ignoredPackagesForClassDependencies = new HashSet<>();
		ignoredPackagesForClassDependencies.add(new PackageInfo("java.lang"));
		ignoredPackagesForClassDependencies.add(new PackageInfo("java.util"));

		DependencyReport<PackageInfo> writePackageDependencyReport = createPackageDependencyReport(classFinder);
		DependencyReport<ClassInfo> classInfoPackageDependencyReport = createClassDependencyReport(classFinder, ClassInfo.class.getPackage().getName(), ignoredPackagesForClassDependencies);
		DependencyReport<ClassInfo> javaClassParserPackageDependencyReport = createClassDependencyReport(classFinder, JavaClassParser.class.getPackage().getName(), ignoredPackagesForClassDependencies);
		DependencyInversionReport classDependencyInversion = createClassDependencyInversion(classFinder, JavaClassParser.class.getPackage().getName());

		parseClasses(classFinder, "bin");

		writeContentToDotFile(writePackageDependencyReport, "project.dot");
		writeContentToDotFile(classInfoPackageDependencyReport, ClassInfo.class.getPackage().getName() + ".dot");
		writeContentToDotFile(javaClassParserPackageDependencyReport, JavaClassParser.class.getPackage().getName() + ".dot");
		writeContentToConsole(classDependencyInversion, JavaClassParser.class.getName() + ".txt");

	}

	private static DependencyInversionReport createClassDependencyInversion(ClassFinder classFinder, final String packageName) {
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

	private static void writeContentToConsole(DependencyInversionReport report, String string) {
		SortedSet<ClassInfo> classes = report.getIncludedTypes();
		for (ClassInfo info : classes) {
			System.out.println(info.getName() + " " + report.getDependencyInversionFor(info));
		}
	}

	protected static DependencyReport<ClassInfo> createClassDependencyReport(ClassFinder classFinder, final String packageName, Set<PackageInfo> ignoredPackages) throws IOException {
		final ClassDependencyReport baseReport = new ClassDependencyReport();
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

	protected static DependencyReport<PackageInfo> createPackageDependencyReport(ClassFinder classFinder) throws IOException {
		final PackageDependencyReport baseReport = new PackageDependencyReport();
		ClassRepository.addListener(new ClassRepositoryListener() {
			@Override
			public void classCreated(ClassInfo newClass) {
				baseReport.addClass(newClass);
			}
		});
		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		return report;
	}

	protected static <T extends TypeInfo> void writeContentToDotFile(DependencyReport<T> report, String fileName) throws IOException {
		DotOutputFormat<T> output = new DotOutputFormat<>(true);
		FileWriter writer = new FileWriter(fileName);
		output.write(report, writer);
		writer.flush();
		writer.close();
	}

	private static void parseClasses(ClassFinder classFinder, String baseFolder) {
		for (File file : classFinder.getClassFilesIn(new File(baseFolder))) {
			parseFile(file);
		}

	}

	protected static void parseFile(File file) {
		try {
			JavaClassParser parser = new JavaClassParser();
			parser.parse(file);
		} catch (Exception e) {
			System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}
}
