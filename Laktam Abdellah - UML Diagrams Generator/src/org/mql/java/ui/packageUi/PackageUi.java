package org.mql.java.ui.packageUi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.SuperType;

public class PackageUi extends JPanel {
	private static final long serialVersionUID = 1L;
	private String fqName;
	private String simpleName;
	private int padding = 5;
	private boolean root;
	private List<SuperType> types;
	private List<PackageType> subPackages;
	private Header header;
	private TypesPanel typesPanel;
	private JPanel body;
	private List<PackageUi> subPackagesUi;
	private int bottomPadding = 15;
	
	public PackageUi(PackageType pckg, boolean root) {
		this.subPackagesUi = new Vector<PackageUi>();
		this.root = root;// we draw the fqName only for the root
		this.fqName = pckg.getFQName();
		System.out.println(fqName);
		this.simpleName = pckg.getSimpleName();
		this.subPackages = pckg.getPackages();
		this.types = pckg.getOnlyThisPackageTypes();
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(null);
		if (root) {
			this.header = new Header(fqName);
			add(header);
		} else {
			this.header = new Header(simpleName);
			add(header);
		}
		//
		this.typesPanel = new TypesPanel(types);
		add(typesPanel);
		typesPanel.setLocation(padding, header.getHeight());
		//
		this.body = new JPanel();
		add(body);
//		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
//		body.setLocation(padding, header.getHeight() + typesPanel.getHeight());
		fillBody();
		setSize(calculateSize());
//		setBorder(new LineBorder(Color.black));
	}

//	public void fillBody() {
//		for (PackageType subP : subPackages) {
//			PackageUi subPUi = new PackageUi(subP, false);
//			body.add(subPUi);
//		}
//	}
	public void fillBody() {
		// add counter to add two in each line
		Dimension d = new Dimension(padding, header.getHeight() + typesPanel.getHeight() + padding * 2);
		int counter = 0;
		for (PackageType subP : subPackages) {
			PackageUi subPUi = new PackageUi(subP, false);
			Dimension subD = subPUi.calculateSize();
//			body.add(subPUi);
			add(subPUi);
			subPackagesUi.add(subPUi);
			subPUi.setLocation(d.width, d.height);
			d.width += subD.width + subD.width / 2;
//			d.height += subD.height;//we will just place them horizontally
		}


	}

//	public void setBodySize() {
//		setSize(getSize());
//		System.out.println(fqName + " : " + getSize().height + ", " + getSize().width);
//	}

	public Dimension calculateSize() {
		if (subPackages.isEmpty()) {
//			return new Dimension(500, 500);
			return new Dimension(typesPanel.getWidth() + padding * 2,
					header.getHeight() + typesPanel.getHeight() + padding * 2);
		} else {
			Dimension d = new Dimension();
			for (PackageType subP : subPackages) {
				PackageUi subPUi = new PackageUi(subP, false);
				Dimension subD = subPUi.calculateSize();
				d.width += subD.width;
				d.height += subD.height;
			}
			d.width += typesPanel.getWidth() + padding * 2;// compare typespanel width with the sum of subPckgs with and
															// set w to max
			d.height += header.getHeight() + typesPanel.getHeight() + bottomPadding;
			return d;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return calculateSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(0, header.getHeight(), getSize().width - 2, getSize().height - header.getHeight() - 2);
	}

}
