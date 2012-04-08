package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.List;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;

public class JavaClassBuilder {

	private ConstantPoolInfo constants = new ConstantPoolInfo();
	private VersionInfo versionInfo;
	private List<Constant> constantPool = new ArrayList<>();
	private int accessFlags;
	private int classNameIndex;

	private int superclassNameIndex;
	private List<MethodInfo> methods = new ArrayList<>();
	private List<FieldInfo> fields = new ArrayList<>();
	private List<String> interfaces = new ArrayList<>();
	private AttributeInfo attributes;

	public ClassInfoBase create() {
		ParsedClassDetails parsedClassDetails = new ParsedClassDetails(accessFlags, ClassRepository.getClass(constants.getConstantClassName(superclassNameIndex + 1)), interfaces, fields, methods, attributes, versionInfo);
		ClassInfoBase result = ClassRepository.getClass(constants.getConstantClassName(classNameIndex + 1));
		result.setDetails(parsedClassDetails);
		if (result.isInnerClass() && !result.equals(result.getEnclosingClass())) {
			result.getEnclosingClass().addInnerClass(result);
		}
		return result;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public void addConstant(Constant constant) {
		constants.add(constant);
		this.constantPool.add(constant);
	}

	public Constant getConstant(int index) {
		return constants.get(index);
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

	public void addInterfaceInfo(String interfaceName) {
		interfaces.add(interfaceName);
	}

	public void setAttributes(AttributeInfo attributeInfo) {
		this.attributes = attributeInfo;
	}

	public void setVersionInfo(VersionInfo info) {
		this.versionInfo = info;
	}

	public ConstantPoolInfo getConstants() {
		return constants;
	}

}
