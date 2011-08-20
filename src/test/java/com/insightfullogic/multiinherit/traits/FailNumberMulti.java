package com.insightfullogic.multiinherit.traits;

public abstract class FailNumberMulti implements Similarity, Ordering {

	private int val;

	public void setVal(final int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public FailNumberMulti() {
		super();
	}

	/**
	 * Similar if the two are within 5 of each other
	 */
	@Override
	public boolean isSimilar(final Object other) {
		if (other instanceof FailNumberMulti) {
			final FailNumberMulti num = (FailNumberMulti) other;
			return Math.abs(val - num.val) < 5;
		}
		return false;
	}

}
