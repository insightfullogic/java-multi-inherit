package com.insightfullogic.multiinherit;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import com.insightfullogic.multiinherit.common.ResolutionInfo;

public class TestCommon {

	@Test
	public void resolutionInfoContracts() {
		EqualsVerifier.forClass(ResolutionInfo.class).verify();
	}

}
