package com.insightfullogic.multiinherit.traits;

public abstract class Number implements Similarity {

	private int val;

	public void setVal(final int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public Number() {
		super();
	}

	/**
	 * Similar if the two are within 5 of each other
	 */
	@Override
	public boolean isSimilar(final Object other) {
		if (other instanceof Number) {
			final Number num = (Number) other;
			return Math.abs(val - num.val) < 5;
		}
		return false;
	}

}
