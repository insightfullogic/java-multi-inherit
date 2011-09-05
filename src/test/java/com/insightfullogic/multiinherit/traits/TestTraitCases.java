package com.insightfullogic.multiinherit.traits;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestTraitCases {

	@Test
	public void testSingleTrait() {
		final Injector injector = Guice.createInjector(new MultiModule(true, Number.class));
		final Number num5 = injector.getInstance(Number.class), num6 = injector.getInstance(Number.class);
		num5.setVal(5);
		num6.setVal(6);
		final boolean sim = num5.isSimilar(num6);
		assertEquals(sim, !num5.isNotSimilar(num6));
	}

	@Test
	public void testMultipleTraitInheritance() {
		final Injector injector = Guice.createInjector(new MultiModule(true, NumberMulti.class));
		final NumberMulti num5 = injector.getInstance(NumberMulti.class), num6 = injector.getInstance(NumberMulti.class);
		num5.setVal(5);
		num6.setVal(6);
		final boolean sim = num5.isSimilar(num6);
		assertEquals(sim, !num5.isNotSimilar(num6));

		assertEquals(-1, num5.compare(num6));
		assertTrue(num5.lessThan(num6));
		assertFalse(num5.greaterThan(num6));
		assertFalse(num5.equalsOrd(num6));

		assertEquals(1, num6.compare(num5));
		assertFalse(num6.lessThan(num5));
		assertTrue(num6.greaterThan(num5));
		assertFalse(num6.equalsOrd(num5));

		assertEquals(0, num5.compare(num5));
		assertTrue(num5.equalsOrd(num5));
		assertFalse(num5.lessThan(num5));
		assertFalse(num5.greaterThan(num5));
	}

	@Test(expected = ProvisionException.class)
	public void testTraitCombinedWithInterface() {
		final Injector injector = Guice.createInjector(new MultiModule(true, FailSimilarity.class));
		injector.getInstance(FailSimilarity.class);
	}

	@Test(expected = ProvisionException.class)
	public void testTraitLackingImplementation() {
		final Injector injector = Guice.createInjector(new MultiModule(true, FailNumber.class));
		injector.getInstance(FailNumber.class);
	}

	@Test(expected = ProvisionException.class)
	public void testMultiTraitLackingOneImplementation() {
		final Injector injector = Guice.createInjector(new MultiModule(true, FailNumberMulti.class));
		injector.getInstance(FailNumberMulti.class);
	}

	@Test
	public void testTraitExtendingTrat() {
		final Injector injector = Guice.createInjector(new MultiModule(true, true, ExtendedNumber.class));
		final ExtendedNumber num1 = injector.getInstance(ExtendedNumber.class);
		final ExtendedNumber num2 = injector.getInstance(ExtendedNumber.class);
		final ExtendedNumber num3 = injector.getInstance(ExtendedNumber.class);
		num1.setF(0.25f);
		num2.setF(0.35f);
		num3.setF(1.0f);

		assertTrue(num1.isSimilar(num2));
		assertTrue(num1.isNotSimilar(num3));
	}
}
