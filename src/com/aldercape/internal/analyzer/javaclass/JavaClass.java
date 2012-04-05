package com.aldercape.internal.analyzer.javaclass;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public class JavaClass extends ClassInfoBase implements ClassInfo {

	private VersionInfo version;

	public JavaClass(String constantClassName, VersionInfo versionInfo, ClassDetails parsedClassDetails) {
		super(constantClassName, parsedClassDetails);
		this.version = versionInfo;
	}

	public int getMagic() {
		return version.getMagicNumber();
	}

	public int getMinor() {
		return version.getMinorVersion();
	}

	public int getMajor() {
		return version.getMajorVersion();
	}

	public boolean isPublic() {
		return true;
	}

}
