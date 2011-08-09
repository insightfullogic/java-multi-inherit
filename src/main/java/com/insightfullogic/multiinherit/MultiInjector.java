package com.insightfullogic.multiinherit;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class MultiInjector {
	
	@Inject Injector injector;
	
	public <T> T getInstance(Class<T> combined) {
		Class<?>[] parents = combined.getInterfaces();
		int n = parents.length;
		Class<?>[] interfaces = Arrays.copyOf(parents,n + 1);
		interfaces[n] = combined;
		Map<Class<?>,Object> lookup = new HashMap<Class<?>,Object>();
		for (Class<?> cls : parents) {
			lookup.put(cls, injector.getInstance(cls));
		}
		MultiInvocationHandler mih = new MultiInvocationHandler(lookup);
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, mih);
	}
	
}
