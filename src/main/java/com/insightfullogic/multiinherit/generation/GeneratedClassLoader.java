package com.insightfullogic.multiinherit.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	public synchronized Class<?> defineClass(final String binaryName, final ClassNode cn) {
		final Class<?> cls = findLoadedClass(binaryName);
		if (cls != null) {
			return cls;
		}
		final ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		final byte[] b = cw.toByteArray();
		final File f = new File(System.getProperty("user.home") + File.separator + "dump" + File.separator
				+ binaryName.replace('.', File.separatorChar) + ".class");
		f.getParentFile().mkdirs();
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(b);
			out.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return defineClass(binaryName, b, 0, b.length);
	}

}