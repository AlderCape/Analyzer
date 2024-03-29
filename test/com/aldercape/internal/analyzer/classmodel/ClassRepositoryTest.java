package com.aldercape.internal.analyzer.classmodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassRepositoryTest {

	@Test
	public void shouldAlwaysReturnAClassInfo() {
		ClassInfoBase class1 = new ClassRepository().getClass("testclass");
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
		ClassRepository repository = new ClassRepository();
		repository.addListener(l);
		ClassInfoBase class1 = repository.getClass("testclass");
		assertSame(class1, l.newClass);
		l.newClass = null;
		repository.getClass("testclass");
		assertNull(l.newClass);
	}
}
