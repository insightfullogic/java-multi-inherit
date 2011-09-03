package com.insightfullogic.multiinherit.hierachy;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;
import com.insightfullogic.multiinherit.simple.Combined;

public class TestHierachyCases {

	@Test
	public void reallyCombined() {
		final Injector in = Guice.createInjector(new MultiModule(true, true, Combined.class, MoreCombined.class));
		final Combined comb = in.getInstance(Combined.class);
		final MoreCombined moreComb = in.getInstance(MoreCombined.class);

		assertEquals(comb.a(), moreComb.a());
		assertEquals(comb.b(), moreComb.b());
		assertEquals("d", moreComb.d());
	}
}
