package com.aldercape.internal.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaClass implements ClassInfo {

	private int magic;
	private int minor;
	private int major;
	private int accessFlags;
	private List<Constant> constantPool;
	private String className;
	private String superclassName;
	private int interfaceCount;
	private int fieldsCount;
	private int methodsCount;
	private int attributesCount;
	private List<FieldInfo> fields = new ArrayList<>();
	private List<MethodInfo> methods = new ArrayList<>();
	private PackageInfo classPackage;

	public JavaClass(int magic, int minor, int major) {
		this.magic = magic;
		this.minor = minor;
		this.major = major;
	}

	public String getClassName() {
		return className;
	}

	public int getMagic() {
		return magic;
	}

	public int getMinor() {
		return minor;
	}

	public int getMajor() {
		return major;
	}

	public int getConstantPoolSize() {
		return constantPool.size();
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public boolean isPublic() {
		return true;
	}

	public void setConstants(List<Constant> constantPool) {
		this.constantPool = new ArrayList<Constant>(constantPool);
	}

	public void setClassName(String className) {
		this.className = className;
		classPackage = new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	public String getSuperclassName() {
		return superclassName;
	}

	public void setSuperclassName(String superclassName) {
		this.superclassName = superclassName;
	}

	public int getInterfaceCount() {
		return interfaceCount;
	}

	public void setInterfaceCount(int interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	public int getFieldsCount() {
		return fieldsCount;
	}

	public void setFieldsCount(int fieldsCount) {
		this.fieldsCount = fieldsCount;
	}

	public int getMethodsCount() {
		return methodsCount;
	}

	public void setMethodsCount(int methodsCount) {
		this.methodsCount = methodsCount;
	}

	public int getAttributesCount() {
		return attributesCount;
	}

	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}

	public FieldInfo getField(int i) {
		return fields.get(i);
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = new ArrayList<>(fields);
	}

	public MethodInfo getMethod(int i) {
		return methods.get(i);
	}

	public void setMethods(List<MethodInfo> methods) {
		this.methods = new ArrayList<MethodInfo>(methods);
	}

	public Set<PackageInfo> getPackageDependencies() {
		Set<PackageInfo> result = new HashSet<>();
		for (FieldInfo field : fields) {
			PackageInfo packageInfo = field.getDependentPackage();
			result.add(packageInfo);
		}
		for (MethodInfo method : methods) {
			Set<PackageInfo> dependentPackages = method.getDependentPackages();
			for (PackageInfo packageInfo : dependentPackages) {
				result.add(packageInfo);
			}
		}
		result.remove(getPackage(getClassName()));
		return result;
	}

	private PackageInfo getPackage(String className) {
		return classPackage;
	}

	public PackageInfo getPackage() {
		return getPackage(getClassName());
	}

}
