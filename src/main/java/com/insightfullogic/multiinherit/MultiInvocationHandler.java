package com.insightfullogic.multiinherit;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class MultiInvocationHandler implements InvocationHandler {

	private Map<Class<?>,Object> cache;
	
	public MultiInvocationHandler(Map<Class<?>, Object> lookup) {
		super();
		this.cache = lookup;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object obj = cache.get(method.getDeclaringClass());
		return method.invoke(obj, args);
	}

}
