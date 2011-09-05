package com.insightfullogic.multiinherit;

import org.junit.Test;

import com.insightfullogic.multiinherit.api.MultiModule;

public class TestApi {

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void dumpClassesOption() {
		new MultiModule(false, true);
	}
}
