/**
 * Polynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.*;

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
	
	/*Tableau polynome composer de monomes (=éléments du tabl)*/
	private double[] Monomes;
	
	/**
	 * Constructeur : 
	 * crée un polynôme à partir d'un tableau de Monomes.
     * @param monomes Tableau de coefficients (ex: {3.0, 2.0, 1.0} pour 3x^2 + 2x + 1)
     * @throws IllegalArgumentException si le tableau est nul ou vide.
	 */
	// TODO faire une boucle for pour récupérer chaques élément du tableau (récup chaques monomes)
	public Polynome(double[] monomes) {
		if (monomes == null || monomes.length == 0) {
            throw new IllegalArgumentException("Erreur : Le tableau de coefficients ne peut pas être vide !");
        }
        this.Monomes = monomes;
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
	
	
	/*	Getter
	 * @return Monomes[] renvoie un tableau représentant le polynome 
	 *        		   composé d'élément qui sont les coef des monomes.
	 */
	 public double[] getMonomes() {
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
	        for (int parcour = 0, puissance = Monomes.length -1; parcour <= Monomes.length; parcour++, puissance--) {
	        
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
