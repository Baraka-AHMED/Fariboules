package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import gui.Point;
import jeu.Joueur;
import jeu.Plateau;
import jeu.astar.Node;
import utilitaire.AwtToGui;


public class MonJoueur extends Joueur {

	private static int TOUR_PREMIER_CYCLE = 1000;	
	private static int RESSOURCE_PAR_COLLINE = 50;	
	private static int RESSOURCE_MAX =500;		
	private List<Node> cheminASuivre;
	private ArrayList<Joueur> ennemis;
	private List<Node> positionsEnnemies;
	private int taille;	
	private gui.Point positionJoueur;
	private Plateau etatDuJeu;
	private List<Point> collines;
	private List<Point> fabriques;
	private static int ressourcesCollectes;




	public MonJoueur(String nom) {
		super(nom);
		fabriques = new ArrayList<Point>();
		collines = new ArrayList<Point>();
		cheminASuivre = new ArrayList<Node>();
		positionsEnnemies = new ArrayList<Node>();
		ennemis = new ArrayList<Joueur>();
	}
	@Override
	public int donneRessourcesPourFabrique(Point p) {
		int ressourcesDonnees = 0;
		ressourcesDonnees = (int) (this.donneRessources()*0.75);
		return ressourcesDonnees;
		
	}
	public List<gui.Point> trouverCollines(Plateau etatDuJeu) {
		List<gui.Point> collinesTrouvees = new ArrayList<>();        
		taille = etatDuJeu.donneTaille();
		int taille2 = taille /2;
		for (int x = 0; x < taille2; x++) {
			for (int y = 0; y < taille2; y++) {
				gui.Point point = new gui.Point(x, y); 
				if (etatDuJeu.donneContenuCellule(point) == Plateau.ENDROIT_COLLINE) {
					collinesTrouvees.add(point);
				}
			}
		}
		return collinesTrouvees;
	}

	public List<gui.Point> trouverFabriques(Plateau etatDuJeu) {
		List<gui.Point> fabriquesTrouvees = new ArrayList<>();
		taille = etatDuJeu.donneTaille();
		int taille2 = taille /2;
		for (int x = 0; x < taille2; x++) {
			for (int y = 0; y < taille2; y++) {
				gui.Point point = new gui.Point(x, y); 
				if (etatDuJeu.donneContenuCellule(point) >= Plateau.ENDROIT_FABRIQUE_LIBRE && etatDuJeu.donneContenuCellule(point) <= Plateau.ENDROIT_FABRIQUE_J4) {
					fabriquesTrouvees.add(point);
				}
			}
		}
		return fabriquesTrouvees;
	}

