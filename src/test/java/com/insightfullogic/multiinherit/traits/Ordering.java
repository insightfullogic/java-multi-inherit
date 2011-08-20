package com.insightfullogic.multiinherit.traits;

import com.insightfullogic.multiinherit.api.TraitWith;

@TraitWith(OrderingImpl.class)
public interface Ordering {

	public int compare(Object r);

	public boolean lessThan(Object r);

	public boolean greaterThan(Object r);

	public boolean equalsOrd(final Object other);

}
