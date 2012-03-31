package com.aldercape.internal.analyzer.reports;

import java.util.List;
import java.util.Set;
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

	public Set<? extends T> getChildrenFor(T type);

	public SortedSet<T> getIncludedTypes();

	List<MetricPair> getMetricsPair(T type);

}
