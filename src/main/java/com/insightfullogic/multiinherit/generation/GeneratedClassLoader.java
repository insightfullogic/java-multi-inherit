package com.insightfullogic.multiinherit.generation;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class GeneratedClassLoader extends ClassLoader {

	/**
	 * Generate the bytecode for an ASM Tree Class, and then load it into the
	 * VM.
	 * 
	 * @param cn
	 * @return
	 */
	public Class<?> defineClass(final String binaryName, final ClassNode cn) {
		final ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		final byte[] b = cw.toByteArray();
		return defineClass(binaryName, b, 0, b.length);
	}

}
