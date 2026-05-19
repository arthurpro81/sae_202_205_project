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
	// partie créées par amaury
	public String limite(char limite) {
		if (limite != '+' && limite != '-') {
			throw new IllegalArgumentException("Erreur : L'étude de limite de cette version ne peut être que '+' ou '-' !");
		}
		double degrMax = Monomes[0];
		String reponse = " "; // initialisation pour passer le packetage
		if (limite == '+' ) {
			if (degrMax < 0) {
				reponse = "-infini";
			} else {
				reponse = "+infini";
			}
		} else {
			if (degrMax < 0 && degrMax/2 == 0) {
				reponse ="-infini";
			} else if (degrMax < 0 && degrMax/2 != 0) {
				reponse ="+infini";
			} else if (degrMax > 0 && degrMax/2 == 0) {
				reponse ="+infini";
			} else if (degrMax > 0 && degrMax/2 != 0) {
				reponse ="-infini";
			}
		}
		return reponse;
	}
		
	
	
    /**
     * Getter
     * Méthode imuable de récupération de monomes à partir d'un objet polynome
     * ex : Polynome(3x^3+5x-10).getMonome() -> {3x^3, 5x, -10}
     * @return - liste de monomes du polynomes
     */
	//TODO : conflict ArrayList (monomes) with String (getMomones)
	public  ArrayList<String> getMonomes() {
	   // Parcours chaques caracteres du polynome -> STOP quand parcourut tout les caracteres 
	   // Si caractere == + ou - alors prendre carac parcouruts  mettre dans une variable de 		   stockage et mettre cette variable dans le tableau Monomes[]
	   
	   String stockageDeCaractere = "";				//STUB
	   char recuperationDeCaractereActuel;	//STUB
	   for (int parcoursDeCaractere = 0; parcoursDeCaractere < this.polynome.length() - 1; parcoursDeCaractere++) {
		
		   recuperationDeCaractereActuel = this.polynome.charAt(parcoursDeCaractere);	//récupération du carac actuel évaluer 
		   if (recuperationDeCaractereActuel != '+' || recuperationDeCaractereActuel != '-') {
			   
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
