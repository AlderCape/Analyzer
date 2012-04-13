import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

		DependencyReport<PackageInfo> writePackageDependencyReport = writePackageDependencyReport(classFinder);
		DependencyReport<ClassInfo> classInfoPackageDependencyReport = writeClassDependencyReport(classFinder, ClassInfo.class.getPackage().getName());
		DependencyReport<ClassInfo> javaClassParserPackageDependencyReport = writeClassDependencyReport(classFinder, JavaClassParser.class.getPackage().getName());
		DependencyInversionReport classDependencyInversion = writeClassDependencyInversion(classFinder, JavaClassParser.class.getPackage().getName());

		addClassesToReport(classFinder, "bin");

		writeContentToDotFile(writePackageDependencyReport, "project.dot");
		writeContentToDotFile(classInfoPackageDependencyReport, ClassInfo.class.getPackage().getName() + ".dot");
		writeContentToDotFile(javaClassParserPackageDependencyReport, JavaClassParser.class.getPackage().getName() + ".dot");
		writeContentToConsole(classDependencyInversion, JavaClassParser.class.getName() + ".txt");

	}

	private static DependencyInversionReport writeClassDependencyInversion(ClassFinder classFinder, final String packageName) {
		final DependencyInversionReport baseReport = new DependencyInversionReport();
		ClassRepository.addListener(new ClassRepositoryListener() {

			@Override
			public void classCreated(ClassInfo newClass) {
				if (newClass.getPackage().equals(packageName)) {
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

	protected static DependencyReport<ClassInfo> writeClassDependencyReport(ClassFinder classFinder, final String packageName) throws IOException {
		final ClassDependencyReport baseReport = new ClassDependencyReport();
		ClassRepository.addListener(new ClassRepositoryListener() {

			@Override
			public void classCreated(ClassInfo newClass) {
				if (newClass.getPackage().equals(packageName)) {
					baseReport.addClass(newClass);
				}
			}
		});

		FilteredDependencyReport<ClassInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("java.util"));

		return report;
	}

	protected static DependencyReport<PackageInfo> writePackageDependencyReport(ClassFinder classFinder) throws IOException {
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

	private static void addClassesToReport(ClassFinder classFinder, String baseFolder) {
		Set<File> classFiles = classFinder.getClassFilesIn(new File(baseFolder));
		for (File file : classFiles) {
			try {
				JavaClassParser parser = new JavaClassParser();
				parser.parse(file);
			} catch (Exception e) {
				System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
				e.printStackTrace();
			}
		}

	}
}
