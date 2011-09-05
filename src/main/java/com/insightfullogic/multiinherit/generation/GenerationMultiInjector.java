package com.insightfullogic.multiinherit.generation;

import static java.util.Arrays.asList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiInjector;
import com.insightfullogic.multiinherit.api.Prefer;
import com.insightfullogic.multiinherit.api.TraitWith;
import com.insightfullogic.multiinherit.api.TypeHierachyException;

public class GenerationMultiInjector implements MultiInjector {

	private final Injector injector;
	private final GeneratedClassLoader loader;
	private final String injectAnn;
	private final String overrideAnn;

	private final Map<String, ClassCache> loadedNames;
	private final Map<Class<?>, Class<?>> concreteTraits;

	@Inject
	public GenerationMultiInjector(final Injector injector) {
		injectAnn = Type.getDescriptor(Inject.class);
		overrideAnn = Type.getDescriptor(Override.class);
		loadedNames = Maps.newHashMap();
		concreteTraits = Maps.newHashMap();
		loader = AccessController.doPrivileged(new PrivilegedAction<GeneratedClassLoader>() {
			@Override
			public GeneratedClassLoader run() {
				return injector.getInstance(GeneratedClassLoader.class);
			}
		});
		this.injector = injector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(final Class<T> combined) {
		final String binaryName = combined.getName() + "Impl";
		ClassCache cache = loadedNames.get(binaryName);

		if (cache == null) {
			final String name = Type.getInternalName(combined) + "Impl";
			// Create the basic class using ASM's Tree api
			final ClassNode cn = new ClassNode();
			cn.version = Opcodes.V1_6;
			cn.access = Opcodes.ACC_PUBLIC;
			cn.name = name;
			final Set<String> alreadyImplementedMethods = new HashSet<String>();
			if (combined.isInterface()) {
				cn.superName = "java/lang/Object";
				// Actually add the concrete combined type
				cn.interfaces.add(Type.getInternalName(combined));
			} else {
				cn.superName = Type.getInternalName(combined);
				for (final Method method : combined.getDeclaredMethods()) {
					alreadyImplementedMethods.add(method.getName() + Type.getMethodDescriptor(method));
				}
			}
			final Map<Method, FieldInfo> methods = Maps.newHashMap();
			final Map<Class<?>, TraitInfo> traits = Maps.newHashMap();
			final Set<Class<?>> immediateTraits = Sets.newHashSet();

			for (final Class<?> inter : combined.getInterfaces()) {
				// Create a field for every parent
				final String fieldName = inter.getName().replace('.', '_') + "_Inst";
				final String descriptor = Type.getDescriptor(inter);
				final FieldNode field = new FieldNode(Opcodes.ACC_PUBLIC, fieldName, descriptor, null, null);

				// The Trait case:
				final TraitWith traitWith = inter.getAnnotation(TraitWith.class);
				if (traitWith != null) {
					final Class<?> impl = traitWith.value();
					immediateTraits.add(inter);
					addTrait(combined, traits, inter, impl);
				} else {
					field.visibleAnnotations = asList(new AnnotationNode(injectAnn));
				}

				final String internal = Type.getInternalName(inter);
				cn.interfaces.add(internal);
				cn.fields.add(field);

				for (final Method meth : inter.getMethods()) {
					methods.put(meth, new FieldInfo(fieldName, internal, Type.getDescriptor(inter)));
				}
			}

			// Validate trait final method implementations
			for (final Class<?> key : immediateTraits) {
				final TraitInfo info = traits.get(key);
				for (final Class<?> parent : info.getParentTraits().values()) {
					removeImplementedMethods(parent, info.getToImplement());
				}
				final List<Method> toImplement = new ArrayList<Method>(info.getToImplement());
				removeImplementedMethods(combined, toImplement);
				if (!toImplement.isEmpty()) {
					throw new TypeHierachyException(MessageFormat.format(
							"Error composing trait {0} with class {1} Cannot find implementations for {2}", key.getName(), combined.getName(),
							toImplement));
				}
			}

			newConstructor(cn);

			// Lookup preferences
			final Map<String, String> preferences = Maps.newHashMap();
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
				final String methodInfo = method.getName() + descriptor;
				final String prefer = preferences.get(methodInfo);
				final FieldInfo field = meth.getValue();
				// either no preference, or this is preferred
				if (!alreadyImplementedMethods.contains(methodInfo) && (prefer == null || field.getTypeInternalName().equals(prefer))) {
					// null is Signature
					cn.methods.add(newAdapterMethod(name, method, field));
				}
			}

			// Generate Traits
			final Map<Class<?>, Class<?>> traitInstances = Maps.newHashMap();
			for (final Entry<Class<?>, TraitInfo> trait : traits.entrySet()) {
				final TraitInfo info = trait.getValue();
				final String binaryTraitName = info.getImplementation().getName() + "Concrete";
				final Class<?> generatedTraitImplementation = loader.defineClass(binaryTraitName, newConcreteTrait(info));
				traitInstances.put(trait.getKey(), generatedTraitImplementation);
				concreteTraits.put(trait.getKey(), generatedTraitImplementation);
			}

			cache = new ClassCache(loader.defineClass(binaryName, cn), traitInstances);
			loadedNames.put(binaryName, cache);
		}

		try {
			final Class<?> implClass = cache.getImplementation();
			final T inst = (T) implClass.newInstance();
			injector.injectMembers(inst);
			// Inject traits
			final Map<Class<?>, Class<?>> traits = cache.getTraits();
			for (final Field field : implClass.getDeclaredFields()) {
				final Class<?> traitInterface = field.getType();
				if (traits.containsKey(traitInterface)) {
					field.set(inst, getTrait(traitInterface, inst));
				}
			}
			return inst;
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Object getTrait(final Class<?> traitInterface, final Object parent) throws InvocationTargetException, InstantiationException,
			IllegalAccessException {
		final Class<?> traitClass = concreteTraits.get(traitInterface);
		// Always 1 constructor:
		final Constructor<?> cons = traitClass.getConstructors()[0];
		final Object traitInstance = cons.newInstance(Collections.nCopies(cons.getParameterTypes().length, parent).toArray());
		for (final Field f : traitInstance.getClass().getFields()) {
			if (f.getName().startsWith("parent")) {
				f.set(traitInstance, getTrait(f.getType(), traitInstance));
			}
		}
		return traitInstance;
	}

	private <T> void addTrait(final Class<T> combined, final Map<Class<?>, TraitInfo> traits, final Class<?> inter, final Class<?> impl) {
		// to implement = {required methods} - {implemented methods}
		final List<Method> toImplement = Lists.newArrayList(inter.getMethods());
		removeImplementedMethods(impl, toImplement);
		if (combined.isInterface() && !toImplement.isEmpty()) {
			throw new TypeHierachyException("Cannot combined interface " + combined.getName() + "with trait: " + inter.getName()
					+ " since it has unimplemented methods");
		}
		final Map<Class<?>, Class<?>> parentTraits = Maps.newHashMap();
		for (final Class<?> parentInter : inter.getInterfaces()) {
			final TraitWith trait = parentInter.getAnnotation(TraitWith.class);
			if (trait != null) {
				final Class<?> parentImpl = trait.value();
				parentTraits.put(parentInter, parentImpl);
				addTrait(impl, traits, parentInter, parentImpl);
			}
		}
		traits.put(inter, new TraitInfo(impl, inter, toImplement, parentTraits));
	}

	private void removeImplementedMethods(final Class<?> impl, final List<Method> toImplement) {
		for (final Method implemented : impl.getDeclaredMethods()) {
			final String mName = implemented.getName();
			final Class<?>[] mArgs = implemented.getParameterTypes();
			final Iterator<Method> it = toImplement.iterator();
			while (it.hasNext()) {
				final Method absMethod = it.next();
				if (absMethod.getName().equals(mName) && Arrays.deepEquals(absMethod.getParameterTypes(), mArgs)) {
					it.remove();
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private ClassNode newConcreteTrait(final TraitInfo info) {
		final Type interfaceType = Type.getType(info.getInterfase());
		final Type superType = Type.getType(info.getImplementation());
		final String implName = superType.getInternalName();
		final ClassNode cn = new ClassNode();
		final List<Method> toImplement = info.getToImplement();
		final Map<Class<?>, Class<?>> parentTraits = info.getParentTraits();
		cn.version = Opcodes.V1_6;
		cn.access = Opcodes.ACC_PUBLIC;
		cn.name = implName + "Concrete";
		cn.superName = implName;

		newParamConstructor(cn, types(toImplement.size(), interfaceType));

		// add trait fields, one for each possible method overload
		for (int i = 0; i < toImplement.size(); i++) {
			cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "overload" + i, interfaceType.getDescriptor(), null, null));
		}

		{
			int i = 0;
			for (final Method method : toImplement) {
				final FieldInfo fi = new FieldInfo("overload" + i, interfaceType.getInternalName(), interfaceType.getDescriptor());
				cn.methods.add(newAdapterMethod(cn.name, method, fi));
				i++;
			}
		}

		// add fields for parent classes
		{
			int i = 0;
			for (final Entry<Class<?>, Class<?>> parent : parentTraits.entrySet()) {
				final Type parentInterfaceType = Type.getType(parent.getKey());
				final String fieldName = "parent" + i;
				cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, fieldName, parentInterfaceType.getDescriptor(), null, null));
				final FieldInfo fi = new FieldInfo(fieldName, parentInterfaceType.getInternalName(), parentInterfaceType.getDescriptor());
				for (final Method method : parent.getValue().getDeclaredMethods()) {
					cn.methods.add(newAdapterMethod(cn.name, method, fi));
				}
				i++;
			}
		}

		return cn;
	}

	private Type[] types(final int n, final Type val) {
		final Type[] buff = new Type[n];
		Arrays.fill(buff, val);
		return buff;
	}

	private MethodNode newAdapterMethod(final String name, final Method method, final FieldInfo field) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final MethodNode mn = newMethod(method);
		mn.maxStack = 1 + parameterTypes.length;
		final InsnList isns = mn.instructions;
		isns.add(new VarInsnNode(Opcodes.ALOAD, 0));
		isns.add(new FieldInsnNode(Opcodes.GETFIELD, name, field.getName(), field.getTypeDescriptor()));
		for (int i = 0; i < parameterTypes.length; i++) {
			isns.add(new VarInsnNode(getLoadConst(parameterTypes[i]), i + 1));
		}
		isns.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, field.getTypeInternalName(), method.getName(), Type.getMethodDescriptor(method)));
		isns.add(new InsnNode(getReturnConst(method.getReturnType())));
		return mn;
	}

	private MethodNode newMethod(final Method method) {
		final String descriptor = Type.getMethodDescriptor(method);
		final String[] exceptions = getExceptions(method);
		final int n = method.getParameterTypes().length;
		final MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, method.getName(), descriptor, null, exceptions);
		mn.maxLocals = 1 + n;
		mn.visibleAnnotations = Arrays.asList(new AnnotationNode(overrideAnn));
		return mn;
	}

	private String[] getExceptions(final Method method) {
		final Class<?>[] exceptionTypes = method.getExceptionTypes();
		final String[] exceptions = new String[exceptionTypes.length];
		for (int i = 0; i < exceptions.length; i++) {
			exceptions[i] = exceptionTypes[i].getCanonicalName();
		}
		return exceptions;
	}

	private void newConstructor(final ClassNode cn) {
		newParamConstructor(cn);
	}

	@SuppressWarnings("unchecked")
	private void newParamConstructor(final ClassNode cn, final Type... parameters) {
		final MethodNode cons = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, parameters), null, null);
		final int n = parameters.length;
		cons.maxLocals = 1 + n;
		cons.maxStack = 2;
		final InsnList consIsns = cons.instructions;
		consIsns.add(new VarInsnNode(Opcodes.ALOAD, 0));
		consIsns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, cn.superName, "<init>", "()V"));
		consIsns.add(new VarInsnNode(Opcodes.ALOAD, 0));
		for (int i = 0; i < n; i++) {
			final Type param = parameters[i];
			// load parameter
			consIsns.add(new VarInsnNode(getLoadConst(param), i + 1));
			// store field
			consIsns.add(new FieldInsnNode(Opcodes.PUTFIELD, cn.name, "overload" + i, param.getDescriptor()));
		}
		consIsns.add(new InsnNode(Opcodes.RETURN));
		cn.methods.add(cons);
	}

	private int getReturnConst(final Class<?> type) {
		// System.out.println(type == Long.T);
		if (type == Void.TYPE) {
			return Opcodes.RETURN;
		} else if (type == Double.TYPE) {
			return Opcodes.DRETURN;
		} else if (type == Float.TYPE) {
			return Opcodes.FRETURN;
		} else if (type == Integer.TYPE || type == Byte.TYPE || type == Short.TYPE || type == Boolean.TYPE || type == Character.TYPE) {
			return Opcodes.IRETURN;
		} else if (type == Long.TYPE) {
			return Opcodes.LRETURN;
		}
		return Opcodes.ARETURN;
	}

	private int getLoadConst(final Class<?> type) {
		if (type == Double.TYPE) {
			return Opcodes.DLOAD;
		} else if (type == Float.TYPE) {
			return Opcodes.FLOAD;
		} else if (type == Integer.TYPE || type == Byte.TYPE || type == Short.TYPE || type == Boolean.TYPE || type == Character.TYPE) {
			return Opcodes.ILOAD;
		} else if (type == Long.class) {
			return Opcodes.LLOAD;
		}
		return Opcodes.ALOAD;
	}

	private int getLoadConst(final Type type) {
		final int sort = type.getSort();
		switch (sort) {
		case Type.DOUBLE:
			return Opcodes.DLOAD;
		case Type.FLOAT:
			return Opcodes.FLOAD;
		case Type.INT:
		case Type.BYTE:
		case Type.SHORT:
		case Type.BOOLEAN:
		case Type.CHAR:
			return Opcodes.ILOAD;
		case Type.LONG:
			return Opcodes.LLOAD;
		case Type.OBJECT:
			return Opcodes.ALOAD;
		default:
			throw new IllegalArgumentException("Unknown sort type: " + sort);
		}
	}

}