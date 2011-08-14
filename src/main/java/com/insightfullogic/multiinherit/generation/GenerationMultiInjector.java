package com.insightfullogic.multiinherit.generation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiInjector;
import com.insightfullogic.multiinherit.api.Prefer;

public class GenerationMultiInjector implements MultiInjector {

	@Inject
	Injector injector;

	private final GeneratedClassLoader loader = new GeneratedClassLoader();
	private final String injectAnn = Type.getDescriptor(Inject.class);
	private final String overrideAnn = Type.getDescriptor(Override.class);

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(final Class<T> combined) {
		final String binaryName = combined.getName() + "Impl";
		final String name = Type.getInternalName(combined) + "Impl";
		// Create the basic class using ASM's Tree api
		final ClassNode cn = new ClassNode();
		cn.version = Opcodes.V1_6;
		cn.access = Opcodes.ACC_PUBLIC;
		cn.name = name;
		cn.superName = "java/lang/Object";
		if (combined.isInterface()) {
			final Map<Method, FieldInfo> methods = new HashMap<Method, FieldInfo>();

			for (final Class<?> inter : combined.getInterfaces()) {
				// Create a field for every parent
				final String fieldName = inter.getName().replace('.', '_') + "_Inst";
				final String descriptor = Type.getDescriptor(inter);
				final FieldNode field = new FieldNode(Opcodes.ACC_PUBLIC, fieldName, descriptor, null, null);
				field.visibleAnnotations = Arrays.asList(new AnnotationNode(injectAnn));

				final String internal = Type.getInternalName(inter);
				cn.interfaces.add(internal);
				cn.fields.add(field);

				for (final Method meth : inter.getDeclaredMethods()) {
					methods.put(meth, new FieldInfo(fieldName, internal, Type.getDescriptor(inter)));
				}
			}

			// Actually add the concrete combined type
			cn.interfaces.add(Type.getInternalName(combined));

			// build the constructor:
			final MethodNode cons = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			cons.maxLocals = 1;
			cons.maxStack = 1;
			final InsnList consIsns = cons.instructions;
			consIsns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			consIsns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
			consIsns.add(new InsnNode(Opcodes.RETURN));
			cn.methods.add(cons);

			// Lookup preferences
			final Map<String, String> preferences = new HashMap<String, String>();
			for (final Method meth : combined.getDeclaredMethods()) {
				final Prefer prefer = meth.getAnnotation(Prefer.class);
				if (prefer != null) {
					preferences.put(meth.getName() + Type.getMethodDescriptor(meth), Type.getInternalName(prefer.value()));
				}
			}

			// Generate Adapter Methods
			for (final Entry<Method, FieldInfo> meth : methods.entrySet()) {
				final Method method = meth.getKey();
				final String descriptor = Type.getMethodDescriptor(method);
				final String prefer = preferences.get(method.getName() + descriptor);
				final FieldInfo field = meth.getValue();
				// either no preference, or this is preferred
				if (prefer == null || field.getTypeInternalName().equals(prefer)) {
					// null is Signature
					final Class<?>[] exceptionTypes = method.getExceptionTypes();
					final String[] exceptions = new String[exceptionTypes.length];
					for (int i = 0; i < exceptions.length; i++) {
						exceptions[i] = exceptionTypes[i].getCanonicalName();
					}
					final MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, method.getName(), descriptor, null, null);
					mn.maxLocals = 1;
					mn.maxStack = 1;
					final InsnList isns = mn.instructions;
					// TODO: deal with arguments
					isns.add(new VarInsnNode(Opcodes.ALOAD, 0));
					isns.add(new FieldInsnNode(Opcodes.GETFIELD, name, field.getName(), field.getTypeDescriptor()));
					isns.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, field.getTypeInternalName(), method.getName(), descriptor));
					isns.add(new InsnNode(Opcodes.ARETURN));
					mn.visibleAnnotations = Arrays.asList(new AnnotationNode(overrideAnn));
					cn.methods.add(mn);
				}
			}

		} else {
			throw new UnsupportedOperationException("Class Inheritance yet to be implemented");
		}
		final Class<?> implClass = loader.defineClass(binaryName, cn);
		try {
			final T inst = (T) implClass.newInstance();
			injector.injectMembers(inst);
			return inst;
		} catch (final InstantiationException e) {
			throw new IllegalArgumentException(e);
		} catch (final IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	class FieldInfo {
		private final String name;
		private final String typeInternalName;
		private final String typeDescriptor;

		public FieldInfo(final String name, final String typeInternalName, final String typeDescriptor) {
			super();
			this.name = name;
			this.typeInternalName = typeInternalName;
			this.typeDescriptor = typeDescriptor;
		}

		public String getName() {
			return name;
		}

		public String getTypeInternalName() {
			return typeInternalName;
		}

		public String getTypeDescriptor() {
			return typeDescriptor;
		}

	}

}
