package com.insightfullogic.multiinherit.traits;

public abstract class NumberMulti implements Similarity, Ordering {

	private int val;

	public void setVal(final int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public NumberMulti() {
		super();
	}

	/**
	 * Similar if the two are within 5 of each other
	 */
	@Override
	public boolean isSimilar(final Object other) {
		if (other instanceof NumberMulti) {
			final NumberMulti num = (NumberMulti) other;
			return Math.abs(val - num.val) < 5;
		}
		return false;
	}

	@Override
	public int compare(final Object other) {
		if (other instanceof NumberMulti) {
			final NumberMulti nm = (NumberMulti) other;
			return new Integer(val).compareTo(nm.val);
		}
		return new Integer(hashCode()).compareTo(other.hashCode());
	}

}
