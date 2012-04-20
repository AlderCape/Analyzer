package com.aldercape.internal.analyzer.javaclass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldercape.internal.analyzer.classmodel.AttributeInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfo;
import com.aldercape.internal.analyzer.classmodel.ClassInfoBase;
import com.aldercape.internal.analyzer.classmodel.ClassRepository;
import com.aldercape.internal.analyzer.classmodel.FieldInfo;
import com.aldercape.internal.analyzer.classmodel.MethodInfo;

public class JavaClassBuilder {

	private ConstantPoolInfo constantPool = new ConstantPoolInfo();
	private VersionInfo versionInfo;
	private int accessFlags;
	private String classNameIndex;

	private ClassInfo superclass;
	private Set<MethodInfo> methods = new HashSet<>();
	private List<FieldInfo> fields = new ArrayList<>();
	private List<ClassInfo> interfaces = new ArrayList<>();
	private AttributeInfo attributes;

	public ClassInfoBase create(ClassRepository repository) {
		ParsedClassDetails parsedClassDetails = new ParsedClassDetails(accessFlags, superclass, interfaces, fields, methods, attributes, versionInfo);
		ClassInfoBase result = repository.getClass(classNameIndex);
		result.setDetails(parsedClassDetails);
		// if (result.isInnerClass()) {
		// result.getEnclosingClass().addInnerClass(result);
		// }
		return result;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public void addConstant(Constant constant) {
		constantPool.add(constant);
	}

	public Constant getConstant(int index) {
		return constantPool.get(index);
	}

	public void addMethodInfo(MethodInfo info) {
		this.methods.add(info);
	}

	public void addFieldInfo(FieldInfo info) {
		fields.add(info);
	}

	public void setClassNameIndex(String classNameIndex) {
		this.classNameIndex = classNameIndex;
	}

	public void setSuperclassNameIndex(ClassInfo superclassNameIndex) {
		this.superclass = superclassNameIndex;
	}

	public void addInterfaceInfo(ClassInfo interfaceName) {
		interfaces.add(interfaceName);
	}

	public void setAttributes(AttributeInfo attributeInfo) {
		this.attributes = attributeInfo;
	}

	public void setVersionInfo(VersionInfo info) {
		this.versionInfo = info;
	}

	public ConstantPoolInfo getConstants() {
		return constantPool;
	}

	public String getConstantClassName(int index) throws IOException {
		return constantPool.getConstantClassName(index + 1);
	}

}
