package com.insightfullogic.multiinherit.traits;

public abstract class SimilarityImpl implements Similarity {
	@Override
	public boolean isNotSimilar(final Object other) {
		return !isSimilar(other);
	}
}