package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

import javax.print.DocFlavor.READER;

import org.mql.java.extraction.SuperType;
import org.mql.java.extraction.relationships.Relationship;

public class PackageType {
	private String name;
	private List<PackageType> packages;
	private List<ClassType> classes;
	private List<InterfaceType> interfaces;
	private List<AnnotationType> annotations;
	private List<EnumerationType> enumerations;
	private List<String> internalClasses;

	PackageType(String name) {
		this.name = name;
		this.packages = new Vector<PackageType>();
		this.classes = new Vector<ClassType>();
		this.interfaces = new Vector<InterfaceType>();
		this.enumerations = new Vector<EnumerationType>();
		this.annotations = new Vector<AnnotationType>();
		this.internalClasses = new Vector<String>();
	}
	
	public List<String> getInternalClasses() {
		List<SuperType> types = getTypes();
		for (SuperType type : types) {
			internalClasses.add(type.getFQName());
		}
		return internalClasses;
	}
	
	

	@Override
	public String toString() {
//		String s = name + "\n";
//		for (PackageType p : packages) {
//			s += "- " + p + "\n";
//		}
//		return printTree("\t");
		return printFullTree("\t");
	}

	public String printTree(String indentation) {
		String s = indentation + name + "\n";

		if (!classes.isEmpty()) {
			s += indentation + " ** classes \n";
			for (ClassType c : classes) {
				s += indentation + " -- " + c.getSimpleName() + "\n";
			}
		}

		if (!interfaces.isEmpty()) {
			s += indentation + " ** interfaces \n";
			for (InterfaceType i : interfaces) {
				s += indentation + " -- " + i.getSimpleName() + "\n";
			}
		}
		// delegate to subpackages
		if (!packages.isEmpty()) {
			indentation += "\t";
			for (PackageType p : packages) {
				s += p.printTree(indentation);
			}
		}

		return s;
	}

	public String printFullTree(String indentation) {
		String s = "";
		if (!getOnlyThisPackageTypes().isEmpty()) {
			s = indentation + name + "\n";
			if (!classes.isEmpty()) {
				s += indentation + " ** classes \n";
				for (ClassType c : classes) {
					s += indentation + " -- " + c.getSimpleName() + "\n";
				}
			}

			if (!interfaces.isEmpty()) {
				s += indentation + " ** interfaces \n";
				for (InterfaceType i : interfaces) {
					s += indentation + " -- " + i.getSimpleName() + "\n";
				}
			}
		}
		// delegate to subpackages
		if (!packages.isEmpty()) {
			if(!"".equals(s)) {//so i don't add indentation when the superPackage is empty
				indentation += "\t";	
			}
			
			for (PackageType p : packages) {
				s += p.printFullTree(indentation);
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

	public List<EnumerationType> getEnumerations() {
		return enumerations;
	}

	public void addAnnotation(AnnotationType a) {
		annotations.add(a);
	}

	public List<AnnotationType> getAnnotations() {
		return annotations;
	}

	public void addClass(ClassType c) {
		classes.add(c);
	}

	public List<ClassType> getClasses() {
		return classes;
	}

	public void addInterface(InterfaceType i) {
		interfaces.add(i);
	}

	public List<InterfaceType> getInterfaces() {
		return interfaces;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEmpty() {
		// check if subpackages are empty
		for (PackageType p : packages) {
			if (!p.isEmpty()) {
				return false;
			}
		}
		// if all subpackages are empty we check if the packages itself is empty
		return !containTypes();
	}

	public boolean containTypes() {
		return !(classes.isEmpty() && interfaces.isEmpty() && annotations.isEmpty() && enumerations.isEmpty());
	}

	public void deleteEmptyPackages() {
		List<PackageType> toDelete = new Vector<PackageType>();
		for (PackageType p : packages) {
			if (p.isEmpty()) {
				toDelete.add(p);

			} else {
				p.deleteEmptyPackages();
			}
		}
		packages.removeAll(toDelete);
	}

	public List<SuperType> getOnlyThisPackageTypes() {
		List<SuperType> types = new Vector<SuperType>();
		types.addAll(classes);
		types.addAll(interfaces);
		types.addAll(annotations);
		types.addAll(enumerations);
		return types;
	} 
	
	public List<SuperType> getTypes() {
		List<SuperType> types = new Vector<SuperType>();
		types.addAll(classes);
		types.addAll(interfaces);
		types.addAll(annotations);
		types.addAll(enumerations);
		for (PackageType subP : packages) {
			types.addAll(subP.getTypes());
		}
		return types;
	}

	public List<Relationship> getRelationships() {
		List<SuperType> types = getTypes();
		List<Relationship> relationships = new Vector<Relationship>();
		for (SuperType t : types) {
			relationships.addAll(t.getRelationships());
		}
		for (PackageType subP : packages) {
			relationships.addAll(subP.getRelationships());
		}
		return relationships;
	}

}
