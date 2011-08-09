package com.insightfullogic.multiinherit;
import java.lang.reflect.Method;
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
		final Class<?>[] parents = combined.getInterfaces();
		Class<?>[] interfaces = null;
		if(combined.isInterface()) {			
			interfaces = Arrays.copyOf(parents,parents.length + 1);
			interfaces[parents.length] = combined;
		} else {
			interfaces = parents;
			throw new UnsupportedOperationException("To be implemented in future versions");
		}
		
		final Map<ResolutionInfo,Method> preferences = new HashMap<ResolutionInfo, Method>();
		for (Method meth : combined.getDeclaredMethods()) {
			final Prefer prefer = meth.getAnnotation(Prefer.class);
			if(prefer != null) {
				final Class<?> preferredClass = prefer.value();
				try {
					final Method preferredMethod = preferredClass.getMethod(meth.getName(), meth.getParameterTypes());
					preferences.put(new ResolutionInfo(meth), preferredMethod);
				} catch (SecurityException e) {
					throw new IllegalArgumentException(e);
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		final Map<Class<?>,Object> lookup = new HashMap<Class<?>,Object>();
		for (Class<?> cls : parents) {
			lookup.put(cls, injector.getInstance(cls));
		}
		MultiInvocationHandler mih = new MultiInvocationHandler(lookup,preferences);
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, mih);
	}

	// TODO: implement subclasses abstract classes, by creating a concrete implementation with proxies:
	//	return (T) new CustomAB() {
	//	@Override
	//	public String a() {
	//		return "a";
	//	}
	//};
	
}
