package com.insightfullogic.multiinherit.arguments;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.Util;
import com.insightfullogic.multiinherit.Util.Do;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestArgumentCases {

	@Test
	public void checkArguments() {
		Util.withBools(new Do<Boolean>() {
			@Override
			public void apply(final Boolean b) {
				final Injector injector = Guice.createInjector(new MultiModule(b, CombinedArguments.class));
				final CombinedArguments combined = injector.getInstance(CombinedArguments.class);

				assertEquals("a", combined.a());
				assertEquals("something", combined.nothing("something"));
			}
		});
	}

}
