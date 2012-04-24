package com.aldercape.internal.analyzer.reports;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;
import com.aldercape.internal.analyzer.classmodel.TypeInfo;
import com.aldercape.internal.analyzer.reports.DependencyReport.MetricPair;

public class FilteredDependencyReportTest {

	private FilteredDependencyReport<TypeInfo> report;
	protected SortedSet<TypeInfo> children;
	protected SortedSet<TypeInfo> parents;
	private SortedSet<TypeInfo> includedTypes;
	protected List<MetricPair> metricsPair;

	@Before
	public void setUp() {
		DependencyReport<TypeInfo> baseReport = new DependencyReport<TypeInfo>() {

			@Override
			public SortedSet<? extends TypeInfo> getChildrenFor(TypeInfo type) {
				return children;
			}

			@Override
			public SortedSet<TypeInfo> getParentsFor(TypeInfo type) {
				return parents;
			}

			@Override
			public SortedSet<TypeInfo> getIncludedTypes() {
				return includedTypes;
			}

			@Override
			public List<DependencyReport.MetricPair> getMetricsPair(TypeInfo type) {
				return metricsPair;
			}

			@Override
			public String getReportName() {
				return "Base report";
			}
		};
		report = new FilteredDependencyReport<>(baseReport);
		children = new TreeSet<>();
		parents = new TreeSet<>();
		includedTypes = new TreeSet<>();
		metricsPair = new ArrayList<>();

	}

	@Test
	public void shouldDelegateToBaseReport() {
		TypeInfo type = createTypeInfo("thispackage", "Me");
		children.add(createTypeInfo("package", "name"));
		parents.add(createTypeInfo("parentpackage", "Parent"));
		includedTypes.add(type);
		assertEquals("Base report", report.getReportName());
		assertEquals(children, report.getChildrenFor(type));
		assertEquals(parents, report.getParentsFor(type));
		assertEquals(includedTypes, report.getIncludedTypes());
		assertSame(metricsPair, report.getMetricsPair(type));
	}

	@Test
	public void shouldFilterSpecifiedPackages() {
		TypeInfo unfilteredChild = createTypeInfo("unfilteredPackage", "Child1");
		TypeInfo unfilteredParent = createTypeInfo("unfilteredPackage", "Parent1");
		TypeInfo unfilteredType = createTypeInfo("unfilteredPackage", "Type1");

		children.add(createTypeInfo("filteredPackage", "Child1"));
		parents.add(createTypeInfo("filteredPackage", "Parent1"));
		children.add(unfilteredChild);
		parents.add(unfilteredParent);
		includedTypes.add(unfilteredType);
		includedTypes.add(createTypeInfo("filteredPackage", "Type1"));
		report.ignorePackage(new PackageInfo("filteredPackage"));
		assertEquals("Base report", report.getReportName());
		assertEquals(Collections.singleton(unfilteredChild), report.getChildrenFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredParent), report.getParentsFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredType), report.getIncludedTypes());
	}

	@Test
	public void shouldIncludeSpecifiedPackagesEvenIfIgnored() {
		TypeInfo unfilteredChild = createTypeInfo("unfilteredPackage", "Child1");
		TypeInfo unfilteredParent = createTypeInfo("unfilteredPackage", "Parent1");
		TypeInfo unfilteredType = createTypeInfo("unfilteredPackage", "Type1");

		children.add(createTypeInfo("filteredPackage", "Child1"));
		parents.add(createTypeInfo("filteredPackage", "Parent1"));
		children.add(unfilteredChild);
		parents.add(unfilteredParent);
		includedTypes.add(unfilteredType);
		includedTypes.add(createTypeInfo("filteredPackage", "Type1"));

		report.includePackage(new PackageInfo("filteredPackage"));
		report.ignorePackage(new PackageInfo("filteredPackage"));
		assertEquals(children, report.getChildrenFor(unfilteredType));
		assertEquals(parents, report.getParentsFor(unfilteredType));
		assertEquals(includedTypes, report.getIncludedTypes());
	}

	@Test
	public void shouldBeAbleToFilterSubPackages() {
		TypeInfo unfilteredChild = createTypeInfo("unfilteredPackage", "Child1");
		TypeInfo unfilteredParent = createTypeInfo("unfilteredPackage", "Parent1");
		TypeInfo unfilteredType = createTypeInfo("unfilteredPackage", "Type1");

		children.add(createTypeInfo("filteredPackage.subpackage", "Child1"));
		parents.add(createTypeInfo("filteredPackage.subpackage", "Parent1"));
		children.add(unfilteredChild);
		parents.add(unfilteredParent);
		includedTypes.add(unfilteredType);
		includedTypes.add(createTypeInfo("filteredPackage.subpackage", "Type1"));

		report.ignoreParentPackage(new PackageInfo("filteredPackage"));
		assertEquals(Collections.singleton(unfilteredChild), report.getChildrenFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredParent), report.getParentsFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredType), report.getIncludedTypes());
	}

	@Test
	public void shouldBeAbleToFilterExactMatch() {
		TypeInfo unfilteredChild = createTypeInfo("unfilteredPackage", "Child1");
		TypeInfo unfilteredParent = createTypeInfo("unfilteredPackage", "Parent1");
		TypeInfo unfilteredType = createTypeInfo("unfilteredPackage", "Type1");

		children.add(createTypeInfo("filteredPackage", "Child1"));
		parents.add(createTypeInfo("filteredPackage", "Parent1"));
		children.add(unfilteredChild);
		parents.add(unfilteredParent);
		includedTypes.add(unfilteredType);
		includedTypes.add(createTypeInfo("filteredPackage", "Type1"));

		report.ignoreParentPackage(new PackageInfo("filteredPackage"));
		assertEquals(Collections.singleton(unfilteredChild), report.getChildrenFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredParent), report.getParentsFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredType), report.getIncludedTypes());
	}

	@Test
	public void shouldNotFilterSubPackageThatDoesOnlyMatchBySubpartOfPackage() {
		TypeInfo unfilteredChild = createTypeInfo("unfilteredPackage", "Child1");
		TypeInfo unfilteredParent = createTypeInfo("unfilteredPackage", "Parent1");
		TypeInfo unfilteredType = createTypeInfo("unfilteredPackage", "Type1");

		children.add(unfilteredChild);
		parents.add(unfilteredParent);
		includedTypes.add(unfilteredType);

		report.ignoreParentPackage(new PackageInfo("unfi"));
		assertEquals(Collections.singleton(unfilteredChild), report.getChildrenFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredParent), report.getParentsFor(unfilteredType));
		assertEquals(Collections.singleton(unfilteredType), report.getIncludedTypes());
	}

	private TypeInfo createTypeInfo(final String packageName, final String name) {
		class TypeInfoStub implements TypeInfo, Comparable<TypeInfoStub> {

			@Override
			public PackageInfo getPackage() {
				return new PackageInfo(packageName);
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public int compareTo(TypeInfoStub other) {
				int result = getPackage().compareTo(other.getPackage());
				if (result == 0) {
					result = getName().compareTo(other.getName());
				}
				return result;
			}

			@Override
			public String toString() {
				return "Package:" + packageName + " Name:" + name;
			}
		}
		return new TypeInfoStub();
	}
}
