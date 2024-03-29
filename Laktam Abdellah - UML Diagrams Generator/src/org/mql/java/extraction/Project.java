package org.mql.java.extraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;
import org.mql.java.extraction.relationships.RelationshipEnd;

public class Project {
	private String name;
	private List<PackageType> packages;
	private List<String> internalTypes;

	public Project(String name) {
		this.name = name;
		this.packages = new Vector<PackageType>();
		this.internalTypes = new Vector<String>();
	}

	public List<String> getInternalTypes() {
		for (PackageType p : packages) {
			internalTypes.addAll(p.getInternalTypes());
		}
		return internalTypes;
	}
	
	public void setPackages(List<PackageType> packages) {
		this.packages = packages;
	}

	public List<String> getExternalTypes() {
		List<String> internalTypes = getInternalTypes();
		List<String> externalTypes = new Vector<String>();// Uniqueness
		// we will start by getting the relationshipEnd (TO)
		// because from will always be an internalClass
		// then we test each one if it is in the internal list
		Set<Relationship> relationships = getRelationshipsSet();
		// we need to create a set of (To) relationshipEnds
		Set<RelationshipEnd> toEnds = new HashSet<>();
		for (Relationship r : relationships) {
			toEnds.add(r.getTo());
		}

		// loop over ends and test if each one exist in internalClasses
		for (RelationshipEnd rEnd : toEnds) {
			if (!internalTypes.contains(rEnd.getFQName())) {
				externalTypes.add(rEnd.getFQName());
			}
		}
		return externalTypes;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "Project : " + name + "\n";
		for (PackageType p : packages) {
			s += p;
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPackage(PackageType p) {
		packages.add(p);
	}

	public void addPackages(List<PackageType> packages) {
		this.packages.addAll(packages);
	}

	public List<PackageType> getPackages() {
		return packages;
	}

	public List<PackageType> getPackagesInOneLevel(){
		List<PackageType> pckgs = new Vector<PackageType>();
		pckgs.addAll(packages);
		for (PackageType p : pckgs) {
			getPackages(pckgs, p);
		}
		return pckgs;
	}

	private void getPackages(List<PackageType> pckgs, PackageType p) {
		List<PackageType> subPckgs = p.getPackages();
		if (!subPckgs.isEmpty()) {
			for (PackageType subP : subPckgs) {
				pckgs.add(subP);
				getPackages(pckgs, subP);
			}
		}

	}

//	public List<PackageType> deleteEmptyPackages() {
//		List<PackageType> toDelete = new Vector<PackageType>();
//		for (PackageType p : packages) {
//			if (p.isEmpty()) {
//				toDelete.add(p);
//			} else {
//				p.deleteEmptyPackages();
//			}
//		}
//		packages.removeAll(toDelete);
//		return packages;
//	}

	public List<SuperType> getTypes() {
		List<SuperType> types = new Vector<SuperType>();
		for (PackageType p : packages) {
			types.addAll(p.getTypes());
		}
		return types;
	}
	
	public SuperType getType(String fqName) {
		List<SuperType> types = getTypes();
		for (SuperType type : types) {
			if (type.fqName.equals(fqName)) {
				return type;
			}
		}
		return null;
	}

	public List<Relationship> getRelationships() {
		List<Relationship> relationships = new Vector<>();
		for (PackageType p : packages) {
			relationships.addAll(p.getRelationships());
		}
		return relationships;
	}

	// this use redefined equals() and hashcode()
	public Set<Relationship> getRelationshipsSet() {
		List<Relationship> relationships = getRelationships();
		Set<Relationship> relationshipsSet = new HashSet<Relationship>();
		for (Relationship r : relationships) {
			relationshipsSet.add(r);
		}
		return relationshipsSet;
	}

	public PackageType getPackage(String packageFQName) {
		for (PackageType p : packages) {
			if (p.getFQName().equals(packageFQName)) {
				return p;
			}else if(p.getPackage(packageFQName) != null) {
				return p.getPackage(packageFQName);
			}
		}
		return null;//doesn't exist
	}
	
//	class RelationshipsComparator implements Comparator<Relationship>{
//		//don't need order, used only for uniqueness
//		@Override
//		public int compare(Relationship r1, Relationship r2) {
//			//negative integer, zero, or a positive integer 
//			//as the first argument is less than, equal to, or greater than the second.
//			if(r1.getTo().getFQName().equals(r2.getTo().getFQName()) 
//					&& r1.getFrom().getFQName().equals(r2.getFrom().getFQName()) 
//					&& r1.getType().equals(r2.getType())) {
//				return 0;	
//			}
//			return -1;
//			
//		}
//		
//	}
}
