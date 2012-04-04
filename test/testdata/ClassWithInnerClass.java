package testdata;

import java.util.List;

public class ClassWithInnerClass {

	public static class InnerStatic {
		public List<?> l;
	}

	public class InnerNonStatic {
		public List<?> l;
	}
}
