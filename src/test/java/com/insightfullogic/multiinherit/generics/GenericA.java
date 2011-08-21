package com.insightfullogic.multiinherit.generics;

import com.google.inject.ImplementedBy;

@ImplementedBy(GenericAImpl.class)
public interface GenericA<T> {

	public T a(T t);

}

class GenericAImpl<T> implements GenericA<T> {

	@Override
	public T a(final T t) {
		return t;
	}

}