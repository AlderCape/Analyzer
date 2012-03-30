package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;
import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class JavaClass implements ClassInfo {

	private AccessInfo accessInfo;
	private ConstantPoolInfo constantPool;
	private String className;
	private String superclassName;
	private List<FieldInfo> fields = new ArrayList<>();
	private List<MethodInfo> methods = new ArrayList<>();
	private PackageInfo classPackage;
	private List<String> interfaces = new ArrayList<>();
	private AttributeInfo attributes = new AttributeInfo();
	private VersionInfo version;

	public JavaClass(VersionInfo version) {
		this.version = version;
	}

	public String getClassName() {
		return className;
	}

	public int getMagic() {
		return version.getMagicNumber();
	}

	public int getMinor() {
		return version.getMinorVersion();
	}

	public int getMajor() {
		return version.getMajorVersion();
	}

	public int getConstantPoolSize() {
		return constantPool.size();
	}

	public int getAccessFlags() {
		return accessInfo.getRawValue();
	}

	public void setAccessFlags(int accessFlags) {
		accessInfo = new AccessInfo(accessFlags);
	}

	public boolean isPublic() {
		return true;
	}

	public void setConstants(ConstantPoolInfo constants) {
		this.constantPool = constants;
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
		return attributes.size();
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

	public void setAttributes(AttributeInfo attributes) {
		this.attributes = attributes;
	}

	@Override
	public Set<PackageInfo> getPackageDependencies() {
		Set<PackageInfo> result = new HashSet<>();
		result.add(getPackage(superclassName));
		for (String interfaceName : interfaces) {
			result.add(getPackage(interfaceName));
		}
		for (FieldInfo field : fields) {
			Set<PackageInfo> packageInfo = field.getDependentPackages();
			result.addAll(packageInfo);
		}
		for (MethodInfo method : methods) {
			Set<PackageInfo> dependentPackages = method.getDependentPackages();
			for (PackageInfo packageInfo : dependentPackages) {
				result.add(packageInfo);
			}
		}
		result.addAll(attributes.getDependentPackages());
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
		return accessInfo.isAbstract();
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		return Collections.emptySet();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(ClassInfo o) {
		return getName().compareTo(o.getName());
	}
}
