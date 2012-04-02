package testdata;

import java.util.Comparator;

public class ClassWithAnInterface implements Comparator<ClassWithAnInterface> {

	@Override
	public int compare(ClassWithAnInterface o1, ClassWithAnInterface o2) {
		return 0;
	}

}
