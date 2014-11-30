package vuecontroleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modele.ModeEnum;
import modele.Regle;

public class FrameRegle extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Vue vue; 
	private Regle regles;
	private List<Regle> listeRegles;
	private boolean modification;
	private JCheckBox[] tabCkBxToBorn;
	private JCheckBox[] tabCkBxToSurvive;
	private int hauteur, largeur;

	public FrameRegle(Regle r, Vue vue, List<Regle> listeRegles, boolean modif) {
        super();
        this.vue = vue;
        this.setRegles(r);
        if(r == null)
            this.setRegles(new Regle());
        this.listeRegles = listeRegles;
        this.modification = modif;

		hauteur = 3; largeur = 3;
		if (vue.getGrille().getMode() == ModeEnum.TRIANGLE) {
			hauteur = 4; largeur = 3;
		}
		else if (vue.getGrille().getMode() == ModeEnum.GRILLE || vue.getGrille().getMode() == ModeEnum.TORUS) {
			hauteur = 3; largeur = 3;
		}
		
		tabCkBxToBorn = new JCheckBox[hauteur * largeur];
		tabCkBxToSurvive = new JCheckBox[hauteur * largeur];
        build();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
            }
        });

    }
	
	public void build() 
	{

        setTitle("Regles");
        setSize(new Dimension(500, 200));
        
        setLayout(new BorderLayout());
        
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        JPanel panelToBorn = new JPanel();
        panelToBorn.setLayout(new BorderLayout());
        JPanel panelToSurvive = new JPanel();
        panelToSurvive.setLayout(new BorderLayout());
        
        JLabel labelToBorn = new JLabel("Nombre de voisins pour naître");
        JLabel labelToSurvive = new JLabel("Nombre de voisins pour survivre");
        
		JPanel gridToBorn = new JPanel (new GridLayout(hauteur, largeur, 1, 1));
		gridToBorn.setBorder(BorderFactory.createLineBorder(Color.black,1));
		gridToBorn.setBackground(Color.black);
		JPanel gridToSurvive = new JPanel (new GridLayout(hauteur, largeur, 1, 1));
		gridToSurvive.setBorder(BorderFactory.createLineBorder(Color.black,1));
		gridToSurvive.setBackground(Color.black);
		
		for(int i = 0; i<hauteur; i++)
		{
			for(int j = 0; j<largeur; j++)
			{
				int num = largeur * i + j;
				tabCkBxToBorn[num] =  new JCheckBox("" + num);
				tabCkBxToBorn[num].setBackground(Color.white);
				if(regles.getToBorn().contains(num))
					tabCkBxToBorn[num].setSelected(true);
				gridToBorn.add(tabCkBxToBorn[num]);
				
				tabCkBxToSurvive[num] =  new JCheckBox("" + num);
				tabCkBxToSurvive[num].setBackground(Color.white);
				if(regles.getToSurvive().contains(num))
					tabCkBxToSurvive[num].setSelected(true);
				gridToSurvive.add(tabCkBxToSurvive[num]);
			}
		}
		panelToBorn.add(labelToBorn, BorderLayout.NORTH);
		panelToBorn.add(gridToBorn, BorderLayout.CENTER);
		panelToSurvive.add(labelToSurvive, BorderLayout.NORTH);
		panelToSurvive.add(gridToSurvive, BorderLayout.CENTER);
		
		panelCentral.add(panelToBorn);
		panelCentral.add(panelToSurvive);
		
		add(panelCentral, BorderLayout.CENTER);
		
		
		JPanel panelBt = new JPanel();
		JButton btEnregistrer = new JButton("Enregistrer");
		JButton btSupprimer = new JButton("Supprimer");
		
		panelBt.add(btEnregistrer);
		if (modification && listeRegles.size()>1)
			panelBt.add(btSupprimer);
		
		add(panelBt, BorderLayout.SOUTH);
		
		btEnregistrer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(!modification)
				{
					regles.getToBorn().clear();
					regles.getToSurvive().clear();
					for(int i = 0; i < hauteur*largeur; i++)
					{
						if(tabCkBxToBorn[i].isSelected())
							regles.getToBorn().add(i);
						if(tabCkBxToSurvive[i].isSelected())
							regles.getToSurvive().add(i);
					}
					listeRegles.add(regles);
				}
				else
				{
					regles.getToBorn().clear();
					regles.getToSurvive().clear();
					for(int i = 0; i < hauteur*largeur; i++)
					{
						if(tabCkBxToBorn[i].isSelected())
							regles.getToBorn().add(i);
						if(tabCkBxToSurvive[i].isSelected())
							regles.getToSurvive().add(i);
					}
				}
				vue.updateCbRegles();
				fermer();
			}
		});
	}

	
	public void fermer()
	{
		this.dispose();
	}

	public Regle getRegles() {
		return regles;
	}

	public void setRegles(Regle regles) {
		this.regles = regles;
	}
}
