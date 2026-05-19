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
	
	//Liste oû sera stocker les monômes récupéré grace à la méthode getMonomes()
	private ArrayList<String> monomes = new ArrayList<>();

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
	    int degMax = 0;
	    String monomeDegMax = "";	//STUB

	    for (int indiceDuMonome = 0; indiceDuMonome < this.monomes.size(); indiceDuMonome++) {

	        String monomeActuel = this.monomes.get(indiceDuMonome);

	        // Pattern qui cherche ^ suivi d'un ou plusieurs chiffres
	        Pattern patternExposant = Pattern.compile("\\^(\\d+)");
	        Matcher matcherExposant = patternExposant.matcher(monomeActuel);

	        if (matcherExposant.find()) {
	            // group(1) récupère uniquement les chiffres après le ^
	            int exposantActuel = Integer.parseInt(matcherExposant.group(1));

	            if (exposantActuel > degMax) {
	                degMax    = exposantActuel;
	                monomeDegMax = monomeActuel;
	            }
	        }
	    }
		return degMax;
	}
	
	/*
	 * 
	 */
	public ArrayList<Double> coefficient() {
		return 0;
	}
	
	/*
	 * 
	 */
	public ArrayList<Integer> racine() {
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
     * ex : Polynome(3x^3 +5x- 10).getMonome() -> {3x^3, 5x, -10}
     * @return - liste de monomes du polynomes
     */
	//TODO : conflict ArrayList (monomes) with String (getMomones)
	public  ArrayList<String> getMonomes() {
	   // Parcours chaques caracteres du polynome -> STOP quand parcourut tout les caracteres 
	   // Si caractere == + ou - alors prendre carac parcouruts  mettre dans une variable de 		   stockage et mettre cette variable dans le tableau Monomes[]
	   
	   String stockageDeCaractere = "";				//STUB
	   char recuperationDeCaractereActuel;	        //STUB
	   this.polynome = this.polynome.replaceAll("\\s+", "");
	   for (int parcoursDeCaractere = 0; parcoursDeCaractere < this.polynome.length(); parcoursDeCaractere++) {
		
		   recuperationDeCaractereActuel = this.polynome.charAt(parcoursDeCaractere);	//récupération du carac actuel évaluer 
		   if (recuperationDeCaractereActuel != '+' && recuperationDeCaractereActuel != '-') {
			   
			   stockageDeCaractere += recuperationDeCaractereActuel;
		   } else {
			
			   this.monomes.add(stockageDeCaractere);
			   if (recuperationDeCaractereActuel == '+') {
				   stockageDeCaractere = "";
			   } else {
				   stockageDeCaractere = "-";
			   }
		   }
	   }
	   if (!stockageDeCaractere.isEmpty()) {
	       this.monomes.add(stockageDeCaractere);
	   }
	   return this.monomes;
	}	
	
	/* Setter
	 * Met à jour la liste 'monomes' en ajoutant à l'indice donnée le monome souhaité 
	 * @param monomeAAjouter - monome que l'on souhaite insérer
	 * @parm indiceList - indice de la liste oû placer le monome à ajouter
	 */
	public void setMonomes(String monomeAAjouter, int indiceList) {
		this.monomes.set(indiceList, monomeAAjouter);
	}
	

    /**
     * Affichage du polynôme sous forme de texte simple.
     * Format : Polynome(3x^2+2x^1+10).toString() -> 3x^2+2x^1+10
     * @return messageAffiche - affichage du polynome sous Format 
     */
    @Override
	public String toString() {
		 String messageAffiche = this.polynome;
		 return messageAffiche;
	}	
}
