/**
 * Polynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Classe Polynome
 * Explication d'un polynome :
 * 		Ex : 3x^2 + 6x + 7
 * 
 * 			Nombre de termes ou de monomes : 3
 * 			degres : 2
 * 			coefficient : [3.0, 6.0]
 * 			racine : [-1]
 * 			limite : -Infinite = +Infini // + Infini = +Infinite
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
    	 String regex = "^\\s?-?(?:\\d*x(?:\\^\\d+)?|\\d+)(?:\\s?[+\\-]\\s?(?:\\d*x(?:\\^\\d+)?|\\d+))*$";
 
    	 if (!polynome.matches(regex)) {
    	     throw new IllegalArgumentException("Erreur : caracteres inconnues ou caractere 'x' absent");
    	 }
    	this.polynome = polynome;
    	this.remplirMonomes();
      }
	
    /*
     * Méthode appelée par le constructeur à chaque création d'objet Polynome.
     * Méthode permétant de remplir directement this.monomes avec les monomes du polynome
     */
	private void remplirMonomes() {
		   String stockageDeCaractere = "";				//STUB
		   char recuperationDeCaractereActuel;
		   String polyNoSpace = this.polynome.replaceAll("\\s+", "");
		   
		   for (int parcoursDeCaractere = 0; parcoursDeCaractere < polyNoSpace.length(); parcoursDeCaractere++) {
			
			   recuperationDeCaractereActuel = polyNoSpace.charAt(parcoursDeCaractere);	//récupération du carac actuel évaluer 
			   
			   if (recuperationDeCaractereActuel != '+' && recuperationDeCaractereActuel != '-') {
				   stockageDeCaractere += recuperationDeCaractereActuel;
			   } else {
				   this.monomes.add(stockageDeCaractere);
				   stockageDeCaractere = (recuperationDeCaractereActuel == '-') ? "-" : "";
			   }
		   }
		   if (!stockageDeCaractere.isEmpty()) {
		       this.monomes.add(stockageDeCaractere);
		   }
	}

	/* Méthode recherchant les degres d'un objet Polynome() et renvoyant sont degres le plus haut
	 * @return degMax - renvoie le degres max du polynome sous la forme d'un int 
	 */
	public int degres() {
	    int degMax = 0;
	    String monomeDegMax;
	    for (int indiceDuMonome = 0; indiceDuMonome < this.monomes.size(); indiceDuMonome++) {

	        String monomeActuel = this.monomes.get(indiceDuMonome);

	        // Pattern qui cherche ^ suivi d'un ou plusieurs chiffres
	        Pattern patternDegres = Pattern.compile("\\^(\\d+)");
	        Matcher matcherDegres = patternDegres.matcher(monomeActuel);

	        if (matcherDegres.find()) {
	            // group(1) récupère uniquement les chiffres après le ^
	            int DegresActuel = Integer.parseInt(matcherDegres.group(1));

	            if (DegresActuel > degMax) {
	                degMax    = DegresActuel;
	                monomeDegMax = monomeActuel;
	            }
	        }
	    }
		return degMax;
	}
	
	/*
	 * 
	 */
	public ArrayList<String> coefficient() {
		ArrayList<String> coefficient = new ArrayList<>();
		for (int indiceDuMonome = 0; indiceDuMonome < this.monomes.size(); indiceDuMonome++) {
			String monomeActuel = this.monomes.get(indiceDuMonome);
			monomeActuel = monomeActuel.replaceAll("x.*", "");
			
	        if (monomeActuel.isEmpty()) {
	            monomeActuel = "1";
	        } else if (monomeActuel.equals("-")) {
	            monomeActuel = "-1";
	        }
			
			coefficient.add(monomeActuel);
		}
		
		return coefficient;
	}
	
	/*
	 * 
	 */
	public ArrayList<Integer> racine() {
		return null;	//STUB
	}
	
	/* Méthode permettant de déterminer la limite d'un polynome en + ou - l'infini
	 * @param limiteChercher  - paramettre indiquant quelle limite on cherche à trouver (+ ou - l'infini) 
	 * @return resultatLimite - renvoie "+Infini" ou "-Infini" en fonction 
	 * 							de la limite du polynome (cas simple) 
	 */
	public String limite(char limiteChercher) {
		
	    if (limiteChercher != '+' && limiteChercher != '-') {
	        throw new IllegalArgumentException("Erreur : saisie != '+' || '-'");
	    }

	    int degMax      = 0,
	        indexDegMax = 0;


	    String resultatLimite;

	    // Récupération du DegMax et de l'indexDegMax
	    for (int indiceMonomeActuel = 0; indiceMonomeActuel < this.monomes.size(); indiceMonomeActuel++) {
	        Pattern patternDegres = Pattern.compile("\\^(\\d+)");
	        Matcher matcherDegres = patternDegres.matcher(this.monomes.get(indiceMonomeActuel));
	        
	        if (matcherDegres.find()) {
	            int DegresActuel = Integer.parseInt(matcherDegres.group(1));
	            if (DegresActuel > degMax) {
	                degMax      = DegresActuel;
	                indexDegMax = indiceMonomeActuel;
	            }
	        }
	    }
	    
	    // convertion String -> int pour conparaison
        int coefficientDuMonomeDegresMax = Integer.parseInt(this.coefficient().get(indexDegMax));

	    // Détermination de la limite
	    resultatLimite = (limiteChercher == '+') ? (coefficientDuMonomeDegresMax > 0) ? "+Infini" : "-Infini" 
	    										 : (degMax % 2 == 0) ? (coefficientDuMonomeDegresMax > 0) ? "+Infini" : "-Infini" 
	    											                 : (coefficientDuMonomeDegresMax > 0) ? "-Infini" : "+Infini";
	    /*
	    if (limiteChercher == '+') {
	        // En +infini : signe du coefficient positif
	        resultatLimite = (coefficientDuMonomeDegresMax > 0) ? "+Infini" : "-Infini";
	    } else {
	        // En -infini : dépend du signe ET de la parité du degré
	        if (degMax % 2 == 0) {
	            // Degré pair : même comportement qu'en +infini
	            resultatLimite = (coefficientDuMonomeDegresMax > 0) ? "+Infini" : "-Infini";
	        } else {
	            // Degré impair : comportement opposé qu'en +infini
	            resultatLimite = (coefficientDuMonomeDegresMax > 0) ? "-Infini" : "+Infini";
	        }
	    }
	    */
	    
	    return resultatLimite;
	}

	
    /**
     * Getter
     * Méthode imuable de récupération de monomes à partir d'un objet polynome
     * ex : Polynome(3x^3 +5x- 10).getMonome() -> [3x^3, 5x, -10]
     * @return - liste des monomes du polynomes
     */
	public  ArrayList<String> getMonomes() {
	    return this.monomes;
	}	
	
	/* Setter
	 * Met à jour la liste 'monomes' en ajoutant à l'indice donnée le monome souhaité 
	 * @param monomeAAjouter - monome que l'on souhaite insérer
	 * @param indiceList - indice de la liste oû placer le monome à ajouter
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
