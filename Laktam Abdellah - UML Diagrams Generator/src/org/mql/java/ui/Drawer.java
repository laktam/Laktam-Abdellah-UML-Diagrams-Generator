package org.mql.java.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
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

public class Drawer extends JFrame {
	private static final long serialVersionUID = 1L;
	private Project project;
	private Map<String, JPanel> drawnTypes;
	private JPanel panel;
	
	public Drawer(Project project) {
		this.project = project;
		this.drawnTypes = new HashMap<String, JPanel>();
		
		this.panel = new JPanel();
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(panel);  
		  
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  

		//		setLayout(new FlowLayout());
		add(scrollPane);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void drawClassDiagram(String packageFQName) {
		PackageType pckg = project.getPackage(packageFQName);
		if (pckg != null) {
//		List<String> fqNames = pckg.getInternalTypes();
			List<SuperType> packageTypes = pckg.getOnlyThisPackageTypes();

			int row = 0;
			int column = 0;
			for (SuperType type : packageTypes) {
				TypeUI typeUi = drawType(type, row, column);
				column++;
				if(column == 6) {
					column = 0;
					row++;
				}

			}

		}

//		setSize(3000, 3000);
//		repaint();
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
}
