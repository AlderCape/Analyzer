import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.javaclass.ClassFinder;
import com.aldercape.internal.analyzer.javaclass.JavaClassParser;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.ClassDependencyReport;
import com.aldercape.internal.analyzer.reports.FilteredDependencyReport;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {

	public static void main(String[] args) throws IOException {
		ClassFinder classFinder = new ClassFinder();
		writePackageDependencyReport(classFinder);

		DotOutputFormat<ClassInfo> classOutput = new DotOutputFormat<ClassInfo>(true);
		writeClassDependencyReport(classFinder, classOutput, ClassInfo.class.getPackage().getName());
		writeClassDependencyReport(classFinder, classOutput, JavaClassParser.class.getPackage().getName());
	}

	protected static void writeClassDependencyReport(ClassFinder classFinder, DotOutputFormat<ClassInfo> classOutput, String packageName) throws IOException {
		Writer writer = new FileWriter(packageName + ".dot");

		String fileName = packageName.replace('.', '/');
		ClassDependencyReport baseReport = new ClassDependencyReport();
		Set<File> classFiles = classFinder.getClassFilesIn(new File("bin/" + fileName));
		for (File file : classFiles) {
			try {
				JavaClassParser parser = new JavaClassParser();
				baseReport.addClass(parser.parse(file));
			} catch (Exception e) {
				System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
				e.printStackTrace();
			}
		}
		FilteredDependencyReport<ClassInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));
		report.ignorePackage(new PackageInfo("java.util"));
		classOutput.write(report, writer);
		writer.flush();
		writer.close();
	}

	protected static void writePackageDependencyReport(ClassFinder classFinder) throws IOException {
		PackageDependencyReport baseReport = new PackageDependencyReport();

		Set<File> classFiles = classFinder.getClassFilesIn(new File("bin"));
		for (File file : classFiles) {
			try {
				JavaClassParser parser = new JavaClassParser();
				baseReport.addClass(parser.parse(file));
			} catch (Exception e) {
				System.out.println("Faild to parse file: " + file + " (" + e.getMessage() + ")");
				e.printStackTrace();
			}
		}

		FilteredDependencyReport<PackageInfo> report = new FilteredDependencyReport<>(baseReport);
		report.ignorePackage(new PackageInfo("java.lang"));

		DotOutputFormat<PackageInfo> output = new DotOutputFormat<PackageInfo>(true);
		FileWriter writer = new FileWriter("project.dot");
		output.write(report, writer);
		writer.flush();
		writer.close();
	}
}
