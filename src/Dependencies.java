import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.aldercape.internal.analyzer.javaclass.ClassFinder;
import com.aldercape.internal.analyzer.javaclass.JavaClassParser;
import com.aldercape.internal.analyzer.outputformats.DotOutputFormat;
import com.aldercape.internal.analyzer.reports.PackageDependencyReport;

public class Dependencies {

	public static void main(String[] args) throws IOException {
		ClassFinder classFinder = new ClassFinder();
		PackageDependencyReport report = new PackageDependencyReport();
		Set<File> classFiles = classFinder.getClassFilesIn(new File("bin"));
		for (File file : classFiles) {
			if (!file.getName().endsWith("JavaClass.class") && !file.getName().endsWith("ConstantPoolType.class") && !file.getName().endsWith("PackageInfo.class") && !file.getName().endsWith("Test.class") && !file.getName().endsWith("Dependencies.class") && !file.getName().endsWith("ClassFinder$1.class")) {
				JavaClassParser parser = new JavaClassParser("");
				System.out.println("Parsing file: " + file.getAbsolutePath());
				report.addClass(parser.parse(new FileInputStream(file)));
			}
		}

		DotOutputFormat output = new DotOutputFormat();
		FileWriter writer = new FileWriter("project.dot");
		output.write(report, writer);
		writer.flush();
		writer.close();
	}
}
