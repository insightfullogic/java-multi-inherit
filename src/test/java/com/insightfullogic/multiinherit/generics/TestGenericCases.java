package com.insightfullogic.multiinherit.generics;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestGenericCases {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void simpleCases() {
		final Injector injector = Guice.createInjector(new MultiModule(true, GenericCombined.class));
		final GenericCombined inst = injector.getInstance(GenericCombined.class);
		Assert.assertEquals(1, inst.a(1));
		Assert.assertEquals(1, inst.b(1));

		final GenericCombined<Integer, Integer> casted = injector.getInstance(GenericCombined.class);
		final Integer one = new Integer(1);
		Assert.assertEquals(one, casted.a(1));
		Assert.assertEquals(one, casted.b(1));
	}

	@Test
	public void singleTrait() {
		final Injector injector = Guice.createInjector(new MultiModule(true, GenericCombinedTrait.class));
		final GenericCombinedTrait trait = injector.getInstance(GenericCombinedTrait.class);
		final Integer one = new Integer(1), two = new Integer(2);
		trait.set(one);
		Assert.assertEquals(one, trait.get());
		trait.inc();
		Assert.assertEquals(two, trait.get());
	}

}
