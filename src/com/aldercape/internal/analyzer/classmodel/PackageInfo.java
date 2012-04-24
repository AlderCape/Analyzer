package com.aldercape.internal.analyzer.classmodel;

public class PackageInfo implements Comparable<PackageInfo>, TypeInfo {

	private String name;

	public PackageInfo(String name) {
		this.name = name;
	}

	public boolean isParentTo(PackageInfo info) {
		if (!info.getPackage().getName().startsWith(getName())) {
			return false;
		}
		return info.getPackage().getName().charAt(name.length()) == '.';
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		PackageInfo other = (PackageInfo) obj;
		return getName().equals(other.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public int compareTo(PackageInfo o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public PackageInfo getPackage() {
		return this;
	}

}
