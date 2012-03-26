package com.aldercape.internal.analyzer.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.aldercape.internal.analyzer.ClassInfo;
import com.aldercape.internal.analyzer.PackageInfo;

public class PackageDependencyInfo implements Comparable<PackageDependencyInfo> {

	private PackageInfo packageInfo;
	private List<ClassInfo> classes = new ArrayList<>();
	private List<ClassInfo> afferentClasses = new ArrayList<>();

	public PackageDependencyInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	@Override
	public int compareTo(PackageDependencyInfo o) {
		return packageInfo.compareTo(o.packageInfo);
	}

	public void add(ClassInfo info) {
		classes.add(info);
	}

	public SortedSet<PackageInfo> efferentSet() {
		TreeSet<PackageInfo> result = new TreeSet<>();
		for (ClassInfo c : classes) {
			result.addAll(c.getPackageDependencies());
		}
		return result;
	}

	public TreeSet<PackageInfo> getAfferent() {
		TreeSet<PackageInfo> result = new TreeSet<>();
		for (ClassInfo c : afferentClasses) {
			result.add(c.getPackage());
		}
		return result;
	}

	public void addAfferentClass(ClassInfo info) {
		afferentClasses.add(info);
	}

	public float getAbstractness() {
		int classCount = 0;
		int abstractCount = 0;
		for (ClassInfo classInfo : classes) {
			classCount++;
			if (classInfo.isAbstract()) {
				abstractCount++;
			}
		}
		return (float) abstractCount / classCount;
	}

	protected int totalCoupling() {
		return efferentSet().size() + getAfferent().size();
	}

	protected float getInstability() {
		float totalCoupling = totalCoupling();
		if (totalCoupling == 0) {
			return 0;
		}
		return efferentSet().size() / totalCoupling;
	}

	protected float getDistance() {
		return Math.abs(getAbstractness() + getInstability() - 1);
	}

}
