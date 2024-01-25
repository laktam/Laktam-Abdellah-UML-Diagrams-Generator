package org.mql.java.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.relationships.Relationship;
import org.mql.java.ui.Plan.Location;

public class RelationshipsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Set<Relationship> relationships;
	private PackageType pckg;
	private Plan plan;
	private Map<String, TypeUI> drawnTypes;

	public RelationshipsPanel(Set<Relationship> relationships, PackageType packg, Plan plan,
			Map<String, TypeUI> drawnTypes) {
		this.relationships = relationships;
		this.pckg = packg;
		this.plan = plan;
		this.drawnTypes = drawnTypes;
		setOpaque(false);
		setSize(3000, 3000);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		Set<Relationship> relationships = pckg.getRelationshipsSet();
		for (Relationship r : relationships) {
			if (pckg.isInternal(r.getFrom().getFQName()) && pckg.isInternal(r.getTo().getFQName())) {
				TypeUI fromTypeUi = drawnTypes.get(r.getFrom().getFQName());
				TypeUI toTypeUi = drawnTypes.get(r.getTo().getFQName());
//				System.out.println("type location " + fromTypeUi.getLocation().x + ", " + fromTypeUi.getLocation().y);
//				int sides = fromTypeUi.getClosestSideTo(toTypeUi);
				Point[] points = fromTypeUi.getClosestPoints(toTypeUi);
				Point p1 = points[0];
				Point p2 = points[1];
//				System.out.println("from : " + p1.x + ", " + p1.y + " to : " + p2.x + ", " + p2.y);
				// if !canDraw : (can draw mean doesn't pass through another type (margin not
				// included))
				// line to closest corner (by corner i don't mean the exact corner but a little
				// bit to the inside (with a counter local to each type))
				// else direct line
				if (plan.canDraw(p1, p2, drawnTypes.get(r.getFrom().getFQName()),
						drawnTypes.get(r.getTo().getFQName()))) {
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
					if (!r.getType().equals("aggregation")) {
						drawHead(g, p1, p2, r);
					} else {
						drawDiamond(g, p2, p1, r);
					}

				} else {
					Polyline polyline = plan.getPolyline(p1, p2, drawnTypes.get(r.getFrom().getFQName()),
							drawnTypes.get(r.getTo().getFQName()));
					g.drawPolyline(polyline.getXarray(), polyline.getYarray(), polyline.getSize());
					// call draw head with the last two points of the polygon (create a method)
					Point lastTwo[] = polyline.getLastTwoPoints();
					if (!r.getType().equals("aggregation")) {
						drawHead(g, lastTwo[0], lastTwo[1], r);
					} else {
						Point firstTwo[] = polyline.getFirstTwoPoints();
						drawDiamond(g,firstTwo[1] ,firstTwo[0] , r);
					}
				}

			}
		}
	}

	public void drawHead(Graphics g, Point tail, Point tip, Relationship r) {
		// draw arrow or triangle
		// dependency
//		drawArrowHead(g, tail, tip, r);

		if (r.getType().equals("dependency")) {
			drawArrowHead(g, tail, tip, r);
		} else if (r.getType().equals("implementation") || r.getType().equals("inheritence")) {
			drawTriangle(g, tail, tip, r);
		} else if (r.getType().equals("aggregation")) {
			drawDiamond(g, tail, tip, r);
		}
	}

	public void drawArrowHead(Graphics g, Point tail, Point tip, Relationship r) {
		double phi = Math.toRadians(20);// degree
		int barb = 15;// length
		((Graphics2D) g).setPaint(Color.black);
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);
		// System.out.println("theta = " + Math.toDegrees(theta));
		double x, y, rho = theta + phi;
		for (int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			((Graphics2D) g).draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
	}

	public void drawTriangle(Graphics g, Point tail, Point tip, Relationship r) {
		double phi = Math.toRadians(20);// degree
		int barb = 15;// length
		((Graphics2D) g).setPaint(Color.black);
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);
		// System.out.println("theta = " + Math.toDegrees(theta));
		Polygon polygon = new Polygon();
		polygon.addPoint(tip.x, tip.y);
		double x, y, rho = theta + phi;
		for (int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			polygon.addPoint((int) x, (int) y);
//			((Graphics2D) g).draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
		g.drawPolygon(polygon);
	}

	public void drawDiamond(Graphics g, Point tail, Point tip, Relationship r) {
		double phi = Math.toRadians(45);// degree
		int barb = 15;// length
		((Graphics2D) g).setPaint(Color.black);
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);
		Polygon polygon = new Polygon();
		polygon.addPoint(tip.x, tip.y);
		double x, y, rho = theta + phi;
		Point points[] = new Point[]{new Point(),new Point()};
		for (int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			points[j].x = (int) x;
			points[j].y = (int) y;
			polygon.addPoint((int) x, (int) y);
//			((Graphics2D) g).draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
		g.drawPolygon(polygon);
		// second traingle

		drawArrowHead(g, points[0], points[1], r, 45);
		drawArrowHead(g, points[1], points[0], r, 45);
		g.drawPolygon(polygon);
	}
	public void drawArrowHead(Graphics g, Point tail, Point tip, Relationship r,int degree) {
		double phi = Math.toRadians(degree);// degree
		int barb = 15;// length
		((Graphics2D) g).setPaint(Color.black);
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);
		// System.out.println("theta = " + Math.toDegrees(theta));
		double x, y, rho = theta + phi;
		for (int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			((Graphics2D) g).draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
	}

}
