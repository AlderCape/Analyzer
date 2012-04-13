package com.aldercape.internal.analyzer.classmodel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassRepositoryTest {

	@Before
	public void setUp() {
		ClassRepository.reset();
	}

	@Test
	public void shouldAlwaysReturnAClassInfo() {
		ClassInfoBase class1 = ClassRepository.getClass("testclass");
		assertEquals("testclass", class1.getName());
	}

	@Test
	public void shouldNotifyListenersWhenANewInstanceIsCreated() {
		class ListenerSpy implements ClassRepository.ClassRepositoryListener {
			private ClassInfo newClass;

			@Override
			public void classCreated(ClassInfo newClass) {
				this.newClass = newClass;
			}
		}
		;
		ListenerSpy l = new ListenerSpy();
		ClassRepository.addListener(l);
		ClassInfoBase class1 = ClassRepository.getClass("testclass");
		assertSame(class1, l.newClass);
		l.newClass = null;
		ClassRepository.getClass("testclass");
		assertNull(l.newClass);
	}
}
