package com.insightfullogic.multiinherit.disambiguate;

import com.insightfullogic.multiinherit.Prefer;
import com.insightfullogic.multiinherit.simple.A;
import com.insightfullogic.multiinherit.simple.B;

public interface CombinedPreferC extends A, B, C {

	@Prefer(C.class)
	public String b();
	
}