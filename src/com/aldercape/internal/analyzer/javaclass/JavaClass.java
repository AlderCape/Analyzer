package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.ClassInfo;
import com.aldercape.internal.analyzer.FieldInfo;
import com.aldercape.internal.analyzer.MethodInfo;
import com.aldercape.internal.analyzer.PackageInfo;

public class JavaClass implements ClassInfo {

	private int magic;
	private int minor;
	private int major;
	private int accessFlags;
	private List<Constant> constantPool;
	private String className;
	private String superclassName;
	private int attributesCount;
	private List<FieldInfo> fields = new ArrayList<>();
	private List<MethodInfo> methods = new ArrayList<>();
	private PackageInfo classPackage;
	private List<String> interfaces = new ArrayList<>();
	private boolean isAbstract;
	private List<AttributeInfo> attributes = new ArrayList<>();

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
		classPackage = getPackage(getClassName());
	}

	public String getSuperclassName() {
		return superclassName;
	}

	public void setSuperclassName(String superclassName) {
		this.superclassName = superclassName;
	}

	public int getInterfaceCount() {
		return interfaces.size();
	}

	public void setInterfaces(List<String> interfaces) {
		this.interfaces = new ArrayList<>(interfaces);
	}

	public int getFieldsCount() {
		return fields.size();
	}

	public int getMethodsCount() {
		return methods.size();
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

	public void setAttributes(List<AttributeInfo> attributes) {
		this.attributes = new ArrayList<AttributeInfo>(attributes);
	}

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		Set<PackageInfo> result = new HashSet<>();
		result.add(getPackage(superclassName));
		for (String interfaceName : interfaces) {
			result.add(getPackage(interfaceName));
		}
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
		for (AttributeInfo attribute : attributes) {
			Set<PackageInfo> dependentPackages = attribute.getDependentPackages();
			result.addAll(dependentPackages);
		}
		result.remove(classPackage);
		return result;
	}

	private PackageInfo getPackage(String className) {
		return new PackageInfo(className.substring(0, className.lastIndexOf('.')));
	}

	@Override
	public PackageInfo getPackage() {
		return classPackage;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

}
