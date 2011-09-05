package com.insightfullogic.multiinherit.traits;

public abstract class ExtendedSimilarityImpl implements ExtendedSimilarity {

	@Override
	public boolean isSimilar(final Object other) {
		final float diff = difference(other);
		if (diff > 1.0 || diff < 0.0) {
			throw new IllegalStateException("Diff must be > 1.0 and < 0.0, value is " + diff);
		}
		return diff < 0.5;
	}
}
