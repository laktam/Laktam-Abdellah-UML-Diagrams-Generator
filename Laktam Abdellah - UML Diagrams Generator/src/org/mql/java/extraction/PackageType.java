package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class PackageType {
	private String name;
	private List<ClassType> classes;
	private List<InterfaceType> interfaces;
	private List<AnnotationType> annotations;
	private List<EnumerationType> enumerations;
	
	PackageType(String name){
		this.name = name;
		this.classes = new Vector<ClassType>();
		this.interfaces = new Vector<InterfaceType>();
		this.enumerations = new Vector<EnumerationType>();
		this.annotations = new Vector<AnnotationType>();
	}
	
	public void addEnumeration(EnumerationType e) {
		enumerations.add(e);
	}
	
	public List<EnumerationType> getEnumerations(){
		return enumerations;
	}
	
	public void addAnnotation(AnnotationType a) {
		annotations.add(a);
	}
	
	public List<AnnotationType> getAnnotation(){
		return annotations;
	}
	
	public void addClass(ClassType c) {
		classes.add(c);
	}
	
	public List<ClassType> getClasses(){
		return classes;
	}
	
	public void addInterface(InterfaceType i) {
		interfaces.add(i);
	}
	
	public List<InterfaceType> getInterfaces(){
		return interfaces;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
