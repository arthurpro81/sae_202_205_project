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
 * @author Rémi Gavens, Florian Ferreira, Amaury Daveau, Leo Gomez, Arthur Chamayou--Valentin
 * @version beaucoup trop, aled...
 */
public class Polynome {

	// Liste oû sera stocker les monômes récupéré grace à la méthode getMonomes()
	private ArrayList<String> monomes = new ArrayList<>();

	// polynome saisie par l'utilisateur sous le format : 3x^2 + 7x^1 +5
	private String polynome;

	/**
	 * Constructeur de la classe Polynome. Valide et initialise et canonise un
	 * polynôme à partir d'une chaîne de caractères. Le format accepté est : [-]ax^A
	 * [+|-] bx^B [+|-] ... avec espaces optionnels. Exemples valides : "3x^2+2x-1",
	 * "-x^3+5", "7x", "42".
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
	 * Méthode privé appellé par le constructeur uniquement Méthode permétant de
	 * canonisé une liste de monome -> un monome = degrés unique (deux monomes ne
	 * peuvent pas avoir le meme deg)
	 */
	private void simplifierMonome() {
		int indiceMonomeActuel = 0;
		while (indiceMonomeActuel < this.monomes.size() - 1) {
			int degActuel = getDegres(this.monomes.get(indiceMonomeActuel));
			int degSuivant = getDegres(this.monomes.get(indiceMonomeActuel + 1));

			if (degActuel == degSuivant) {
				int sommeCoeff = getCoefficient(this.monomes.get(indiceMonomeActuel))
						+ getCoefficient(this.monomes.get(indiceMonomeActuel + 1));

				this.monomes.remove(indiceMonomeActuel + 1);
				this.monomes.remove(indiceMonomeActuel);

				if (sommeCoeff != 0) {
					if (degActuel != 0) {
						// cas coefficients implicites 1 et -1
						String monomeReconstruit = construireMonomeString(sommeCoeff, degActuel);
						monomeReconstruit = monomeReconstruit.startsWith("+") 
						                  ? monomeReconstruit.substring(1) : monomeReconstruit; 
						this.monomes.add(indiceMonomeActuel, monomeReconstruit);
						
					} else {
						this.monomes.add(indiceMonomeActuel, sommeCoeff + "");
					}
				}
			} else {
				indiceMonomeActuel++;
			}
		}

		if (this.monomes.isEmpty()) {
			this.monomes.add("0");
		}
	}

