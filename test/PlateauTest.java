package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gui.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import jeu.MaitreDuJeu;
import jeu.Plateau;
import jeu.astar.Node;

/**
 * Tests de la classe Plateau.
 * La classe Plateau dépend de la classe Joueur.
 * Ce ne sont pas à proprement parler des tests unitaires car les tests réalisés ici ne sont pas 
 * réalisés en isolation. Pour des tests en isolation, il aurait fallu créer des doublures de 
 * Joueur.
 *
 * @author lucile
 *
 */
class PlateauTest {  
  private static String description1;
  private static String description2;
  private static String joueurs1;
  private static String joueurs2;
  private static String joueursPetitPlateau;
  private static String joueursPlateauParDefaut;
  private static Random hasard;
  
  private Plateau plateau1;
  private Plateau plateau2;
  private Plateau plateauPetit;
  private Plateau plateauGrand1;

  private int entierAleatoire(int max) {
    // renvoie un nb aleatoire entre 0 et max-1
    return hasard.nextInt(max);
  }
  
  /**
   * Initialisation des représentations textuelles des plateaux. 
   */
  @BeforeAll
  static void init() {
    hasard = new Random();
    joueursPetitPlateau = ",Joueur0:0:1:1:0:0,Joueur1:1:4:0:0:0,"
        + "Joueur2:2:6:1:0:0,Joueur3:3:1:5:0:0,0,16";
    joueursPlateauParDefaut = ",Joueur0:0:6:5:0:0,Joueur1:1:6:14:0:0,"
        + "Joueur2:2:13:14:0:0,Joueur3:3:13:5:0:0,0,32";
    /* Description d'un plateau 8x8 non symetrique avec :
     * 7 fabriques (f-)
     * 3 collines ($$)
     * 12 arbres (##)
     * joueur de rang 0 (@1) en position (1,1) 
     * joueur de rang 1 (@2) en position (4,0) 
     * joueur de rang 2 (@3) en position (6,1) 
     * joueur de rang 3 (@4) en position (1,5) 
     */
    description1 = 
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
    joueurs1 = ",Joueur0:0:1:1:0:0,Joueur1:1:4:0:0:0,"
        + "Joueur2:2:6:1:0:0,Joueur3:3:1:5:0:0,0,24";
    /* Description d'un plateau de taille 9x9 non symetrique */
    description2 = 
          "+------------------+\n" 
        + "|              $$  |\n" 
        + "|                  |\n"
        + "|##  f-  ##        |\n" 
        + "|  ##  f1          |\n" 
        + "|      @2          |\n" 
        + "|                  |\n"
        + "|##        ##  f3  |\n" 
        + "|  ##              |\n" 
        + "|  ##              |\n" 
        + "+------------------+";
    joueurs2 = ",Joueur0:0:-1:-1:0:0,Joueur1:1:3:4:0:0,"
        + "Joueur2:2:-1:-1:0:0,Joueur3:3:-1:-1:0:0,0,40";
  }

  /**
   * Initialisation de 4 plateaux.
   */
  @BeforeEach
  void setUp() throws Exception {
    plateauPetit = new Plateau(4, MaitreDuJeu.PLATEAU_PETIT1);
    plateauGrand1 = new Plateau(8, MaitreDuJeu.PLATEAU_GRAND1);
    plateau1 = new Plateau(6, description1); 
    plateau2 = new Plateau(10, description2);
  }
  
  @Nested
  class EtatDuPlateau {
    /*
     ******************************************************* 
     *                ETAT DU PLATEAU                      *
     *******************************************************
     */
    /**
     * Test method for {@link jeu.Plateau#Plateau(int, int)}.
     */
    @Test
    void testPlateauIntInt() {
      String attendu = 
          "+----------+\n" 
          + "|          |\n" 
          + "|          |\n" 
          + "|          |\n" 
          + "|          |\n"
          + "|          |\n" 
          + "+----------+,"
          + "Joueur0:0:-1:-1:0:0,Joueur1:1:-1:-1:0:0,"
          + "Joueur2:2:-1:-1:0:0,Joueur3:3:-1:-1:0:0,0,120";
      assertEquals(attendu, new Plateau(30, 5).toString());
      assertEquals(5, new Plateau(1, 5).donneTaille());
      assertEquals(0, new Plateau(2, 4).donneTourCourant());
      assertEquals(42, new Plateau(42, 4).donneNombreDeTours());
      assertEquals(8, new Plateau(8, 4).donneNombreDeTours());
      assertEquals(44, new Plateau(44, 4).donneNombreDeTours());
      assertEquals(0, new Plateau(10, 15).donneJoueurCourant());
    }

    /**
     * Test method for {@link jeu.Plateau#Plateau(int, java.lang.String)}.
     */
    @Test
    void testPlateauIntString() {
      /* plateau1 */
      assertEquals(description1 + joueurs1, plateau1.toString());    
      assertEquals(6, plateau1.donneNombreDeTours());
      assertEquals(8, plateau1.donneTaille());
      assertEquals(0, plateau1.donneTourCourant());
      assertEquals(0, plateau1.donneJoueurCourant());
      
      /* plateau2 */
      assertEquals(9, plateau2.donneTaille());
      assertEquals(0, plateau2.donneTourCourant());
      assertEquals(10, plateau2.donneNombreDeTours());
      assertEquals(0, plateau2.donneJoueurCourant());
      assertEquals(description2 + joueurs2, plateau2.toString());
  
      /* plateauPetit */
      assertEquals(8, plateauPetit.donneTaille());
      assertEquals(0, plateauPetit.donneTourCourant());
      assertEquals(4, plateauPetit.donneNombreDeTours());
      assertEquals(0, plateauPetit.donneJoueurCourant());
      assertEquals(MaitreDuJeu.PLATEAU_PETIT1 + joueursPetitPlateau, plateauPetit.toString());
  
      /* plateauGrand1 */
      assertEquals(20, plateauGrand1.donneTaille());
      assertEquals(0, plateauGrand1.donneTourCourant());
      assertEquals(8, plateauGrand1.donneNombreDeTours());
      assertEquals(0, plateauGrand1.donneJoueurCourant());
      assertEquals(MaitreDuJeu.PLATEAU_GRAND1 + joueursPlateauParDefaut, plateauGrand1.toString());
    }
    
