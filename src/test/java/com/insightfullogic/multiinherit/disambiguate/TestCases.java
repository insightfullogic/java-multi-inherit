package com.insightfullogic.multiinherit.disambiguate;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;
import com.insightfullogic.multiinherit.simple.A;

public class TestCases {

	@Test
	public void checkAtPrefer() {
		Injector injector = Guice.createInjector(new MultiModule(CombinedPreferC.class));
		A aImpl = injector.getInstance(A.class);
		C cImpl = injector.getInstance(C.class);
		CombinedPreferC combined = injector.getInstance(CombinedPreferC.class);

		// Ensure that combined prefers the implementation it has inherited from C
		assertEquals(aImpl.a(),combined.a());
		assertEquals(cImpl.b(),combined.b());
	}

}
