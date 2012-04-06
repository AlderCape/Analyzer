package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class ParsedClassDetails implements ClassDetails {

	private ClassInfo superclassName;
	private AccessInfo accessInfo;

	private List<FieldInfo> fields = new ArrayList<>();
	private List<MethodInfo> methods = new ArrayList<>();
	private List<String> interfaces = new ArrayList<>();
	private AttributeInfo attributes = new AttributeInfo();
	private VersionInfo versionInfo;

	// public ParsedClassDetails(int accessFlags, String superClassName,
	// List<String> interfaces, List<FieldInfo> fields, List<MethodInfo>
	// methods, AttributeInfo attributes) {
	// this(accessFlags, ClassRepository.getClass(superClassName), interfaces,
	// fields, methods, attributes);
	// this.accessInfo = new AccessInfo(accessFlags);
	// this.interfaces = interfaces;
	// this.fields = fields;
	// this.methods = methods;
	// this.attributes = attributes;
	// }

	public ParsedClassDetails(int accessFlags, ClassInfo superClass, List<String> interfaces, List<FieldInfo> fields, List<MethodInfo> methods, AttributeInfo attributes, VersionInfo versionInfo) {
		this.superclassName = superClass;
		this.accessInfo = new AccessInfo(accessFlags);
		this.interfaces = interfaces;
		this.fields = fields;
		this.methods = methods;
		this.attributes = attributes;
		this.versionInfo = versionInfo;
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
	public Set<ClassInfo> getClassDependencies(ClassInfo baseClass) {
		Set<ClassInfo> result = new HashSet<>();
		result.add(superclassName);
		for (String interfaceName : interfaces) {
			result.add(ClassRepository.getClass(interfaceName));
		}
		for (FieldInfo field : fields) {
			Set<ClassInfo> packageInfo = field.getDependentClasses();
			result.addAll(packageInfo);
		}
		for (MethodInfo method : methods) {
			Set<ClassInfo> dependentPackages = method.getDependentClasses();
			for (ClassInfo packageInfo : dependentPackages) {
				result.add(packageInfo);
			}
		}
		result.addAll(attributes.getDependentClasses());
		result.remove(baseClass);
		return result;
	}

	@Override
	public Set<PackageInfo> getPackageDependencies(ClassInfo baseClass) {
		Set<PackageInfo> result = new HashSet<>();
		Set<ClassInfo> classDependencies = getClassDependencies(baseClass);
		for (ClassInfo classInfo : classDependencies) {
			result.add(classInfo.getPackage());
		}
		result.remove(baseClass.getPackage());
		return result;
	}

}
