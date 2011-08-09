package com.insightfullogic.multiinherit.disambiguate;

import com.google.inject.ImplementedBy;

@ImplementedBy(CImpl.class)
public interface C {

	public String b();
}

class CImpl implements C {

	@Override
	public String b() {
		return "c";
	}
	
}
