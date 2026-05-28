/**
 * Polynome.java											20/05/2026
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
    	    String stockageDeCaractere = "";
    	    char recuperationDeCaractereActuel;
    	    String polyNoSpace = this.polynome.replaceAll("\\s+", "");
    	    
    	    for (int parcoursDeCaractere = 0; parcoursDeCaractere < polyNoSpace.length(); parcoursDeCaractere++) {
    	        recuperationDeCaractereActuel = polyNoSpace.charAt(parcoursDeCaractere);
    	        
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
    	    
    	    this.monomes.remove("");
    	    
    	    
    	}

	/* Méthode recherchant les degres d'un objet Polynome() et renvoyant sont degres le plus haut
	 * @return degMax - renvoie le degres max du polynome sous la forme d'un int 
	 */
	public int degres() {
	    int degMax = 0;
	    for (int indiceDuMonome = 0; indiceDuMonome < this.monomes.size(); indiceDuMonome++) {

	        String monomeActuel = this.monomes.get(indiceDuMonome);

	        // Pattern qui cherche ^ suivi d'un ou plusieurs chiffres
	        Pattern patternDegres = Pattern.compile("\\^(\\d+)");
	        Matcher matcherDegres = patternDegres.matcher(monomeActuel);

	        if (matcherDegres.find()) {
	            // group(1) récupère uniquement les chiffres après le ^
	            int DegresActuel = Integer.parseInt(matcherDegres.group(1));

	            if (DegresActuel > degMax) {
	            	String monomeDegMax;
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
	public ArrayList<Integer> coefficient() {
		ArrayList<Integer> coefficient = new ArrayList<>();
		for (int indiceDuMonome = 0; indiceDuMonome < this.monomes.size(); indiceDuMonome++) {
			String monomeActuel = this.monomes.get(indiceDuMonome);
			monomeActuel = monomeActuel.replaceAll("x.*", "");
			
	        if (monomeActuel.isEmpty()) {
	            monomeActuel = "1";
	        } else if (monomeActuel.equals("-")) {
	            monomeActuel = "-1";
	        }
	        int coefficientMonomeActuel = Integer.parseInt(monomeActuel);
			coefficient.add(coefficientMonomeActuel);
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
        int coefficientMonomeDegrMax = this.coefficient().get(indexDegMax);

	    // Détermination de la limite
	    resultatLimite = (limiteChercher == '+') ? (coefficientMonomeDegrMax > 0) ? "+Infini" : "-Infini" 
	    										 : (degMax % 2 == 0) ? (coefficientMonomeDegrMax > 0) ? "+Infini" : "-Infini" 
	    											                 : (coefficientMonomeDegrMax > 0) ? "-Infini" : "+Infini";
	    
	    return resultatLimite;
	}
	
	/* Méthode calculant la dérivé d'un objet Polynome et renoyant une ArrayList<String> de monomes du polynome dérivé.
	 * Cette Méthode ne permet pas de calculer la primitive d'un objet Polynome. 
	 * @param exposantDerive - exposant de la deriver du polynome dériver - EX: f'(f'(p)) -> exposant = 2
	 * @return - renvoie une la liste des monomes du polynome dérivé
	 */
	public ArrayList<String> derive(int exposantDerive) {
	    if (exposantDerive <= 0) {
	        throw new IllegalArgumentException("Erreur : parametre <= 0");
	    }

	    ArrayList<String> monomesDerive = new ArrayList<>(this.monomes);

	    // Boucle de répétition (f'', f''', etc.)
	    for (int repetition = 0; repetition < exposantDerive; repetition++) {

	        ArrayList<String> monomesDeriveCourant = new ArrayList<>();

	        // Pattern capturant coefficient (groupe 1) et exposant (groupe 2)
	        Pattern patternAvecExposant = Pattern.compile("(-?\\d*)x\\^(\\d+)");
	        // Pattern capturant coefficient d'un monôme de la forme Ax (sans ^)
	        Pattern patternSansExposant = Pattern.compile("(-?\\d*)x$");

	        for (int parcourMonome = 0; parcourMonome < monomesDerive.size(); parcourMonome++) {
	            String monomeCourant = monomesDerive.get(parcourMonome);

	            Matcher matcherAvecExposant = patternAvecExposant.matcher(monomeCourant);
	            Matcher matcherSansExposant = patternSansExposant.matcher(monomeCourant);

	            if (matcherAvecExposant.find()) {
	                // --- Cas Ax^b → A*b x^(b-1) ---
	                String coeffStr = matcherAvecExposant.group(1); // groupe 1 : coefficient
	                String degStr   = matcherAvecExposant.group(2); // groupe 2 : exposant

	                // Gestion coefficient implicite (ex: "x^2" → coeffStr = "")
	                int coeff = coeffStr.isEmpty()        ? 1
	                          : coeffStr.equals("-")      ? -1
	                          : Integer.parseInt(coeffStr);

	                int deg          = Integer.parseInt(degStr);
	                int nouveauCoeff = coeff * deg;
	                int nouveauDeg   = deg - 1;

	                // Reconstruction
	                String monomeDerive;
	                if (nouveauDeg == 0) {
	                    monomeDerive = String.valueOf(nouveauCoeff); // constante
	                } else if (nouveauDeg == 1) {
	                    monomeDerive = nouveauCoeff + "x";           // Ax
	                } else {
	                    monomeDerive = nouveauCoeff + "x^" + nouveauDeg; // Ax^b
	                }
	                monomesDeriveCourant.add(monomeDerive);

	            } else if (matcherSansExposant.find()) {
	                //Cas Ax → A (la dérivée de Ax est A)
	                String coeffStr  = matcherSansExposant.group(1);
	                int coeff = coeffStr.isEmpty()   ? 1
	                          : coeffStr.equals("-") ? -1
	                          : Integer.parseInt(coeffStr);
	                monomesDeriveCourant.add(String.valueOf(coeff));

	            }
	            //constante -> dérivée = 0, on ne l'ajoute pas
	        }
	        monomesDerive = monomesDeriveCourant;
	    }
	    return monomesDerive;
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
	 * Calcule le produit de deux polynômes.
	 * Ex : (3x^2 + 2x + 1) * (x + 2) → 3x^3 + 8x^2 + 5x + 2
	 * @param autrePolynome - le second polynôme à multiplier avec this
	 * @return ArrayList<String> - liste des monômes du polynôme résultant
	 */
	public ArrayList<String> multiplication(Polynome autrePolynome) {

	    ArrayList<String> monomesA = this.monomes;
	    ArrayList<String> monomesB = autrePolynome.getMonomes();

	    int degMaxResultat = this.degres() + autrePolynome.degres();
	    int[] coeffsResultat = new int[degMaxResultat + 1];

	    Pattern patternAvecExposant = Pattern.compile("(-?\\d*)x\\^(\\d+)");
	    Pattern patternSansExposant = Pattern.compile("(-?\\d*)x$");

	    for (String monomeA : monomesA) {
	        for (String monomeB : monomesB) {
	            int coeffA = extraireCoefficient(monomeA, patternAvecExposant, patternSansExposant);
	            int coeffB = extraireCoefficient(monomeB, patternAvecExposant, patternSansExposant);
	            int degA   = extraireDegre(monomeA, patternAvecExposant, patternSansExposant);
	            int degB   = extraireDegre(monomeB, patternAvecExposant, patternSansExposant);

	            coeffsResultat[degA + degB] += coeffA * coeffB;
	        }
	    }

	    ArrayList<String> monomesResultat = new ArrayList<>();

	    for (int deg = degMaxResultat; deg >= 0; deg--) {
	        int coeff = coeffsResultat[deg];
	        if (coeff == 0) continue;

	        String monome;
	        if (deg == 0) {
	            monome = String.valueOf(coeff);
	        } else if (deg == 1) {
	            monome = coeff + "x";
	        } else {
	            monome = coeff + "x^" + deg;
	        }
	        monomesResultat.add(monome);
	    }

	    return monomesResultat;
	}

	private int extraireCoefficient(String monome, Pattern avecExp, Pattern sansExp) {
	    Matcher m1 = avecExp.matcher(monome);
	    Matcher m2 = sansExp.matcher(monome);

	    String coeffStr;
	    if (m1.find()) {
	        coeffStr = m1.group(1);
	    } else if (m2.find()) {
	        coeffStr = m2.group(1);
	    } else {
	        return Integer.parseInt(monome);
	    }

	    return coeffStr.isEmpty()   ? 1
	         : coeffStr.equals("-") ? -1
	         : Integer.parseInt(coeffStr);
	}

	private int extraireDegre(String monome, Pattern avecExp, Pattern sansExp) {
	    Matcher m1 = avecExp.matcher(monome);
	    if (m1.find()) {
	        return Integer.parseInt(m1.group(2));
	    }
	    Matcher m2 = sansExp.matcher(monome);
	    if (m2.find()) {
	        return 1;
	    }
	    return 0;
	}
	

    /**
     * Affichage du polynôme.
     * Format : Polynome(3x^2+2x^1+10).toString() -> 3x^2+2x^1+10
     * @return messageAffiche - affichage du polynome sous Format 
     */
    @Override
	public String toString() {
		 String messageAffiche = this.polynome;
		 return messageAffiche;
	}	
}