package org.mql.java.extraction;

import java.util.List;

public class Method {
	private String name;
	private String modifiers;
	private List<Parameter> parameters;
	private String returnType;
	
	Method(String name){
		this.name = name;
	}
	
	public String getModifiers() {
		return modifiers;
	}
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void addParameter(String name, String type) {
		parameters.add(new Parameter(name, type));
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
