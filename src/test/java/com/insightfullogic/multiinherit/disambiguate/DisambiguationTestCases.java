package com.insightfullogic.multiinherit.disambiguate;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.Util;
import com.insightfullogic.multiinherit.Util.Do;
import com.insightfullogic.multiinherit.api.MultiModule;
import com.insightfullogic.multiinherit.simple.A;

public class DisambiguationTestCases {

	@Test
	public void checkAtPrefer() {
		Util.withBools(new Do<Boolean>() {
			@Override
			public void apply(final Boolean b) {
				final Injector injector = Guice.createInjector(new MultiModule(b, CombinedPreferC.class));
				final A aImpl = injector.getInstance(A.class);
				final C cImpl = injector.getInstance(C.class);
				final CombinedPreferC combined = injector.getInstance(CombinedPreferC.class);

				// Ensure that combined prefers the implementation it has
				// inherited from C
				assertEquals(aImpl.a(), combined.a());
				assertEquals(cImpl.b(), combined.b());
			}
		});
	}

}
