package com.insightfullogic.multiinherit;

public class Util {

	public static interface Do<T> {
		public void apply(T t);
	}

	public static void withBools(final Do<Boolean> what) {
		what.apply(true);
		what.apply(false);
	}
}
