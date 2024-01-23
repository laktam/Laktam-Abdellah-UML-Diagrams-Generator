package org.mql.java.ui;

import java.awt.Dimension;

public class Position {
	TypeUI typeUi;
	Dimension dimension;
	private boolean filled = false;

	public Position() {
		this.dimension = new Dimension();
	}

	public Position(TypeUI typeUi, Dimension dimension, boolean filled) {
		super();
		this.typeUi = typeUi;
		this.dimension = dimension;
		this.filled = filled;
	}

	

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public boolean isFilled() {
		return filled;
	}

	public TypeUI getTypeUi() {
		return typeUi;
	}

	public void setTypeUi(TypeUI typeUi) {
		this.typeUi = typeUi;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}
