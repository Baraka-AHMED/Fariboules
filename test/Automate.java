package test;

import gui.Point;
import jeu.Joueur;
import jeu.Plateau;

/**
 * Joueur qui joue une séquence d'actions prédéterminée.
 * 
 *
 * @author Lucile
 *
 */
public class Automate extends Joueur {

  /** Séquence d'actions de déplacement du joueur. */
  private String deplacements;
  
  /** Numéro de l'action dans la séquence d'actions. */
  private int numero;

  /**
   * Crée un joueur de nom donné et de séquence d'actions donnée.
   * La séquence d'actions est une chaîne formée des caractères D (pour DROITE), G
   * (pour GAUCHE), H (pour HAUT), B (pour BAS) et . (pour RIEN). Par exemple, la
   * chaîne "D.BBH" définit la séquence d'actions : DROITE, RIEN, BAS, BAS, HAUT.
   *
   * @param nom du joueur
   * @param dep la séquence des déplacements du joueur
   */
  public Automate(String nom, String dep) {
    super(nom);
    deplacements = dep;
    numero = 0;
  }


  /**
   *  Redéfinit la méthode {@link jeu.Joueur#faitUneAction(jeu.Plateau)}.
   *  La méthode renvoie les actions dans l'ordre de la séquence deplacements.
   *  Une fois la séquence entièrement parcourue, la méthode renvoie l'action RIEN.
   *
   *  @param p le plateau de jeu.
   */
  @Override
  public Action faitUneAction(Plateau p) {
    if (numero >= deplacements.length()) {
      return Action.RIEN;
    }
    switch (deplacements.charAt(numero++)) {
      case 'H':
        return Action.HAUT;
      case 'B':
        return Action.BAS;
      case 'D':
        return Action.DROITE;
      case 'G':
        return Action.GAUCHE;
      case '.':
        return Action.RIEN;
      default:
        throw new Error("Action inconnue");
    }
  }
  
  /** 
   * Variable publique qui permet de forcer le nombre de brins à ajouter à une fabrique 
   * dont la valeur doit être modifiée à chaque fois que le joueur-automate essaie de se déplacer
   * vers une fabrique libre ou qu'il est déjà en train d'utiliser. C'est la valeur qui sera alors 
   * ajoutée à la fabrique. 
   */
  public int REPONSE = -1;
  
  /**
   *  Redéfinit la méthode {@link jeu.Joueur#donneRessourcesPourFabrique(gui.Point)}
   *  pour déterminer le nombre de brins de farigoule à placer dans une fabrique pour
   *  l'utiliser.
   *
   *  @param p la position de la fabrique destinataire
   *  @return le nombre de brins à ajouter
   */
  	@Override
	public int donneRessourcesPourFabrique(Point p) {
		return REPONSE;
	}
}
