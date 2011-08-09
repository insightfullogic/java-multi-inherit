package com.insightfullogic.multiinherit;

import com.google.inject.AbstractModule;

public class MultiModule extends AbstractModule {

	private static final Object lock = new Object();
	private static boolean initialised = false;
	
	private final Class<?>[] classes;
	
	public MultiModule(Class<?> ... classes) {
		this.classes = classes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	}

	@Override
	protected void configure() {
		bindMulti(classes);
	}
	
}
