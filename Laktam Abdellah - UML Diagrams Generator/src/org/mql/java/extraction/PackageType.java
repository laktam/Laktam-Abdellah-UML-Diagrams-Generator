package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class PackageType {
	private String name;
	private List<PackageType> packages;
	private List<ClassType> classes;
	private List<InterfaceType> interfaces;
	private List<AnnotationType> annotations;
	private List<EnumerationType> enumerations;
	
	PackageType(String name){
		this.name = name;
		this.packages = new Vector<PackageType>();
		this.classes = new Vector<ClassType>();
		this.interfaces = new Vector<InterfaceType>();
		this.enumerations = new Vector<EnumerationType>();
		this.annotations = new Vector<AnnotationType>();
	}
	
	@Override
	public String toString() {
//		String s = name + "\n";
//		for (PackageType p : packages) {
//			s += "- " + p + "\n";
//		}
		return printTree("\t");
	}
	
	public String printTree(String indentation) {
		String s = indentation + name  + "\n";
		
		if(!classes.isEmpty()) {
			s+= indentation + " ** classes \n";
			for (ClassType c : classes) {
				s+= indentation + " -- "  + c.getName() + "\n";
			}	
		}
		
		
		if(!interfaces.isEmpty()) {
			s+= indentation + " ** interfaces \n";		
			for (InterfaceType i : interfaces) {
				s+= indentation + " -- "  + i.getName() + "\n";
			}	
		}
		
		if(!packages.isEmpty()) {
			indentation += "\t";
			for (PackageType p : packages) {
				s += p.printTree(indentation);
			}
		}
		
		return s;
	}
	
	public void addPackages(List<PackageType> packages) {
		this.packages.addAll(packages);
	}
	
	public List<PackageType> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageType> packages) {
		this.packages = packages;
	}

	public void addPackage(PackageType p) {
		packages.add(p);
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
	
	public List<AnnotationType> getAnnotations(){
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
