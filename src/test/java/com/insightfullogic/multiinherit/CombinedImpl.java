package com.insightfullogic.multiinherit;

import com.google.inject.Inject;

/**
 * 
 * @author rlmw
 *
 * Example of how to manually proxy everything
 */
public class CombinedImpl implements Combined {

	@Inject A a;
	@Inject B b;
	
	@Override
	public String a() {
		return a.a();
	}

	@Override
	public String b() {
		return b.b();
	}

}
