package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassDetails;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo.AccessModifier;

public class ParsedClassDetails implements ClassDetails {

	private static final String CONSTRUCTOR_METHOD_NAME = "<init>";
	private ClassInfo superclassName;
	private AccessInfo accessInfo;

	private List<FieldInfo> fields = new ArrayList<>();
	private Set<MethodInfo> methods = new HashSet<>();
	private List<ClassInfo> interfaces = new ArrayList<>();
	private AttributeInfo attributes = new AttributeInfo();
	private VersionInfo versionInfo;

	public ParsedClassDetails(int accessFlags, ClassInfo superClass, List<ClassInfo> interfaces, List<FieldInfo> fields, Set<MethodInfo> methods, AttributeInfo attributes, VersionInfo versionInfo) {
		this.superclassName = superClass;
		this.accessInfo = new AccessInfo(accessFlags);
		this.fields = fields;
		this.methods = methods;
		this.attributes = attributes;
		this.versionInfo = versionInfo;
		this.interfaces = interfaces;
	}

	@Override
	public ClassInfo getEnclosingClass() {
		return attributes.getEnclosingClass();
	}

	@Override
	public boolean isInnerClass() {
		return attributes.isInnerClass();
	}

	@Override
	public boolean isAbstract() {
		return accessInfo.isAbstract();
	}

	@Override
	public boolean isEnumeration() {
		return accessInfo.isEnumeration();
	}

	@Override
	public Set<ClassInfo> getClassDependencies(ClassInfo baseClass) {
		Set<ClassInfo> result = new HashSet<>();
		result.add(superclassName);
		result.addAll(interfaces);
		for (FieldInfo field : fields) {
			result.addAll(field.getDependentClasses());
		}
		for (MethodInfo method : methods) {
			result.addAll(method.getDependentClasses());
		}
		result.addAll(attributes.getDependentClasses());
		result.remove(baseClass);
		return result;
	}

	@Override
	public Set<MethodInfo> getMethods() {
		Set<MethodInfo> result = new HashSet<>();
		for (MethodInfo info : methods) {
			if (!CONSTRUCTOR_METHOD_NAME.equals(info.getName())) {
				result.add(info);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public List<MethodInfo> getPublicMethods() {
		return getMethods(new MethodFilter() {
			@Override
			public boolean isValid(MethodInfo method) {
				return method.getAccessModifier() == AccessModifier.PUBLIC;
			}
		});
	}

	public List<MethodInfo> getPrivateMethods() {
		return getMethods(new MethodFilter() {
			@Override
			public boolean isValid(MethodInfo method) {
				return method.getAccessModifier() == AccessModifier.PRIVATE;
			}
		});
	}

	public List<MethodInfo> getProtectedMethods() {
		return getMethods(new MethodFilter() {
			@Override
			public boolean isValid(MethodInfo method) {
				return method.getAccessModifier() == AccessModifier.PROTECTED;
			}
		});
	}

	protected List<MethodInfo> getMethods(MethodFilter protectedFilter) {
		List<MethodInfo> result = new ArrayList<>();
		for (MethodInfo info : getMethods()) {
			if (protectedFilter.isValid(info)) {
				result.add(info);
			}
		}
		return result;
	}

	private interface MethodFilter {
		public boolean isValid(MethodInfo method);
	}

}
