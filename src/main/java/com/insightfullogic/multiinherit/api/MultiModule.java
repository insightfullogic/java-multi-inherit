package com.insightfullogic.multiinherit.api;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.insightfullogic.multiinherit.generation.GenerationMultiInjector;
import com.insightfullogic.multiinherit.reflection.ReflectionMultiInjector;

public class MultiModule extends AbstractModule {

	private final Class<?>[] classes;
	private final boolean useGeneration;
	private final boolean dumpClasses;

	public MultiModule(final boolean useGeneration, final Class<?>... classes) {
		this(useGeneration, false, classes);
	}

	public MultiModule(final boolean useGeneration, final boolean dumpClasses, final Class<?>... classes) {
		if (!useGeneration && dumpClasses) {
			throw new IllegalArgumentException("You cannot dump the generate class files if you're not generating class files");
		}
		this.classes = classes;
		this.useGeneration = useGeneration;
		this.dumpClasses = dumpClasses;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void bindMulti(final Class<?>... classes) {
		if (useGeneration) {
			bind(MultiInjector.class).to(GenerationMultiInjector.class);
			bind(Boolean.class).annotatedWith(Names.named("dump")).toInstance(dumpClasses);
		} else {
			bind(MultiInjector.class).to(ReflectionMultiInjector.class);
		}
		for (final Class cls : classes) {
			bind(cls).toProvider(new MultiProvider(cls));
		}
	}

	@Override
	protected void configure() {
		bindMulti(classes);
	}

}

class MultiProvider<T> implements Provider<T> {

	private final Class<T> interfase;

	MultiProvider(final Class<T> interfase) {
		this.interfase = interfase;
	}

	@Inject
	private MultiInjector injector;

	@Override
	public T get() {
		return injector.getInstance(interfase);
	}
}