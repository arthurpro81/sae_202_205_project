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
 * 			coefficient du terme de plus haut degré : 3.0
 * 			racine : TODO (méthode numérique)
 * 			limite : -Infinie = +Infinie // +Infinie = +Infinie
 * 			
 */
public class Polynome {
	
	// Liste où sera stocké les monômes récupérés grâce à la méthode getMonomes()
	private ArrayList<String> monomes = new ArrayList<>();

    // Polynome saisi par l'utilisateur sous le format : 3x^2+7x^1+5
    private String polynome;

    /**
     * Constructeur de polynômes.
     * @param polynome - polynôme rentré par l'utilisateur
     * @throws IllegalArgumentException si le polynôme contient des caractères invalides ou est vide.
     */
    public Polynome(String polynome) {
    	String regex = "^(?:\\d*x(?:\\^\\d+)?|\\d+)(?:\\s?[+\\-]\\s?(?:\\d*x(?:\\^\\d+)?|\\d+))*$";
        // Sortie Err 
    	// Si la saisie contient des caractères autres que : "0-9|x|[:space:]|^|+|-"
    	// -> regex : ^(?:\d*x(?:\^\d+)?|\d+)(?:\s?[+\-]\s?(?:\d*x(?:\^\d+)?|\d+))*$
    	if (!polynome.matches(regex)) {
    	    throw new IllegalArgumentException("Erreur : caracteres inconnus ou caractere 'x' absent");
    	}
    	this.polynome = polynome;
    	// On remplit la liste de monomes dès la construction
    	this.monomes = this.getMonomes();
    }

	
    /**
     * Retourne le degré du polynôme, c'est-à-dire l'exposant le plus élevé trouvé parmi les monômes.
     * Ex : Polynome("3x^2+6x+7").degres() -> 2
     * @return degre - le degré maximal du polynôme (0 si que des constantes)
     */
	public int degres() {
		int degre = 0; 								// Par défaut : degré 0 (constante)
		
		for (int indiceMono = 0; indiceMono < this.monomes.size(); indiceMono++) {
			String monomeActuel = this.monomes.get(indiceMono);
			
			if (monomeActuel.contains("x")) {
				
				if (monomeActuel.contains("^")) {
					// Cas : 3x^2 -> on récupère ce qui est après "^"
					int positionChapeau        = monomeActuel.indexOf('^');
					String exposantEnChaine    = monomeActuel.substring(positionChapeau + 1);
					int exposantActuel         = Integer.parseInt(exposantEnChaine);
					
					if (exposantActuel > degre) {
						degre = exposantActuel;
					}
				} else {
					// Cas : 3x ou x -> exposant implicite = 1
					if (1 > degre) {
						degre = 1;
					}
				}
			}
			// Cas : constante (ex : 7) -> exposant = 0, ne change pas le degré
		}
		return degre;
	}
	
    /**
     * Retourne le coefficient du terme de plus haut degré du polynôme.
     * Ex : Polynome("3x^2+6x+7").coefficient() -> 3.0
     * Ex : Polynome("x^2+1").coefficient()      -> 1.0  (coefficient implicite)
     * @return coefficientMax - coefficient du monôme dominant
     */
	public double coefficient() {
		int    degreeMax      = this.degres();
		double coefficientMax = 0.0;
		
		for (int indiceMono = 0; indiceMono < this.monomes.size(); indiceMono++) {
			String monomeActuel = this.monomes.get(indiceMono);
			
			// On cherche le monôme dont le degré correspond au degré max
			int degreeMonomeActuel = 0;
			
			if (monomeActuel.contains("x")) {
				if (monomeActuel.contains("^")) {
					int positionChapeau     = monomeActuel.indexOf('^');
					String exposantEnChaine = monomeActuel.substring(positionChapeau + 1);
					degreeMonomeActuel      = Integer.parseInt(exposantEnChaine);
				} else {
					degreeMonomeActuel = 1;
				}
			}
			
			if (degreeMonomeActuel == degreeMax) {
				
				if (!monomeActuel.contains("x")) {
					// Cas : constante pure (ex : "5", "10") -> le coefficient est la valeur entière
					coefficientMax = Double.parseDouble(monomeActuel.trim());
				} else {
					// Récupération du coefficient avant le "x"
					int positionX        = monomeActuel.indexOf('x');
					String coeffEnChaine = monomeActuel.substring(0, positionX).trim();
					
					if (coeffEnChaine.isEmpty() || coeffEnChaine.equals("+")) {
						// Cas : x^2 ou +x^2 -> coefficient implicite = 1
						coefficientMax = 1.0;
					} else if (coeffEnChaine.equals("-")) {
						// Cas : -x^2 -> coefficient implicite = -1
						coefficientMax = -1.0;
					} else {
						coefficientMax = Double.parseDouble(coeffEnChaine);
					}
				}
			}
		}
		return coefficientMax;
	}
	
