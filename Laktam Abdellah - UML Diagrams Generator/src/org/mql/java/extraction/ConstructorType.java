package org.mql.java.extraction;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

public class ConstructorType {
	private String name;
	private List<ParameterType> parameters;
	
	ConstructorType(String name, Parameter parameters[]){
		this.parameters = new Vector<ParameterType>();
		for (Parameter p : parameters) {
			this.parameters.add(new ParameterType(p.getName(), p.getType()));
		}
		this.name = name;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParameterType> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterType> parameters) {
		this.parameters = parameters;
	}
	
	
}
