package com.insightfullogic.multiinherit.generics;

import com.google.inject.ImplementedBy;

@ImplementedBy(GenericBImpl.class)
public interface GenericB<T> {

	public T b(T t);

}

class GenericBImpl<T> implements GenericB<T> {

	@Override
	public T b(final T t) {
		return t;
	}

}