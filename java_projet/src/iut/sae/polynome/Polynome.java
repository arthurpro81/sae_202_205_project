/**
 * Polynome.java											20/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Polynome de la forme Ax^a+Bx^b+Cx^c+[...]+d
 * @author Rémi Gavens, Amaury Daveau, Leo Gomez, Arthur Chamayou--Valentin, Florian Ferreira
 * @version beaucoup trop, aled...
 */
public class Polynome {
	
	//Liste oû sera stocker les monômes récupéré grace à la méthode getMonomes()
	private ArrayList<String> monomes = new ArrayList<>();

    // polynome saisie par l'utilisateur sous le format : 3x^2 + 7x^1 +5
    private String polynome;

    /**
     * Constructeur de la classe Polynome.
     * Valide et initialise un polynôme à partir d'une chaîne de caractères.
     * Le format accepté est : [-]ax^A [+|-] bx^B [+|-] ... avec espaces optionnels.
     * Exemples valides : "3x^2+2x-1", "-x^3+5", "7x", "42".
     *
     * @param polynome chaîne représentant le polynôme à créer
     * @throws IllegalArgumentException si le format est invalide
     */
    public Polynome(String polynome) {
        String regex = "^\\s?-?(?:\\d*x(?:\\^\\d+)?|\\d+)(?:\\s?[+\\-]\\s?(?:\\d*x(?:\\^\\d+)?|\\d+))*$";

        if (!polynome.matches(regex)) {
            throw new IllegalArgumentException("Erreur : format invalide. Ex: ax^A+bx^B+cx^C+dx^D+[...]");
        }
        
        this.polynome = polynome;
        this.remplirMonomes();
        this.simplifierMonome();
        this.reconstructionPoly();
    }

   /*
    * Méthode privé appellé uniquement par le constructeur
    * Méthode permétant de construire un polynome (type String) 
    * grâce à une liste de monome (type ArrayList<Integer>)
    */
	private void reconstructionPoly() {
		String polyReconstruit = "";
		for(String monome : this.monomes) {
			polyReconstruit += (monome.startsWith("-")) ? monome 
														: "+" + monome;
		}
		this.polynome = (polyReconstruit.startsWith("+")) ? polyReconstruit.substring(1) 
														  : polyReconstruit;
	}


	/*
	 * Méthode privé appellé par le constructeur uniquement
	 * Méthode permétant de canonisé une liste de monome
	 * -> un monome = degrés unique (deux monomes ne peuvent pas avoir le meme deg) 
	 */
    private void simplifierMonome() {
    	int indiceMonomeActuel = 0;
    	while(indiceMonomeActuel < this.monomes.size() -1) {
    		int degActuel = getDegres(this.monomes.get(indiceMonomeActuel));
    		int degSuivant = getDegres(this.monomes.get(indiceMonomeActuel +1));
    		
    		if (degActuel == degSuivant) {
    			int sommeCoeff = getCoefficient(this.monomes.get(indiceMonomeActuel))
    						   + getCoefficient(this.monomes.get(indiceMonomeActuel +1));
    			
    			this.monomes.remove(indiceMonomeActuel +1);
    			this.monomes.remove(indiceMonomeActuel);
    			
    			if (sommeCoeff != 0) {
    				if(degActuel != 0) {
    					this.monomes.add(indiceMonomeActuel, sommeCoeff + "x^" + degActuel);
    				} else {
    					this.monomes.add(indiceMonomeActuel, sommeCoeff + "");
    				}
    			}
    		} else {
    			indiceMonomeActuel++;
    		}
    	}
    	
    	if(this.monomes.isEmpty()) {
    		this.monomes.add("0");
    	}
	}



