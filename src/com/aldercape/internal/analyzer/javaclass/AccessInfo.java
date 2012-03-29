package com.aldercape.internal.analyzer.javaclass;

public class AccessInfo {

	static final int ACC_ABSTRACT = 0x400;

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

}
