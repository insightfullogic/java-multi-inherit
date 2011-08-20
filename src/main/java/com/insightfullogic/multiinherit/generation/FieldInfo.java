package com.insightfullogic.multiinherit.generation;

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