	 /**
      * Méthode privée appelée une seule fois par le constructeur.
      * Décompose le polynôme en une liste de monômes stockée dans this.monomes.
      * Les espaces sont supprimés avant le traitement.
      * Le signe '-' est conservé en préfixe du monôme suivant.
      * Les entrées vides éventuelles sont supprimées en fin de traitement.
      *
      * Exemple : "3x^2 + 2x - 10" produit ["3x^2", "2x", "-10"]
      */
	private void remplirMonomes() {
		   String stockageDeCaractere = "";
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
		   this.monomes.remove("");
		   
		   // trie des monomes en fonction de leurs degres décroissant
		   
		   for (int indiceMonomeActuel = 1; indiceMonomeActuel < this.monomes.size(); indiceMonomeActuel++) {
			    String monomeAInserer = this.monomes.get(indiceMonomeActuel);
			    int degresMonomeAInserer    = getDegres(monomeAInserer);
			    int indiceMonomeGauche = indiceMonomeActuel - 1;

			    // Remonte monomeAInserer tant que son degré est supérieur à celui du voisin gauche
			    while (indiceMonomeGauche >= 0 && getDegres(this.monomes.get(indiceMonomeGauche)) < degresMonomeAInserer) {
			        this.monomes.set(indiceMonomeGauche + 1, this.monomes.get(indiceMonomeGauche));
			        indiceMonomeGauche--;
			    }
			    this.monomes.set(indiceMonomeGauche + 1, monomeAInserer);
			}
	}
	
	/**
	 * Retourne le degré d'un monôme String sous forme d'un int.
	 * "x" et "ax" sont traités comme degré 1, les constantes comme degré 0.
	 */
	private int getDegres(String monome) {
		int degresMonome;
	    // convertion ax -> ax^1
	    Pattern patternSansDegre = Pattern.compile("^-?\\d*x$");
	    if (patternSansDegre.matcher(monome).find()) {
	        monome = monome.replaceAll("x.*", "x^1");
	    }
	    // récupération du degres du monome
	    Pattern patternDegre = Pattern.compile("\\^(\\d+)");
	    Matcher matcher = patternDegre.matcher(monome);
	    if (matcher.find()) {
	        degresMonome = Integer.parseInt(matcher.group(1));
	    } else {
	    	degresMonome = 0;
	    }
	    return degresMonome;
	}
	
	/**
	 * Retourne le coefficient d'un monôme String sous forme d'un int.
	 * "x" et "ax" sont traités comme degré 1, les constantes comme degré 0.
	 */
	private int getCoefficient(String monome) {
		int coeffMonome;
	    String coeffString = monome.replaceAll("x.*", "");
	        
	    coeffMonome = (coeffString.isEmpty()) ? 1 : (coeffString.equals("-")) ? -1 : Integer.parseInt(coeffString);
	    return coeffMonome;
	}
	

	/**
	 * retourne le degré le plus élevé du polynôme.
	 * Les monômes de la forme 'Ax' (sans exposant écrit) sont traités comme 'Ax^1'.
	 * Retourne 0 si le polynôme est une constante pure.
	 *
	 * @return le degré maximal du polynôme sous forme d'entier
	 */
	public int MaxDegres() {
	    return getDegres(this.monomes.get(0));
	}
	
	/**
	 * Extrait et retourne la liste des coefficients du polynôme dans l'ordre des monômes.
	 * Un coefficient implicite (ex : "x^2") est traité comme 1.
	 * Un coefficient négatif implicite (ex : "-x^3") est traité comme -1.
	 *
	 * @return ArrayList<Integer> des coefficients dans l'ordre des monômes
	 */
	public ArrayList<Integer> coefficient() {
		ArrayList<Integer> coefficient = new ArrayList<>();
		
		for (String monomeActuel : this.monomes) {
			coefficient.add(getCoefficient(monomeActuel));
		}
		return coefficient;
	}
	
	/*
	 * TODO à faire à la fin
	 */
	public ArrayList<Integer> racine() {
		return null;	//STUB
	}
	
