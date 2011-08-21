package com.insightfullogic.multiinherit.generics;

public abstract class GenericTraitImpl<A> implements GenericTrait<A> {

	private A a;

	@Override
	public void set(final A a) {
		this.a = a;
	}

	@Override
	public A get() {
		return a;
	}

}
