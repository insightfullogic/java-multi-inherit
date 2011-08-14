package com.insightfullogic.multiinherit.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.insightfullogic.multiinherit.api.Prefer;

public class Util {

	public static <T> Map<ResolutionInfo, Method> getPreferences(Class<T> combined) {
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
		return preferences;
	}
	
}
