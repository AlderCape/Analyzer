package com.aldercape.internal.analyzer.reports;

import com.aldercape.internal.analyzer.classmodel.ClassInfo;

public interface ClassConsumer {

	public void addClass(ClassInfo info);
}
