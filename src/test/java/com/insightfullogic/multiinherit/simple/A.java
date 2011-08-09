package com.insightfullogic.multiinherit.simple;

import com.google.inject.ImplementedBy;

@ImplementedBy(AImpl.class)
public interface A {

	public String a();
}

class AImpl implements A{

	@Override
	public String a() {
		return "a";
	}
	
}