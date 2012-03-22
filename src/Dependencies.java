import java.io.FileWriter;
import java.io.IOException;

import com.aldercape.internal.classparser.DotOutputFormat;
import com.aldercape.internal.classparser.JavaClass;
import com.aldercape.internal.classparser.JavaClassParser;
import com.aldercape.internal.classparser.PackageDependencyReport;

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
