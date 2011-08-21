package com.insightfullogic.multiinherit.generics;

import com.insightfullogic.multiinherit.api.TraitWith;

@TraitWith(GenericTraitImpl.class)
public interface GenericTrait<A> {

	public void set(A a);

	public A get();

	public void inc();
	
}
