package com.insightfullogic.multiinherit.traits;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestCases {

	@Test
	public void checkNumber() {
		final Injector injector = Guice.createInjector(new MultiModule(true, Number.class));
		final Number num5 = injector.getInstance(Number.class), num6 = injector.getInstance(Number.class);
		num5.setVal(5);
		num6.setVal(6);
		assertEquals(num5.isSimilar(num6), !num5.isNotSimilar(num6));
	}

}
