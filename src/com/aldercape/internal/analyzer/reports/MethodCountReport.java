package com.aldercape.internal.analyzer.reports;

import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo.AccessModifier;

public class MethodCountReport extends SingleClassReport {

	public int getMethodCount(ClassInfo info) {
		return info.getMethods().size();
	}

	public int getPublicMethodCount(ClassInfoStub info) {
		return getPublicMethods(info).size();
	}

	public int getPrivateMethodCount(ClassInfoStub info) {
		return getPrivateMethods(info).size();
	}

	public int getProtectedMethodCount(ClassInfoStub info) {
		return getProtectedMethods(info).size();
	}

	private List<MethodInfo> getPublicMethods(ClassInfoStub info) {
		return filterMethods(info, MethodInfo.AccessModifier.PUBLIC);
	}

	private List<MethodInfo> getPrivateMethods(ClassInfo info) {
		return filterMethods(info, MethodInfo.AccessModifier.PRIVATE);
	}

	private List<MethodInfo> getProtectedMethods(ClassInfo info) {
		return filterMethods(info, MethodInfo.AccessModifier.PROTECTED);
	}

	private List<MethodInfo> filterMethods(ClassInfo info, AccessModifier accessModifier) {
		List<MethodInfo> result = new ArrayList<>();
		for (MethodInfo methodInfo : info.getMethods()) {
			if (methodInfo.getAccessModifier() == accessModifier) {
				result.add(methodInfo);
			}
		}
		return result;
	}
}
