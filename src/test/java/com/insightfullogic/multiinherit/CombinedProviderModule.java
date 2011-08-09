package com.insightfullogic.multiinherit;


public class CombinedProviderModule extends MultiModule {

	@Override
	protected void configure() {
		bindMulti(Combined.class);
	}
	
}
