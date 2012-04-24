package com.aldercape.internal.analyzer.javaclass;

public class AccessInfo {

	static final int ACC_ABSTRACT = 0x400;

	private static final int ACC_ENUM = 0x4000;

	private int accessFlags;

	public AccessInfo(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public int getRawValue() {
		return accessFlags;
	}

	public boolean isAbstract() {
		return (ACC_ABSTRACT & getRawValue()) != 0;
	}

	public boolean isEnumeration() {
		return (ACC_ENUM & getRawValue()) != 0;
	}

}
