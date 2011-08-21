package com.insightfullogic.multiinherit.generics;

public abstract class GenericCombinedTrait implements GenericTrait<Integer> {

	@Override
	public void inc() {
		final Integer val = get();
		set(val.intValue() + 1);
	}

}
