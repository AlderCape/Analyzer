package com.aldercape.internal.classparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class MethodInfoTest {

	@Test
	public void noDependencies() {
		List<String> parameters = new ArrayList<String>();
		MethodInfo methodInfo = new MethodInfo(-1, "testMethod", parameters);
		assertEquals(Collections.emptySet(), methodInfo.getDependentPackages());
	}

	@Test
	public void dependsOnJavaLang() {
		List<String> parameters = Collections.singletonList("java.lang.String");
		MethodInfo methodInfo = new MethodInfo(-1, "testMethod", parameters);
		assertEquals(Collections.singleton(new PackageInfo("java.lang")), methodInfo.getDependentPackages());
	}

	@Test
	public void dependsOnJavaLangAndJavaUtil() {
		List<String> parameters = new ArrayList<>();
		parameters.add("java.lang.String");
		parameters.add("java.util.List");
		Set<PackageInfo> expected = new HashSet<>();
		expected.add(new PackageInfo("java.lang"));
		expected.add(new PackageInfo("java.util"));

		MethodInfo methodInfo = new MethodInfo(-1, "testMethod", parameters);
		assertEquals(expected, methodInfo.getDependentPackages());
	}
}
