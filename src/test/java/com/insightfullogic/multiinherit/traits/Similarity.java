package com.insightfullogic.multiinherit.traits;

import com.insightfullogic.multiinherit.api.TraitWith;

@TraitWith(SimilarityImpl.class)
public interface Similarity {

	public boolean isSimilar(Object other);

	public boolean isNotSimilar(Object other);

}
