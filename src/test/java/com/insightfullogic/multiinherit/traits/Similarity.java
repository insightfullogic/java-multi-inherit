package com.insightfullogic.multiinherit.traits;

import com.insightfullogic.multiinherit.api.TraitWith;

@TraitWith(SimilarityImpl.class)
public interface Similarity {

	public boolean isSimilar(Object other);

	public boolean isNotSimilar(Object other);

}

abstract class SimilarityImpl implements Similarity {
	@Override
	public boolean isNotSimilar(final Object other) {
		return !isSimilar(other);
	}
}
