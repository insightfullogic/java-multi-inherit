package com.insightfullogic.multiinherit;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MultiProvider<T> implements Provider<T> {

	private Class<T> interfase;
		
	MultiProvider(Class<T> interfase) {
		this.interfase = interfase;
	}

	@Inject MultiInjector injector;
	
	@Override
	public T get() {
		return injector.getInstance(interfase);
	}
}
