package com.insightfullogic.multiinherit.common;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ResolutionInfo {

	private final String name;
	private final Class<?>[] parameterTypes;

	public ResolutionInfo(Method method) {
		super();
		this.name = method.getName();
		this.parameterTypes = method.getParameterTypes();
	}
	
	@Override
	public String toString() {
		return "ResolutionInfo [name=" + name + ", parameterTypes="
				+ Arrays.toString(parameterTypes) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(parameterTypes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolutionInfo other = (ResolutionInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(parameterTypes, other.parameterTypes))
			return false;
		return true;
	} 
}
