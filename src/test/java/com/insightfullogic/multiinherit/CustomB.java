/**
 * 
 */
package com.insightfullogic.multiinherit;

/**
 * @author joliver
 *
 */
public abstract class CustomB implements A, B {

	/**
	 * @see com.insightfullogic.multiinherit.B#b()
	 */
	@Override
	public String b() {
		return "c";
	}
	
}
