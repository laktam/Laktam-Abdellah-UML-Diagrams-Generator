package org.mql.java.extraction2;

import java.util.List;
import java.util.Vector;

public class Package {
	private String name;
	private List<Class> classes;
	private List<Interface> interfaces;
	private List<Annotation> annotations;
	private List<Enumeration> enumerations;
	
	Package(String name){
		this.name = name;
		this.classes = new Vector<Class>();
		this.interfaces = new Vector<Interface>();
		this.enumerations = new Vector<Enumeration>();
		this.annotations = new Vector<Annotation>();
	}
	
	public void addEnumeration(Enumeration e) {
		enumerations.add(e);
	}
	
	public List<Enumeration> getEnumerations(){
		return enumerations;
	}
	
	public void addAnnotation(Annotation a) {
		annotations.add(a);
	}
	
	public List<Annotation> getAnnotations(){
		return annotations;
	}
	
	public void addClass(Class c) {
		classes.add(c);
	}
	
	public List<Class> getClasses(){
		return classes;
	}
	
	public void addInterface(Interface i) {
		interfaces.add(i);
	}
	
	public List<Interface> getInterfaces(){
		return interfaces;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
}
