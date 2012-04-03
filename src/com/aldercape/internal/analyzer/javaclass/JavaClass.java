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

public class JavaClass extends ClassInfoBase implements ClassInfo {

	private String superclassName;

	private AccessInfo accessInfo;
	private ConstantPoolInfo constantPool;
	private VersionInfo version;

	private List<FieldInfo> fields = new ArrayList<>();
	private List<MethodInfo> methods = new ArrayList<>();
	private List<String> interfaces = new ArrayList<>();
	private AttributeInfo attributes = new AttributeInfo();

	public JavaClass(String className, VersionInfo version) {
		super(className);
		this.version = version;
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
		Set<ClassInfo> classDependencies = getClassDependencies();
		for (ClassInfo classInfo : classDependencies) {
			result.add(classInfo.getPackage());
		}
		result.remove(getPackage());
		return result;
	}

	@Override
	public boolean isAbstract() {
		return accessInfo.isAbstract();
	}

	@Override
	public Set<ClassInfo> getClassDependencies() {
		Set<ClassInfo> result = new HashSet<>();
		result.add(new SimpleClassInfo(superclassName));
		for (String interfaceName : interfaces) {
			result.add(new SimpleClassInfo(interfaceName));
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
		result.remove(this);
		return result;
	}

	@Override
	public int compareTo(ClassInfo o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj) {
		return getName().equals(((ClassInfo) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
