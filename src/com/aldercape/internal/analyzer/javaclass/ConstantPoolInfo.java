package com.aldercape.internal.analyzer.javaclass;

import java.util.ArrayList;
import java.util.List;

public class ConstantPoolInfo {

	private List<Constant> constantPool = new ArrayList<>();

	public void add(Constant constant) {
		constantPool.add(constant);
	}

	public Constant get(int index) {
		return constantPool.get(index - 1);
	}

	protected String getConstantClassName(int index) {
		return ((String) get(index).getObject()).replace("/", ".");
	}

	public int size() {
		return constantPool.size();
	}

}
