package com.insightfullogic.multiinherit.simple;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.Util;
import com.insightfullogic.multiinherit.Util.Do;
import com.insightfullogic.multiinherit.api.MultiModule;

public class SimpleTestCases {

	@Test
	public void egs() {
		Util.withBools(new Do<Boolean>() {
			@Override
			public void apply(final Boolean b) {
				final Injector injector = Guice.createInjector(new MultiModule(b, Combined.class));
				final A aImpl = injector.getInstance(A.class);
				final B bImpl = injector.getInstance(B.class);
				final Combined combinedImpl = injector.getInstance(CombinedImpl.class);
				final Combined byInjector = injector.getInstance(Combined.class);

				// Is the manual approach ok?
				assertEquals(aImpl.a(), combinedImpl.a());
				assertEquals(bImpl.b(), combinedImpl.b());

				// Is the automated approach ok?
				assertEquals(combinedImpl.a(), byInjector.a());
				assertEquals(combinedImpl.b(), byInjector.b());
			}
		});
	}

}
