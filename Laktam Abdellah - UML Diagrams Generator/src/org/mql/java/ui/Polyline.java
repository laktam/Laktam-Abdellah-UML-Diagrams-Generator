package org.mql.java.ui;

import java.awt.Point;
import java.util.List;
import java.util.Vector;

public class Polyline {
	private List<Integer> xPoints;
	private List<Integer> yPoints;

	public Polyline() {
		this.xPoints = new Vector<Integer>();
		this.yPoints = new Vector<Integer>();
	}

	public List<Integer> getxPoints() {
		return xPoints;
	}

	public List<Integer> getyPoints() {
		return yPoints;
	}

	public void addXPoint(int xPoint) {
		xPoints.add(xPoint);
	}

	public void addYPoint(int yPoint) {
		yPoints.add(yPoint);
	}

	public void addPoint(Point p) {
		xPoints.add(p.x);
		yPoints.add(p.y);
	}

	public int[] getXarray() {
		return xPoints.stream().mapToInt(i->i).toArray();
	}

	public int[] getYarray() {
		return yPoints.stream().mapToInt(i->i).toArray();
	}

	public int getSize() {
		return xPoints.size();
	}
	
	public Point[] getLastTwoPoints() {
		Point p1 = new Point(xPoints.get(getSize() - 2), yPoints.get(getSize() - 2));
		Point p2 = new Point(xPoints.get(getSize() - 1), yPoints.get(getSize() - 1));
		return new Point[] {p1, p2};
	}
	public Point[] getFirstTwoPoints() {
		Point p1 = new Point(xPoints.get(0), yPoints.get(0));
		Point p2 = new Point(xPoints.get(1), yPoints.get(1));
		return new Point[] {p1, p2};
	}
}
