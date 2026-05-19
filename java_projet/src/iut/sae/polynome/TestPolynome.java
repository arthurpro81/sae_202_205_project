/**
 * TestPolynome.java										05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

// Lorsque les tests ne seront plus dans le même fichier : package iut.sae.polynome.test;

/**
 * Classe de test pour vérifier si la création et les méthodes de polynômes fonctionnent.
 * Affiche une erreur si un test échoue.
 */
public class TestPolynome {

    /**
     * Lance tous les tests.
     */
    public void lancerTests() {
        System.out.println("Début des tests...");
        System.out.println("------------------");

        // --- Tests du constructeur ---
        try {
            testCreationValide();
            System.out.println("Test création valide      : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test création valide : " + echecAnormal.getMessage());
        }

        try {
            testCreationInvalide();
            System.out.println("Test création invalide    : OK (erreurs bien détectées)");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test création invalide : " + echecAnormal.getMessage());
        }

        // --- Tests des méthodes ---
        try {
            testGetMonomes();
            System.out.println("Test getMonomes           : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test getMonomes : " + echecAnormal.getMessage());
        }

        try {
            testDegres();
            System.out.println("Test degres               : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test degres : " + echecAnormal.getMessage());
        }

        try {
            testCoefficient();
            System.out.println("Test coefficient          : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test coefficient : " + echecAnormal.getMessage());
        }

        try {
            testToString();
            System.out.println("Test toString             : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR test toString : " + echecAnormal.getMessage());
        }

        System.out.println("------------------");
        System.out.println("Fin des tests.");
    }

