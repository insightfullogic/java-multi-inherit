package com.insightfullogic.multiinherit.api;
import com.google.inject.Singleton;

public interface MultiInjector {
	
	public <T> T getInstance(Class<T> combined);
	
}
