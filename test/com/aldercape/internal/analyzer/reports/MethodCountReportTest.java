package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;

public class MethodCountReportTest {

	@Test
	public void test() {
		MethodCountReport report = new MethodCountReport();
		ClassInfoStub classInfo1 = new ClassInfoStub("FirstClass");
		Set<MethodInfo> methods = new HashSet<>();
		MethodInfo publicMethod = new MethodInfo() {

			@Override
			public String getName() {
				return "publicMethod";
			}

			@Override
			public Set<ClassInfo> getDependentClasses() {
				return Collections.emptySet();
			}

			@Override
			public AccessModifier getAccessModifier() {
				return MethodInfo.AccessModifier.PUBLIC;
			}
		};
		MethodInfo protectedMethod = new MethodInfo() {

			@Override
			public String getName() {
				return "protectedMethod";
			}

			@Override
			public Set<ClassInfo> getDependentClasses() {
				return Collections.emptySet();
			}

			@Override
			public AccessModifier getAccessModifier() {
				return MethodInfo.AccessModifier.PROTECTED;
			}
		};
		MethodInfo privateMethod = new MethodInfo() {

			@Override
			public String getName() {
				return "privateMethod";
			}

			@Override
			public Set<ClassInfo> getDependentClasses() {
				return Collections.emptySet();
			}

			@Override
			public AccessModifier getAccessModifier() {
				return MethodInfo.AccessModifier.PRIVATE;
			}
		};
		methods.add(publicMethod);
		methods.add(protectedMethod);
		methods.add(privateMethod);
		classInfo1.setMethods(methods);
		report.addClass(classInfo1);
		assertEquals(Collections.singleton(classInfo1), report.getIncludedTypes());
		assertEquals(3, report.getMethodCount(classInfo1));
		assertEquals(1, report.getPublicMethodCount(classInfo1));
		assertEquals(1, report.getPrivateMethodCount(classInfo1));
		assertEquals(1, report.getProtectedMethodCount(classInfo1));
	}
}
