package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import gui.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jeu.Joueur;
import jeu.MaitreDuJeu;
import jeu.MaitreDuJeuListener;
import jeu.Plateau;

/**
 * Test de la classe MaitreDuJeu.
 *
 * @author lucile
 */
class MaitreDuJeuTest {
  Plateau plateau;
  MaitreDuJeu jeu;
  Joueur j1;
  Joueur j2;
  Joueur j3;
  Joueur j4;
  boolean informeSpectateurs;
  
  /* Description d'un plateau 8x8 non symetrique avec :
   * 8 fabriques (f-)
   * 3 collines ($$)
   * 12 arbres (##)
   * joueur de rang 0 (@1) en position (1,1) 
   * joueur de rang 1 (@2) en position (4,0) 
   * joueur de rang 2 (@3) en position (6,1) 
   * joueur de rang 3 (@4) en position (1,5) 
   */
  static final String description = 
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
  
  @BeforeEach
  void setUp() {
    plateau = new Plateau(1200, description);
    jeu = new MaitreDuJeu(plateau);
    jeu.addEcouteurDuJeu(new MaitreDuJeuListener() {
      @Override
      public void unJeuAChange(MaitreDuJeu arg0) {
      }
      
      @Override
      public void nouveauMessage(MaitreDuJeu arg0, String arg1) {
        System.out.println(jeu.donnePlateau());
        System.out.println(">>> " + arg0 + arg1 + "\n");
      }

      @Override
      public void afficheSymbole(MaitreDuJeu arg0, Symboles arg1, Point arg2, int arg3, int arg4) {
      }
    });
    jeu.avecLog = false;
    informeSpectateurs = true;    
  }
    
