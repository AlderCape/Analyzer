package com.aldercape.internal.analyzer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.javaclass.ParsedMethodInfo;

public class MethodInfoTest {

	@Test
	public void noDependencies() {
		List<String> parameters = new ArrayList<String>();
		MethodInfo methodInfo = new ParsedMethodInfo(-1, "testMethod", parameters);
		assertEquals(Collections.emptySet(), methodInfo.getDependentClasses());
	}

	@Test
	public void dependsOnJavaLang() {
		List<String> parameters = Collections.singletonList("java.lang.String");
		MethodInfo methodInfo = new ParsedMethodInfo(-1, "testMethod", parameters);
		assertEquals(Collections.singleton(new ClassInfoBase("java.lang.String")), methodInfo.getDependentClasses());
	}

	@Test
	public void dependsOnJavaLangAndJavaUtil() {
		List<String> parameters = new ArrayList<>();
		parameters.add("java.lang.String");
		parameters.add("java.util.List");
		Set<ClassInfo> expected = new HashSet<>();
		expected.add(new ClassInfoBase("java.lang.String"));
		expected.add(new ClassInfoBase("java.util.List"));

		MethodInfo methodInfo = new ParsedMethodInfo(-1, "testMethod", parameters);
		assertEquals(expected, methodInfo.getDependentClasses());
	}
}