    /**
     * Test method for {@link jeu.Plateau#donneJoueur(int)}.
     */
    @Test
    void testDonneJoueur() {
      assertEquals("Joueur Joueur0:0:1:1:0:0", plateau1.donneJoueur(0).toString());
      assertEquals("Joueur Joueur1:1:4:0:0:0", plateau1.donneJoueur(1).toString());
      assertEquals("Joueur Joueur2:2:6:1:0:0", plateau1.donneJoueur(2).toString());
      assertEquals("Joueur Joueur3:3:1:5:0:0", plateau1.donneJoueur(3).toString());
      assertEquals(new Point(1, 1), plateau1.donneJoueur(0).donnePosition());
      assertEquals(new Point(4, 0), plateau1.donneJoueur(1).donnePosition());
      assertEquals(new Point(6, 1), plateau1.donneJoueur(2).donnePosition());
      assertEquals(new Point(1, 5), plateau1.donneJoueur(3).donnePosition());
      int indice = entierAleatoire(4);
      assertEquals("Joueur" + indice, plateau1.donneJoueur(indice).donneNom());
      assertEquals(0, plateau1.donneJoueur(indice).donneRessources());
      assertEquals(0, plateau1.donneJoueur(indice).donnePoints());
      assertEquals(indice, plateau1.donneJoueur(indice).donneRang());
      String [] couleurJoueurs = { "bleu", "vert", "rouge", "jaune" };
      assertEquals(couleurJoueurs[indice], plateau1.donneJoueur(indice).donneCouleur());
    }
    
    /**
     * Test method for {@link jeu.Plateau#donneJoueurEnPosition(Point)}.
     */
    @Test
    void testDonneJoueurEnPositionPoint() {   
      assertEquals("Joueur0", plateau1.donneJoueurEnPosition(new Point(1, 1)).donneNom());
      assertEquals("Joueur1", plateau1.donneJoueurEnPosition(new Point(4, 0)).donneNom());
      assertEquals("Joueur2", plateau1.donneJoueurEnPosition(new Point(6, 1)).donneNom());
      assertEquals("Joueur3", plateau1.donneJoueurEnPosition(new Point(1, 5)).donneNom());
      assertNull(plateau1.donneJoueurEnPosition(new Point(0, 0))); // $$
      assertNull(plateau1.donneJoueurEnPosition(new Point(0, 2))); // ##
      assertNull(plateau1.donneJoueurEnPosition(new Point(2, 0))); // f-
      assertNull(plateau1.donneJoueurEnPosition(new Point(4, 1))); // F-
      assertNull(plateau1.donneJoueurEnPosition(new Point(7, 6))); // F3
      assertNull(plateau1.donneJoueurEnPosition(new Point(1, 0))); // vide
    }
    
    /**
     * Test method for {@link jeu.Plateau#donneJoueurEnPosition(int,int)}.
     */
   @Test
    void testDonneJoueurEnPositionIntInt() {   
      assertEquals("Joueur0", plateau1.donneJoueurEnPosition(1, 1).donneNom());
      assertEquals("Joueur1", plateau1.donneJoueurEnPosition(4, 0).donneNom());
      assertEquals("Joueur2", plateau1.donneJoueurEnPosition(6, 1).donneNom());
      assertEquals("Joueur3", plateau1.donneJoueurEnPosition(1, 5).donneNom());
      assertNull(plateau1.donneJoueurEnPosition(0, 0)); // $$
      assertNull(plateau1.donneJoueurEnPosition(0, 2)); // ##
      assertNull(plateau1.donneJoueurEnPosition(2, 0)); // f-
      assertNull(plateau1.donneJoueurEnPosition(4, 1)); // F-
      assertNull(plateau1.donneJoueurEnPosition(7, 6)); // F3
      assertNull(plateau1.donneJoueurEnPosition(1, 0)); // vide
    }
    
   /**
    * Test method for {@link jeu.Plateau#nombreDeFabriquesJoueur(int)}.
    */
    @Test
    void testNombreDeFabriquesJoueurInt() {     	
        /* plateau1 
         * +----------------+ 
         * |$$  f-  @2  $$  | 
         * |  @1    F-  @3$$| 
         * |##  ##  ##  ##  | 
         * |  ##  ##  ##  ##|
         * |              F1| 
         * |  @4  F1        | 
         * |              f3| 
         * |  ##F1######F1  | 
         * +----------------+
         */     
      assertEquals(4, plateau1.nombreDeFabriquesJoueur(0));       
      assertEquals(0, plateau1.nombreDeFabriquesJoueur(1));     
      assertEquals(1, plateau1.nombreDeFabriquesJoueur(2));     
      assertEquals(0, plateau1.nombreDeFabriquesJoueur(3));
     }
    
