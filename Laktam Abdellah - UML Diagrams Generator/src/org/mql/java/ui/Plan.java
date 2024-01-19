package org.mql.java.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

public class Plan {
//	private int columns;
//	private List<Integer> heights;
//	private List<Integer> width;

	private Position[][] positions;

	public Plan(int rows, int columns) {
		// init
		this.positions = new Position[rows][columns];
		for (int row = 0; row < positions.length; row++) {// rows
			for (int column = 0; column < positions[row].length; column++) {
				positions[row][column] = new Position();
			}
		}
	}

	// 1
	public void setPosition(int row, int column, int width, int height, TypeUI typeUi) {
		positions[row][column] = new Position(typeUi, new Dimension(width, height), true);
		// get the max width of a vertical line
		int maxWidth = 0;
		for (int r = 0; r < positions.length; r++) {
			int w = positions[r][column].getDimension().width;
			if (w > maxWidth) {
				maxWidth = w;
			}
		}
		// make the vertical line width equal to max width
		for (int r = 0; r < positions.length; r++) {
			positions[r][column].getDimension().width = maxWidth;
		}
		for (int r = 0; r < row; r++) {
			//we need to also change the width of the typeUI stored here if there is one
			//we need also to change positions 
			positions[r][column].getTypeUi().setWidth(maxWidth);

		}
	}

	public Point getDrawingPosition(int row, int column) {
		int x = 0, y = 0;
		for (int r = 0; r < row; r++) {
			y += positions[r][column].getDimension().height;
		}
		for (int c = 0; c < column; c++) {
			x += positions[row][c].getDimension().width;
		}
		
		return new Point(x, y);
		
	}
	
	public Position getPosition(int row, int column) {
		return positions[row][column];
	}
}
