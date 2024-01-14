package org.mql.java.extraction;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

public class ConstructorType {
	private String simpleName;
	private String fqName;
	private List<ParameterType> parameters;

	ConstructorType(String simpleName, String fqName, Parameter parameters[]) {
		this.parameters = new Vector<ParameterType>();
		for (Parameter p : parameters) {
			this.parameters.add(new ParameterType(p));
		}
		this.simpleName = simpleName;
		this.fqName = fqName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public String getFqName() {
		return fqName;
	}

	public List<ParameterType> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterType> parameters) {
		this.parameters = parameters;
	}

}
