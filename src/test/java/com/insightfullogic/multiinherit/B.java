package com.insightfullogic.multiinherit;

import com.google.inject.ImplementedBy;

@ImplementedBy(BImpl.class)
public interface B {

	public String b();
}

class BImpl implements B {

	@Override
	public String b() {
		return "b";
	}
	
}
