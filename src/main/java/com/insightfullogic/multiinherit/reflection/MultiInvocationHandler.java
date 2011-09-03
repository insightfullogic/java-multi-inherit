package com.insightfullogic.multiinherit.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.insightfullogic.multiinherit.common.ResolutionInfo;

class MultiInvocationHandler implements InvocationHandler {

	private final Map<Class<?>, Object> cache;
	private final Map<ResolutionInfo, Method> preferences;

	public MultiInvocationHandler(final Map<Class<?>, Object> lookup, final Map<ResolutionInfo, Method> preferences) {
		super();
		cache = lookup;
		this.preferences = preferences;
	}

	@Override
	public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {
		final Method preferred = preferences.get(new ResolutionInfo(method));
		if (preferred != null) {
			method = preferred;
		}
		final Object obj = cache.get(method.getDeclaringClass());
		return method.invoke(obj, args);
	}

}
