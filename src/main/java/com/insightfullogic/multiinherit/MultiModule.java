package com.insightfullogic.multiinherit;

import com.google.inject.AbstractModule;

public abstract class MultiModule extends AbstractModule {

	private static final Object lock = new Object();
	private static boolean initialised = false;
	
	protected void bindMulti(Class<?> ... classes) {
		synchronized (lock) {
			if(!initialised) {
				bind(MultiInjector.class);
				initialised = true;
			}
		}
		for (Class cls : classes) {			
			bind(cls).toProvider(new MultiProvider(cls));
		}
//		bind(Object.class).annotatedWith(Names.named("foo")).toProvider(null);
	}
	
}
