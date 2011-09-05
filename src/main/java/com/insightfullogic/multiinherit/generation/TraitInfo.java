package com.insightfullogic.multiinherit.generation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

class TraitInfo {

	private final Class<?> implementation;
	private final Class<?> interfase;
	private final List<Method> toImplement;
	private final Map<Class<?>, Class<?>> parentTraits;

	public Map<Class<?>, Class<?>> getParentTraits() {
		return parentTraits;
	}

	public Class<?> getImplementation() {
		return implementation;
	}

	public List<Method> getToImplement() {
		return toImplement;
	}

	TraitInfo(final Class<?> implementation, final Class<?> interfase, final List<Method> toImplement, final Map<Class<?>, Class<?>> parentTraits) {
		super();
		this.implementation = implementation;
		this.interfase = interfase;
		this.toImplement = toImplement;
		this.parentTraits = parentTraits;
	}

	@Override
	public String toString() {
		return "TraitInfo [implementation=" + implementation + ", interfase=" + interfase + ", toImplement=" + toImplement + "]";
	}

	public Class<?> getInterfase() {
		return interfase;
	}

}