    /**
     * Vérifie qu'on peut créer un polynôme normalement.
     * @throws Exception si une combinaison valide lève une erreur
     */
    private void testCreationValide() throws Exception {
    	String[] valide = {
    			"3x",
    			"3x^2",
    			"3x^10-9x^2-3",
    			"x",
    			"5",
    			"x^3+x+1"
    	};
    	for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
    		try {
    			new Polynome(valide[numeroTest]);
    		} catch (IllegalArgumentException attendue) {
    			throw new Exception("ERR combinaison invalide indice : " + numeroTest + " -> " + valide[numeroTest]);
    		}
    	}
    }
    
    /**
     * Vérifie que le programme détecte bien les cas erronés.
     * @throws Exception si une combinaison invalide ne lève pas d'erreur
     */
    private void testCreationInvalide() throws Exception {
    	String[] invalide = {
    			"3y",      // variable autre que x
    			"",        // chaîne vide
    			"3x^2s",   // caractère parasite après l'exposant
    			"abc",     // que des lettres
    			"3x^",     // exposant manquant après ^
    	};
    	for (int numeroTest = 0; numeroTest < invalide.length; numeroTest++) {
    		try {
    			new Polynome(invalide[numeroTest]);
    			// Si on arrive ici, l'erreur n'a pas été détectée -> test échoue
    			throw new Exception("ERR combinaison valide indice : " + numeroTest + " -> \"" + invalide[numeroTest] + "\"");
    		} catch (IllegalArgumentException attendue) {
    			// Comportement attendu : l'erreur a bien été levée
    		}
    	}
    }

    /**
     * Vérifie que getMonomes() découpe correctement le polynôme en monômes.
     * @throws Exception si le découpage est incorrect
     */
    private void testGetMonomes() throws Exception {
    	// Format : { polynome, monome[0], monome[1], monome[2], ... }
    	String[][] casDeTests = {
    			{"3x^3+5x-10", "3x^3", "5x", "-10"},
    			{"3x",         "3x"},
    			{"5",          "5"},
    			{"x^2+1",      "x^2", "1"},
    	};
    	
    	for (int numeroTest = 0; numeroTest < casDeTests.length; numeroTest++) {
    		String   polynomeActuel   = casDeTests[numeroTest][0];
    		Polynome objetPolynome    = new Polynome(polynomeActuel);
    		
    		// On compare chaque monôme attendu avec celui obtenu
    		for (int indiceMonome = 1; indiceMonome < casDeTests[numeroTest].length; indiceMonome++) {
    			String monomeAttendu = casDeTests[numeroTest][indiceMonome];
    			String monomeObtenu  = objetPolynome.getMonomes().get(indiceMonome - 1);
    			
    			if (!monomeAttendu.equals(monomeObtenu)) {
    				throw new Exception(
    					"ERR test " + numeroTest + " -> monome[" + (indiceMonome - 1) + "] "
    					+ "attendu=\"" + monomeAttendu + "\" obtenu=\"" + monomeObtenu + "\""
    				);
    			}
    		}
    	}
    }

    /**
     * Vérifie que degres() retourne le bon degré.
     * @throws Exception si le degré calculé est incorrect
     */
    private void testDegres() throws Exception {
    	// Format : { polynome, degre_attendu }
    	Object[][] casDeTests = {
    			{"3x^2+6x+7", 2},
    			{"3x",        1},
    			{"5",         0},
    			{"x^10+x",    10},
    			{"x^3+x^2+1", 3},
    	};
    	
    	for (int numeroTest = 0; numeroTest < casDeTests.length; numeroTest++) {
    		String   polynomeActuel = (String)  casDeTests[numeroTest][0];
    		int      degreAttendu   = (Integer) casDeTests[numeroTest][1];
    		Polynome objetPolynome  = new Polynome(polynomeActuel);
    		int      degreObtenu    = objetPolynome.degres();
    		
    		if (degreObtenu != degreAttendu) {
    			throw new Exception(
    				"ERR test " + numeroTest + " (\"" + polynomeActuel + "\") "
    				+ "-> degre attendu=" + degreAttendu + " obtenu=" + degreObtenu
    			);
    		}
    	}
    }

    /**
     * Vérifie que coefficient() retourne le bon coefficient dominant.
     * @throws Exception si le coefficient calculé est incorrect
     */
    private void testCoefficient() throws Exception {
    	// Format : { polynome, coefficient_attendu }
    	Object[][] casDeTests = {
    			{"3x^2+6x+7", 3.0},
    			{"3x",        3.0},
    			{"5",         5.0},  // constante pure : degré 0, coefficient = 5
    			{"x^2+1",     1.0},  // coefficient implicite
    	};
    	
    	for (int numeroTest = 0; numeroTest < casDeTests.length; numeroTest++) {
    		String   polynomeActuel      = (String) casDeTests[numeroTest][0];
    		double   coefficientAttendu  = (Double) casDeTests[numeroTest][1];
    		Polynome objetPolynome       = new Polynome(polynomeActuel);
    		double   coefficientObtenu   = objetPolynome.coefficient();
    		
    		if (coefficientObtenu != coefficientAttendu) {
    			throw new Exception(
    				"ERR test " + numeroTest + " (\"" + polynomeActuel + "\") "
    				+ "-> coefficient attendu=" + coefficientAttendu + " obtenu=" + coefficientObtenu
    			);
    		}
    	}
    }
    
    /**
     * Vérifie que toString() retourne bien la chaîne du polynôme d'origine.
     * @throws Exception si la chaîne retournée est incorrecte
     */
    private void testToString() throws Exception {
    	String[] casDeTests = {"3x^2+2x+10", "x", "5", "3x^10-9x^2-3"};
    	
    	for (int numeroTest = 0; numeroTest < casDeTests.length; numeroTest++) {
    		Polynome objetPolynome = new Polynome(casDeTests[numeroTest]);
    		String   chaine        = objetPolynome.toString();
    		
    		if (!chaine.equals(casDeTests[numeroTest])) {
    			throw new Exception(
    				"ERR test " + numeroTest + " -> attendu=\"" + casDeTests[numeroTest]
    				+ "\" obtenu=\"" + chaine + "\""
    			);
    		}
    	}
    }
}