	private void initialiserJeu() {
		collines.clear();
		fabriques.clear();
		taille = etatDuJeu.donneTaille();
		ressourcesCollectes = this.donneRessources();

		int tailleMoitie = taille/2;

		for(int i = 0; i < tailleMoitie; i++) {
			for(int j = 0;  j < tailleMoitie; j++) {
				gui.Point point = AwtToGui.convertAwtPointToGuiPoint(new Point(i, j));
				int contenu = etatDuJeu.donneContenuCellule(point);
				if(contenu == Plateau.ENDROIT_COLLINE) {
					collines.add(point);
					collines.add(new Point(taille - 1 - point.x, point.y));
					collines.add(new Point(point.x, taille - 1 - point.y));
					collines.add(new Point(taille - 1 - point.x, taille - 1 - point.y));
				}

				else if(Plateau.ENDROIT_FABRIQUE_LIBRE <= contenu && contenu <= Plateau.ENDROIT_FABRIQUE_J4) {
					fabriques.add(point);
					fabriques.add(new Point(taille - 1 - point.x, point.y));
					fabriques.add(new Point(point.x, taille - 1 - point.y));
					fabriques.add(new Point(taille - 1 - point.x, taille - 1 - point.y));
				}

			}
		}
	} 
	/*

	private ArrayList<Node> donnerCheminOptimise(ArrayList<ArrayList<Node>> cheminsPossibles){
		// Calcul du nombre de yourtes actuellement traversés
		int nombreMaxDeCollinesSurChemin = 0;
		int tailleMinimaleChemin = cheminsPossibles.get(0).size();

		for(ArrayList<Node> chemin : cheminsPossibles) {
			int nombreDeCollinesSurChemin = 0;
			for(Point colline : collines) {
				if(chemin.contains(colline)){
					nombreDeCollinesSurChemin++;
				}
			}

			if(nombreDeCollinesSurChemin > nombreMaxDeCollinesSurChemin) {
				nombreMaxDeCollinesSurChemin = nombreDeCollinesSurChemin;
			}
		} 

		// -------------------- Recherche de nouveaux chemins --------------------
		Point pointArrivee = cheminsPossibles.get(0).get(tailleMinimaleChemin - 1);			 
		ArrayList<ArrayList<Node>> nouveauxChemins = new ArrayList<ArrayList<Node>>();

		// Recherche de nouveaux chemins passant par une yourte
		if(nombreMaxDeCollinesSurChemin == 0) {
			for(Point colline :  collines) {
				if(!positionJoueur.equals(colline)) {
					ArrayList<Node> cheminVersColline = etatDuJeu.donneCheminAvecObstaclesSupplementaires(AwtToGui.convertAwtPointToGuiPoint(positionJoueur), AwtToGui.convertAwtPointToGuiPoint(colline), positionsEnnemies);
					if(cheminVersColline != null && cheminVersColline.size() < tailleMinimaleChemin) {
						ArrayList<Node> cheminCollineVersArrivee = etatDuJeu.donneCheminAvecObstaclesSupplementaires(AwtToGui.convertAwtPointToGuiPoint(colline), AwtToGui.convertAwtPointToGuiPoint(pointArrivee), positionsEnnemies);
						if(cheminCollineVersArrivee != null && cheminVersColline.size() + cheminCollineVersArrivee.size() == tailleMinimaleChemin) {
							ArrayList<Node> nouveauChemin = new ArrayList<Node>();
							nouveauChemin.addAll(cheminVersColline);
							nouveauChemin.addAll(cheminCollineVersArrivee);
							nouveauxChemins.add(nouveauChemin);
						} 
					}
				} 
			}
		}
		// Recherche de nouveaux chemins passant pas plus de yourtes
		else {
			for(ArrayList<Node> chemin : cheminsPossibles) {
				for(int i = chemin.size() - 2; i >= 0; i--) { // -2 car on ne prend pas le point d'arrivée
					Point colline = chemin.get(i);
					if(collines.contains(colline)) { // On cherche depuis le dernier yourte traversé
						for(Point colline2: collines) {
							if(!colline.equals(colline2) && !positionJoueur.equals(colline2) && !chemin.contains(colline2)) {
								ArrayList<Node> cheminVersYourte = etatDuJeu.donneCheminAvecObstaclesSupplementaires(AwtToGui.convertAwtPointToGuiPoint(colline), AwtToGui.convertAwtPointToGuiPoint(colline2), positionsEnnemies);
								if(cheminVersYourte != null && cheminVersYourte.size() < chemin.size() - i) {
									ArrayList<Node> cheminCollineVersArrivee = etatDuJeu.donneCheminAvecObstaclesSupplementaires(AwtToGui.convertAwtPointToGuiPoint(colline), AwtToGui.convertAwtPointToGuiPoint(pointArrivee), positionsEnnemies);
									if(cheminCollineVersArrivee != null && chemin.size() - i + cheminCollineVersArrivee.size() == tailleMinimaleChemin) {
										ArrayList<Node> nouveauChemin = new ArrayList<Node>();
										nouveauChemin.addAll(chemin.subList(0, i+1));
										nouveauChemin.addAll(cheminVersYourte);
										nouveauChemin.addAll(cheminCollineVersArrivee);
										nouveauxChemins.add(nouveauChemin);
									}
								}
							}
						}
						break;
					}
				}
			}
		}

		// Remplacement des anciens chemins par les nouveaux trouvés et on continue récursivement
		if(nouveauxChemins.size() > 0) {
			return donnerCheminOptimise(nouveauxChemins);
		}

		// Tous les chemins sont de la même taille donc on choisi par défaut le premier de la liste
		return cheminsPossibles.get(0);
	}
	 */
	private void intialiserTour() {
		ennemis.clear();
		cheminASuivre.clear();
		positionsEnnemies.clear(); 
		positionJoueur = donnePosition();
		//int tourCourant = etatDuJeu.donneTourCourant();
		if(fabriques.isEmpty()) { 
			initialiserJeu();
		}
		// On récupère les positions ennemies (+ les côtés pour les éviter)
		List<String> s = new ArrayList();  
		s.add("bleu");
		s.add("vert");
		s.add("rouge");
		s.add("jaune");
		int i = 0;
		for(String couleur : s) {
			if(this.donneCouleur() != couleur) {
				Joueur ennemi = etatDuJeu.donneJoueur(i);
				Point positionEnnemi = ennemi.donnePosition();
				ennemis.add(ennemi);
				positionsEnnemies.add(new Node(positionEnnemi.x, positionEnnemi.y));
				if(positionEnnemi.x < taille - 1) {
					positionsEnnemies.add(new Node(positionEnnemi.x + 1, positionEnnemi.y));
				}
				if (positionEnnemi.x > 0) {
					positionsEnnemies.add(new Node(positionEnnemi.x - 1, positionEnnemi.y));
				}
				if(positionEnnemi.y < taille - 1) {
					positionsEnnemies.add(new Node(positionEnnemi.x, positionEnnemi.y + 1));
				}
				if (positionEnnemi.y > 0) {
					positionsEnnemies.add(new Node(positionEnnemi.x , positionEnnemi.y - 1));
				}
			}
			if(i < 4)  i++;
		}

		Collections.sort(ennemis, (e1, e2) -> Integer.compare(e2.donnePoints(), e1.donnePoints())); // On tri par meilleurs scores
	}

