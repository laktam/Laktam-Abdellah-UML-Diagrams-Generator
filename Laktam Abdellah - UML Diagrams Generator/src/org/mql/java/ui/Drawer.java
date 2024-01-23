package org.mql.java.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.mql.java.extraction.ClassType;
import org.mql.java.extraction.InterfaceType;
import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.Project;
import org.mql.java.extraction.SuperType;
import org.mql.java.extraction.relationships.Relationship;
import org.mql.java.ui.Plan.Location;

public class Drawer extends JFrame {
	private static final long serialVersionUID = 1L;
	private Project project;
	private Map<String, TypeUI> drawnTypes;
	private JPanel panel;
	private PackageType pckg;
	private int row, column;
	private Plan plan;

	public Drawer(Project project) {
		this.project = project;
		this.drawnTypes = new HashMap<String, TypeUI>();

		this.panel = new JPanel();
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(panel);

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
//			packageTypes = packageTypes.reversed();
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

			int size = (int) Math.ceil(Math.sqrt(packageTypes.size()));
			Plan plan = new Plan(size, size, panel);
			this.plan = plan;
			TypeUI.setPlan(plan);

//			List<SuperType> orderedList = new Vector<SuperType>();
//			for (SuperType type : packageTypes) {
//				getTypesInOrder(type, orderedList);
//			}
			List<InterfaceType> interfaces = pckg.getInterfaces();
			// check if the interface extends other interfaces => draw them
			// use the first list to put every interface followed by classes that implements
			// it
			// this loop draw top most interfaces
			for (InterfaceType i : interfaces) {
				Set<Relationship> rs = i.getRelationshipsSet();
				for (Relationship r : rs) {
					if (r.getType().equals("extention")) {
						Location location = plan.getEmptySpot();
						drawType(r.getTo().getFQName(), location.getRow(), location.getColumn());
					}
				}
			}
			// draw interfaces and classes that implements them, maybe draw subclass for
			// each class
			List<ClassType> classes = pckg.getClasses();
			for (InterfaceType i : interfaces) {
				Location location = plan.getEmptySpot();
				drawType(i, location.getRow(), location.getColumn());
				// and classes that implements them,
				for (ClassType c : classes) {
					Set<Relationship> rs = c.getRelationshipsSet();
					for (Relationship r : rs) {
						if (r.getType().equals("implementation") && r.getTo().getFQName().equals(i.getFQName())) {
							Location location2 = plan.getNearestTo(location.getRow(), location.getColumn());
							drawType(c, location2.getRow(), location2.getColumn());

						}
					}
				}
			}
			// look for classes that extends them
			String[] keys = drawnTypes.keySet().toArray(new String[0]);
			for (String fqName : keys) {
				TypeUI type = drawnTypes.get(fqName);
				SuperType drawnClass = pckg.getType(fqName);
				Set<Relationship> rs = pckg.getRelationshipsSet();

				for (Relationship r : rs) {
					if (r.getType().equals("extension") && r.getTo().getFQName().equals(drawnClass.getFQName())) {
						Location location = plan.getTypeLocation(type);
						Location nearestL = plan.getNearestTo(location.getRow(), location.getColumn());
						TypeUI typeUi = drawType(r.getFrom().getFQName(), nearestL.getRow(), nearestL.getColumn());
					}
				}
			}
			// look for relations from drawnClasses
			String[] fqNames = drawnTypes.keySet().toArray(new String[0]);
			for (String fqName : fqNames) {
				TypeUI type = drawnTypes.get(fqName);
				SuperType drawnClass = pckg.getType(fqName);
				Location location = plan.getTypeLocation(type);
				Set<Relationship> rs = drawnClass.getRelationshipsSet();
				for (Relationship r : rs) {
					// extension and implementations already drawn
					if (pckg.isInternal(r.getTo().getFQName())) {
						Location nearestL = plan.getNearestTo(location.getRow(), location.getColumn());
						drawType(r.getTo().getFQName(), nearestL.getRow(), nearestL.getColumn());
					}

				}
			}
			// look for relationships to drawnTypes (draw getFrom())
			fqNames = drawnTypes.keySet().toArray(new String[0]);
			for (String fqName : fqNames) {
				TypeUI type = drawnTypes.get(fqName);
//				SuperType drawnClass = pckg.getType(fqName);
				Location location = plan.getTypeLocation(type);
				Set<Relationship> rs = pckg.getRelationshipsSet();
				for (Relationship r : rs) {
					// extension and implementations already drawn
					if (r.getTo().getFQName().equals(fqName) && pckg.isInternal(r.getFrom().getFQName())) {
						Location nearestL = plan.getNearestTo(location.getRow(), location.getColumn());
						drawType(r.getFrom().getFQName(), nearestL.getRow(), nearestL.getColumn());
					}

				}
			}
			// look if rest of types have any relation from or to drawn types
			for (SuperType type : packageTypes) {
				Set<Relationship> rs = pckg.getRelationshipsSet();
				if (!isDrawn(type.getFQName())) {
					for (Relationship r : rs) { // isdrawn mean also is internal
						if (r.getFrom().getFQName().equals(type.getFQName()) && isDrawn(r.getTo().getFQName())) {
							// draw it near type in To
							Location location = plan.getTypeLocation(drawnTypes.get(r.getTo().getFQName()));
							Location nearesrL = plan.getNearestTo(location.getRow(), location.getColumn());
							drawType(r.getFrom().getFQName(), nearesrL.getRow(), nearesrL.getColumn());
						} else if (r.getTo().getFQName().equals(type.getFQName()) && isDrawn(r.getFrom().getFQName())) {
							// draw it near type in To
							Location location = plan.getTypeLocation(drawnTypes.get(r.getFrom().getFQName()));
							Location nearesrL = plan.getNearestTo(location.getRow(), location.getColumn());
							drawType(r.getTo().getFQName(), nearesrL.getRow(), nearesrL.getColumn());
						}
					}

				}
			}

//			 draw rest of types and there relations
			for (SuperType type : packageTypes) {
				Location location = plan.getEmptySpot();
				drawType(type, location.getRow(), location.getColumn());

				Set<Relationship> rs = type.getRelationshipsSet();
				for (Relationship r : rs) {
					if (pckg.isInternal(r.getTo().getFQName())) {
						Location nearestL = plan.getNearestTo(location.getRow(), location.getColumn());
						drawType(r.getTo().getFQName(), nearestL.getRow(), nearestL.getColumn());
					}

				}
			}
		}

