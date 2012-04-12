import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.classmodel.TypeInfo;
import com.aldercape.internal.analyzer.javaclass.ClassFinder;
import com.aldercape.internal.analyzer.javaclass.ClassRepository;
import com.aldercape.internal.analyzer.javaclass.JavaClassParser;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.ClassConsumer;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;
import com.aldercape.internal.analyzer.reports.DependencyReport;
import com.aldercape.internal.analyzer.reports.FilteredDependencyReport;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {

	public static void main(String[] args) throws IOException {
		ClassFinder classFinder = new ClassFinder();

		writePackageDependencyReport(classFinder);
		writeClassDependencyReport(classFinder, ClassInfo.class.getPackage().getName());
		writeClassDependencyReport(classFinder, JavaClassParser.class.getPackage().getName());
	}

	protected static void writeClassDependencyReport(ClassFinder classFinder, String packageName) throws IOException {
		ClassDependencyReport baseReport = new ClassDependencyReport();
		ClassRepository.reset();
		addClassesToReport(classFinder, baseReport, "bin/" + packageName.replace('.', '/'));

		FilteredDependencyReport<ClassInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("java.util"));

		writeContentToDotFile(report, packageName + ".dot");
	}

	protected static void writePackageDependencyReport(ClassFinder classFinder) throws IOException {
		PackageDependencyReport baseReport = new PackageDependencyReport();
		addClassesToReport(classFinder, baseReport, "bin");
		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));

		writeContentToDotFile(report, "project.dot");
	}

	protected static <T extends TypeInfo> void writeContentToDotFile(DependencyReport<T> report, String fileName) throws IOException {
		DotOutputFormat<T> output = new DotOutputFormat<>(true);
		FileWriter writer = new FileWriter(fileName);
		output.write(report, writer);
		writer.flush();
		writer.close();
	}

	private static void addClassesToReport(ClassFinder classFinder, ClassConsumer consumer, String baseFolder) {
		Set<File> classFiles = classFinder.getClassFilesIn(new File(baseFolder));
		for (File file : classFiles) {
			try {
				JavaClassParser parser = new JavaClassParser();
				consumer.addClass(parser.parse(file));
			} catch (Exception e) {
				System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
				e.printStackTrace();
			}
		}

	}
}
