package com.insightfullogic.multiinherit.primitives;

import com.google.inject.ImplementedBy;

@ImplementedBy(PrimImpl.class)
public interface Prim {

	public int p();

	public long p(int x);

	public boolean p(short s, byte b);

}

class PrimImpl implements Prim {

	@Override
	public int p() {
		return 5;
	}

	@Override
	public long p(final int x) {
		return x;
	}

	@Override
	public boolean p(final short s, final byte b) {
		return false;
	}

}