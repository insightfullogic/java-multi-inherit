package com.insightfullogic.multiinherit.primitives;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.Util;
import com.insightfullogic.multiinherit.Util.Do;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestPrimitiveCases {

	@Test
	public void testPrim() {
		Util.withBools(new Do<Boolean>() {
			@Override
			public void apply(final Boolean b) {
				final Injector injector = Guice.createInjector(new MultiModule(b, PrimCombined.class));
				final PrimCombined inst = injector.getInstance(PrimCombined.class);
				assertEquals(5, inst.p());
				assertEquals(5l, inst.p(5));
				assertEquals(false, inst.p((short) 5, (byte) 3));
			}
		});
	}
}
