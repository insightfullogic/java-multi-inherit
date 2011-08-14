package com.insightfullogic.multiinherit;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.insightfullogic.multiinherit.api.MultiModule;
import com.insightfullogic.multiinherit.simple.Combined;

public class TestGeneration {

	@Test
	public void simple() {
		final Injector injector = Guice.createInjector(new MultiModule(true, Combined.class));
		final Combined combined = injector.getInstance(Combined.class);
		final Class<? extends Combined> cls = combined.getClass();
		Assert.assertEquals("com.insightfullogic.multiinherit.simple.CombinedImpl", cls.getName());

		final Field[] fields = cls.getFields();
		Assert.assertEquals(2, fields.length);

		final Field a = fields[0];
		Assert.assertEquals("com_insightfullogic_multiinherit_simple_A_Inst", a.getName());
		Assert.assertEquals("com.insightfullogic.multiinherit.simple.A", a.getType().getName());
		Assert.assertNotNull(a.getAnnotation(Inject.class));

		final Field b = fields[1];
		Assert.assertEquals("com_insightfullogic_multiinherit_simple_B_Inst", b.getName());
		Assert.assertEquals("com.insightfullogic.multiinherit.simple.B", b.getType().getName());
		Assert.assertNotNull(b.getAnnotation(Inject.class));
	}
}
