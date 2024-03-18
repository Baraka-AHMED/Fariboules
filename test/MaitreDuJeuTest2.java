package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import gui.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jeu.Joueur;
import jeu.Joueur.Action;
import jeu.MaitreDuJeu;
import jeu.MaitreDuJeuListener;
import jeu.Plateau;

/**
 * Test de la classe MaitreDuJeu.
 *
 * @author lucile
 */
class MaitreDuJeuTest2 {
  Plateau plateau;
  MaitreDuJeu jeu;
  Joueur j1;
  Joueur j2;
  Joueur j3;
  Joueur j4;
  boolean informeSpectateurs;

   
  @Test
  public void testRechargeDUneFabrique() {
	  String description = 
		        "+----------------+\n" 
		      + "|$$f-    @2  $$  |\n" 
		      + "|@1      f-  @3$$|\n"
		      + "|##  ##  ##  ##  |\n" 
		      + "|  ##  ##  ##  ##|\n" 
		      + "|              f1|\n" 
		      + "|  @4    f1      |\n"
		      + "|              f3|\n" 
		      + "|  ##f1######f1  |\n" 
		      + "+----------------+";
	  plateau = new Plateau(20, description);
	  jeu = new MaitreDuJeu(plateau);
	  jeu.metJoueurEnPosition(0, new Automate("A", "H.DBHDDDDD"));
	  jeu.metJoueurEnPosition(1, new Automate("B", "."));
	  jeu.metJoueurEnPosition(2, new Automate("C", "."));
	  jeu.metJoueurEnPosition(3, new Automate("D", "."));
	  j1 = plateau.donneJoueur(0);
	  j2 = plateau.donneJoueur(1);
	  j3 = plateau.donneJoueur(2);
	  j4 = plateau.donneJoueur(3);
	  assertFalse( plateau.donneStocksDesFabriques().containsKey(new Point(0,1)));
	  assertEquals("Joueur A:0:0:1:0:0", j1.toString()); 
	  assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
	  
	  // j1 joue H vers colline(0,0)
	  Action a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.HAUT;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:50:4", j1.toString()); 
	  assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
	  assertFalse( plateau.donneStocksDesFabriques().containsKey(new Point(0,1)));
	 	  
	  // j1 joue R dans colline(0,0)
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.RIEN;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
	  System.out.println(plateau.donneContenuCellule(0,0));
	  assertTrue( Plateau.contientUneColline(plateau.donneContenuCellule(0,0)));
	  assertFalse( plateau.donneStocksDesFabriques().containsKey(new Point(0,1)));
	  assertEquals("Joueur A:0:0:0:100:8", j1.toString()); 
	  
	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = 50; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:50:13", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 49);
	  
	  // j1 joue B vers vide(0,1)
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.BAS;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:1:49:18", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 48);
	  
	  // j1 joue H vers colline(0,0)
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.HAUT;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:99:23", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 47);

	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = -1; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:99:28", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 46);

	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = 0; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:99:33", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 45);
	  
	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = 100; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:99:38", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 44);

	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = 10; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:89:43", j1.toString()); 
	  assertEquals(5, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 53);
	  assertEquals("{[x=1,y=0]=53, [x=2,y=7]=1, [x=7,y=6]=1, [x=4,y=5]=1, [x=6,y=7]=1, [x=7,y=4]=1}",
			  plateau.donneStocksDesFabriques().toString());
	  
	  // j1 joue D vers fabrique(1,0)
	  ((Automate)j1).REPONSE = 89; // nombre de brins qu'il met dans la fabrique
	  a = jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  assert a == Action.DROITE;
	  for (int i = 0; i < 3; i++) {
	   	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
	  }
	  assertEquals("Joueur A:0:0:0:0:48", j1.toString()); 
	  assertEquals(1, plateau.nombreDeFabriquesJoueur(0));
	  assertTrue( plateau.donneStocksDesFabriques().get(new Point(1,0)) == 141);
  }


}
