package com.aldercape.internal.analyzer.javaclass;


public class Constant {

	private ConstantPoolType type;
	private int nameIndex;
	private int typeIndex;
	private Object object;

	@Override
	public String toString() {
		return "Constant [type=" + type + ", nameIndex=" + nameIndex + ", typeIndex=" + typeIndex + ", object=" + object + "]";
	}

	public Constant(ConstantPoolType type, int nameIndex, int typeIndex) {
		this.type = type;
		this.nameIndex = nameIndex;
		this.typeIndex = typeIndex;
	}

	public Constant(ConstantPoolType type, int nameIndex) {
		this(type, nameIndex, 0);
	}

	public Constant(ConstantPoolType type, Object object) {
		this.type = type;
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

}
