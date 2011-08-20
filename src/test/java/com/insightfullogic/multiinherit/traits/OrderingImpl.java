package com.insightfullogic.multiinherit.traits;

public abstract class OrderingImpl implements Ordering {

	@Override
	public boolean lessThan(final Object other) {
		return compare(other) < 0;
	}

	@Override
	public boolean greaterThan(final Object other) {
		return compare(other) > 0;
	}

	@Override
	public boolean equalsOrd(final Object other) {
		return compare(other) == 0;
	}

}
