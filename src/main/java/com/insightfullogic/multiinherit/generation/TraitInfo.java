package com.insightfullogic.multiinherit.generation;

import java.lang.reflect.Method;
import java.util.List;

public class TraitInfo {

	private final Class<?> implementation;
	private final Class<?> interfase;
	private final List<Method> toImplement;

	public Class<?> getImplementation() {
		return implementation;
	}

	public List<Method> getToImplement() {
		return toImplement;
	}

	public TraitInfo(final Class<?> implementation, final Class<?> interfase, final List<Method> toImplement) {
		super();
		this.implementation = implementation;
		this.interfase = interfase;
		this.toImplement = toImplement;
	}

	@Override
	public String toString() {
		return "TraitInfo [implementation=" + implementation + ", interfase=" + interfase + ", toImplement=" + toImplement + "]";
	}

	public Class<?> getInterfase() {
		return interfase;
	}

}
