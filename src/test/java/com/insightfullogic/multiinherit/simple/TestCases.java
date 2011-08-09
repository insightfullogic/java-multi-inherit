package com.insightfullogic.multiinherit.simple;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.MultiInjector;
import com.insightfullogic.multiinherit.MultiModule;

public class TestCases {

	@Test
	public void egs() {
		Injector injector = Guice.createInjector();
		A aImpl = injector.getInstance(A.class);
		B bImpl = injector.getInstance(B.class);
		Combined combinedImpl = injector.getInstance(CombinedImpl.class);
		
		MultiInjector multiInjector = injector.getInstance(MultiInjector.class); 
		Combined byInjector = multiInjector.getInstance(Combined.class);
		
		// Manual Tests
		assertEquals(aImpl.a(),combinedImpl.a());
		assertEquals(bImpl.b(),combinedImpl.b());
		
		// Manual == Automated
		assertEquals(combinedImpl.a(), byInjector.a());
		assertEquals(combinedImpl.b(), byInjector.b());
	}
	
	@Test
	public void byProvider() {
		Injector injector = Guice.createInjector(new MultiModule(Combined.class));
		Combined combinedImpl = injector.getInstance(CombinedImpl.class);
		Combined byInjector = injector.getInstance(Combined.class);
		
		// The same as above, but with slicker syntax
		assertEquals(combinedImpl.a(), byInjector.a());
		assertEquals(combinedImpl.b(), byInjector.b());
	}

}
