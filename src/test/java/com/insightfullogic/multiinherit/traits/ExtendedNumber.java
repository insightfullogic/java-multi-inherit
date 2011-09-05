package com.insightfullogic.multiinherit.traits;

public abstract class ExtendedNumber implements ExtendedSimilarity {

	private float f;

	@Override
	public float difference(final Object other) {
		if (other instanceof ExtendedNumber) {
			final ExtendedNumber num = (ExtendedNumber) other;
			return Math.abs(f - num.f) / Math.max(num.f, f);
		}
		return 1.0f;
	}

	public float getF() {
		return f;
	}

	public void setF(final float f) {
		this.f = f;
	}

}
