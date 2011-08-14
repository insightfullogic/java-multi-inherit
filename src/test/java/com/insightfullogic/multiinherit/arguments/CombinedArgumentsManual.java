package com.insightfullogic.multiinherit.arguments;

import com.google.inject.Inject;
import com.insightfullogic.multiinherit.simple.A;

public class CombinedArgumentsManual implements A, D {

	@Inject
	A a;

	@Inject
	D d;

	@Override
	public String nothing(final String argument) {
		return d.nothing(argument);
	}

	@Override
	public String a() {
		return a.a();
	}

}
