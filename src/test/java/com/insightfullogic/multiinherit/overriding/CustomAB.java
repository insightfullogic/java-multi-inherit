/**
 * 
 */
package com.insightfullogic.multiinherit.overriding;

import com.insightfullogic.multiinherit.simple.A;
import com.insightfullogic.multiinherit.simple.B;

/**
 * @author joliver
 *
 */
public abstract class CustomAB implements A, B {

	/**
	 * @see com.insightfullogic.multiinherit.simple.B#b()
	 */
	@Override
	public String b() {
		return "c";
	}
	
}
