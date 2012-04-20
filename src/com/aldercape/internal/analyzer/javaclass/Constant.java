package com.aldercape.internal.analyzer.javaclass;

public class Constant {

	private ConstantPoolType type;
	private int nameIndex;
	private Object object;

	public enum ConstantAttributeType {
		Code, RuntimeVisibleAnnotations, Exceptions, InnerClasses, LocalVariableTable, SourceFile, LineNumberTable, Signature, LocalVariableTypeTable, StackMapTable, EnclosingMethod, ConstantValue, Unkown;
	}

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

	public String getName(ConstantPoolInfo constants) {
		return (String) constants.get(getNameIndex()).getObject();
	}

	public ConstantAttributeType getAttributeType() {
		if (type != ConstantPoolType.Utf8) {
			return ConstantAttributeType.Unkown;
		}
		try {
			return ConstantAttributeType.valueOf((String) object);
		} catch (IllegalArgumentException e) {
			System.out.println("Unrecognized parser requsted: " + object);
			return ConstantAttributeType.Unkown;
		}
	}

}
