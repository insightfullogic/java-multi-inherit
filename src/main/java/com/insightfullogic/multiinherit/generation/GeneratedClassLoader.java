package com.insightfullogic.multiinherit.generation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class GeneratedClassLoader extends ClassLoader {

	@Inject
	@Named("dump")
	Boolean dumpClasses;

	/**
	 * Generate the bytecode for an ASM Tree Class, and then load it into the
	 * VM.
	 * 
	 * @param cn
	 * @return
	 */
	public synchronized Class<?> defineClass(final String binaryName, final ClassNode cn) {
		final ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		final byte[] b = cw.toByteArray();
		if (dumpClasses) {
			final File f = new File(System.getProperty("user.home") + File.separator + "dump" + File.separator
					+ binaryName.replace('.', File.separatorChar) + ".class");
			final File dir = f.getParentFile();
			if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
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
			} else {
				throw new IllegalStateException("Unable to create directory: " + f.getParent());
			}
		}
		return defineClass(binaryName, b, 0, b.length);
	}

}