package main;

import jeu.Joueur;
import jeu.Plateau;

/**
 * Un joueur dont la stratÃ©gie de jeu est dÃ©finie par
 * {@link #faitUneAction(Plateau) }, Ã  utiliser dans le {@link Lanceur} du jeu.
 *
 * @author ???
 */
public class MonJoueur extends Joueur {

  public MonJoueur(String sonNom) {
    super(sonNom);
  }

  @Override
  public Action faitUneAction(Plateau etatDuJeu) {
    return super.faitUneAction(etatDuJeu); // a modifier
  }

}