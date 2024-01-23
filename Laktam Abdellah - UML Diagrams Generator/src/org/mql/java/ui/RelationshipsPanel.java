package org.mql.java.ui;

import java.awt.Graphics;
import java.awt.Point;
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
				System.out.println(plan.canDraw(p1, p2));
				if (plan.canDraw(p1, p2)) {
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
				}

			}
		}
	}

}
