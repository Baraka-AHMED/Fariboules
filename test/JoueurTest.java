package test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import jeu.Joueur;
import jeu.Joueur.Action;

/**
 * Test de la classe Joueur.
 *
 * @author Lucile
 *
 */
class JoueurTest {

  private Joueur un;
  private Joueur deux;

  @BeforeEach
  void setUp() {
    un = new Joueur("un");
    deux = new Joueur();
  }

  /**
   * Test du constructeur {@link jeu.Joueur#Joueur()} et des getters.
   */
  @Test
  void testConstructeurs() {
    assertEquals("un", un.donneNom());
    assertEquals("Fernandel", deux.donneNom());

    assertEquals(0, un.donneRessources());
    assertEquals(0, deux.donneRessources());

    assertEquals("[x=0,y=0]", un.donnePosition().toString());
    assertEquals("[x=0,y=0]", deux.donnePosition().toString());

    assertEquals(0, un.donnePoints());
    assertEquals(0, deux.donnePoints());

    assertEquals(-1, un.donneRang());
    assertEquals(-1, deux.donneRang());

    assertEquals("Joueur un:-1:0:0:0:0", un.toString());
    assertEquals("Joueur Fernandel:-1:0:0:0:0", deux.toString());

    assertThrows(ArrayIndexOutOfBoundsException.class, () -> un.donneCouleur());
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> deux.donneCouleur());   
  }

  /**
   * Test de la methode {@link jeu.Joueur#encode()}.
   */
  @Test
  void testEncode() {
    assertEquals("un:-1:0:0:0:0", un.encode());
    String code = "moi:2:10:20:100:50";
    Joueur j = Joueur.decode(code);
    assertEquals(code, j.encode());
  }

  /**
   * Test de la methode {@link jeu.Joueur#decode(java.lang.String)}.
   */
  @Test
  void testDecode() {
    String code = "moi:2:10:20:100:50";
    Joueur j = Joueur.decode(code);
    assertEquals("moi", j.donneNom());
    assertEquals(2, j.donneRang());
    assertEquals("rouge", j.donneCouleur());
    assertEquals(new Point(10, 20), j.donnePosition());
    assertEquals(100, j.donneRessources());
    assertEquals(50, j.donnePoints());
    assertEquals("Joueur " + code, j.toString());
  }

  /**
   * Test de la methode {@link jeu.Joueur#faitUneAction(Plateau)} qui definit le
   * comportement par defaut du Joueur, c'est-à-dire un comportement aleatoire
   * (independant de l'etat du plateau).
   */
  @RepeatedTest(10)
  void testFaitUneAction() {
    int fois = 10000;
    int nbActions = Action.values().length;
    int[] occurrences = new int[nbActions];
    for (int i = 0; i < fois; i++) {
      int rang = un.faitUneAction(null).ordinal();
      assertTrue(rang >= 0 && rang < nbActions);
      occurrences[rang]++;
    }
    /* La fréquence de RIEN est inférieure à 5% */
    assertTrue(occurrences[0] < fois * 0.05);
    /* La fréquence des autres actions est la même et de l'orde de 24% */
    for (int i = 1; i < nbActions; i++) {
      assertEquals(fois * 0.24, occurrences[i] * 1.0, fois / 20.0);
    }
  }
}
