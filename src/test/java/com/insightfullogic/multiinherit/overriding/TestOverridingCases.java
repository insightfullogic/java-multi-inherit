package com.insightfullogic.multiinherit.overriding;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.insightfullogic.multiinherit.api.MultiModule;
import com.insightfullogic.multiinherit.simple.A;

public class TestOverridingCases {

	@Test
	public void customImplementation() {
		final Injector injector = Guice.createInjector(new MultiModule(true, CustomAB.class));
		final A aImpl = injector.getInstance(A.class);
		final CustomAB combined = injector.getInstance(CustomAB.class);

		// The custom implementation returns 'c'
		assertEquals("c", combined.b());

		// The A is proxied from AImpl
		assertEquals(aImpl.a(), combined.a());
	}

	@Test(expected = ProvisionException.class)
	public void customImplementationAndReflection() {
		final Injector injector = Guice.createInjector(new MultiModule(false, CustomAB.class));
		injector.getInstance(CustomAB.class);
	}

}
