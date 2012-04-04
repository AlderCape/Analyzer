package com.aldercape.internal.analyzer.javaclass;

public class Constant {

	private ConstantPoolType type;
	private int nameIndex;
	private Object object;

	@Override
	public String toString() {
		return "Constant [type=" + type + ", nameIndex=" + nameIndex + ", object=" + object + "]";
	}

	public Constant(ConstantPoolType type, int nameIndex, int typeIndex) {
		this(type, nameIndex);
	}

	public Constant(ConstantPoolType type, int nameIndex) {
		this.type = type;
		this.nameIndex = nameIndex;
	}

	public Constant(ConstantPoolType type, Object object) {
		this.type = type;
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public ConstantPoolType getType() {
		return type;
	}

	public boolean isAnnotation() {
		return type == ConstantPoolType.Utf8 && "RuntimeVisibleAnnotations".equals(object);
	}

	public boolean isException() {
		return type == ConstantPoolType.Utf8 && "Exceptions".equals(object);
	}

	public boolean isInnerClass() {
		return type == ConstantPoolType.Utf8 && "InnerClasses".equals(object);
	}

	protected String getName(JavaClassBuilder builder) {
		return (String) builder.getConstant(getNameIndex()).getObject();
	}

}