	@Override
	public Action faitUneAction(Plateau etatDuJeu) {
		this.etatDuJeu = etatDuJeu;
		intialiserTour();
		if (this.donneRessources() <= 100) {
	        cheminASuivre = collecterRessourcesDesCollines();
	    } else if (cheminASuivre.isEmpty()) { // Si vous avez déjà les ressources nécessaires ou si le chemin vers la colline est complété.
	        cheminASuivre = trouverCheminOptimalVersFabrique();
	    }
	    
	    if (!cheminASuivre.isEmpty()) {	        
	        return determinerActionBaséeSurProchainePosition(cheminASuivre.get(0));
	    }
	    return Action.RIEN; 
		
	}
	

	
	private List<Node> collecterRessourcesDesCollines() {
	    return collines.stream()
	        .map(colline -> etatDuJeu.donneCheminAvecObstaclesSupplementaires(positionJoueur, AwtToGui.convertAwtPointToGuiPoint(colline), positionsEnnemies))
	        .filter(Objects::nonNull)
	        .filter(chemin -> !chemin.isEmpty())
	        .min(Comparator.comparingInt(List::size))
	        .orElse(new ArrayList<>());
	}




	private List<Node> trouverCheminOptimalVersFabrique() {
	    return fabriques.stream()
	        .filter(fabrique -> Plateau.contientUneFabriqueQuiNeLuiAppartientPas(this, etatDuJeu.donneContenuCellule(fabrique.x, fabrique.y)))
	        .map(fabrique -> etatDuJeu.donneCheminAvecObstaclesSupplementaires(positionJoueur, AwtToGui.convertAwtPointToGuiPoint(fabrique), positionsEnnemies))
	        .filter(Objects::nonNull)
	        .filter(chemin -> !chemin.isEmpty())
	        .min(Comparator.comparingInt(List::size))
	        .orElse(new ArrayList<>());
	}




	private Action determinerActionBaséeSurProchainePosition(Node prochainePosition) {
		if (positionJoueur.x < prochainePosition.getX()) {
			return Action.DROITE;
		} else if (positionJoueur.x > prochainePosition.getX()) {
			return Action.GAUCHE;
		} else if (positionJoueur.y < prochainePosition.getY()) {
			return Action.BAS;
		} else if (positionJoueur.y > prochainePosition.getY()) {
			return Action.HAUT;
		}
		return Action.RIEN;
	}
	
	

}
