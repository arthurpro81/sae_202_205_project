/**
 * Polynome.java											20/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Polynome de la forme Ax^a+Bx^b+Cx^c+[...]+d ainsi que toutes les fonctionnalités annexes
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
     * Valide et initialise et canonise un polynôme à partir d'une chaîne de caractères.
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
        this.polynome = reconstructionPoly(this.monomes);
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
		   //TODO remplacer par le trie à bulle de leo
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
	
   /*
    * Méthode privé appellé uniquement par le constructeur et dans la division
    * Méthode permétant de construire un polynome (type String) 
    * grâce à une liste de monome (type ArrayList<Integer>)
    */
	private String reconstructionPoly(ArrayList<String> monomes) {
		String polyReconstruit = "";
		for(String monome : monomes) {
			polyReconstruit += (monome.startsWith("-")) ? monome 
														: "+" + monome;
		}
		return polyReconstruit = (polyReconstruit.startsWith("+")) ? polyReconstruit.substring(1) 
														  : polyReconstruit;
	}
	
	/**
	 * Méthode privé appellé uniquement par pour les calcules polynomiale
	 * Construit la représentation String d'un monôme depuis son coefficient et son degré.
	 * Gère le signe pour l'intégration dans une somme (préfixe '+' ou '-').
	 */
	private String construireMonomeString(int coeff, int deg) {
	    String monome;
	    if (deg == 0)      monome = String.valueOf(coeff);
	    else if (deg == 1) monome = coeff + "x";
	    else               monome = coeff + "x^" + deg;

	    return monome.startsWith("-") ? monome : "+" + monome;
	}
	
	/**
	 * Cherche dans la liste de monômes l'indice du premier monôme
	 * ayant le même degré que celui passé en paramètre.
	 * Retourne -1 si aucun monôme de ce degré n'est trouvé.
	 *
	 * @param monomes   liste de monômes dans laquelle chercher
	 * @param degCherche degré recherché
	 * @return l'indice du monôme trouvé, ou -1
	 */
	private int trouverIndiceMemeDegre(ArrayList<String> monomes, int degCherche) {
	    for (int indice = 0; indice < monomes.size(); indice++) {
	        if (getDegres(monomes.get(indice)) == degCherche) {
	            return indice;
	        }
	    }
	    return -1;
	}
	
	/**
	 * Distribue un monôme sur tous les monômes de autrePolynome
	 * et accumule les produits dans la chaîne somme.
	 * Responsabilité : une ligne de la distributivité.
	 *
	 * @param monomeThis    le monôme de this à distribuer
	 * @param autrePolynome le polynôme sur lequel distribuer
	 * @return la chaîne somme mise à jour avec les nouveaux produits
	 */
	private String distribuerMonome(String monomeThis, Polynome autrePolynome) {
		String somme = "";
	    for (int indiceAutre = 0; indiceAutre < autrePolynome.getMonomes().size(); indiceAutre++) {
	        String monomeAutre  = autrePolynome.getMonomes().get(indiceAutre);
	        String monomeProduit = multiplierDeuxMonomes(monomeThis, monomeAutre);
	        if(monomeProduit.startsWith("+")) {monomeProduit = monomeProduit.substring(1);}
	        somme += (monomeProduit.startsWith("-")) ? monomeProduit : "+" + monomeProduit;
	    }
	    return somme;
	}
	
	/**
	 * Multiplie deux monômes et retourne le monôme résultat sous forme de String.
	 * Les coefficients sont multipliés et les degrés additionnés.
	 * Exemple : "3x^2" × "5x" → "15x^3"
	 *
	 * @param monome1 premier monôme
	 * @param monome2 second monôme
	 * @return le monôme produit sous forme de String
	 */
	private String multiplierDeuxMonomes(String monome1, String monome2) {
	    int coeffResultat = getCoefficient(monome1) * getCoefficient(monome2);
	    int degResultat   = getDegres(monome1)      + getDegres(monome2);
	    return construireMonomeString(coeffResultat, degResultat);
	}
	
    /**
     * Divise deux monômes et retourne le monôme quotient sous forme de String.
     * Le coefficient résultant est la division entière des deux coefficients.
     * Le degré résultant est la soustraction des deux degrés.
     * Exemple : "6x^3" ÷ "2x" → coeffResultat=3, degResultat=2 → "+3x^2"
     *
     * @param monome1 monôme dividende
     * @param monome2 monôme diviseur
     * @return le monôme quotient sous forme de String avec signe préfixe
     */
    private String diviserDeuxMonomes(String monome1, String monome2) {
        int coeffResultat = getCoefficient(monome1) / getCoefficient(monome2);
        int degResultat   = getDegres(monome1)      - getDegres(monome2);
        return construireMonomeString(coeffResultat, degResultat);
    }
	

	/** Applicable sur un Object Polynome()
	 * retourne le degré le plus élevé du polynôme.
	 * Les monômes de la forme 'Ax' (sans exposant écrit) sont traités comme 'Ax^1'.
	 * Retourne 0 si le polynôme est une constante pure.
	 *
	 * @return le degré maximal du polynôme sous forme d'entier
	 */
	public int MaxDegres() {
	    return getDegres(this.monomes.get(0));
	}
	
	/** Applicable sur un Object Polynome()
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
	
	/* Applicaable sur un Object Polynome()
	 * retourne une ArrayList des racines du polynome sous la forme de Double
	 * Si aucunes racine alors renvoie une ArrayList vide
	 * @return listeRacine ArrayList<Double> des racines
	 * TODO Faire La méthode apres le ccalcule polynomiale
	 */
	public ArrayList<Double> racine() {
		return null;	//STUB
	}
	
	/** Applicable sur un Object Polynome()
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
	
	/** Applicable sur un Object Polynome()
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
	
	/** Applicable sur un Object Polynome()
	 * Retourne la liste des monômes du polynôme.
	 * Cette liste est construite une seule fois par le constructeur et ne change pas.
	 * Ne pas modifier la liste retournée directement depuis l'extérieur de la classe.
	 *
	 * Exemple : Polynome("3x^2+2x-1").getMonomes() produit ["3x^2", "2x", "-1"]
	 *
	 * @return ArrayList<String> des monômes dans l'ordre de saisie
	 */
	public ArrayList<String> getMonomes() {
	    return this.monomes;
	}	
	
	
	/** Applicable sur un monome String
	 * Retourne le degré d'un monôme sous forme d'un int.
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
	
	/** Applicable sur un monome String
	 * Retourne le coefficient d'un monôme sous forme d'un int.
	 * "x" et "ax" sont traités comme degré 1, les constantes comme degré 0.
	 */
	private int getCoefficient(String monome) {
		int coeffMonome;
	    String coeffString = monome.replaceAll("x.*", "");
	        
	    coeffMonome = (coeffString.isEmpty()) ? 1 : (coeffString.equals("-")) ? -1 : Integer.parseInt(coeffString);
	    return coeffMonome;
	}
	
	
	/*CALcULE pOlinomiale*/
	
	/** Applicable sur un objet Polynome
	 * TODO Faire la JavaDoc
	 * 
	 */
	public String addition(Polynome autrePolynome) {
		String strSommePoly = "";
		//si l'indice d'un monome de autrePolynome ne fait pas partie de la liste alors il faudra l'ajouter à strSommePoly
		ArrayList<Integer> indiceAutrePolyDejaTrouver = new ArrayList<>();
		
		//Boucles recherchant les monomes au degres communs de this et de autrePolynome
		//Boucle passant en revue les monomes de this
		for (int indiceThis=0; indiceThis < this.getMonomes().size();indiceThis++) {
			String monomeThis = this.getMonomes().get(indiceThis);
			int degMonomeThis = getDegres(monomeThis);
			int coeffThis = getCoefficient(monomeThis);
			
			//ajout de l'indice du monome de autrePolynome à la liste des monomes de degres commun
			int indicePartenaire = trouverIndiceMemeDegre(autrePolynome.getMonomes(), degMonomeThis);
			
			//Degres communs -> addition coeff
	        if (indicePartenaire != -1) {
	        	indiceAutrePolyDejaTrouver.add(indicePartenaire);
	            int coeffAutre = getCoefficient(autrePolynome.getMonomes().get(indicePartenaire));
	            int coeffSomme = coeffThis + coeffAutre;

	            if (coeffSomme != 0) {
	                strSommePoly += construireMonomeString(coeffSomme, degMonomeThis);
	            }
	        } else {
	            // Pas de partenaire -> monôme de this ajouté seul
	            strSommePoly += monomeThis.startsWith("-") ? monomeThis : "+" + monomeThis;
	        }
		}
		
		// Pas de partenaire -> monôme de autrePolynome ajouté seul
	    for (int indiceAutre = 0; indiceAutre < autrePolynome.getMonomes().size(); indiceAutre++) {
	        if (!indiceAutrePolyDejaTrouver.contains(indiceAutre)) {
	            String monomeAutre = autrePolynome.getMonomes().get(indiceAutre);
	            strSommePoly += monomeAutre.startsWith("-") ? monomeAutre : "+" + monomeAutre;
	        }
	    }
	    
	    //nettoyage du + devant le polynome String
	    if (strSommePoly.startsWith("+")) {
	        strSommePoly = strSommePoly.substring(1);
	    }
	    
	    //Si la somme des 2 poly = 0
	    if (strSommePoly.isEmpty()) {
	        strSommePoly = "0";
	    }

	    return strSommePoly;
		
	}
	
	/** Applicable sur un objet Polynome
	 * TODO Faire la JavaDoc
	 * 
	 */
	public String soustraction(Polynome autrePolynome) {
		String strSommePoly = "";
		//si l'indice d'un monome de autrePolynome ne fait pas partie de la liste alors il faudra l'ajouter à strSommePoly
		ArrayList<Integer> indiceAutrePolyDejaTrouver = new ArrayList<>();
		
		//Boucles recherchant les monomes au degres communs de this et de autrePolynome
		//Boucle passant en revue les monomes de this
		for (int indiceThis=0; indiceThis < this.getMonomes().size();indiceThis++) {
			String monomeThis = this.getMonomes().get(indiceThis);
			int degMonomeThis = getDegres(monomeThis);
			int coeffThis = getCoefficient(monomeThis);
			
			//ajout de l'indice du monome de autrePolynome à la liste des monomes de degres commun
			int indicePartenaire = trouverIndiceMemeDegre(autrePolynome.getMonomes(), degMonomeThis);
			
			//Degres communs -> addition coeff
	        if (indicePartenaire != -1) {
	        	indiceAutrePolyDejaTrouver.add(indicePartenaire);
	            int coeffAutre = getCoefficient(autrePolynome.getMonomes().get(indicePartenaire));
	            int coeffSomme = coeffThis - coeffAutre;

	            if (coeffSomme != 0) {
	                strSommePoly += construireMonomeString(coeffSomme, degMonomeThis);
	            }
	        } else {
	            // Pas de partenaire -> monôme de this ajouté seul
	            strSommePoly += monomeThis.startsWith("-") ? monomeThis : "+" + monomeThis;
	        }
		}
		
		// Pas de partenaire -> monôme de autrePolynome ajouté seul
	    for (int indiceAutre = 0; indiceAutre < autrePolynome.getMonomes().size(); indiceAutre++) {
	        if (!indiceAutrePolyDejaTrouver.contains(indiceAutre)) {
	            String monomeAutre = autrePolynome.getMonomes().get(indiceAutre);
	            //inversion du signe des monomes
	            monomeAutre = monomeAutre.startsWith("-") ? monomeAutre.substring(1) : "-" + monomeAutre;
	            strSommePoly += monomeAutre.startsWith("-") ? monomeAutre : "+" + monomeAutre;
	        }
	    }
	    
	    //nettoyage du + devant le polynome String
	    if (strSommePoly.startsWith("+")) {
	        strSommePoly = strSommePoly.substring(1);
	    }
	    
	    //Si la somme des 2 poly = 0
	    if (strSommePoly.isEmpty()) {
	        strSommePoly = "0";
	    }

	    return strSommePoly;
	}
	
	/**
	 * Multiplie this par autrePolynome par distributivité.
	 * Chaque monôme de this est multiplié par chaque monôme de autrePolynome.
	 * Les termes de même degré sont regroupés automatiquement par le constructeur
	 * lors de la création du polynôme résultat.
	 *
	 * @param autrePolynome le polynôme multiplicateur
	 * @return String représentant le polynôme produit, canonisable via new Polynome()
	 */
	public String multiplication(Polynome autrePolynome) {
	    String strProduitPoly = "";

	    //distributivité de chaque monôme de this × chaque monôme de autrePolynome
	    for (int indiceThis = 0; indiceThis < this.monomes.size(); indiceThis++) {
	        String monomeThis = this.monomes.get(indiceThis);

	        strProduitPoly += distribuerMonome(monomeThis, autrePolynome);
	    }

	    //nettoyage du + devant le polynome String
	    if (strProduitPoly.startsWith("+")) {
	        strProduitPoly = strProduitPoly.substring(1);
	    }

	    //Si 1 des 2 poly est nul
	    if (strProduitPoly.isEmpty()) {
	        strProduitPoly = "0";
	    }

	    return strProduitPoly;
	}
	
	
	/**
	 * Divise this par autrePolynome par division euclidienne.
	 * Retourne le quotient sous forme de String canonisable via new Polynome().
	 * Si la division n'est pas exacte, le reste est ignoré.
	 * Le polynôme étant canonisé, le monôme dominant est toujours à l'indice 0.
	 *
	 * @param autrePolynome le polynôme diviseur
	 * @return String représentant le quotient
	 * @throws IllegalArgumentException si le degré du diviseur est supérieur à celui du dividende
	 */
	public String[] division(Polynome autrePolynome) {

	    if (autrePolynome.MaxDegres() > this.MaxDegres()) {
	        throw new IllegalArgumentException(
	            "Erreur : degré du diviseur supérieur au degré du dividende"
	        );
	    }

	    String strQuotient             = "";
	    ArrayList<String> resteCourant = new ArrayList<>(this.monomes);
	    String termeDominantDiviseur   = autrePolynome.getMonomes().get(0);

	    while (!resteCourant.isEmpty()
	            && getDegres(resteCourant.get(0)) >= autrePolynome.MaxDegres()) {

	        // Étape 1 : terme du quotient
	        String termeQuotient = diviserDeuxMonomes(resteCourant.get(0), termeDominantDiviseur);

	        // Étape 2 : accumulation dans le quotient
	        strQuotient += termeQuotient;

	        // Étape 3 : termeQuotient × diviseur
	        String termeQuotientNettoye = termeQuotient.startsWith("+")
	                                    ? termeQuotient.substring(1)
	                                    : termeQuotient;
	        String produit = autrePolynome.multiplication(new Polynome(termeQuotientNettoye));

	        // Étape 4 : nouveau reste = resteCourant - produit
	        String strResteCourant = new Polynome(reconstructionPoly(resteCourant))
	                                     .soustraction(new Polynome(produit));

	        // Étape 5 : mise à jour du reste
	        resteCourant = strResteCourant.equals("0")
	                     ? new ArrayList<>()
	                     : new Polynome(strResteCourant).getMonomes();
	    }

	    // Nettoyage du '+' initial du quotient
	    if (strQuotient.startsWith("+")) {
	        strQuotient = strQuotient.substring(1);
	    }
	    if (strQuotient.isEmpty()) {
	        strQuotient = "0";
	    }
	    
	    // Si resteCourant est vide → division exacte → reste = "0"
	    // Sinon → reconstructionPoly(resteCourant) reconstruit la String du reste
	    String strReste = resteCourant.isEmpty() ? "0" : reconstructionPoly(resteCourant);

	    //REtourne du tableau [quotient, reste]
	    return new String[]{strQuotient, strReste};
	}
	

	/** Applicable sur un Object Polynome()
	 * Remplace un monôme dans la liste interne à l'indice spécifié.
	 * Aucune validation du format du monôme n'est effectuée.
	 * A utiliser avec précaution pour ne pas corrompre la cohérence de l'objet.
	 *
	 * @param monomeAAjouter le monôme sous forme String à insérer
	 * @param indiceList     l'indice (base 0) de la position à remplacer dans this.monomes
	 * TODO assurer le cohérence avec this.polynome
	 */
	public void setMonomes(String monomeAAjouter, int indiceList) {
		this.monomes.set(indiceList, monomeAAjouter);
	}
	

	/** Applicable sur un Object Polynome()
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
