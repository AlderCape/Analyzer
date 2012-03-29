package com.aldercape.internal.analyzer;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aldercape.internal.analyzer.classmodel.PackageInfo;

public class PackageInfoTest {

	@Test
	public void valueObject() {
		PackageInfo a1 = new PackageInfo("a");
		PackageInfo a2 = new PackageInfo("a");
		PackageInfo b = new PackageInfo("b");
		assertEquals("a", a1.toString());
		assertEquals(a1, a2);
		assertFalse(a1.equals(b));
		assertFalse(a1.equals(null));
		assertEquals(a1.hashCode(), a2.hashCode());
	}

}
