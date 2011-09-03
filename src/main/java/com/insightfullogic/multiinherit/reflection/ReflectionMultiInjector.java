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
import com.insightfullogic.multiinherit.api.Prefer;
import com.insightfullogic.multiinherit.common.ResolutionInfo;

@Singleton
public class ReflectionMultiInjector implements MultiInjector {

	@Inject
	private Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(final Class<T> combined) {
		final Class<?>[] parents = combined.getInterfaces();
		Class<?>[] interfaces = null;
		if (combined.isInterface()) {
			interfaces = Arrays.copyOf(parents, parents.length + 1);
			interfaces[parents.length] = combined;
		} else {
			interfaces = parents;
			throw new UnsupportedOperationException("To be implemented in future versions");
		}
		final Map<ResolutionInfo, Method> preferences1 = new HashMap<ResolutionInfo, Method>();
		for (final Method meth : combined.getDeclaredMethods()) {
			final Prefer prefer = meth.getAnnotation(Prefer.class);
			if (prefer != null) {
				final Class<?> preferredClass = prefer.value();
				try {
					final Method preferredMethod = preferredClass.getMethod(meth.getName(), meth.getParameterTypes());
					preferences1.put(new ResolutionInfo(meth), preferredMethod);
				} catch (final SecurityException e) {
					throw new IllegalArgumentException(e);
				} catch (final NoSuchMethodException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}

		final Map<ResolutionInfo, Method> preferences = preferences1;
		final Map<Class<?>, Object> lookup = new HashMap<Class<?>, Object>();
		for (final Class<?> cls : parents) {
			lookup.put(cls, injector.getInstance(cls));
		}
		final MultiInvocationHandler mih = new MultiInvocationHandler(lookup, preferences);
		return (T) Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, mih);
	}

}
