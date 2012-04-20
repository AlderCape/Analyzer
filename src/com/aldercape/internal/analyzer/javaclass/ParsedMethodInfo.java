package com.aldercape.internal.analyzer.javaclass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ParsedMethodInfo implements MethodInfo {

	private static final int PUBLIC_ACC_FLAG = 0x1;
	private static final int PRIVATE_ACC_FLAG = 0x2;
	private static final int PROTECTED_ACC_FLAG = 0x4;

	private String methodName;
	private List<String> parameters;
	private AttributeInfo attributeInfo = new AttributeInfo();

	private int accessFlag;
	private ClassRepository repository;

	public ParsedMethodInfo(int accessFlag, String methodName, List<String> parameters, ClassRepository repository) {
		this.accessFlag = accessFlag;
		this.methodName = methodName;
		this.parameters = parameters;
		this.repository = repository;
	}

	@Override
	public String getName() {
		return methodName;
	}

	@Override
	public String toString() {
		return "MethodInfo [methodName=" + methodName + ", parameters=" + parameters + "]";
	}

	protected PackageInfo getPackage(String className) {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	public void setAttribute(AttributeInfo attributeInfo) {
		this.attributeInfo = attributeInfo;
	}

	@Override
	public Set<ClassInfo> getDependentClasses() {
		Set<ClassInfo> result = new HashSet<>();
		for (String dependency : parameters) {
			result.add(repository.getClass(dependency));
		}
		result.addAll(attributeInfo.getDependentClasses());
		return result;
	}

	@Override
	public AccessModifier getAccessModifier() {
		if (isPublic()) {
			return AccessModifier.PUBLIC;
		} else if (isPrivate()) {
			return AccessModifier.PRIVATE;
		} else if (isProtected()) {
			return AccessModifier.PROTECTED;
		}
		return AccessModifier.PUBLIC;
	}

	private boolean isPublic() {
		return (PUBLIC_ACC_FLAG & accessFlag) != 0;
	}

	private boolean isPrivate() {
		return (PRIVATE_ACC_FLAG & accessFlag) != 0;
	}

	private boolean isProtected() {
		return (PROTECTED_ACC_FLAG & accessFlag) != 0;
	}

}