    /**
     * Test method for {@link jeu.Plateau#coordonneeValide(int, int)}.
     */
    @Test
    void testCoordonneeValide() {
      assertFalse(plateau1.coordonneeValide(8, 0));
      assertFalse(plateau1.coordonneeValide(3, 8));
      assertFalse(plateau1.coordonneeValide(-1, -1));
      assertTrue(plateau1.coordonneeValide(0, 0));
      assertTrue(plateau1.coordonneeValide(7, 7));
      assertTrue(plateau1.coordonneeValide(5, 2));
    }
     
    /**
     * Test method for
     * {@link jeu.Plateau#joueurPeutAllerIci(int, int, boolean, boolean)}.
     */
    @Test
    void testJoueurPeutAllerIci() { 
      // sur la colline $$ en (0,0) => toujours
      assertTrue(plateau1.joueurPeutAllerIci(0, 0, false, false));
      assertTrue(plateau1.joueurPeutAllerIci(0, 0, true, true));
      assertTrue(plateau1.joueurPeutAllerIci(0, 0, false, true));
      assertTrue(plateau1.joueurPeutAllerIci(0, 0, true, false));
  
      // sur la case vide en (2,6) => toujours
      assertTrue(plateau1.joueurPeutAllerIci(2, 6, false, false));
      assertTrue(plateau1.joueurPeutAllerIci(2, 6, true, true));
      assertTrue(plateau1.joueurPeutAllerIci(2, 6, false, true));
      assertTrue(plateau1.joueurPeutAllerIci(2, 6, true, false));
  
      // sur la zone infranchissable ## en (0,2) => jamais
      assertFalse(plateau1.joueurPeutAllerIci(0, 2, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(0, 2, true, true));
      assertFalse(plateau1.joueurPeutAllerIci(0, 2, false, true));
      assertFalse(plateau1.joueurPeutAllerIci(0, 2, true, false));
  
      // sur le joueur @1 en (1,1)
      assertTrue(plateau1.joueurPeutAllerIci(1, 1, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(1, 1, true, true)); // deja un joueur
      assertTrue(plateau1.joueurPeutAllerIci(1, 1, false, true));
      assertFalse(plateau1.joueurPeutAllerIci(1, 1, true, false)); // deja un joueur
  
      // sur la fabrique f- en (2,0)
      assertTrue(plateau1.joueurPeutAllerIci(2, 0, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(2, 0, true, true)); // deja une fabrique
      assertFalse(plateau1.joueurPeutAllerIci(2, 0, false, true)); // deja une fabrique
      assertTrue(plateau1.joueurPeutAllerIci(2, 0, true, false));
  
      // sur la fabrique f3 en (7,6)
      assertTrue(plateau1.joueurPeutAllerIci(7, 6, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(7, 6, true, true)); // deja une fabrique
      assertFalse(plateau1.joueurPeutAllerIci(7, 6, false, true)); // deja une fabrique
      assertTrue(plateau1.joueurPeutAllerIci(7, 6, true, false));
      
      // sur le fabrique f- en (4, 1)
      assertTrue(plateau1.joueurPeutAllerIci(4, 1, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(4, 1, true, true)); // deja une fabrique
      assertFalse(plateau1.joueurPeutAllerIci(4, 1, false, true)); // deja une fabrique
      assertTrue(plateau1.joueurPeutAllerIci(4, 1, true, false));
  
      // sur le fabrique f1 en (7, 4)
      assertTrue(plateau1.joueurPeutAllerIci(7, 4, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(7, 4, true, true)); // deja une fabrique
      assertFalse(plateau1.joueurPeutAllerIci(7, 4, false, true)); // deja une fabrique
      assertTrue(plateau1.joueurPeutAllerIci(7, 4, true, false));
  
      // sur une case en dehors du plateau => jamais
      assertFalse(plateau1.joueurPeutAllerIci(8, 0, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(3, 8, false, false));
      assertFalse(plateau1.joueurPeutAllerIci(-1, -1, false, false));
    }
    
    /**
     * Random test method for {@link jeu.Plateau#donneContenuCellule()}.
     */
    @RepeatedTest(10)
    void testDonneContenuCellulePoint() {
      int x = entierAleatoire(8);
      int y = entierAleatoire(8);
      assertEquals(plateau1.donneContenuCellule(x, y), 
          plateau1.donneContenuCellule(new Point(x, y)));
    }

  }
  
  
  @Nested
  class TypesDeCellules {
    /*
    ******************************************************* 
    *                TYPES DE CELLULE                     *
    *******************************************************
    */
    /**
     * Test des cases infranchissables.
     */
    @Test
    public void testZoneInfranchissablePlateau() {
      /* plateau1 
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */     
      assertTrue(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(2, 0))); //fabrique
      assertTrue(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(4, 1))); //fabrique
      assertTrue(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(0, 2))); //arbre
      
      assertFalse(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(0, 0))); //colline
      assertFalse(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(1, 0))); //vide
      assertFalse(Plateau.contientUneZoneInfranchissable(
          plateau1.donneContenuCellule(4, 0))); //joueur 2
      
      int contenuCellule = plateau1.donneContenuCellule(0, 2);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_INFRANCHISSABLE);
      assertTrue((contenuCellule & Plateau.ENDROIT_INFRANCHISSABLE) != 0);  
    }
    
    /**
     * Test des cases de départ de joueur.
     */
    @Test
    public void testZoneDepart() {
      /* Le plateau de jeu : @2 en (4,0)
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */     
      assertTrue(Plateau.contientUnDepart(plateau1.donneContenuCellule(1, 1))); //depart joueur 1
      assertFalse(Plateau.contientUnDepart(plateau1.donneContenuCellule(2, 0))); //fabrique
      assertFalse(Plateau.contientUnDepart(plateau1.donneContenuCellule(4, 1))); //fabrique
      assertFalse(Plateau.contientUnDepart(plateau1.donneContenuCellule(0, 2))); //arbre
      assertFalse(Plateau.contientUnDepart(plateau1.donneContenuCellule(0, 0))); //colline
      assertFalse(Plateau.contientUnDepart(plateau1.donneContenuCellule(1, 0))); //vide 
      
      assertEquals(1, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(1, 1))); //depart joueur 1
      assertEquals(2, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(4, 0))); //depart joueur 1
      assertEquals(3, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(6, 1))); //depart joueur 1
      assertEquals(4, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(1, 5))); //depart joueur 1
      assertEquals(0, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(0, 0))); //colline
      assertEquals(0, Plateau.donneProprietaireDuPointDeDepart(
          plateau1.donneContenuCellule(1, 0))); //vide
    }
    
    /**
     * Test des cases colline.
     */
    @Test
    public void testZoneColline() {
      /* Le plateau de jeu : @2 en (4,0)
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */     
      assertTrue(Plateau.contientUneColline(plateau1.donneContenuCellule(0, 0))); //colline
      assertFalse(Plateau.contientUneColline(plateau1.donneContenuCellule(1, 1))); //depart joueur 1
      assertFalse(Plateau.contientUneColline(plateau1.donneContenuCellule(2, 0))); //fabrique
      assertFalse(Plateau.contientUneColline(plateau1.donneContenuCellule(4, 1))); //fabrique
      assertFalse(Plateau.contientUneColline(plateau1.donneContenuCellule(0, 2))); //arbre
      assertFalse(Plateau.contientUneColline(plateau1.donneContenuCellule(1, 0))); //vide
      
      int contenuCellule = plateau1.donneContenuCellule(0, 0); // colline
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_COLLINE);
      assertTrue((contenuCellule & Plateau.ENDROIT_COLLINE) != 0);
    }
  
    /**
     * Test des cases vides.
     */
    @Test
    public void testZoneVide() {
      /* Le plateau de jeu : @2 en (4,0)
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */        
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 0))); //vide
      assertFalse(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(0, 0))); //colline
      assertFalse(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 1))); //depart joueur 1
      assertFalse(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(2, 0))); //fabrique
      assertFalse(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(4, 1))); //fabrique
      assertFalse(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(0, 2))); //arbre
      
      int contenuCellule = plateau1.donneContenuCellule(1, 0); // vide
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_VIDE);
    }
  
    /**
     * Test des cases contenant un joueur.
     */
    @Test
    public void testZoneJoueur() {
      /* Le plateau de jeu : @2 en (4,0)
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */        
      assertTrue(Plateau.contientUnJoueur(plateau1.donneContenuCellule(1, 1))); // joueur @1
      assertTrue(Plateau.contientUnJoueur(plateau1.donneContenuCellule(4, 0))); // joueur @2
      assertTrue(Plateau.contientUnJoueur(plateau1.donneContenuCellule(6, 1))); // joueur @2
      assertTrue(Plateau.contientUnJoueur(plateau1.donneContenuCellule(1, 5))); // joueur @4
      assertFalse(Plateau.contientUnJoueur(plateau1.donneContenuCellule(1, 0))); //vide
      assertFalse(Plateau.contientUnJoueur(plateau1.donneContenuCellule(0, 0))); //colline
      assertFalse(Plateau.contientUnJoueur(plateau1.donneContenuCellule(2, 0))); //fabrique
      assertFalse(Plateau.contientUnJoueur(plateau1.donneContenuCellule(4, 1))); //fabrique
      assertFalse(Plateau.contientUnJoueur(plateau1.donneContenuCellule(0, 2))); //arbre
  
      assertTrue(Plateau.contientLeJoueur(plateau1.donneContenuCellule(4, 0), 1));
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(4, 0), 3));
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(1, 0), 0)); //vide
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(0, 0), 1)); //colline
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(2, 0), 2)); //fabrique
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(4, 1), 3)); //fabrique
      assertFalse(Plateau.contientLeJoueur(plateau1.donneContenuCellule(0, 2), 0)); //arbre
      
      int contenuCellule = plateau1.donneContenuCellule(1, 1); // joueur @1
      assertTrue((contenuCellule & Plateau.MASQUE_PRESENCE_JOUEUR) != 0); // joueur
      assertTrue((contenuCellule & Plateau.MASQUE_PRESENCE_JOUEUR) 
          == Plateau.PRESENCE_JOUEUR1); // joueur @1
      assertFalse((contenuCellule & Plateau.MASQUE_PRESENCE_JOUEUR) == Plateau.PRESENCE_JOUEUR2);
      assertFalse((contenuCellule & Plateau.MASQUE_PRESENCE_JOUEUR) == Plateau.PRESENCE_JOUEUR3);
      assertFalse((contenuCellule & Plateau.MASQUE_PRESENCE_JOUEUR) == Plateau.PRESENCE_JOUEUR4);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_DEPART_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_DEPART_J2);

    }
    
    /**
     * Tests des cases de type fabrique.
     */
    @Test
    public void testZoneFabrique() {
      /* plateau1 
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */        
      assertTrue(Plateau.contientUneFabrique(plateau1.donneContenuCellule(2, 0))); // fabrqiue
      assertTrue(Plateau.contientUneFabrique(plateau1.donneContenuCellule(4, 1))); // fabrique
      assertFalse(Plateau.contientUneFabrique(plateau1.donneContenuCellule(1, 0))); // vide
      assertFalse(Plateau.contientUneFabrique(plateau1.donneContenuCellule(0, 0))); // colline
      assertFalse(Plateau.contientUneFabrique(plateau1.donneContenuCellule(1, 1))); //depart joueur 1
      assertFalse(Plateau.contientUneFabrique(plateau1.donneContenuCellule(0, 2))); //arbre
         
      assertTrue(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(0), 
          plateau1.donneContenuCellule(2, 0))); // f-
      assertTrue(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(1), 
          plateau1.donneContenuCellule(4, 1))); // f-
      assertTrue(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(2), 
          plateau1.donneContenuCellule(7, 4))); // f1
      assertFalse(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(0), 
          plateau1.donneContenuCellule(7, 4))); // f1
      assertTrue(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(3), 
          plateau1.donneContenuCellule(7, 6))); // f3
      assertFalse(Plateau.contientUneFabriqueQuiNeLuiAppartientPas(plateau1.donneJoueur(2), 
          plateau1.donneContenuCellule(7, 6))); // f3
      
      assertEquals(plateau1.donneJoueur(2).donneRang() + 1, 
          Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(7, 6))); //f3
      assertEquals(plateau1.donneJoueur(0).donneRang() + 1, 
          Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(7, 4))); //f1
      assertEquals(0, Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(4, 1))); //f-
      assertEquals(0, Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(2, 0))); //f-
      assertEquals(-1, Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(2, 2))); //arbre
      assertEquals(-1, Plateau.donneUtilisateurDeLaFabrique(plateau1.donneContenuCellule(0, 0))); //vide
      assertEquals(-1, Plateau.donneUtilisateurDeLaFabrique(
          plateau1.donneContenuCellule(1, 1))); //joueur @1   
      assertEquals(-1, Plateau.donneUtilisateurDeLaFabrique(
          plateau1.donneContenuCellule(0, 0))); //colline
           
      assertEquals(Plateau.ENDROIT_FABRIQUE_J1, Plateau.donneCelluleDeLaFabriqueDuJoueur(0));
      assertEquals(Plateau.ENDROIT_FABRIQUE_J2, Plateau.donneCelluleDeLaFabriqueDuJoueur(1));
      assertEquals(Plateau.ENDROIT_FABRIQUE_J3, Plateau.donneCelluleDeLaFabriqueDuJoueur(2));
      assertEquals(Plateau.ENDROIT_FABRIQUE_J4, Plateau.donneCelluleDeLaFabriqueDuJoueur(3));
      assertEquals(Plateau.ENDROIT_FABRIQUE_LIBRE, Plateau.donneCelluleDeLaFabriqueDuJoueur(-1));
 
      int contenuCellule = plateau1.donneContenuCellule(2, 0); // f-
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROIT_FABRIQUE) != 0);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_LIBRE);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J2);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J3);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J4);

      contenuCellule = plateau1.donneContenuCellule(4, 1); // f-
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROIT_FABRIQUE) != 0);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_LIBRE);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J2);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J3);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J4);

      contenuCellule = plateau1.donneContenuCellule(7, 4); // f1
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROIT_FABRIQUE) != 0);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_LIBRE);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J2);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J3);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J4);

      contenuCellule = plateau1.donneContenuCellule(7, 6); // f3
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROIT_FABRIQUE) != 0);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_LIBRE);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J2);
      assertTrue((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J3);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J4);
      
      contenuCellule = plateau1.donneContenuCellule(0, 0); // colline
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROIT_FABRIQUE) != 0);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_LIBRE);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J1);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J2);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J3);
      assertFalse((contenuCellule & Plateau.MASQUE_ENDROITS) == Plateau.ENDROIT_FABRIQUE_J4);
   }
  }
  
  @Nested
  class RepresentationTextuelleDuTableau {
    /*
    ******************************************************* 
    *     REPRESENTATION TEXTUELLE DU PLATEAU             *
    *******************************************************
    */
     /**
     * Test method for {@link jeu.Plateau#toJavaCode()}.
     */
    @Test
    void testToJavaCode() {
      String attendu = "String tableau_ascii = "
          + "\"+----------------+\\n\"+\n" 
          + "\"|$$  f-  @2  $$  |\\n\"+\n"
          + "\"|  @1    f-  @3$$|\\n\"+\n" 
          + "\"|##  ##  ##  ##  |\\n\"+\n" 
          + "\"|  ##  ##  ##  ##|\\n\"+\n"
          + "\"|              f1|\\n\"+\n" 
          + "\"|  @4    f1      |\\n\"+\n" 
          + "\"|              f3|\\n\"+\n"
          + "\"|  ##f1######f1  |\\n\"+\n" 
          + "\"+----------------+\\n\"+" + "\n";
      assertEquals(attendu, plateau1.toJavaCode().toString());
    }
  
    /**
     * Test method for {@link jeu.Plateau#encode()}.
     */
    @Test
    void testEncode() {
      String description = description1.replace('\n', 'X');
      assertEquals(description1 + joueurs1, plateau1.encode('\n'));
      assertEquals(description + joueurs1, plateau1.encode('X'));
    }
  
    /**
     * Test method for
     * {@link jeu.Plateau#decode(java.lang.String, java.lang.String)}.
     */
    @Test
    void testDecodeStringString() {
      Plateau nouveau = Plateau.decode(plateau1.encode('X'), "X");
      assertEquals(plateau1, nouveau);
      assertEquals(0, nouveau.donneJoueurCourant());
  
      nouveau = Plateau.decode(plateau2.encode('Q'), "Q");
      assertEquals(plateau2, nouveau);
      assertEquals(0, nouveau.donneJoueurCourant());
    }
  
    /**
     * Test method for {@link jeu.Plateau#equals()}.
     */
    @Test
    void testEquals() {
      assertEquals(new Plateau(6, description1), plateau1);
      assertNotEquals(new Plateau(4, description1), plateau1);
    }
  }

  @Nested
  class RechercheDeCheminsOuDeZones {
    /*
     ********************************************************* 
     *     RECHERCHE DE CHEMIN OU DE ZONE ENVIRONNANTE       *
     *********************************************************
     **/
  
    /**
     * Test method for {@link jeu.Plateau#cherche(, int, int)}.
     */
    @Test
    void testCherche1() {
      /* plateau1 
       * +----------------+ 
       * |$$  f-  @2  $$  | 
       * |  @1    F-  @3$$| 
       * |##  ##  ##  ##  | 
       * |  ##  ##  ##  ##|
       * |              F1| 
       * |  @4  F1        | 
       * |              f3| 
       * |  ##F1######F1  | 
       * +----------------+
       */     
      Point positionJoueur4 = new Point(1, 5);
      assertTrue(Plateau.contientLeJoueur(plateau1.donneContenuCellule(positionJoueur4), 3));
      int rayon;
      System.out.println(plateau1);
      /* recherches autour de la case @4 avec un rayon 0 => le joueur */
      rayon = 0;
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_FABRIQUE).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_COLLINE).toString());
  
      /* recherches autour de la case @4 avec un rayon 1 => le joueur */
      rayon = 1;
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_FABRIQUE).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_COLLINE).toString());
  
      /* recherches autour de la case @4 avec un rayon 2 => le joueur & F1 */
      rayon = 2;
      assertEquals("{1=[], 2=[[x=2,y=7]], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals("{1=[], 2=[[x=2,y=7]], 4=[]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_FABRIQUE).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_COLLINE).toString());
  
      /* recherches autour de la case @4 avec un rayon 3 => le joueur & 2F1 */
      rayon = 3;
      assertEquals("{1=[], 2=[[x=4,y=5], [x=2,y=7]], "
          + "4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[], 2=[], 4=[[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals("{1=[], 2=[[x=4,y=5], [x=2,y=7]], 4=[]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_FABRIQUE).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_COLLINE).toString());
   
      /* recherches autour de la case @4 avec un rayon 5 => 4 joueurs & 2 collines & 5 fabriques */
      rayon = 5;
      assertEquals(
          "{1=[[x=0,y=0], [x=6,y=0]], "
              + "2=[[x=2,y=0], [x=4,y=1], [x=4,y=5], "
              + "[x=2,y=7], [x=6,y=7]], "
              + "4=[[x=4,y=0], [x=1,y=1], [x=6,y=1], "
              + "[x=1,y=5]]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[[x=0,y=0], [x=6,y=0]], 2=[], 4=[]}",
          plateau1.cherche(positionJoueur4, rayon, Plateau.CHERCHE_COLLINE).toString());
      assertEquals(2, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_COLLINE).size());
      assertEquals(5, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_FABRIQUE).size());
      assertEquals(4, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_JOUEUR).size());
      
      /* recherches autour de la case @4 avec un rayon 6 => 4 joueurs & 3 collines & 7 fabriques */
      rayon = 6;
      assertEquals(3, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_COLLINE).size());
      assertEquals(7, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_FABRIQUE).size());
      assertEquals(4, plateau1.cherche(positionJoueur4, rayon, 
          Plateau.CHERCHE_TOUT).get(Plateau.CHERCHE_JOUEUR).size());
    }
  
    /**
     * Test method for {@link jeu.Plateau#cherche(, int, int)}.
     */
    @Test
    void testCherche2() {
      Point position = new Point(4, 1);
      assertTrue(Plateau.contientUneFabrique(plateau1.donneContenuCellule(position)));
      int rayon;
  
      /* recherches autour de la case F- en (2,0) avec un rayon 0 */
      rayon = 0;
      assertEquals("{1=[], 2=[[x=4,y=1]], 4=[]}",
          plateau1.cherche(position, rayon, Plateau.CHERCHE_TOUT).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(position, rayon, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals("{1=[], 2=[[x=4,y=1]], 4=[]}",
          plateau1.cherche(position, rayon, Plateau.CHERCHE_FABRIQUE).toString());
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(position, rayon, Plateau.CHERCHE_COLLINE).toString());
  
      /* recherches autour de la case F- en (2,0) avec d'autres rayons */
      assertEquals("{1=[], 2=[], 4=[[x=4,y=0]]}",
          plateau1.cherche(position, 1, Plateau.CHERCHE_JOUEUR).toString());
      assertEquals(2, plateau1.cherche(position, 2, 
          Plateau.CHERCHE_JOUEUR).get(Plateau.CHERCHE_JOUEUR).size());
      assertTrue(plateau1.cherche(position, 2, 
          Plateau.CHERCHE_JOUEUR).get(Plateau.CHERCHE_JOUEUR).contains(new Point(4, 0)));
      assertTrue(plateau1.cherche(position, 2, 
          Plateau.CHERCHE_JOUEUR).get(Plateau.CHERCHE_JOUEUR).contains(new Point(6, 1)));
      assertEquals("{1=[], 2=[], 4=[]}", 
          plateau1.cherche(position, 1, Plateau.CHERCHE_COLLINE).toString());
      assertEquals("{1=[[x=6,y=0]], 2=[], 4=[]}",
          plateau1.cherche(position, 2, Plateau.CHERCHE_COLLINE).toString());
      assertEquals(2, plateau1.cherche(position, 3, 
          Plateau.CHERCHE_COLLINE).get(Plateau.CHERCHE_COLLINE).size());
      assertTrue(plateau1.cherche(position, 3, 
          Plateau.CHERCHE_COLLINE).get(Plateau.CHERCHE_COLLINE).contains(new Point(6, 0)));
      assertTrue(plateau1.cherche(position, 3, 
          Plateau.CHERCHE_COLLINE).get(Plateau.CHERCHE_COLLINE).contains(new Point(7, 1)));
      assertEquals(2, plateau1.cherche(position, 2, 
          Plateau.CHERCHE_FABRIQUE).get(Plateau.CHERCHE_FABRIQUE).size());
      assertTrue(plateau1.cherche(position, 2, 
          Plateau.CHERCHE_FABRIQUE).get(Plateau.CHERCHE_FABRIQUE).contains(new Point(2, 0)));
      assertTrue(plateau1.cherche(position, 2, 
          Plateau.CHERCHE_FABRIQUE).get(Plateau.CHERCHE_FABRIQUE).contains(new Point(4, 1)));
    }
  
    /**
     * Test method for
     * {@link jeu.Plateau#donneCheminEntre(, )}.
     */
    @Test
    void testDonneCheminEntre() {
      /*depart ou arrivee null */
      assertNull(plateau1.donneCheminEntre(null, new Point(6, 1)));
      assertNull(plateau1.donneCheminEntre(new Point(0, 5), null));
      assertNull(plateau1.donneCheminEntre(null, null));
  
      /*depart ou arrivee hors limites */
      assertNull(plateau1.donneCheminEntre(new Point(0, 5), new Point(8, 2)));
      assertNull(plateau1.donneCheminEntre(new Point(0, -1), new Point(0, 1)));
  
      /*debut ok et arrivee infranchissable */
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(0, 2)));
      assertEquals("[(1,1), (1,2), (0,2)]", 
          plateau1.donneCheminEntre(new Point(1, 0), new Point(0, 2)).toString());
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(1, 3)));
      assertEquals("[(1,1), (1,2), (1,3)]", 
          plateau1.donneCheminEntre(new Point(1, 0), new Point(1, 3)).toString());
  
      /*depart ou arrivee ok */
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 0)));
      assertTrue(Plateau.contientLeJoueur(plateau1.donneContenuCellule(6, 1), 2));
      assertEquals("[(1,1), (2,1), (3,1), (3,0), (4,0), (5,0), (5,1), (6,1)]",
          plateau1.donneCheminEntre(new Point(1, 0), new Point(6, 1)).toString());
      assertEquals("[(5,1), (5,0), (4,0), (3,0), (3,1), (2,1), (1,1), (1,0)]",
          plateau1.donneCheminEntre(new Point(6, 1), new Point(1, 0)).toString());
  
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 2)));
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(3, 2)));
      assertEquals("[(1,1), (2,1), (3,1), (3,2)]",
          plateau1.donneCheminEntre(new Point(1, 2), new Point(3, 2)).toString());
      assertEquals("[(3,1), (2,1), (1,1), (1,2)]",
          plateau1.donneCheminEntre(new Point(3, 2), new Point(1, 2)).toString());
  
      /*debut infranchissable et arrivee ok */
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(0, 2)));
      assertEquals("[(1,2), (1,1), (2,1), (3,1), (3,2)]",
          plateau1.donneCheminEntre(new Point(0, 2), new Point(3, 2)).toString());
  
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(1, 3)));
      assertEquals("[(1,2), (1,1), (1,0)]", 
          plateau1.donneCheminEntre(new Point(1, 3), new Point(1, 0)).toString());
  
      /* chemin impossible */
      assertNull(plateau1.donneCheminEntre(new Point(1, 0), new Point(1, 5)));
      assertNull(plateau1.donneCheminEntre(new Point(1, 1), new Point(1, 4)));
      
      /* chemin qui doit éviter des fabriques */
      assertEquals( "[(2,1), (3,1), (3,0), (4,0), (5,0), (6,0), (6,1), (7,1)]", 
          plateau1.donneCheminEntre(new Point(1,1), new Point(7,1)).toString());
      assertEquals( "[(2,1), (3,1), (3,0), (4,0), (5,0), (6,0)]", 
          plateau1.donneCheminEntre(new Point(1,1), new Point(6,0)).toString());
      assertNull( plateau1.donneCheminEntre(new Point(1,1), new Point(6,3)));
     
      /* chemin qui traverse les collines */
      assertNotNull( plateau1.donneCheminEntre(new Point(1,1), new Point(7,2)));
      assertNotNull( plateau1.donneCheminEntre(new Point(4,0), new Point(7,0)));
     }
  
    /**
     * Test method for
     * {@link jeu.Plateau#donneCheminAvecObstaclesSupplementaires(, 
     * , java.util.ArrayList)}.
     */
    @Test
    void testDonneCheminAvecObstaclesSupplementaires() {
      List<Node> obstacles;
  
      /*depart ou arrivee null */
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(null, new Point(6, 1), null));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(0, 5), null, null));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(null, null, null));
  
      /*depart ou arrivee hors limites */
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(0, 5), 
          new Point(8, 2), null));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(0, -1), 
          new Point(0, 1), null));
  
      /*debut ok et arrivee infranchissable */
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(0, 2)));
      assertEquals("[(1,1), (1,2), (0,2)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0), 
              new Point(0, 2), null).toString());
      assertNotEquals("[(1,1), (1,2), (0,2)]",
          plateau1
              .donneCheminAvecObstaclesSupplementaires(new Point(1, 0), new Point(0, 2), 
                  Arrays.asList(new Node(1, 2)))
              .toString());
      assertEquals("[(1,1), (1,2), (0,2)]",
          plateau1
              .donneCheminAvecObstaclesSupplementaires(new Point(1, 0), new Point(0, 2), 
                  Arrays.asList(new Node(0, 2)))
              .toString());
  
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(1, 3)));
      assertEquals("[(1,1), (1,2), (1,3)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0), 
              new Point(1, 3), null).toString());
      assertEquals("[(1,1), (1,2), (1,3)]", 
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0),
              new Point(1, 3), Arrays.asList(new Node(4, 4), new Node(5, 2))).toString());
  
      /*depart ou arrivee ok */
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 0)));
      assertTrue(Plateau.contientLeJoueur(plateau1.donneContenuCellule(6, 1), 2));
      assertEquals("[(1,1), (2,1), (3,1), (3,0), (4,0), (5,0), (5,1), (6,1)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0), 
              new Point(6, 1), null).toString());
      assertEquals("[(5,1), (5,0), (4,0), (3,0), (3,1), (2,1), (1,1), (1,0)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(6, 1), 
              new Point(1, 0), null).toString());
      obstacles = Arrays.asList(new Node(3, 1));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0), 
          new Point(6, 1), obstacles));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(6, 1), 
          new Point(1, 0), obstacles));
  
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(1, 2)));
      assertTrue(Plateau.contientUneZoneVide(plateau1.donneContenuCellule(3, 2)));
      assertEquals("[(1,1), (2,1), (3,1), (3,2)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 2), 
              new Point(3, 2), null).toString());
  
      assertEquals("[(3,1), (2,1), (1,1), (1,2)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(3, 2), 
              new Point(1, 2), null).toString());
  
      assertEquals("[(3,5), (3,6), (4,6), (5,6)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(3, 4), 
              new Point(5, 6), null).toString());
      obstacles = Arrays.asList(new Node(4, 4), new Node(6, 4), new Node(7, 4));
      assertEquals("[(3,5), (3,6), (4,6), (5,6)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(3, 4), 
              new Point(5, 6), obstacles).toString());
      obstacles = Arrays.asList(new Node(3, 6), new Node(3, 1), new Node(3, 7));
      assertEquals("[(4,4), (5,4), (5,5), (5,6)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(3, 4), 
              new Point(5, 6), obstacles).toString());
      obstacles = Arrays.asList(new Node(3, 6), new Node(5, 4));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(3, 4), 
          new Point(5, 6), obstacles));
  
      /*debut infranchissable et arrivee ok */
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(0, 2)));
      assertEquals("[(1,2), (1,1), (2,1), (3,1), (3,2)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(0, 2), 
              new Point(3, 2), null).toString());
      obstacles = Arrays.asList(new Node(5, 5), new Node(2, 1));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(0, 2), 
          new Point(3, 2), obstacles));
  
      assertTrue(Plateau.contientUneZoneInfranchissable(plateau1.donneContenuCellule(1, 3)));
      assertEquals("[(1,2), (1,1), (1,0)]",
          plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 3), 
              new Point(1, 0), null).toString());
      obstacles = Arrays.asList(new Node(1, 2), new Node(1, 1), new Node(1, 0));
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 3), 
          new Point(1, 0), obstacles));
  
      /*chemin impossible */
      assertNull(plateau1.donneCheminAvecObstaclesSupplementaires(new Point(1, 0), 
          new Point(1, 5), null));
    }
  }

  
  @Nested
  class GenerationDePlateauAleatoire {
    /*
     ******************************************************** 
     *            GENERATION DE PLATEAU ALEATOIRE           *
     ********************************************************
     */
  
    /**
     * Test method for
     * {@link jeu.Plateau#generePlateauAleatoire(int, int, int, int, int)}.
     */
    @RepeatedTest(10)
    void testGenerePlateauAleatoire() {
      Plateau nouveau = null;
      while (nouveau == null) {
        nouveau = Plateau.generePlateauAleatoire(100, 5, 2, 5, 5);
      }
      assertNotNull( nouveau);
      assertEquals(100, nouveau.donneNombreDeTours());
      assertEquals(10, nouveau.donneTaille());
      int nbFabriques = 0;
      int nbArbres = 0;
      int nbCollines = 0;
      int nbVides = 0;
      int nbDeparts = 0;
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          int n = nouveau.donneContenuCellule(i, j);
          if (Plateau.contientUneFabrique(n)) {
               nbFabriques++; 
           } else if (Plateau.contientUneZoneInfranchissable(n)) {
            nbArbres++;
          }
          if (Plateau.contientUneColline(n)) {
            nbCollines++;
          }
          if (Plateau.contientUneZoneVide(n)) {
            nbVides++;
          }
          if (Plateau.contientUnDepart(n)) {
            nbDeparts++;
          }
        }
      }
      assertEquals(20, nbArbres);
      assertEquals(20, nbFabriques);
      assertEquals(8, nbCollines);
      assertEquals(4, nbDeparts);
      assertEquals(nouveau.donneTaille() * nouveau.donneTaille(), 
          nbArbres + nbFabriques + nbCollines + nbDeparts + nbVides);
    }
  }

}
