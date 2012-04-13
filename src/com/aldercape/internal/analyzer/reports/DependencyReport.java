package com.aldercape.internal.analyzer.reports;

import java.util.List;
import java.util.SortedSet;

public interface DependencyReport<T> {

	public class MetricPair {

		private String message;
		private Object value;

		public MetricPair(String message, Object value) {
			this.message = message;
			this.value = value;
		}

		public String getMessage() {
			return message;
		}

		public Object getValue() {
			return value;
		}

	}

	public SortedSet<? extends T> getChildrenFor(T type);

	public SortedSet<T> getParentsFor(T type);

	public SortedSet<T> getIncludedTypes();

	public List<MetricPair> getMetricsPair(T type);

	public String getReportName();

}
