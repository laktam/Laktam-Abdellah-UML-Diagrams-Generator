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
		int w = 0;
		for (int r = 0; r < positions.length; r++) {
			w = positions[r][column].getDimension().width;
			if (w > maxWidth) {
				maxWidth = w;
			}
		}
		// make the vertical line width equal to max width
		for (int r = 0; r < positions.length; r++) {
			positions[r][column].getDimension().width = maxWidth;
		}
		for (int r = 0; r <= row; r++) {
			// we need to also change the width of the typeUI stored here if there is one
			// we need also to change positions
			if (positions[r][column].isFilled() && positions[r][column].getTypeUi().getW() < maxWidth) {// positions[r][column].getTypeUi()
																										// != null
				positions[r][column].getTypeUi().setWidth(maxWidth);
//				positions[r][column].getDimension().width = maxWidth;//maybe not necessary
			}
		}
	}

	public Point getDrawingPosition(int row, int column) {
		int x = 0, y = 0;
		for (int r = 0; r < row; r++) {
			y += positions[r][column].getDimension().height;
//			y += positions[r][column].getTypeUi().getH();
		}
		for (int c = 0; c < column; c++) {
			x += positions[row][c].getDimension().width;
//			x += positions[row][c].getTypeUi().getW();
		}

		return new Point(x, y);

	}

	public Position getPosition(int row, int column) {
		return positions[row][column];
	}

	public void update(int column) {
		for (int r = 0; r < positions.length; r++) {
			for (int c = column + 1; c < positions[r].length; c++) {
				if (positions[r][c].isFilled()) {
					positions[r][c].getTypeUi().setWidth(positions[r][c].getTypeUi().getW());// i called setWith here to
																								// call set bounds to
																								// change X cordinate
				}
			}
		}
	}

//	public void distanceTo(int fromRow, int fromColumn, int toRow, int toColumn) {
//
//	}

	public Location getNearestTo(int row, int column) {
		// maybe just make this decide on positioning ? and check also this(row, column)
		// if isFilled
		// look for closest !isFilled
		if (!positions[row][column].isFilled()) {// it will never be!
			return new Location(row, column);
		}
		int closestR = 0;
		int closestC = 0;
		Point point = getDrawingPosition(row, column);
		// o o o
		// o o o
		// o o x :positions[positions.length - 1][positions[positions.length - 1].length
		// - 1].getTypeUi().getLocation().distance(point);
		double closestDistance = 1000000;// just to not set it to 0

		// t : this, x : current
		// x t o
		// o o o
//		if (column - 1 >= 0) {
////			Point p1 = getDrawingPosition(row, column - 1);
//			Position pos = positions[row][column - 1];
//			if (!pos.isFilled()) {
//				closestDistance = pos.getTypeUi().getLocation().distance(poit);
//				closestR = row;
//				closestC = column - 1;
//			}
//		}
//
//		// o t o
//		// x o o
//		if (row + 1 < positions.length && column - 1 >= 0) {
//			Position pos = positions[row + 1][column - 1];
//			if (!pos.isFilled()) {
//				double distance = pos.getTypeUi().getLocation().distance(poit);
//				if (distance < closestDistance) {
//					closestDistance = distance;
//					closestR = row + 1;
//					closestC = column - 1;
//				}
//			}
//		}
//		// o t o
//		// o x o
//		if (row + 1 < positions.length) {
//			Position pos = positions[row + 1][column];
//			if (!pos.isFilled()) {
//				double distance = pos.getTypeUi().getLocation().distance(poit);
//				if (distance < closestDistance) {
//					closestDistance = distance;
//					closestR = row + 1;
//					closestC = column;
//				}
//			}
//		}
//		// o t o
//		// o o x
//		if (row + 1 < positions.length && column + 1 < positions[row + 1].length) {
//			Position pos = positions[row + 1][column + 1];
//			if (!pos.isFilled()) {
//				double distance = pos.getTypeUi().getLocation().distance(poit);
//				if (distance < closestDistance) {
//					closestDistance = distance;
//					closestR = row + 1;
//					closestC = column + 1;
//				}
//			}
//		}
//		// o t x
//		// o o o
//		if (column + 1 < positions[row].length) {
//			Position pos = positions[row][column + 1];
//			if (!pos.isFilled()) {
//				double distance = pos.getTypeUi().getLocation().distance(poit);
//				if (distance < closestDistance) {
//					closestDistance = distance;
//					closestR = row;
//					closestC = column + 1;
//				}
//			}
//		}
//		
		// maybe i need to go one more level ?
		// or just look in all positions
		for (int r = 0; r < positions.length; r++) {
			for (int c = 0; c < positions[r].length; c++) {
				Position pos = positions[r][c];
				if (!pos.isFilled()) {
					Point p = getDrawingPosition(r, c);
//					double distance = pos.getTypeUi().getLocation().distance(point);
					double distance = p.distance(point);
					if (distance < closestDistance) {
						closestDistance = distance;
						closestR = r;
						closestC = c;
					}
				}
			}
		}
		return new Location(closestR, closestC);
	}

	public Location getTypeLocation(TypeUI typeUi) {
		for (int r = 0; r < positions.length; r++) {
			for (int c = 0; c < positions[r].length; c++) {
				Position pos = positions[r][c];
				if (pos.isFilled()) {
					if (pos.getTypeUi().equals(typeUi)) {
						return new Location(r, c);
					}
				}
			}
		}
		return null;// not drawn
	}

	public Position[][] getPositions() {
		return positions;
	}

	public Location getEmptySpot() {
		for (int r = 0; r < positions.length; r++) {
			for (int c = 0; c < positions[r].length; c++) {
				Position pos = positions[r][c];
				if (!pos.isFilled()) {
					return new Location(r, c);
				}
			}
		}
		return null;// full
	}

	class Location {
		int row;
		int column;

		public Location(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int getColumn() {
			return column;
		}

		public int getRow() {
			return row;
		}
	}
}
