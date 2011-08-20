package com.insightfullogic.multiinherit.generation;

import java.util.Map;

public class ClassCache {

	private final Class<?> implementation;
	private final Map<Class<?>, Class<?>> traits;

	public ClassCache(final Class<?> implementation, final Map<Class<?>, Class<?>> traits) {
		super();
		this.implementation = implementation;
		this.traits = traits;
	}

	public Class<?> getImplementation() {
		return implementation;
	}

	public Map<Class<?>, Class<?>> getTraits() {
		return traits;
	}

	@Override
	public String toString() {
		return "ClassCache [implementation=" + implementation + ", traits=" + traits + "]";
	}

}