		drawRelationships();

//			while(it.hasNext()) {
//				Entry<String, TypeUI> entry = it.next();
//				String fqName = entry.getKey();
//				SuperType drawnClass = pckg.getType(fqName);
//				Set<Relationship> rs = pckg.getRelationshipsSet();
//				for (Relationship r : rs) {
//					if(r.getType().equals("extension") && r.getTo().getFQName().equals(drawnClass.getFQName())) {
//						Location location = plan.getTypeLocation(entry.getValue());
//						Location nearestL = plan.getNearestTo(location.getRow(), location.getColumn());
//						TypeUI typeUi = drawType(r.getFrom().getFQName(), nearestL.getRow(), nearestL.getColumn());
//						drawnTypes.put(r.getFrom().getFQName(), typeUi);
//					}
//				}
//			}

//			for (Entry<String, TypeUI> entry : entries) {
//				
//			}
		//
//			orderedList.forEach(System.out::println);
//			System.out.println();
//			SuperType t = project.getType("org.mql.java.semaphores.Semaphore");
//			Set<Relationship> rs = t.getRelationshipsSet();
//			rs.forEach((r) -> {
//				System.out.println(" - type : " + r.getType());
//				System.out.println(" - from : " + r.getFrom().getFQName());
//				System.out.println(" - to : " + r.getTo().getFQName() + "\n");
//
//			});

	}

	private void drawRelationships() {
		Set<Relationship> relationships = pckg.getRelationshipsSet();
		RelationshipsPanel rPanel = new RelationshipsPanel(relationships, pckg, plan, drawnTypes);
		setGlassPane(rPanel);
		getGlassPane().setVisible(true);
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
			Location location = plan.getNearestTo(refRow, refRow);
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
				Location location = plan.getNearestTo(refRow, refRow);
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
		if (!isDrawn(type.getFQName())) {
			TypeUI typeUi = new TypeUI(type, row, column);
			drawnTypes.put(type.getFQName(), typeUi);
			panel.add(typeUi);
			return typeUi;
		}
		return null;
	}

	private TypeUI drawType(String fqName, int row, int column) {
		if (!isDrawn(fqName)) {
			SuperType type = project.getType(fqName);
			TypeUI typeUi = new TypeUI(type, row, column);
			drawnTypes.put(type.getFQName(), typeUi);// i commented this to not have concurentModification while looping
														// throught the map
			panel.add(typeUi);
			return typeUi;
		}
		return null;
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
