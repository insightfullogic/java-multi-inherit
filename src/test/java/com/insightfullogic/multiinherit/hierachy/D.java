package com.insightfullogic.multiinherit.hierachy;

import com.google.inject.ImplementedBy;

@ImplementedBy(DImpl.class)
public interface D {

	public String d();

}

class DImpl implements D {

	@Override
	public String d() {
		return "d";
	}

}