	/**
	 * Méthode privée appelée une seule fois par le constructeur. Décompose le
	 * polynôme en une liste de monômes stockée dans this.monomes. Les espaces sont
	 * supprimés avant le traitement. Le signe '-' est conservé en préfixe du monôme
	 * suivant. Les entrées vides éventuelles sont supprimées en fin de traitement.
	 *
	 * Exemple : "3x^2 + 2x - 10" produit ["3x^2", "2x", "-10"]
	 */
	private void remplirMonomes() {
		String stockageDeCaractere = "";
		char recuperationDeCaractereActuel;
		String polyNoSpace = this.polynome.replaceAll("\\s+", "");

		for (int parcoursDeCaractere = 0; parcoursDeCaractere < polyNoSpace.length(); parcoursDeCaractere++) {

			recuperationDeCaractereActuel = polyNoSpace.charAt(parcoursDeCaractere); // récupération du carac actuel
																						// évaluer

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

		// TRI A BULLES
		//pattern regex cherchant et regroupant les degres d'un monome
		Pattern patternDegre = Pattern.compile("\\^(\\d+)");

		// On répète des passes sur toute la liste
		for (int numeroDePassage = 0; numeroDePassage < this.monomes.size() - 1; numeroDePassage++) {

			// A chaque passe on compare les voisins deux à deux
			for (int indiceMonomeGauche = 0; indiceMonomeGauche < this.monomes.size() - 1
					- numeroDePassage; indiceMonomeGauche++) {

				// On récupère les deux voisins
				String monomeGauche = this.monomes.get(indiceMonomeGauche);
				String monomeDroit = this.monomes.get(indiceMonomeGauche + 1);

				// On extrait leurs degrés avec le Pattern
				Matcher matcherGauche = patternDegre.matcher(monomeGauche);
				Matcher matcherDroit = patternDegre.matcher(monomeDroit);

				int degreGauche = matcherGauche.find() ? Integer.parseInt(matcherGauche.group(1))
						: monomeGauche.contains("x") ? 1 : 0;

				int degreDroit = matcherDroit.find() ? Integer.parseInt(matcherDroit.group(1))
						: monomeDroit.contains("x") ? 1 : 0;

				// Si le gauche est plus PETIT que le droit → on échange
				// (on veut décroissant donc le plus grand doit être à gauche)
				if (degreGauche < degreDroit) {
					this.monomes.set(indiceMonomeGauche, monomeDroit);
					this.monomes.set(indiceMonomeGauche + 1, monomeGauche);
				}
			}
		}
	}

	/*
	 * Méthode privé appellé uniquement par le constructeur et dans la division
	 * Méthode permétant de construire un polynome (type String) grâce à une liste
	 * de monome (type ArrayList<Integer>)
	 */
	private String reconstructionPoly(ArrayList<String> monomes) {
		String polyReconstruit = "";
		for (String monome : monomes) {
			polyReconstruit += (monome.startsWith("-")) ? monome : "+" + monome;
		}
		return polyReconstruit = (polyReconstruit.startsWith("+")) 
							   ? polyReconstruit.substring(1) : polyReconstruit;
	}

	/**
	 * Méthode privé appellé uniquement pour les calcules polynomiale Construit la
	 * représentation String d'un monôme depuis son coefficient et son degré. Gère
	 * le signe pour l'intégration dans une somme (préfixe '+' ou '-').
	 */
	private String construireMonomeString(int coeff, int deg) {
	    String monome;
	    String coeffStr;
	    
	    coeffStr = (coeff == 1) ? "" 
	    						: (coeff == -1) ? "-" 
	    								        : String.valueOf(coeff);
	    
	    monome = (deg == 0) ? String.valueOf(coeff) 
	    					: (deg == 1) ? coeffStr + "x" 
	    								 : coeffStr + "x^" + deg;
	    
	    return monome.startsWith("-") ? monome : "+" + monome;
	}

	/**
	 * Cherche dans la liste de monômes l'indice du premier monôme ayant le même
	 * degré que celui passé en paramètre. Retourne -1 si aucun monôme de ce degré
	 * n'est trouvé.
	 *
	 * @param monomes    liste de monômes dans laquelle chercher
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
	 * Distribue un monôme sur tous les monômes de autrePolynome et accumule les
	 * produits dans la chaîne somme. Responsabilité : une ligne de la
	 * distributivité.
	 *
	 * @param monomeThis    le monôme de this à distribuer
	 * @param autrePolynome le polynôme sur lequel distribuer
	 * @return la chaîne somme mise à jour avec les nouveaux produits
	 */
	private String distribuerMonome(String monomeThis, Polynome autrePolynome) {
		String somme = "";
		for (int indiceAutre = 0; indiceAutre < autrePolynome.getMonomes().size(); indiceAutre++) {
			String monomeAutre = autrePolynome.getMonomes().get(indiceAutre);
			String monomeProduit = multiplierDeuxMonomes(monomeThis, monomeAutre);
			if (monomeProduit.startsWith("+")) {
				monomeProduit = monomeProduit.substring(1);
			}
			if (getCoefficient(monomeProduit) != 0) {
				somme += (monomeProduit.startsWith("-")) ? monomeProduit : "+" + monomeProduit;
			}
		}
		return somme;
	}

	/**
	 * Multiplie deux monômes et retourne le monôme résultat sous forme de String.
	 * Les coefficients sont multipliés et les degrés additionnés.
	 *  Exemple : "3x^2" × "5x" -> "15x^3"
	 * @param monome1 premier monôme
	 * @param monome2 second monôme
	 * @return le monôme produit sous forme de String
	 */
	private String multiplierDeuxMonomes(String monome1, String monome2) {
		int coeffResultat = getCoefficient(monome1) * getCoefficient(monome2);
		int degResultat = getDegres(monome1) + getDegres(monome2);
		return construireMonomeString(coeffResultat, degResultat);
	}

	/**
	 * Divise deux monômes et retourne le monôme quotient sous forme de String. Le
	 * coefficient résultant est la division entière des deux coefficients. Le degré
	 * résultant est la soustraction des deux degrés. 
	 * Exemple : "6x^3" ÷ "2x" -> coeffResultat=3, degResultat=2 → "+3x^2"
	 *
	 * @param monome1 monôme dividende
	 * @param monome2 monôme diviseur
	 * @return le monôme quotient sous forme de String avec signe préfixe
	 */
	private String diviserDeuxMonomes(String monome1, String monome2) {
		int coeffResultat = getCoefficient(monome1) / getCoefficient(monome2);
		int degResultat = getDegres(monome1) - getDegres(monome2);
		return construireMonomeString(coeffResultat, degResultat);
	}

	/**
	 * Applicable sur un Object Polynome() retourne le degré le plus élevé du
	 * polynôme. Les monômes de la forme 'Ax' (sans exposant écrit) sont traités
	 * comme 'Ax^1'. Retourne 0 si le polynôme est une constante pure.
	 *
	 * @return le degré maximal du polynôme sous forme d'entier
	 */
	public int MaxDegres() {
		return getDegres(this.monomes.get(0));
	}

	/**
	 * Applicable sur un Object Polynome() Extrait et retourne la liste des
	 * coefficients du polynôme dans l'ordre des monômes. Un coefficient implicite
	 * (ex : "x^2") est traité comme 1. Un coefficient négatif implicite 
	 * (ex :"-x^3") est traité comme -1.
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

	
	
	 
	/**
	 * Evalue le polynome en x = valeurX par le schema de Hörner.
	 * Principe : P(x) = (...((aₙ)x + aₙ₋₁)x + aₙ₋₂) ... + a₀
	 * Evite le calcul de puissances — uniquement multiplications et additions.
	 * Exemple : x³-6x²+11x-6 en x=2 → coeffs=[1,-6,11,-6]
	 *   resultat=1 → resultat=1×2-6=-4 → resultat=-4×2+11=3 → resultat=3×2-6=0
	 *   → P(2) = 0 ✅
	 *
	 * @param valeurX le point où evaluer le polynome
	 * @return        la valeur P(valeurX) sous forme de double
	 */
	public double xEgale(double valeurX) {
	    // Construire un tableau dense de coefficients de degMax jusqu'à 0
	    // (les degrés absents valent 0) pour que Hörner soit correct.
	    // Ex : x^2+1  → monomes=[x^2, 1], degrés=[2,0]
	    //      tableau dense = [1, 0, 1]  → 1*x²+0*x+1  ✓
	    int degMax = this.MaxDegres();
	    int[] dense = new int[degMax + 1]; // initialisé à 0

	    for (String monome : this.monomes) {
	        int deg   = getDegres(monome);
	        int coeff = getCoefficient(monome);
	        dense[degMax - deg] = coeff; // indice 0 = degré le plus haut
	    }

	    double resultat = 0;
	    for (int coeff : dense) {
	        resultat = resultat * valeurX + coeff;
	    }
	    return resultat;
	}

	/**
	 * Compte le nombre de changements de signe de la suite de Sturm
	 * evaluee au point pointEvaluation. Les valeurs nulles sont ignorees.
	 *
	 * Exemple : suite evaluee → [6.0, -5.0, 0.0, 0.25]
	 *   signes retenus :  +      -         +     (0 ignore)
	 *   changements    :     1         1        → V = 2
	 *
	 * @param suiteSturm       la suite de Sturm sous forme ArrayList<Polynome>
	 * @param pointEvaluation  le point x où evaluer chaque element de la suite
	 * @return                 le nombre de changements de signe V(pointEvaluation)
	 */
	private int compterChangementsDeSigneSturm(ArrayList<Polynome> suiteSturm,
	                                           double pointEvaluation) {
	    int    nombreChangements = 0;
	    double dernierSigne      = 0;

	    for (Polynome polynomeSturm : suiteSturm) {
	        double valeur = polynomeSturm.xEgale(pointEvaluation);

	        if (Math.abs(valeur) >= 1e-10) {
	            double signeActuel = valeur > 0 ? 1 : -1;
	            if (dernierSigne != 0 && signeActuel != dernierSigne) {
	                nombreChangements++;
	            }
	            dernierSigne = signeActuel;
	        }
	    }
	    return nombreChangements;
	}

	/**
	 * Construit la suite de Sturm du polynome sous forme ArrayList<Polynome>.
	 *
	 * Suite de Sturm :
	 *   S₀ = P
	 *   S₁ = P' (derivee)
	 *   S₂ = -reste(S₀ ÷ S₁)  → recupere via division()[1]
	 *   S₃ = -reste(S₁ ÷ S₂)
	 *   ...
	 * On s'arrete quand le reste est "0" ou que Sₖ est une constante.
	 *
	 * Chaque Sᵢ est un objet Polynome ce qui permet de reutiliser
	 * directement xEgale() et division() sans conversion de type.
	 *
	 * @return ArrayList<Polynome> la suite de Sturm [S₀, S₁, S₂, ...]
	 */
	private ArrayList<Polynome> construireSuiteSturm() {
	    ArrayList<Polynome> suiteSturm = new ArrayList<>();

	    suiteSturm.add(new Polynome(this.toString()));

	    ArrayList<String> monomesDerivee = this.derive(1);
	    if (monomesDerivee.isEmpty()) {
	        return suiteSturm;
	    }

	    String chaineDerivee = "";
	    for (int indiceMon = 0; indiceMon < monomesDerivee.size(); indiceMon++) {
	        String monome = monomesDerivee.get(indiceMon);
	        chaineDerivee += (indiceMon == 0) ? monome
	                                          : (monome.startsWith("-") ? monome : "+" + monome);
	    }
	    suiteSturm.add(new Polynome(chaineDerivee));

	    boolean continuerConstruction = true;
	    while (continuerConstruction) {
	        Polynome avant_dernier = suiteSturm.get(suiteSturm.size() - 2);
	        Polynome dernier       = suiteSturm.get(suiteSturm.size() - 1);

	        // Arrêt si le dernier élément est déjà une constante
	        if (dernier.MaxDegres() == 0) {
	            continuerConstruction = false;
	        } else if (dernier.MaxDegres() > avant_dernier.MaxDegres()) {
	            continuerConstruction = false;
	        } else {
	            try {
	                String resteStr = avant_dernier.division(dernier)[1];

	                if (resteStr.equals("0")) {
	                    continuerConstruction = false;
	                } else {
	                    String negatifReste = new Polynome("0").soustraction(new Polynome(resteStr));
	                    suiteSturm.add(new Polynome(negatifReste));

	                    if (suiteSturm.get(suiteSturm.size() - 1).MaxDegres() == 0) {
	                        continuerConstruction = false;
	                    }
	                }
	            } catch (IllegalArgumentException e) {
	                // Division impossible (degré diviseur > dividende) → on arrête
	                continuerConstruction = false;
	            }
	        }
	    }
	    return suiteSturm;
	}

	/**
	 * Localise une racine dans [borneInferieure, borneSuperieure] par dichotomie.
	 * Precondition : P(borneInferieure) et P(borneSuperieure) sont de signes
	 * opposes, ce qui garantit l'existence d'une racine dans l'intervalle.
	 *
	 * Principe :
	 *   milieu = (borneInferieure + borneSuperieure) / 2
	 *   P(borneInferieure) × P(milieu) < 0 → racine dans [borneInferieure, milieu]
	 *   P(borneInferieure) × P(milieu) > 0 → racine dans [milieu, borneSuperieure]
	 *   P(milieu) ≈ 0                       → milieu est la racine
	 *   Repeter jusqu'a |borneSuperieure - borneInferieure| < 0.00005
	 *
	 * @param borneInferieure borne inferieure de l'intervalle
	 * @param borneSuperieure borne superieure de l'intervalle
	 * @return                la racine arrondie a 4 decimales
	 */
	private double dichotomie(double borneInferieure, double borneSuperieure) {
	    final double PRECISION     = 0.00005;
	    final int    MAX_ITERATIONS = 1000;

	    double valeurBorneInf = this.xEgale(borneInferieure);
	    double milieu         = borneInferieure;

	    int nombreIterations = 0;
	    while (nombreIterations < MAX_ITERATIONS
	           && (borneSuperieure - borneInferieure) > PRECISION) {

	        milieu = (borneInferieure + borneSuperieure) / 2.0;
	        double valeurMilieu = this.xEgale(milieu);

	        if (Math.abs(valeurMilieu) >= 1e-10) {
	            if (valeurBorneInf * valeurMilieu < 0) {
	                borneSuperieure = milieu;
	            } else {
	                borneInferieure = milieu;
	                valeurBorneInf  = valeurMilieu;
	            }
	        } else {
	            borneSuperieure = milieu;  // racine exacte trouvee, on arrete
	            borneInferieure = milieu;
	        }
	        nombreIterations++;
	    }
	    return Math.round(milieu * 10000.0) / 10000.0;
	}

	/**
	 * Cherche recursivement les racines dans [borneInferieure, borneSuperieure]
	 * en subdivisant l'intervalle jusqu'a isoler chaque racine individuelle.
	 *
	 * Si Sturm detecte 0 racines  → on ne fait rien.
	 * Si Sturm detecte 1 racine   → dichotomie pour la localiser.
	 * Si Sturm detecte 2+ racines → on coupe l'intervalle en deux et on
	 *                               rappelle recursivement sur chaque moitie.
	 *
	 * Cela garantit qu'aucune racine n'est oubliee, meme si deux racines
	 * sont proches dans un meme intervalle (ex: -4.8 et -4.6 dans [-5, -4.5]).
	 *
	 * @param suiteSturm       la suite de Sturm (construite une seule fois)
	 * @param borneInferieure  borne inferieure de l'intervalle courant
	 * @param borneSuperieure  borne superieure de l'intervalle courant
	 * @param racinesTrouvees  liste des racines accumulees (modifiee en place)
	 */
	private void chercherRacinesRecursif(ArrayList<Polynome> suiteSturm,
	                                     double borneInferieure,
	                                     double borneSuperieure,
	                                     ArrayList<Double> racinesTrouvees) {

	    int nombreRacines = compterChangementsDeSigneSturm(suiteSturm, borneInferieure)
	                      - compterChangementsDeSigneSturm(suiteSturm, borneSuperieure);

	    if (nombreRacines <= 0) {
	        // Aucune racine dans cet intervalle → rien a faire

	    } else if (nombreRacines == 1) {
	        // Exactement 1 racine → dichotomie
	        double valeurBorneInf = this.xEgale(borneInferieure);
	        double valeurBorneSup = this.xEgale(borneSuperieure);
	        double racineLocalisee;

	        if (Math.abs(valeurBorneInf) < 1e-10) {
	            racineLocalisee = Math.round(borneInferieure * 10000.0) / 10000.0;
	        } else if (Math.abs(valeurBorneSup) < 1e-10) {
	            racineLocalisee = Math.round(borneSuperieure * 10000.0) / 10000.0;
	        } else if (valeurBorneInf * valeurBorneSup < 0) {
	            racineLocalisee = this.dichotomie(borneInferieure, borneSuperieure);
	        } else {
	            // Racine double (tangente a l'axe) → affinage de l'intervalle
	            double marge = (borneSuperieure - borneInferieure) * 0.1;
	            racineLocalisee = this.dichotomie(borneInferieure - marge,
	                                              borneSuperieure + marge);
	        }

	        // Dedoublonnage avant ajout
	        boolean dejaPresente = false;
	        for (double racineExistante : racinesTrouvees) {
	            if (Math.abs(racineExistante - racineLocalisee) < 0.001) {
	                dejaPresente = true;
	            }
	        }
	        if (!dejaPresente) {
	            racinesTrouvees.add(racineLocalisee);
	        }

	    } else {
	        // 2+ racines → subdivision recursive en deux demi-intervalles
	        double milieu = (borneInferieure + borneSuperieure) / 2.0;
	        chercherRacinesRecursif(suiteSturm, borneInferieure, milieu,        racinesTrouvees);
	        chercherRacinesRecursif(suiteSturm, milieu,          borneSuperieure, racinesTrouvees);
	    }
	}

	/**
	 * Applicable sur un objet Polynome(), retourne une ArrayList des racines
	 * comprises entre l'encadrement minimum|maximum. Si aucune racine, retourne
	 * une ArrayList vide.
	 *
	 * Strategie :
	 *   1. Construire la suite de Sturm une seule fois
	 *   2. Appel recursif de chercherRacinesRecursif() sur [minimum, maximum]
	 *      qui subdivise automatiquement jusqu'a isoler chaque racine
	 *   3. Verifier les bornes exactes
	 *   4. Trier le resultat
	 *
	 * @param minimum encadrement minimum inclus
	 * @param maximum encadrement maximum inclus
	 * @throws IllegalArgumentException si minimum > maximum
	 * @return ArrayList<Double> des racines triees dans l'encadrement
	 */
	public ArrayList<Double> racine(int minimum, int maximum) {
	    if (minimum > maximum) {
	        throw new IllegalArgumentException(
	            "Erreur : l'encadrement minimum est superieur a l'encadrement maximum");
	    }

	    ArrayList<Double> listeRacines = new ArrayList<>();

	    if (this.MaxDegres() == 0) {
	        return listeRacines;
	    }

	    ArrayList<Polynome> suiteSturm = construireSuiteSturm();

	    // Recherche recursive sur tout l'encadrement
	    chercherRacinesRecursif(suiteSturm, minimum, maximum, listeRacines);

	    // Tri croissant
	    listeRacines.sort(null);
	    return listeRacines;
	}

	
	
	/**
	 * Applicable sur un Object Polynome() Détermine la limite du polynôme en
	 * +infini ou -infini. La limite est calculée à partir du signe du coefficient
	 * dominant et de la parité du degré dominant selon les règles suivantes : - En
	 * +infini : signe du coefficient dominant - En -infini, degré pair : même signe
	 * que le coefficient dominant - En -infini, degré impair : signe opposé au
	 * coefficient dominant - Constante pure : retourne la valeur de la constante
	 *
	 * @param limiteChercher '+' pour la limite en +infini, '-' pour la limite en
	 *                       -infini
	 * @return "+Infini" ou "-Infini" selon la limite calculée, ou la valeur si
	 *         constante
	 * @throws IllegalArgumentException si limiteChercher est différent de '+' et
	 *                                  '-'
	 */
	public String limite(char limiteChercher) {
		if (limiteChercher != '+' && limiteChercher != '-') {
			throw new IllegalArgumentException("Erreur : saisie != '+' || '-'");
		}

		// Récupération du degres et du coef du monome au plus haut degres (ranger à
		// l'indice 0)
		int degMax = getDegres(this.monomes.get(0));
		int coefficientMonome = this.coefficient().get(0);
		String message;

		// monome = constante
		if (degMax == 0) {
			message = this.monomes.get(0);
		} else {

			// Vérification signe du coeff et si le degres et paire
			boolean coeffPositif = coefficientMonome > 0;
			boolean degresPair = degMax % 2 == 0;

			if (limiteChercher == '+') {
				message = coeffPositif ? "+Infini" : "-Infini";
			} else {
				message = (degresPair == coeffPositif) ? "+Infini" : "-Infini";
			}
		}
		return message;
	}

	/**
	 * Applicable sur un Object Polynome() Calcule la dérivée d'ordre n du polynôme
	 * et retourne la liste de ses monômes. Applique les règles de dérivation
	 * standard : - d/dx(Ax^b) = A*b * x^(b-1) - d/dx(Ax) = A - d/dx(A) = 0 (terme
	 * ignoré dans le résultat) Les coefficients implicites (ex : "x", "-x") sont
	 * traités comme 1 et -1. Cette méthode ne calcule pas la primitive
	 * (exposantDerive doit être strictement positif).
	 *
	 * @param exposantDerive ordre de la dérivation (1 = f', 2 = f'', ...)
	 * @return ArrayList<String> des monômes du polynôme dérivé
	 * @throws IllegalArgumentException si exposantDerive est inférieur ou égal à 0
	 */
	public ArrayList<String> derive(int exposantDerive) {
		// On n'autorise pas le calcule des primitives
		if (exposantDerive <= 0) {
			throw new IllegalArgumentException("Erreur : parametre <= 0");
		}
		
		int coeff;
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
					// Cas Ax^b -> A*b x^(b-1)
					String coeffString = matcherAvecExposant.group(1); // groupe 1 : coefficient
					String degString = matcherAvecExposant.group(2); // groupe 2 : exposant

					// Gestion coefficient implicite
					coeff = coeffString.isEmpty() ? 1
							: coeffString.equals("-") ? -1 : Integer.parseInt(coeffString);

					int deg = Integer.parseInt(degString);
					int nouveauCoeff = coeff * deg;
					int nouveauDeg = deg - 1;

					// Reconstruction
					String monomeDerive = construireMonomeString(nouveauCoeff, nouveauDeg);
					monomeDerive = monomeDerive.startsWith("+") ? monomeDerive.substring(1) : monomeDerive;
					monomesDeriveCourant.add(monomeDerive);

				} else if (matcherSansExposant.find()) {
					// Cas Ax -> A (la dérivée de Ax est A)
					String coeffStr = matcherSansExposant.group(1);
					coeff = coeffStr.isEmpty() ? 1 : coeffStr.equals("-") ? -1 : Integer.parseInt(coeffStr);
					monomesDeriveCourant.add(String.valueOf(coeff));

				}
				// constante -> dérivée = 0, on ne l'ajoute pas
			}
			monomesDerive = monomesDeriveCourant;
		}
		return monomesDerive;
	}

	/**
	 * Applicable sur un Object Polynome() Retourne la liste des monômes du
	 * polynôme. Cette liste est construite une seule fois par le constructeur et ne
	 * change pas. Ne pas modifier la liste retournée directement depuis l'extérieur
	 * de la classe.
	 *
	 * Exemple : Polynome("3x^2+2x-1").getMonomes() produit ["3x^2", "2x", "-1"]
	 *
	 * @return ArrayList<String> des monômes dans l'ordre de saisie
	 */
	public ArrayList<String> getMonomes() {
		return this.monomes;
	}

	/**
	 * Applicable sur un monome String Retourne le degré d'un monôme sous forme d'un
	 * int. "x" et "ax" sont traités comme degré 1, les constantes comme degré 0.
	 * @param monome string  à évaluer
	 * @return degrés du monome en param
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
	 * Applicable sur un monome String Retourne le coefficient d'un monôme sous
	 * forme d'un int. "x" et "ax" sont traités comme degré 1, les constantes comme
	 * degré 0.
	 */
	private int getCoefficient(String monome) {
		int coeffMonome;
		String coeffString = monome.replaceAll("x.*", "");

		coeffMonome = (coeffString.isEmpty()) ? 1 : (coeffString.equals("-")) ? -1 : Integer.parseInt(coeffString);
		return coeffMonome;
	}

	/* CALcULE pOlinomiale */

	/**
	 * Applicable sur un objet Polynome
	 * méthodes permettant d'additionner deux deux objets polynomes (toutes formes de polynomes autorisé)
	 * @param autrePolynome est un objet polynome qui sera additionné à this
	 * @return variable string représentant le polynome cannonisé.
	 */
	public String addition(Polynome autrePolynome) {
		String strSommePoly = "";
		// si l'indice d'un monome de autrePolynome ne fait pas partie de la liste alors
		// il faudra l'ajouter à strSommePoly
		ArrayList<Integer> indiceAutrePolyDejaTrouver = new ArrayList<>();
		
		// Boucles recherchant les monomes au degres communs de this et de autrePolynome
		// Boucle passant en revue les monomes de this
		for (int indiceThis = 0; indiceThis < this.getMonomes().size(); indiceThis++) {
			String monomeThis = this.getMonomes().get(indiceThis);
			int degMonomeThis = getDegres(monomeThis);
			int coeffThis = getCoefficient(monomeThis);

			// ajout de l'indice du monome de autrePolynome à la liste des monomes de degres
			// commun
			int indicePartenaire = trouverIndiceMemeDegre(autrePolynome.getMonomes(), degMonomeThis);

			// Degres communs -> addition coeff
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
		//on ajoute les monômes dans monomesResultat
		if (strSommePoly.startsWith("+")) {
		    strSommePoly = strSommePoly.substring(1);
		}
		if (strSommePoly.isEmpty()) {
		    strSommePoly = "0";
		}
		return new Polynome(strSommePoly).toString();

	}

	/**
	 * Applicable sur un objet Polynome
	 * méthodes permettant de soustraire deux deux objets polynomes (toutes formes de polynomes autorisé)
	 * @param autrePolynome est un objet polynome qui sera additionné à this
	 * @return variable string représentant le polynome cannonisé.
	 */
	public String soustraction(Polynome autrePolynome) {
		String strSommePoly = "";
		// si l'indice d'un monome de autrePolynome ne fait pas partie de la liste alors
		// il faudra l'ajouter à strSommePoly
		ArrayList<Integer> indiceAutrePolyDejaTrouver = new ArrayList<>();

		// Boucles recherchant les monomes au degres communs de this et de autrePolynome
		// Boucle passant en revue les monomes de this
		for (int indiceThis = 0; indiceThis < this.getMonomes().size(); indiceThis++) {
			String monomeThis = this.getMonomes().get(indiceThis);
			int degMonomeThis = getDegres(monomeThis);
			int coeffThis = getCoefficient(monomeThis);

			// ajout de l'indice du monome de autrePolynome à la liste des monomes de degres
			// commun
			int indicePartenaire = trouverIndiceMemeDegre(autrePolynome.getMonomes(), degMonomeThis);

			// Degres communs -> addition coeff
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
				// inversion du signe des monomes
				monomeAutre = monomeAutre.startsWith("-") ? monomeAutre.substring(1) : "-" + monomeAutre;
				strSommePoly += monomeAutre.startsWith("-") ? monomeAutre : "+" + monomeAutre;
			}
		}

		// nettoyage du + devant le polynome String
		if (strSommePoly.startsWith("+")) {
			strSommePoly = strSommePoly.substring(1);
		}

		// Si la somme des 2 poly = 0
		if (strSommePoly.isEmpty()) {
			strSommePoly = "0";
		}

		return new Polynome(strSommePoly).toString();
	}

	/**
	 * Applicable à un objet Polynome
	 * Multiplie this par autrePolynome par distributivité. Chaque monôme de this
	 * est multiplié par chaque monôme de autrePolynome. Les termes de même degré
	 * sont regroupés automatiquement par le constructeur lors de la création du
	 * polynôme résultat.
	 *
	 * @param autrePolynome le polynôme multiplicateur
	 * @return String représentant le polynôme produit, canonisable via new
	 *         Polynome()
	 */
	public String multiplication(Polynome autrePolynome) {
		String strProduitPoly = "";

		// distributivité de chaque monôme de this × chaque monôme de autrePolynome
		for (int indiceThis = 0; indiceThis < this.monomes.size(); indiceThis++) {
			String monomeThis = this.monomes.get(indiceThis);

			strProduitPoly += distribuerMonome(monomeThis, autrePolynome);
		}

		// nettoyage du + devant le polynome String
		if (strProduitPoly.startsWith("+")) {
			strProduitPoly = strProduitPoly.substring(1);
		}

		// Si 1 des 2 poly est nul
		if (strProduitPoly.isEmpty()) {
			strProduitPoly = "0";
		}

		return new Polynome(strProduitPoly).toString();
	}

	/**
	 * Applicable à un objet Polynome
	 * Divise this par autrePolynome par division euclidienne. Retourne le quotient
	 * sous forme de String canonisable via new Polynome(). Si la division n'est pas
	 * exacte, le reste est ignoré. Le polynôme étant canonisé, le monôme dominant
	 * est toujours à l'indice 0.
	 *
	 * @param autrePolynome le polynôme diviseur
	 * @return String représentant le quotient
	 * @throws IllegalArgumentException si le degré du diviseur (autrePolynome) est
	 *                                  supérieur à celui du dividende(this)
	 */
	public String[] division(Polynome autrePolynome) {

		if (autrePolynome.MaxDegres() > this.MaxDegres()) {
			throw new IllegalArgumentException("Erreur : degré du diviseur supérieur au degré du dividende");
		}

		String strQuotient = "";
		ArrayList<String> resteCourant = new ArrayList<>(this.monomes);
		String termeDominantDiviseur = autrePolynome.getMonomes().get(0);

		while (!resteCourant.isEmpty() && getDegres(resteCourant.get(0)) >= autrePolynome.MaxDegres()) {

			// Étape 1 : terme du quotient
			String termeQuotient = diviserDeuxMonomes(resteCourant.get(0), termeDominantDiviseur);

			// Étape 2 : accumulation dans le quotient
			strQuotient += termeQuotient;

			// Étape 3 : termeQuotient × diviseur
			String termeQuotientNettoye = termeQuotient.startsWith("+") ? termeQuotient.substring(1) : termeQuotient;
			String produit = autrePolynome.multiplication(new Polynome(termeQuotientNettoye));

			// Étape 4 : nouveau reste = resteCourant - produit
			String strResteCourant = new Polynome(reconstructionPoly(resteCourant)).soustraction(new Polynome(produit));

			// Étape 5 : mise à jour du reste
			resteCourant = strResteCourant.equals("0") ? new ArrayList<>() : new Polynome(strResteCourant).getMonomes();
		}

		// Nettoyage du '+' initial du quotient
		if (strQuotient.startsWith("+")) {
			strQuotient = strQuotient.substring(1);
		}
		if (strQuotient.isEmpty()) {
			strQuotient = "0";
		}

		// Si resteCourant est vide -> division exacte -> reste = "0"
		// Sinon -> reconstructionPoly(resteCourant) reconstruit la String du reste
		String strReste = resteCourant.isEmpty() ? "0" : reconstructionPoly(resteCourant);

		// REtourne du tableau [quotient, reste]
		return new String[] { strQuotient, strReste };
	}

	/**
	 * Applicable sur un Object Polynome() Remplace un monôme dans la liste interne
	 * à l'indice spécifié. Aucune validation du format du monôme n'est effectuée. A
	 * utiliser avec précaution pour ne pas corrompre la cohérence de l'objet.
	 *
	 * @param monomeAAjouter le monôme sous forme String à insérer
	 * @param indiceList     l'indice (base 0) de la position à remplacer dans
	 *                       this.monomes TODO assurer le cohérence avec
	 *                       this.polynome
	 */
	public void setMonomes(String monomeAAjouter, int indiceList) {
		this.monomes.set(indiceList, monomeAAjouter);
	}

	/**
	 * Applicable sur un Object Polynome() Retourne la représentation textuelle du
	 * polynôme. Correspond exactement à la chaîne originale saisie lors de la
	 * construction, espaces inclus.
	 *
	 * @return String le polynôme sous sa forme de saisie originale
	 */
	@Override
	public String toString() {
		return this.polynome;
	}
}