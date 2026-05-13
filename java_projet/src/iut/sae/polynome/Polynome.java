/**
 * Polynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.*;
import java.util.regex.*;

/*
 * Classe Polynome
 * Explication d'un polynome :
 * 		Ex : 3x^2 + 6x + 7
 * 
 * 			Nombre de termes ou de monomes : 3
 * 			degres : 2
 * 			coefficient : [3.0, 6.0]
 * 			racine : [-1]
 * 			limite : -Infinite = +Infinite // + Infinite = +Infinite
 * 			
 * TODO faire la vrai doc ...
 * 
 */
public class Polynome {
	
	//tableau oû sera stocker les monômes récupéré grace à la méthode getMonomes()
    private String Monomes[];

    // polynome saisie par l'utilisateur sous le format : 3x^2 + 7x^1 +5
    private String polynome;

    /**
     * Constructeur de polynômes
     * @param polynome - polynômes rentrer par l'uti 
     * @throws IllegalArgumentException si le polynôme rentrer est nul (="0") ou vide(="").
     */
     public Polynome(String polynome) {
    	 String regex = "^(?:\\d*x(?:\\^\\d+)?|\\d+)(?:\\s?[+\\-]\\s?(?:\\d*x(?:\\^\\d+)?|\\d+))*$";
         // Sortie Err 
    	 // Si la saisie rentrer contient des cara autre  que : "0-9|x|[:space]|^|+|-"	-> regex : ^(?:\d*x(?:\^\d+)?|\d+)(?:\s?[+\-]\s?(?:\d*x(?:\^\d+)?|\d+))*$
    	 if (!polynome.matches(regex)) {
    	     throw new IllegalArgumentException("Erreur : caracteres inconnues ou caractere 'x' absent");
    	 }
    	this.polynome = polynome;
      }
	
	/*
	 * 
	 */
	public int degres() {
		return 0;
	}
	
	/*
	 * 
	 */
	public double coefficient() {
		return 0;
	}
	
	/*
	 * 
	 */
	public double racine() {
		return 0;
	}
	
	/*
	 * 
	 */
	public double limite() {
		return 0;
	}
	
	
    /**
     * Getter
     * Méthode imuable de récupération de monomes à partir d'un objet polynome
     * ex : Polynome(3x^3+5x-10).getMonome() -> {3x^3, 5x, -10}
     * @return - liste de monomes du polynomes
     */
    //TODO format à retravailler : ex Polynome(3x^3+7x-10).getMonomes()  --> {3x^3+, 7x-, 10}   ->ERR 
	public  String[] getMonomes() {
	   // Parcours chaques caracteres du polynome -> STOP quand parcourut tout les caracteres 
	   // Si caractere == + ou - alors prendre carac parcouruts  mettre dans une variable de 		   stockage et mettre cette variable dans le tableau Monomes[]
	
	   for (int parcoursDeCaractere = 0, indiceTableau = -1, String stockageDeCaractere,
		    String recuperationDeCaractereActuel; parcoursDeCaractere < polynome.length(); parcoursDeCaractere++) {
		
		   recuperationDeCaractereActuel = ;	// TODO mettre le carac actuel evaluer dedans 
		   stockageDeCaractere += recuperationDeCaractereActuel;
		   if (recuperationDeCaractereActuel == "+" || recuperationDeCaractereActuel == '-') {
			
			   this.Monomes[indiceTableau +1] = stockageDeCaractere;
			   stockageDeCaractere = "";	// Vidage
		   }
	   }
	   return this.Monomes;
	}	
	
	/* Setter
	 * @return 
	 * 
	 */
	public int setMonomes() {
		return 0;			//STUB
	}
	
	
	
	
    /**
     * Affichage du polynôme sous forme de texte simple.
     * Format : Polynome[ (3x^2) + (-6x^1) + (7) ]
     */
	//TODO FIX : check que Monomes.length est possible sinon faire this.Monomes.length
    @Override
	public String toString() {
		 String message = "";	//STUB
	        for (int parcour = 0, puissance = Monomes.length -1; parcour < Monomes.length; parcour++, puissance--) {
	        
	            message += " (" + Monomes[parcour];
	            // l'indice parcours les coef multi des X
	            if (parcour < Monomes.length) {
	            	message += "x^" + puissance + ") +";
	            } 
	            // l'indice est en train de parcourir la const 
	            else {
	            	message += ") ";
	            }
	        }
	        return "Polynome[" + message + "]";
	}
    
    
    
    /**
     * 
     * V1 ToString By Flo
     * Affiche le polynôme sous forme de texte simple.
     */
	/*
	@Override
    public String toString() {
        String resultat = "",
               message = "";
        
        for (int parcour = 0; parcour < Monomes.length; parcour++) {
            resultat += Monomes[parcour];
        }
        message = "Polynome[ " + resultat + " ]";		//val intermédiaire
        return message;
    }
    */
	 	
     
	
}
