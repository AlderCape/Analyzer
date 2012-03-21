package com.aldercape.internal.classparser;

import java.util.ArrayList;
import java.util.List;

public class JavaClassBuilder {

	private int magic;
	private int major;
	private int minor;
	private List<Constant> constantPool = new ArrayList<>();
	private int accessFlags;
	private int classNameIndex;
	private int superclassNameIndex;
	private int interfaceCount;
	private int fieldsCount;
	private int methodsCount;
	private int attributesCount;
	private List<MethodInfo> methods = new ArrayList<>();

	public void setMagicNumber(int magic) {
		this.magic = magic;
	}

	public JavaClass create() {
		JavaClass result = new JavaClass(magic, minor, major);
		result.setConstants(constantPool);
		result.setAccessFlags(accessFlags);
		result.setClassName(((String) constantPool.get(classNameIndex).getObject()).replace("/", "."));
		result.setSuperclassName(((String) constantPool.get(superclassNameIndex).getObject()).replace("/", "."));
		result.setInterfaceCount(interfaceCount);
		result.setFieldsCount(fieldsCount);
		result.setMethodsCount(methodsCount);
		result.setMethods(methods);
		result.setAttributesCount(attributesCount);
		return result;
	}

	public void setMinorVersion(int minor) {
		this.minor = minor;
	}

	public void setMajorVersion(int major) {
		this.major = major;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public void addConstant(Constant constant) {
		this.constantPool.add(constant);
	}

	public Constant getConstant(int index) {
		return constantPool.get(index - 1);
	}

	public void addMethodInfo(MethodInfo info) {
		this.methods.add(info);
	}

	public void setClassNameIndex(int classNameIndex) {
		this.classNameIndex = classNameIndex;
	}

	public void setSuperclassNameIndex(int superclassNameIndex) {
		this.superclassNameIndex = superclassNameIndex;
	}

	public void setInterfaceCount(int interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	public void setFieldsCount(int fieldsCount) {
		this.fieldsCount = fieldsCount;
	}

	public void setMethodsCount(int methodsCount) {
		this.methodsCount = methodsCount;
	}

	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}

}
