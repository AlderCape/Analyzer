package com.aldercape.internal.analyzer.javaclass;

public class VersionInfo {

	private int magicNumber;
	private int minorVersion;
	private int majorVersion;

	public VersionInfo(int magicNumber, int minorVersion, int majorVersion) {
		this.magicNumber = magicNumber;
		this.minorVersion = minorVersion;
		this.majorVersion = majorVersion;
	}

	public int getMagicNumber() {
		return magicNumber;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

}
