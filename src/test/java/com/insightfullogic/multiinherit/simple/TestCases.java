package com.insightfullogic.multiinherit.simple;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;

public class TestCases {

	@Test
	public void egs() {
		Injector injector = Guice.createInjector(new MultiModule(Combined.class));
		A aImpl = injector.getInstance(A.class);
		B bImpl = injector.getInstance(B.class);
		Combined combinedImpl = injector.getInstance(CombinedImpl.class);
		Combined byInjector = injector.getInstance(Combined.class);
		
		// Is the manual approach ok?
		assertEquals(aImpl.a(),combinedImpl.a());
		assertEquals(bImpl.b(),combinedImpl.b());
		
		// Is the automated approach ok?
		assertEquals(combinedImpl.a(), byInjector.a());
		assertEquals(combinedImpl.b(), byInjector.b());
	}

}