	/*
	 * TODO : nécessite une méthode numérique (ex : Newton-Raphson)
	 */
	public double racine() {
		return 0;
	}
	
	/*
	 * TODO : analyse du signe du coefficient dominant et de la parité du degré
	 */
	public double limite() {
		return 0;
	}
	
	
    /**
     * Getter.
     * Méthode de récupération des monômes à partir du polynôme stocké.
     * Ex : Polynome("3x^3+5x-10").getMonomes() -> ["3x^3", "5x", "-10"]
     * @return monomes - liste des monômes du polynôme
     */
	public ArrayList<String> getMonomes() {
	   // Parcours de chaque caractère du polynôme.
	   // Quand on rencontre un "+" ou "-" (hors premier caractère), on découpe.
	   
	   ArrayList<String> monomesTrouves      = new ArrayList<>();
	   String            stockageDeCaractere = "";
	   char              recuperationDeCaractereActuel;
	   
	   for (int parcoursDeCaractere = 0; parcoursDeCaractere < this.polynome.length(); parcoursDeCaractere++) {
		   
		   recuperationDeCaractereActuel = this.polynome.charAt(parcoursDeCaractere);
		   
		   // On coupe sur "+" ou "-", sauf si c'est le tout premier caractère (signe du 1er monôme)
		   boolean estSeparateur = (recuperationDeCaractereActuel == '+' || recuperationDeCaractereActuel == '-');
		   boolean estPremierCaractere = (parcoursDeCaractere == 0);
		   
		   if (estSeparateur && !estPremierCaractere) {
			   // On sauvegarde le monôme accumulé jusqu'ici
			   monomesTrouves.add(stockageDeCaractere.trim());
			   
			   // On repart avec le signe courant (si c'est un "-", il appartient au prochain monôme)
			   if (recuperationDeCaractereActuel == '-') {
				   stockageDeCaractere = "-";
			   } else {
				   stockageDeCaractere = "";
			   }
		   } else {
			   // Accumulation du caractère dans le monôme en cours
			   stockageDeCaractere += recuperationDeCaractereActuel;
		   }
	   }
	   // Ajout du dernier monôme (pas suivi d'un séparateur)
	   if (!stockageDeCaractere.trim().isEmpty()) {
		   monomesTrouves.add(stockageDeCaractere.trim());
	   }
	   
	   return monomesTrouves;
	}

	
	/**
	 * Setter.
	 * Met à jour la liste 'monomes' en remplaçant à l'indice donné le monôme souhaité.
	 * @param monomeAAjouter - monôme que l'on souhaite insérer
	 * @param indiceList     - indice de la liste où placer le monôme à ajouter
	 */
	public void setMonomes(String monomeAAjouter, int indiceList) {
		this.monomes.set(indiceList, monomeAAjouter);
	}
	

    /**
     * Affichage du polynôme sous forme de texte simple.
     * Format : Polynome("3x^2+2x^1+10").toString() -> "3x^2+2x^1+10"
     * @return messageAffiche - affichage du polynôme
     */
    @Override
	public String toString() {
		String messageAffiche = this.polynome;
		return messageAffiche;
	}
}
