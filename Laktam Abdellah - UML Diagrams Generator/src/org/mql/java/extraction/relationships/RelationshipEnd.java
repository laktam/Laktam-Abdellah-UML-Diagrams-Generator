package org.mql.java.extraction.relationships;

import java.util.Objects;

public class RelationshipEnd {
	private String simpleName;
	private String fqName;
	private String direction;// from |to

	public RelationshipEnd(String simpleName, String fqName, String direction) {
		super();
		this.simpleName = simpleName;
		this.fqName = fqName;
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public String getFQName() {
		return fqName;
	}

	public String getSimpleName() {
		return simpleName;
	}
	
	@Override
	public boolean equals(Object obj) {
		RelationshipEnd r2 = (RelationshipEnd) obj;
		if(this.direction.equals(r2.direction) && this.fqName.equals(r2.fqName) &&
				this.simpleName.equals(r2.simpleName)
				) {
			return true;
		}
			
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(direction, fqName, simpleName);
	}
}
