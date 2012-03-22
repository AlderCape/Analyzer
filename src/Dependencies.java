import java.io.FileWriter;
import java.io.IOException;

import com.aldercape.internal.analyzer.DotOutputFormat;
import com.aldercape.internal.analyzer.JavaClass;
import com.aldercape.internal.analyzer.JavaClassParser;
import com.aldercape.internal.analyzer.PackageDependencyReport;

public class Dependencies {

	public static void main(String[] args) throws IOException {
		PackageDependencyReport report = new PackageDependencyReport();
		JavaClassParser parser = new JavaClassParser(JavaClass.class.getName());
		report.addClass(parser.parse());

		DotOutputFormat output = new DotOutputFormat();
		FileWriter writer = new FileWriter("project.dot");
		output.write(report, writer);
		writer.flush();
		writer.close();
	}
}
