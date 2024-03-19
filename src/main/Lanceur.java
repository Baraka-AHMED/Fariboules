/*
 * Exemple d'utilisation du JAR Fariboules.jar
 * Version Fariboules en Provence 2024 - 0.1
 */

package main;

//import java.awt.Point;
import gui.Point;
import java.util.ArrayList;
import java.util.HashMap;

import gui.FenetreDeJeu;
import jeu.Joueur;
import jeu.JoueurHumain;
import jeu.MaitreDuJeu;
import jeu.Plateau;
import jeu.astar.Node;

/**
 * Lancement du jeu.
 *
 * @author ClÃ©ment et Lucile
 */
public class Lanceur {

	public static void main(String[] args) {

		/* GÃ©nÃ©ration du plateau */
		//Plateau p = new Plateau(300, MaitreDuJeu.PLATEAU_PETIT3); // un plateau prÃ©dÃ©fini
		Plateau p = Plateau.generePlateauAleatoire(1000, 10, 8, 8, 20); // un plateau alÃ©atoire
		//Plateau p = Plateau.generePlateauAleatoire(1000, 10, 8, 8, 20); // un plateau alÃ©atoire
		
		System.out.println(p);
		
		/* CrÃ©ation du maitre de jeu */
		MaitreDuJeu jeu = new MaitreDuJeu(p);

		/* CrÃ©ation de la fenÃªtre de jeu */
		FenetreDeJeu f = new FenetreDeJeu(jeu, true, false); // sans logo
		//FenetreDeJeu f = new FenetreDeJeu(jeu, true, true); // avec logo

		/* Ajout des 4 joueurs dans le jeu */
		// Par dÃ©faut les joueurs non ajoutÃ©s explicitement sont des instances de Joueur
		jeu.metJoueurEnPosition(0, new MonJoueur("Fernandel")); // un joueur spÃ©cifique
		// jeu.metJoueurEnPosition(1, new Joueur("Raimu",f)); // un joueur Ã  dÃ©placement alÃ©atoire
		//jeu.metJoueurEnPosition(2, new JoueurHumain("Lucile",f)); // un joueur humain
		// jeu.metJoueurEnPosition(3, new Joueur("Pagnol",f)); // un joueur Ã  dÃ©placement alÃ©atoire

		// Envoi des logs de la partie dans un fichier texte
		f.log = new java.io.File("/tmp/titi.log");

		// Ajout d'un listenner des clics souris sur le plateau
		f.setMouseClickListener((int x, int y, int bt) -> {
			System.out.println("On a cliquÃ© sur la cellule " + x + "," + y);

			// Ne fonctionne que pour une partie en cours
			Joueur j = p.donneJoueur(p.donneJoueurCourant());
			System.out.println("Depart=" + j.donnePosition());
			System.out.println("ArrivÃ©e=" + new Point(x, y));

			afficheResultatRecherche(p.cherche(new Point(x, y), 40, Plateau.CHERCHE_TOUT));
			ArrayList<Node> a = p.donneCheminEntre(j.donnePosition(), new Point(x, y));
			f.afficheAstarPath(a);
		});

		// Affichage de la fenÃªtre
		java.awt.EventQueue.invokeLater(() -> {
			f.setVisible(true);
		});
	}

	// Va avec la fonction clic souris.
	private static void afficheResultatRecherche(HashMap<Integer, ArrayList<Point>> cherche) {
		cherche.keySet().stream().map((k) -> {
			System.out.println("Type=" + k);
			return k;
		}).map((k) -> {
			cherche.get(k).forEach((p) -> {
				System.out.println("(" + p.x + "," + p.y + ") ");
			});
			return k;
		}).forEachOrdered((_item) -> {
			System.out.println("\n");
		});
	}
}