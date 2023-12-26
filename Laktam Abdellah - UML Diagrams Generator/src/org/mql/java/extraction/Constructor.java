package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Constructor {
	private String name;
	private List<Parameter> parameters;
	
	Constructor(String name){
		this.parameters = new Vector<Parameter>();
		this.name = name;
	}
	
	public void addParameter(String name, String type) {
		parameters.add(new Parameter(name, type));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	
}
