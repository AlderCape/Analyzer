package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.FieldInfo;
import com.aldercape.internal.analyzer.MethodInfo;

public class JavaClassBuilder {

	private int magic;
	private int major;
	private int minor;
	private List<Constant> constantPool = new ArrayList<>();
	private int accessFlags;
	private int classNameIndex;
	private int superclassNameIndex;
	private int attributesCount;
	private List<MethodInfo> methods = new ArrayList<>();
	private List<FieldInfo> fields = new ArrayList<>();
	private List<String> interfaces = new ArrayList<>();
	private boolean isAbstract;
	private List<AttributeInfo> attributes = new ArrayList<>();

	public void setMagicNumber(int magic) {
		this.magic = magic;
	}

	public JavaClass create() {
		JavaClass result = new JavaClass(magic, minor, major);
		result.setConstants(constantPool);
		result.setAbstract(isAbstract);
		result.setAccessFlags(accessFlags);
		result.setClassName(((String) constantPool.get(classNameIndex).getObject()).replace("/", "."));
		result.setSuperclassName(((String) constantPool.get(superclassNameIndex).getObject()).replace("/", "."));
		result.setInterfaces(interfaces);
		result.setFields(fields);
		result.setMethods(methods);
		result.setAttributesCount(attributesCount);
		result.setAttributes(attributes);
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

	public void addFieldInfo(FieldInfo info) {
		fields.add(info);
	}

	public void setClassNameIndex(int classNameIndex) {
		this.classNameIndex = classNameIndex;
	}

	public void setSuperclassNameIndex(int superclassNameIndex) {
		this.superclassNameIndex = superclassNameIndex;
	}

	public void setAttributesCount(int attributesCount) {
		this.attributesCount = attributesCount;
	}

	public void addInterfaceInfo(String interfaceName) {
		interfaces.add(interfaceName);
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public void printConstantPool() {
		System.out.println(constantPool);
	}

	public void addAttribute(AttributeInfo attributeInfo) {
		this.attributes.add(attributeInfo);
	}

}
