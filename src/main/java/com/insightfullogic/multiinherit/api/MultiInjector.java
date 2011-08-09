package com.insightfullogic.multiinherit.api;

public interface MultiInjector {
	
	public <T> T getInstance(Class<T> combined);
	
}
