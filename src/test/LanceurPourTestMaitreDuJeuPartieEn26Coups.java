/*
 * Exemple d'utilisation du JAR AreneGoodFarm.jar
 * Version Rois MIAGE - 0.1
 */

package test;

import gui.FenetreDeJeu;
import gui.Point;
import java.util.ArrayList;
import java.util.HashMap;

import jeu.Joueur;
import jeu.MaitreDuJeu;
import jeu.Plateau;
import jeu.astar.Node;

/**
 * Lancement du jeu qui correspond exactement aux tests de la classe MaitreDuJeuTest
 * afin de visualiser la carte en parallèle de l'exécution des tests.
 *
 * @author Clément et Lucile
 */
public class LanceurPourTestMaitreDuJeuPartieEn26Coups {

  public static void main(String [] args) {
    String description = 
        "+----------------+\n" 
      + "|$$  f-  @2  $$  |\n" 
      + "|  @1    f-  @3$$|\n"
      + "|##  ##  ##  ##  |\n" 
      + "|  ##  ##  ##  ##|\n" 
      + "|              f1|\n" 
      + "|  @4    f1      |\n"
      + "|              f3|\n" 
      + "|  ##f1######f1  |\n" 
      + "+----------------+";

    // Génération du plateau
    Plateau   p = new Plateau(300, description);

    // Création du maitre de jeu
    MaitreDuJeu jeu = new MaitreDuJeu(p);

    // Création de la fenêtre de jeu
    FenetreDeJeu f = new FenetreDeJeu(jeu, true, false);

    // Ajout des 4 joueurs dans le jeu.
    // Par défaut les joueurs non ajoutés explicitement sont des instances de Joueur
    jeu.metJoueurEnPosition(0, new Automate("A", "DHDGG"));
    jeu.metJoueurEnPosition(1, new Automate("B", ".HBDB"));
    jeu.metJoueurEnPosition(2, new Automate("C", "..DGHGB"));
    jeu.metJoueurEnPosition(3, new Automate("D", "..DDD"));

    // Envoi des logs de la partie dans un fichier texte
    f.log = new java.io.File("/tmp/titi.log");

    // Ajout d'un listenner des clics souris sur le plateau
    f.setMouseClickListener((int x, int y, int bt) -> {
      System.out.println("On a cliqué sur la cellule " + x + "," + y);

      // Ne fonctionne que pour une partie en cours
      Joueur j = p.donneJoueur(p.donneJoueurCourant());
      System.out.println("Depart=" + j.donnePosition());
      System.out.println("Arrivée=" + new Point(x, y));

      afficheResultatRecherche(p.cherche(new Point(x, y), 40, Plateau.CHERCHE_TOUT));
      ArrayList<Node> a = p.donneCheminEntre(j.donnePosition(), new Point(x, y));
      f.afficheAstarPath(a);
    });

    // Affichage de la fenêtre
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