package vuecontroleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.Grille;
import modele.ModeEnum;
import modele.Parametre;
import modele.Regle;
import modele.SimulableGrille;
import modele.Simulation;
import modele.Template;


public class Vue extends JFrame implements Observer, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private Grille grille;
	private Simulation simulation;
	private JComponent[][] cases;
	private Parametre parametres;
	private FrameParametre fp;
	private CaseGraphique caseCurseur;
	private boolean mousePressed = false;
	private CaseGraphique debutSelection;
	private CaseGraphique finSelection;
	private List<Template> lstTemplate;
	private EnregistrementTemplate et;
	private FrameRegle fr;
	private JComboBox cb_Template;
	private boolean utilisationTemplate = false;
	private List<Regle> listeRegles;
	private JComboBox cb_Regles;
	private Vue vue;
	private boolean updateCb;
	
	private JSlider slVitesse;

	public Vue() {
        super();
        vue = this;//Pour que la fenêtre d'enrigistrement de template puisse utiliser la methode updateTemplate
        setParametres(new Parametre());
        setGrille(new SimulableGrille(50,50, this, ModeEnum.TORUS));
        setSimulation(new Simulation((SimulableGrille)getGrille(), false, 500));
        setFp(new FrameParametre(parametres, this));
        setLstTemplate(new ArrayList<Template>());
        
        
		listeRegles = new ArrayList<Regle>();
		listeRegles.add(grille.getRegles());
        
        build();
        
        // Pour démarrer avec des templates.
        chargerTemplate("defaultTemplate.ser");
        // Initialisation de la liste de règles
        updateCbRegles();
        
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                super.windowClosing(arg0);
                System.exit(0);
            }
        });
        getSimulation().start();

    }
	
	public void updateCbRegles()
	{
		updateCb = false;
        cb_Regles.removeAllItems();
        for(Regle r : getListeRegles())
        {
        		cb_Regles.addItem(r);
        		if(r.equals(getGrille().getRegles()))
        			cb_Regles.setSelectedItem(r);
        }
        
        cb_Regles.revalidate();
        cb_Regles.repaint();
        updateCb = true;
	}
	
	public void updateTemplate()
	{
        cb_Template.removeAllItems();
        for(Template t : lstTemplate)
        {
        	cb_Template.addItem(t);
        }
        cb_Template.revalidate();
        cb_Template.repaint();
	}
	
	public void chargerTemplate() {chargerTemplate("template.ser");}
	
	public void chargerTemplate(String nomFichier) {
		try {
			FileInputStream fichier = new FileInputStream(nomFichier);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			List<Template> list2 = (List<Template>) ois.readObject();
			lstTemplate.clear();
			lstTemplate.addAll(list2);
			System.out.println("Chargement de la liste des templates");
			updateTemplate();
			ois.close();
			fichier.close();
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void enregistrerTemplate() {
		//Create a file chooser
		final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setSelectedFile(new File(System.getProperty("user.dir").concat("/template.ser")));
		//In response to a button click:
		File file = null;
		if(fc.showOpenDialog(vue) == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
		}
		if (file != null && file.isFile())
			enregistrerTemplate(file.getName());
		else
			enregistrerTemplate("template.ser");
	}
	
	public void enregistrerTemplate(String nomFichier) {
		try {
			System.out.println("Enregistrement des templates");
			FileOutputStream fichier = new FileOutputStream(nomFichier);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(lstTemplate);
			oos.flush();
			oos.close();
			fichier.close();
		}
		catch (java.io.IOException exception) {
			exception.printStackTrace();
		}
	}
	

	public void chargerRegles() {chargerRegles("regles.ser");}
	
	public void chargerRegles(String nomFichier) {
		try {
			FileInputStream fichier = new FileInputStream(nomFichier);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			List<Regle> listRegle = (List<Regle>) ois.readObject();
			ois.close();
			fichier.close();
			listeRegles = listRegle;
			System.out.println("Chargement des règles");
			updateCbRegles();
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void enregistrerRegles() {enregistrerRegles("regles.ser");}
	
	public void enregistrerRegles(String nomFichier) {
		try {
			System.out.println("Enregistrement des règles");
			FileOutputStream fichier = new FileOutputStream(nomFichier);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(listeRegles);
			oos.flush();
			oos.close();
			fichier.close();
		}
		catch (java.io.IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void update()
	{
		System.out.println("Update de la Vue");
	 	
	 	for (int i = 0; i < grille.getHauteur(); i++) {
			for (int j = 0; j < grille.getLargeur(); j++) {
				if(!getCases()[i][j].equals(caseCurseur))
				getCases()[i][j].setBackground(getGrille().getCases()[i][j].isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort());
				getCases()[i][j].setBorder(BorderFactory.createLineBorder(parametres.getCouleurBordure(),parametres.getTailleBordure()));
			}
		}
	}

	 public void update(Observable o, Object arg) {
	 	//this.removeAll();
	 	update();
	}
    
    public void build() {
        setLayout(new BorderLayout());
        setTitle("Jeu de la vie");
        setSize(new Dimension(818, 768));
        
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setLayout(new GridLayout());
        
        JMenu menuPersistance = new JMenu("Persistance");
        JMenuItem itemEnregistrerTemplate = new JMenuItem("Enregistrer templates");
        JMenuItem itemChargerTemplate = new JMenuItem("Charger templates");
        JMenuItem itemEnregistrerGrille = new JMenuItem("Enregistrer grille");
        JMenuItem itemChargerGrille = new JMenuItem("Charger grille");
        JMenuItem itemEnregistrerRegles = new JMenuItem("Enregistrer les règles");
        JMenuItem itemChargerRegles = new JMenuItem("Charger les règles");
        
        menuPersistance.add(itemEnregistrerTemplate);
        menuPersistance.add(itemChargerTemplate);
        menuPersistance.add(itemEnregistrerGrille);
        menuPersistance.add(itemChargerGrille);
        menuPersistance.add(itemEnregistrerRegles);
        menuPersistance.add(itemChargerRegles);
        jMenuBar.add(menuPersistance);

        JButton btParam = new JButton("Paramètres");
        jMenuBar.add(btParam);

        JCheckBox ckbx_Template = new JCheckBox("Utiliser un template");
        jMenuBar.add(ckbx_Template);
        
        cb_Template = new JComboBox();
        cb_Template.setPreferredSize(new Dimension(50,10));
        cb_Template.validate();
        jMenuBar.add(cb_Template);

        JButton btModifier_temp = new JButton("Modifier");
        jMenuBar.add(btModifier_temp);

        JButton btViderListe = new JButton("Vider la liste");
        jMenuBar.add(btViderListe);
        
        setJMenuBar(jMenuBar);

        itemChargerTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerTemplate();
			}
		});
        itemEnregistrerTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enregistrerTemplate();
			}
		});
        
        itemChargerGrille.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grille.chargerGrille();
				if(!listeRegles.contains(getGrille().getRegles()))
					listeRegles.add(getGrille().getRegles());
				updateCbRegles();
			}
		});
        itemEnregistrerGrille.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grille.enregistrerGrille();
			}
		});
        
        itemChargerRegles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerRegles();
			}
		});
        itemEnregistrerRegles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enregistrerRegles();
			}
		});
        
        ckbx_Template.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        			utilisationTemplate = !utilisationTemplate;
        	}
		});
        btParam.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		fp.setVisible(true);
        	}
		});

        btModifier_temp.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
				et = new EnregistrementTemplate((Template)cb_Template.getSelectedItem(), lstTemplate, parametres,vue,true);
				et.setVisible(true);
        	}
		});
        
        btViderListe.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		lstTemplate.clear();
        		updateTemplate();
        	}
		});

        
        JComponent pan = new JPanel (new GridLayout(grille.getHauteur(), grille.getLargeur()));

		setCases(new JPanel[grille.getHauteur()][grille.getLargeur()]);
		
        for (int i = 0; i < grille.getHauteur(); i++) {
			for (int j = 0; j < grille.getLargeur(); j++) {
				if (grille.getMode() == ModeEnum.TRIANGLE) {
					getCases()[i][j] = new CaseGraphiqueTriangle(i,j,
						getGrille().getCases()[i][j].isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort(),
								parametres.getCouleurBordure(), parametres.getTailleBordure());
				}
				else if (grille.getMode() == ModeEnum.GRILLE || grille.getMode() == ModeEnum.TORUS) {
					getCases()[i][j] = new CaseGraphique(i,j,
							getGrille().getCases()[i][j].isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort(),
									parametres.getCouleurBordure(), parametres.getTailleBordure());
				}
				
				getCases()[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(!utilisationTemplate)
						{
							mousePressed = true;
							debutSelection = (CaseGraphique) e.getSource();
							finSelection = (CaseGraphique) e.getSource();
						}
						else if(lstTemplate.size()>=1)
						{
							int derniera=0, dernierb=0;
							CaseGraphique caseDepart = ((CaseGraphique) e.getSource());
							Template tpSelec = (Template)cb_Template.getSelectedItem();
							int hauteur = tpSelec.getHauteur(), largeur = tpSelec.getLargeur();
							for(int a = 0;a<hauteur;a++)
							{
								for(int b = 0;b<largeur;b++)
								{
									int aa = a+caseDepart.getH(), bb = b+caseDepart.getL();
									if(a+caseDepart.getH()==getGrille().getHauteur()-1)
										derniera = a;
									if(b+caseDepart.getL()==getGrille().getLargeur()-1)
										dernierb = b;
									if(a+caseDepart.getH()>getGrille().getHauteur()-1)
										aa = a-derniera-1;
									if(b+caseDepart.getL()>getGrille().getLargeur()-1)
										bb = b-dernierb-1;
										
									if(tpSelec.getQuadrillage()[a][b])
										getGrille().getCases()[aa][bb].setEtatCourant(true);
								}
							}
							update();
						}
					}
					public void mouseReleased(MouseEvent e) {

						mousePressed = false;
						update();
						if(!utilisationTemplate)
						{
							int largeur, hauteur;
							int l1=finSelection.getL(), l2=debutSelection.getL(), h1=finSelection.getH(), h2=debutSelection.getH();
							if(l1<l2) { l2=finSelection.getL(); l1=debutSelection.getL();}
							if(h1<h2) { h2=finSelection.getH(); h1=debutSelection.getH();}
							largeur = l1 - l2 +1;
							hauteur = h1 - h2 +1;
							
							if(l1!=l2 || h1!=h2)
							{
								System.out.println("largeur " + largeur);
								System.out.println("hauteur" + hauteur);
								
								Template nvTemp = new Template(hauteur,largeur);
	
								int i=h2,j=l2;
								for(int a = 0;a<hauteur;a++)
								{
									System.out.println();
									for(int b = 0;b<largeur;b++)
									{
										boolean etatcase =getGrille().getCases()[i+a][j+b].isEtatCourant();
										String etat = "O";
										if(etatcase)
											etat = "X";
										System.out.print(etat);
										nvTemp.getQuadrillage()[a][b] = etatcase;
									}
									System.out.println();
								}
								et = new EnregistrementTemplate(nvTemp, lstTemplate, parametres,vue,false);
								et.setVisible(true);
							}
							debutSelection = null;
							finSelection = null;
						}
						
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						if(!utilisationTemplate)
						{
							getGrille().getCases()
							[((CaseGraphique) e.getSource()).getH()]
							 [((CaseGraphique) e.getSource()).getL()].changerEtat();
							
							((CaseGraphique) e.getSource())
							.actualiserCouleur(getGrille().getCases()
									[((CaseGraphique) e.getSource()).getH()]
									 [((CaseGraphique) e.getSource()).getL()]
									  .isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort());
						}
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						super.mouseEntered(e);
						if(!utilisationTemplate)
						{
							if(e.isAltDown())
							{
								getGrille().getCases()
								[((CaseGraphique) e.getSource()).getH()]
								 [((CaseGraphique) e.getSource()).getL()].setEtatCourant(true);
							}
							else if(e.isControlDown())
							{
								getGrille().getCases()
								[((CaseGraphique) e.getSource()).getH()]
								 [((CaseGraphique) e.getSource()).getL()].setEtatCourant(false);
							}
							else if(mousePressed)
							{
								finSelection = (CaseGraphique) e.getSource();
								update();
								int hcour = ((CaseGraphique) e.getSource()).getH();
								int lcour = ((CaseGraphique) e.getSource()).getL();
								int hdeb = debutSelection.getH();
								int ldeb = debutSelection.getL();
								int i=hcour,j=lcour,ii=hdeb,jj=ldeb;
								if(hdeb < hcour){ i=hdeb; ii=hcour;}
								if(ldeb < lcour){ j = ldeb; jj = lcour;}
								for(int a = i;a<=ii;a++)
								{
									for(int b = j;b<=jj;b++)
									{
										getCases()[a][b].setBorder(BorderFactory.createLineBorder(parametres.getCouleurBordureSelection(),parametres.getTailleBordure()));
									}
								}
								
							}
							((CaseGraphique) e.getSource())
							.actualiserCouleur(getGrille().getCases()
									[((CaseGraphique) e.getSource()).getH()]
									 [((CaseGraphique) e.getSource()).getL()]
									  .isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort());
							//Activation Curseur
							caseCurseur = ((CaseGraphique) e.getSource());
							caseCurseur.setBackground(Color.green);
						}
						else if(lstTemplate.size()>=1)
						{
							update();
							int derniera=0, dernierb=0;
							CaseGraphique caseDepart = ((CaseGraphique) e.getSource());
							Template tpSelec = (Template)cb_Template.getSelectedItem();
							int hauteur = tpSelec.getHauteur(), largeur = tpSelec.getLargeur();
							for(int a = 0;a<hauteur;a++)
							{
								for(int b = 0;b<largeur;b++)
								{
									int aa = a+caseDepart.getH(), bb = b+caseDepart.getL();
									if(a+caseDepart.getH()==getGrille().getHauteur()-1)
										derniera = a;
									if(b+caseDepart.getL()==getGrille().getLargeur()-1)
										dernierb = b;
									if(a+caseDepart.getH()>getGrille().getHauteur()-1)
										aa = a-derniera-1;
									if(b+caseDepart.getL()>getGrille().getLargeur()-1)
										bb = b-dernierb-1;
										
									if(tpSelec.getQuadrillage()[a][b])
										getCases()[aa][bb].setBorder(BorderFactory.createLineBorder(parametres.getCouleurBordureSelection(),parametres.getTailleBordure()));
								}
							}
						}
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						super.mouseExited(e);
						// Desactivation du curseur
						caseCurseur = null;
						((CaseGraphique) e.getSource())
						.actualiserCouleur(getGrille().getCases()
								[((CaseGraphique) e.getSource()).getH()]
								 [((CaseGraphique) e.getSource()).getL()]
								  .isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort());
						
					}
					
				});
	            pan.add(getCases()[i][j]);
			}
		}
        
		pan.validate();
        pan.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        add(pan,BorderLayout.CENTER);
        

        // Panel de droite
    	JPanel panelControl = new JPanel();
    	panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.Y_AXIS));

        JButton btRegenerer = new JButton("Regénérer");
        JButton btNettoyer = new JButton("Nettoyer");
        JButton btPause = new JButton("Pause");
        btRegenerer.setAlignmentX(CENTER_ALIGNMENT);
        btPause.setAlignmentX(CENTER_ALIGNMENT);
        btNettoyer.setAlignmentX(CENTER_ALIGNMENT);

        slVitesse = new JSlider(1, 700, 200);
        JLabel lbl_Vitesse = new JLabel("Vitesse");
        lbl_Vitesse.setAlignmentX(CENTER_ALIGNMENT);
        
        
        JPanel panelCb_regles = new JPanel();
    	panelCb_regles.setLayout(new FlowLayout());

        cb_Regles = new JComboBox();
        panelCb_regles.add(cb_Regles);
        cb_Regles.validate();
        cb_Regles.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(updateCb)
		    		grille.setRegles((Regle) cb_Regles.getSelectedItem());
		    }
        });

        JButton btAjouterRegle = new JButton("Ajouter une regle");
        JButton btModifierRegle = new JButton("Modifier une regle");

        JPanel panelbt_regles = new JPanel();
    	panelbt_regles.setLayout(new FlowLayout());
        panelbt_regles.add(btAjouterRegle);
        panelbt_regles.add(btModifierRegle);

        JPanel panelEspace = new JPanel();
    	panelEspace.setLayout(new BoxLayout(panelEspace, BoxLayout.Y_AXIS));
        
        // Ajout et positionnement des éléments du panneau de controle
    	JLabel labelControl = new JLabel("Panneau de contrôle");
        labelControl.setAlignmentX(CENTER_ALIGNMENT);
    	panelControl.add(labelControl);

    	panelControl.add(btRegenerer);
    	panelControl.add(btNettoyer);
    	panelControl.add(btPause);
    	
    	panelControl.add(slVitesse);
    	panelControl.add(lbl_Vitesse);

        panelControl.add(panelCb_regles);
        panelControl.add(panelbt_regles);
        panelControl.add(panelEspace);
        
    	panelControl.setAlignmentX(CENTER_ALIGNMENT);
    	
    	add(panelControl, BorderLayout.EAST);
    	
    	

        // Actions des boutons du panneau de contrôle
        slVitesse.addChangeListener(this);
        
        btNettoyer.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		grille.initMort();
        		update();
        	}
		});
        btPause.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		simulation.setPause(!simulation.isPause());
        		if(((JButton)e.getSource()).getText() == "Pause") {
        			((JButton)e.getSource()).setText("Relancer");
        			simulation.pause();
        		}
        		else {
        			((JButton)e.getSource()).setText("Pause");
        			simulation.redemarrer();
        		}
        	}
		});
        btRegenerer.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		grille.regenerer();
        		update();
        	}
		});

        btAjouterRegle.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		fr = new FrameRegle(null, vue, getListeRegles(), false);
        		fr.setVisible(true);
        	}
		});
        btModifierRegle.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		fr = new FrameRegle((Regle) cb_Regles.getSelectedItem(), vue, getListeRegles(), true);
        		fr.setVisible(true);
        	}
		});

        btRegenerer.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		grille.regenerer();
        		update();
        	}
		});
    }
    
    public void actualiserCouleurs() {
    	for (int i = 0; i < grille.getHauteur(); i++) {
			for (int j = 0; j < grille.getLargeur(); j++) {
				getCases()[i][j].setBackground(getGrille().getCases()[i][j].isEtatCourant() ? parametres.getCouleurVie() : parametres.getCouleurMort());
				getCases()[i][j].setBorder(BorderFactory.createLineBorder(parametres.getCouleurBordure(),parametres.getTailleBordure()));
			}
		}
    }

	public void stateChanged(ChangeEvent e) {
		getSimulation().setTempsPause(slVitesse.getMaximum() - slVitesse.getValue());
	}

	public void setGrille(Grille grille) {
		this.grille = grille;
	}

	public Grille getGrille() {
		return grille;
	}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	public Simulation getSimulation() {
		return simulation;
	}

    public JComponent[][] getCases() {
		return cases;
	}

	public void setCases(JComponent[][] cases) {
		this.cases = cases;
	}

	public void setParametres(Parametre parametres) {
		this.parametres = parametres;
	}

	public Parametre getParametres() {
		return parametres;
	}

	public FrameParametre getFp() {
		return fp;
	}

	public void setFp(FrameParametre fp) {
		this.fp = fp;
	}

	
	public List<Template> getLstTemplate() {
		return lstTemplate;
	}
	public void setLstTemplate(List<Template> lstTemplate) {
		this.lstTemplate = lstTemplate;
	}

	public JComboBox getCb_Regles() {
		return cb_Regles;
	}

	public void setCb_Regles(JComboBox cb_Regles) {
		this.cb_Regles = cb_Regles;
	}

	public List<Regle> getListeRegles() {
		return listeRegles;
	}

	public void setListeRegles(List<Regle> listeRegles) {
		this.listeRegles = listeRegles;
	}
}
