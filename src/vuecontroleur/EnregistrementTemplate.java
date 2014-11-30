package vuecontroleur;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import modele.ModeEnum;
import modele.Parametre;
import modele.Template;

public class EnregistrementTemplate extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Template leTemplate;
	private List<Template> lstTemplate;
	private TextField tfNom;
	private JComponent[][] cases;
	private Parametre p;
	private Vue vue;
	private boolean modification;
	
	public EnregistrementTemplate(Template leTemplate, List<Template> lstTemplate, Parametre p, Vue vue, boolean modif) {
		super();
		this.leTemplate = leTemplate;
		this.lstTemplate = lstTemplate;
		this.p= p;
		this.vue = vue;
		this.modification = modif;
		build();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
            }
        });
	}

	public void build()
	{
		JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setLayout(new GridLayout());
        
        setTitle("Enregistrement de template");
        setSize(650, 650);

		JPanel panelNom = new JPanel();
		panelNom.setLayout(new FlowLayout());
		
		JLabel jl_Nom = new JLabel("Nom du template:");
		tfNom = new TextField("", 15);
		if(modification)
			tfNom.setText(leTemplate.getNom());
		JButton bt_enregistre = new JButton("Enregistrer");

		JPanel panelBt = new JPanel();
		panelBt.setLayout(new FlowLayout());
		
		bt_enregistre.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		boolean nomcorrect = true;
        		String nvNom = tfNom.getText();
        		if(nvNom.isEmpty())
        			nomcorrect = false;
        		for(Template t : lstTemplate)
        		{
        			if(t.getNom().equals(nvNom) && !t.equals(leTemplate))
        				nomcorrect =false;
        		}
        		if(nomcorrect)
        		{
        			leTemplate.setNom(nvNom);
        			if(!modification)
        				lstTemplate.add(leTemplate);
        			vue.updateTemplate();
        			fermer();
        		}
        		else
        		{
        			tfNom.setBackground(Color.red);
        		}
        	}
		});
		
		panelNom.add(jl_Nom);
		panelNom.add(tfNom);
		jMenuBar.add(panelNom);

		panelBt.add(bt_enregistre);
		if(modification)
		{
			JButton bt_suppression = new JButton("Supprimer");
			bt_suppression.addMouseListener(new MouseAdapter() {
	        	@Override
	        	public void mouseClicked(MouseEvent e) {
	        		lstTemplate.remove(leTemplate);
        			vue.updateTemplate();
        			fermer();
	        	}
			});
			panelBt.add(bt_suppression);
		}

		jMenuBar.add(panelBt);

        setJMenuBar(jMenuBar);
		
		JComponent pan = new JPanel (new GridLayout(leTemplate.getHauteur(), leTemplate.getLargeur()));

		setCases(new JPanel[leTemplate.getHauteur()][leTemplate.getLargeur()]);

        for (int i = 0; i < leTemplate.getHauteur(); i++) {
			for (int j = 0; j < leTemplate.getLargeur(); j++) {
				if (vue.getGrille().getMode() == ModeEnum.TRIANGLE) {
					getCases()[i][j] = new CaseGraphiqueTriangle(i,j,
							leTemplate.getQuadrillage()[i][j] ? p.getCouleurVie() : p.getCouleurMort(),
									p.getCouleurBordure(), p.getTailleBordure());
				}
				else if (vue.getGrille().getMode() == ModeEnum.GRILLE || vue.getGrille().getMode() == ModeEnum.TORUS) {
					getCases()[i][j] = new CaseGraphique(i,j,
							leTemplate.getQuadrillage()[i][j] ? p.getCouleurVie() : p.getCouleurMort(),
									p.getCouleurBordure(), p.getTailleBordure());
				}
				
            	pan.add(getCases()[i][j]);
            	getCases()[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						leTemplate.getQuadrillage()
						[((CaseGraphique) e.getSource()).getH()]
						 [((CaseGraphique) e.getSource()).getL()] = !leTemplate.getQuadrillage()
									[((CaseGraphique) e.getSource()).getH()]
											 [((CaseGraphique) e.getSource()).getL()];
						
						((CaseGraphique) e.getSource())
						.actualiserCouleur(leTemplate.getQuadrillage()
								[((CaseGraphique) e.getSource()).getH()]
								 [((CaseGraphique) e.getSource()).getL()] ? p.getCouleurVie() : p.getCouleurMort());
					}
            	});
			}
		}
        
		pan.validate();
        pan.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        add(pan);
	}
	
	public void fermer()
	{
		this.dispose();
	}

	public JComponent[][] getCases() {
		return cases;
	}

	public void setCases(JComponent[][] cases) {
		this.cases = cases;
	}
}
