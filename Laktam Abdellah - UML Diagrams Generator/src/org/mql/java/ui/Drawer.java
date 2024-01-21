package org.mql.java.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.mql.java.extraction.ClassType;
import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.Project;
import org.mql.java.extraction.SuperType;
import org.mql.java.extraction.relationships.Relationship;
import org.mql.java.ui.Plan.Location;

public class Drawer extends JFrame {
	private static final long serialVersionUID = 1L;
	private Project project;
	private Map<String, JPanel> drawnTypes;
	private JPanel panel;
	private PackageType pckg;
	private int row, column;
	private Plan plan;

	public Drawer(Project project) {
		this.project = project;
		this.drawnTypes = new HashMap<String, JPanel>();

		this.panel = new JPanel();
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(panel);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// setLayout(new FlowLayout());
		add(scrollPane);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void drawClassDiagram(String packageFQName) {
		pckg = project.getPackage(packageFQName);
		if (pckg != null) {
//		List<String> fqNames = pckg.getInternalTypes();
			List<SuperType> packageTypes = pckg.getOnlyThisPackageTypes();
			packageTypes = packageTypes.reversed();
//			int row = 0;
//			int column = 0;
//			for (SuperType type : packageTypes) {
//				TypeUI typeUi = drawType(type, row, column);
//				column++;
//				if (column == 5) {
//					column = 0;
//					row++;
//				}
//
//			}
			Plan plan = new Plan(4, 4);
			this.plan = plan;
			TypeUI.setPlan(plan);
			// set is not ordered
			List<SuperType> orderedList = new Vector<SuperType>();
			for (SuperType type : packageTypes) {
				getTypesInOrder(type, orderedList);
			}

			orderedList.forEach(System.out::println);
			System.out.println();
			SuperType t = project.getType("org.mql.java.semaphores.Semaphore");
			Set<Relationship> rs = t.getRelationshipsSet();
			rs.forEach((r) -> {
				System.out.println(" - type : " + r.getType());
				System.out.println(" - from : " + r.getFrom().getFQName());
				System.out.println(" - to : " + r.getTo().getFQName() + "\n");

			});
			//
//			int row = 0;
//			int column = 0;
//			for (SuperType type : orderedList) {
//				TypeUI typeUi = drawType(type, row, column);
//				Location location = plan.getNearestTO(row, column);
//				column = location.getColumn();
//				row = location.getRow();
////				column++;
////				if (column == plan.getPositions()[row].length) {
////					column = 0;
////					row++;
////				}
//			}
			// get all type and look for not drawn one

		}

//		setSize(3000, 3000);
//		repaint();
	}

	private void getTypesInOrder(SuperType type, List<SuperType> orderedList) {
//		List<SuperType> packageTypes = pckg.getOnlyThisPackageTypes();
		Set<Relationship> relationships = type.getRelationshipsSet();
		int refRow = 0;
		int refColumn = 0;
		if (!orderedList.contains(type)) {
			orderedList.add(type);
			refRow = row;
			refColumn = column;
			drawType(type, row, column);
			Location location = plan.getNearestTO(refRow, refRow);
			row = location.getRow();
			column = location.getColumn();
		}
		for (Relationship relationship : relationships) {
			// maybe draw here
			String fqName = relationship.getTo().getFQName();
			if (pckg.isInternal(fqName) && !orderedList.contains(pckg.getType(fqName))) {
				SuperType t = pckg.getType(fqName);
				orderedList.add(t);
				drawType(t, row, column);
				Location location = plan.getNearestTO(refRow, refRow);
				row = location.getRow();
				column = location.getColumn();
			}
		}
		
		Location location = plan.getEmptySpot();
		row = location.getRow();
		column = location.getColumn();
//		column++;
//		if(column > plan.getPositions()[row].length) {
//			column = 0;
//			row++;
//		}
		for (Relationship relationship : relationships) {
			// and draw here
			String fqName = relationship.getTo().getFQName();
			if (pckg.isInternal(fqName)) {
				SuperType t = pckg.getType(relationship.getTo().getFQName());
				getTypesInOrder(t, orderedList);

			}
		}
	}

//	public boolean isDrawn(String fqName) {
//		return false;
//	}

	private TypeUI drawType(SuperType type, int row, int column) {
		TypeUI typeUi = new TypeUI(type, row, column);
		drawnTypes.put(type.getFQName(), typeUi);
		panel.add(typeUi);
		return typeUi;
	}

	private TypeUI drawType(String fqName, int row, int column) {
		SuperType type = project.getType(fqName);
		TypeUI typeUi = new TypeUI(type, row, column);
		drawnTypes.put(type.getFQName(), typeUi);
		panel.add(typeUi);
		return typeUi;
	}

	public boolean isDrawn(String fqName) {
		Set<String> keys = drawnTypes.keySet();
		for (String s : keys) {
			if (s.equals(fqName)) {
				return true;
			}
		}
		return false;
	}
}
