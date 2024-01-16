package org.mql.java.ui;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mql.java.extraction.ClassType;
import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.Project;
import org.mql.java.extraction.SuperType;
import org.mql.java.extraction.relationships.Relationship;

public class Drawer extends JFrame {
	private static final long serialVersionUID = 1L;
	private Project project;

	public Drawer(Project project) {
		this.project = project;

		// test
		
	}

	public void drawClassDiagram(String packageFQName) {
		PackageType pckg = project.getPackage(packageFQName);
		if(pckg != null) {
		List<String> fqNames = pckg.getInternalTypes();
		List<SuperType> internalTypes = new Vector<SuperType>();
		for (String fqName : fqNames) {
			internalTypes.add(pckg.getType(fqName));
		}
		// where should we place each class so that the relationships are drawn
		// correctly
		Set<Relationship> relationships = pckg.getRelationshipsSet();

		// test
		drawType("org.mql.java.animation.Pingouin", pckg);
		setSize(300, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		}

	}

	private void drawType(String typeFQName, PackageType pckg) {
		SuperType type = pckg.getType(typeFQName);
		add(new TypeUI(type));
	}
}