	/**
	 * Détermine la limite du polynôme en +infini ou -infini.
	 * La limite est calculée à partir du signe du coefficient dominant
	 * et de la parité du degré dominant selon les règles suivantes :
	 *   - En +infini               : signe du coefficient dominant
	 *   - En -infini, degré pair   : même signe que le coefficient dominant
	 *   - En -infini, degré impair : signe opposé au coefficient dominant
	 *   - Constante pure           : retourne la valeur de la constante
	 *
	 * @param limiteChercher '+' pour la limite en +infini, '-' pour la limite en -infini
	 * @return "+Infini" ou "-Infini" selon la limite calculée, ou la valeur si constante
	 * @throws IllegalArgumentException si limiteChercher est différent de '+' et '-'
	 */
	public String limite(char limiteChercher) {
	    if (limiteChercher != '+' && limiteChercher != '-') {
	        throw new IllegalArgumentException("Erreur : saisie != '+' || '-'");
	    }
	    
	    //Récupération du degres et du coef du monome au plus haut degres (ranger à l'indice 0)
	    int degMax = getDegres(this.monomes.get(0));
	    int coefficientMonome = this.coefficient().get(0);
	    String message;
	    
	    // monome = constante
	    if (degMax == 0) {
	    	message = this.monomes.get(0);
	    } else {
	    	
	    	//Vérification signe du coeff et si le degres et paire
		    boolean coeffPositif = coefficientMonome > 0;
		    boolean degresPair   = degMax % 2 == 0;
	
		    if (limiteChercher == '+') {
		        message = coeffPositif ? "+Infini" : "-Infini";
		    } else {
		        message = (degresPair == coeffPositif) ? "+Infini" : "-Infini";
		    }
	    }
	    return message;
	}
	
	/**
	 * Calcule la dérivée d'ordre n du polynôme et retourne la liste de ses monômes.
	 * Applique les règles de dérivation standard :
	 *   - d/dx(Ax^b) = A*b * x^(b-1)
	 *   - d/dx(Ax)   = A
	 *   - d/dx(A)    = 0  (terme ignoré dans le résultat)
	 * Les coefficients implicites (ex : "x", "-x") sont traités comme 1 et -1.
	 * Cette méthode ne calcule pas la primitive (exposantDerive doit être strictement positif).
	 *
	 * @param exposantDerive ordre de la dérivation (1 = f', 2 = f'', ...)
	 * @return ArrayList<String> des monômes du polynôme dérivé
	 * @throws IllegalArgumentException si exposantDerive est inférieur ou égal à 0
	 */
	public ArrayList<String> derive(int exposantDerive) {
		//On n'autorise pas le calcule des primitives
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
	                //Cas Ax^b -> A*b x^(b-1)
	                String coeffString = matcherAvecExposant.group(1); // groupe 1 : coefficient
	                String degString   = matcherAvecExposant.group(2); // groupe 2 : exposant

	                // Gestion coefficient implicite 
	                int coeff = coeffString.isEmpty()        ? 1
	                          : coeffString.equals("-")      ? -1
	                          : Integer.parseInt(coeffString);

	                int deg          = Integer.parseInt(degString);
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
	 * Retourne la liste des monômes du polynôme.
	 * Cette liste est construite une seule fois par le constructeur et ne change pas.
	 * Ne pas modifier la liste retournée directement depuis l'extérieur de la classe.
	 *
	 * Exemple : Polynome("3x^2+2x-1").getMonomes() produit ["3x^2", "2x", "-1"]
	 *
	 * @return ArrayList<String> des monômes dans l'ordre de saisie
	 */
	public  ArrayList<String> getMonomes() {
	    return this.monomes;
	}	
	
	/**
	 * Remplace un monôme dans la liste interne à l'indice spécifié.
	 * Aucune validation du format du monôme n'est effectuée.
	 * A utiliser avec précaution pour ne pas corrompre la cohérence de l'objet.
	 *
	 * @param monomeAAjouter le monôme sous forme String à insérer
	 * @param indiceList     l'indice (base 0) de la position à remplacer dans this.monomes
	 */
	public void setMonomes(String monomeAAjouter, int indiceList) {
		this.monomes.set(indiceList, monomeAAjouter);
	}
	

	/**
	 * Retourne la représentation textuelle du polynôme.
	 * Correspond exactement à la chaîne originale saisie lors de la construction,
	 * espaces inclus.
	 *
	 * @return String le polynôme sous sa forme de saisie originale
	 */
    @Override
	public String toString() {
		 return this.polynome;
	}	
}
