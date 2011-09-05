package com.insightfullogic.multiinherit.traits;

import com.insightfullogic.multiinherit.api.TraitWith;

@TraitWith(ExtendedSimilarityImpl.class)
public interface ExtendedSimilarity extends Similarity {

	public float difference(final Object other);

}