  /**
   * Test d'une partie en 26 coups par joueur où chaque joueur joue la séquence
   * d'actions suivante : 
   * <ul><li>1er joueur : Droite Haut Droite Gauche Gauche Rien   Rien Rien Rien<li>
   * <li>2e joueur :      Rien   Haut Bas    Droite Bas    Rien   Rien</li> 
   * <li>3e joueur :      Rien   Rien Droite Gauche Haut   Gauche Bas</li>
   * <li>4e joueur :      Rien   Rien Droite Droite Droite Rien   Rien Rien Rien</li></ul>.
   */
  @Test @Disabled
  public void testPartieEn26Coups() {
    jeu.metJoueurEnPosition(0, new Automate("A", "DHDGG...."));
    jeu.metJoueurEnPosition(1, new Automate("B", ".HBDB"));
    jeu.metJoueurEnPosition(2, new Automate("C", "..DGHGB"));
    jeu.metJoueurEnPosition(3, new Automate("D", "..DDD."));
    j1 = plateau.donneJoueur(0);
    j2 = plateau.donneJoueur(1);
    j3 = plateau.donneJoueur(2);
    j4 = plateau.donneJoueur(3);
    assertEquals("{[x=2,y=7]=10, [x=7,y=6]=10, [x=4,y=5]=10, [x=6,y=7]=10, [x=7,y=4]=10}",
    		plateau.donneStocksDesFabriques().toString());
 
     /* Premier tour du jeu : j1=D autres=R */
    for (int i = 0; i < 4; i++) {
      assertEquals(i, plateau.donneJoueurCourant());
      assertEquals(0, plateau.donneTourCourant());
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:2:1:0:4", j1.toString()); 
    assertEquals("Joueur B:1:4:0:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:0:1", j3.toString()); 
    assertEquals("Joueur D:3:1:5:0:0", j4.toString());
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(1, plateau.donneTourCourant());
    assertEquals(0, plateau.donneJoueurCourant());
    assertEquals("{[x=2,y=7]=9, [x=7,y=6]=9, [x=4,y=5]=9, [x=6,y=7]=9, [x=7,y=4]=9}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 0, 0, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Deuxieme tour du jeu : j1=H j2=H autres=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:2:1:0:8", j1.toString()); 
    assertEquals("Joueur B:1:4:0:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:0:2", j3.toString()); 
    assertEquals("Joueur D:3:1:5:0:0", j4.toString());
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(2, plateau.donneTourCourant());
    assertEquals(0, plateau.donneJoueurCourant());
    assertEquals("{[x=2,y=7]=8, [x=7,y=6]=8, [x=4,y=5]=8, [x=6,y=7]=8, [x=7,y=4]=8}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 0, 0, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Troisieme tour du jeu : j2=B autres=D */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:3:1:0:12", j1.toString()); 
    assertEquals("Joueur B:1:4:0:0:0", j2.toString()); 
    assertEquals("Joueur C:2:7:1:50:3", j3.toString()); 
    assertEquals("Joueur D:3:2:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(3, plateau.donneTourCourant());
    assertEquals(0, plateau.donneJoueurCourant());
    assertEquals("{[x=2,y=7]=7, [x=7,y=6]=7, [x=4,y=5]=7, [x=6,y=7]=7, [x=7,y=4]=7}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 0, 0, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Quatrieme tour du jeu : j1=G j2=D j3=G j4=D */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:2:1:0:16", j1.toString()); 
    assertEquals("Joueur B:1:5:0:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:4", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(4, plateau.donneTourCourant());
    assertEquals(0, plateau.donneJoueurCourant());
    assertEquals("{[x=2,y=7]=6, [x=7,y=6]=6, [x=4,y=5]=6, [x=6,y=7]=6, [x=7,y=4]=6}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 0, 0, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Cinquieme tour du jeu : j1=G j2=B j3=P j4=D */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:20", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:5", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(5, plateau.donneTourCourant());
    assertEquals(0, plateau.donneJoueurCourant());
    assertEquals("{[x=2,y=7]=5, [x=7,y=6]=5, [x=4,y=5]=5, [x=6,y=7]=5, [x=7,y=4]=5}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 12, 12, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Sixieme tour du jeu : j2=j3=P autres=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:24", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:6", j3.toString());
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(6, plateau.donneTourCourant());
    assertEquals("{[x=2,y=7]=4, [x=7,y=6]=4, [x=4,y=5]=4, [x=6,y=7]=4, [x=7,y=4]=4}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 11, 11, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Septieme tour du jeu : j2=j3=P autres=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:28", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:7", j3.toString());
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(7, plateau.donneTourCourant());
    assertEquals("{[x=2,y=7]=3, [x=7,y=6]=3, [x=4,y=5]=3, [x=6,y=7]=3, [x=7,y=4]=3}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 10, 10, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Huitieme tour du jeu : j2=j3=P j1=j4=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:32", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:8", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(8, plateau.donneTourCourant());
    assertEquals("{[x=2,y=7]=2, [x=7,y=6]=2, [x=4,y=5]=2, [x=6,y=7]=2, [x=7,y=4]=2}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 9, 9, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Neuvieme tour du jeu : j2=j3=P j1=j4=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:36", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:9", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(4, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(1, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(9, plateau.donneTourCourant());
    assertEquals("{[x=2,y=7]=1, [x=7,y=6]=1, [x=4,y=5]=1, [x=6,y=7]=1, [x=7,y=4]=1}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 8, 8, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
    
    /* Dixieme tour du jeu : j2=j3=P j1=j4=R */
    for (int i = 0; i < 4; i++) {
      jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(10, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 7, 7, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
   
    /* Seizième tour du jeu : j2=j3=P j1=j4=R */
    for( int y = 0 ; y < 6 ; y++) {
    	for (int i = 0; i < 4; i++) {
    		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    	}
    }
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(16, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 1, 1, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
    
    /* Dix-septième tour du jeu : j2=j3=P j1=j4=R */
   	for (int i = 0; i < 4; i++) {
   		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
   	}
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:1:49:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(17, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -10, -10, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
 
    /* Dix-huitième tour du jeu : j4=H */
   	for (int i = 0; i < 4; i++) {
   		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
   	}
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:6:0:99:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(18, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -9, -9, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Dix-neuvième tour du jeu : j4=G */
   	for (int i = 0; i < 4; i++) {
   		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
   	}
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:5:0:98:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(19, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -8, -8, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Dix-neuvième tour du jeu : j4=B j1=j2=j3=R */
   	for (int i = 0; i < 4; i++) {
   		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
   	}
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:5:0:88:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(20, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -7, -7, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
    
    /* Vingtième tour du jeu : j1=j2=j3=j4=R */
   	for (int i = 0; i < 4; i++) {
   		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
   	}
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:5:0:88:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(21, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -6, -6, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

    /* Vingt-cinquième tour du jeu : j4=B j1=j2=j3=R */
    for( int y = 0 ; y < 5 ; y++) {
    	for (int i = 0; i < 4; i++) {
    		jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    	}
    }
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:5:0:88:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(26, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, -1, -1, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));

   
    /* Vingt-sixième tour du jeu : j4=B j1=j2=j3=R */
    for (int i = 0; i < 4; i++) {
    	jeu.joueSuivant(informeSpectateurs, jeu.avecLog);
    }
    assertEquals("Joueur A:0:1:1:0:40", j1.toString()); 
    assertEquals("Joueur B:1:5:1:0:0", j2.toString()); 
    assertEquals("Joueur C:2:5:0:88:10", j3.toString()); 
    assertEquals("Joueur D:3:3:5:0:0", j4.toString()); 
    assertEquals(0, plateau.nombreDeFabriquesJoueur(0));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(1));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(2));
    assertEquals(0, plateau.nombreDeFabriquesJoueur(3));
    assertEquals(27, plateau.donneTourCourant());
    assertEquals("{}",
    		plateau.donneStocksDesFabriques().toString());
    assertEquals( "[0, 13, 12, 0]", Arrays.toString( plateau.donneToursRestantPetanque()));
  }
}
