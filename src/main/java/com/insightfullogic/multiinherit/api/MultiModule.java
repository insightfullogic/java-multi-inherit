package com.insightfullogic.multiinherit.api;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.insightfullogic.multiinherit.generation.GenerationMultiInjector;
import com.insightfullogic.multiinherit.reflection.ReflectionMultiInjector;

public class MultiModule extends AbstractModule {

	private final Class<?>[] classes;
	private final boolean useGeneration;
	
	public MultiModule(final boolean useGeneration, final Class<?> ... classes) {
		this.classes = classes;
		this.useGeneration = useGeneration;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void bindMulti(Class<?> ... classes) {
		if(useGeneration) {
			bind(MultiInjector.class).to(GenerationMultiInjector.class);
		} else {			
			bind(MultiInjector.class).to(ReflectionMultiInjector.class);
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

class MultiProvider<T> implements Provider<T> {

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