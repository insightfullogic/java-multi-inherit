package com.insightfullogic.multiinherit.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.insightfullogic.multiinherit.api.MultiInjector;
import com.insightfullogic.multiinherit.common.ResolutionInfo;
import com.insightfullogic.multiinherit.common.Util;

@Singleton
public class ReflectionMultiInjector implements MultiInjector {

	@Inject Injector injector;
	
	@Override
	public <T> T getInstance(Class<T> combined) {
		final Class<?>[] parents = combined.getInterfaces();
		Class<?>[] interfaces = null;
		if(combined.isInterface()) {			
			interfaces = Arrays.copyOf(parents,parents.length + 1);
			interfaces[parents.length] = combined;
		} else {
			interfaces = parents;
			throw new UnsupportedOperationException("To be implemented in future versions");
		}
		
		final Map<ResolutionInfo, Method> preferences = Util.getPreferences(combined);
		final Map<Class<?>,Object> lookup = new HashMap<Class<?>,Object>();
		for (Class<?> cls : parents) {
			lookup.put(cls, injector.getInstance(cls));
		}
		MultiInvocationHandler mih = new MultiInvocationHandler(lookup,preferences);
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, mih);
	}

}
