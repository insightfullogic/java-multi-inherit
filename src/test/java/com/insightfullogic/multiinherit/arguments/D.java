package com.insightfullogic.multiinherit.arguments;

import com.google.inject.ImplementedBy;

@ImplementedBy(DImpl.class)
public interface D {
	public String nothing(String argument);
}

class DImpl implements D {
	@Override
	public String nothing(final String argument) {
		return argument;
	}

}