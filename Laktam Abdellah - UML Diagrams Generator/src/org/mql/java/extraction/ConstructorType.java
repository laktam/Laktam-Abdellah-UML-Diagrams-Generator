package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class ConstructorType {
	private String name;
	private List<ParameterType> parameters;
	
	ConstructorType(String name, Class<?> parameters[]){
		this.parameters = new Vector<ParameterType>();
		for (Class<?> p : parameters) {
			this.parameters.add(new ParameterType(p.getSimpleName(), p));
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
