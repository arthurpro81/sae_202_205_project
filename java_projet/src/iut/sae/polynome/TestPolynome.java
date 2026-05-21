/**
 * TestPolynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//lorsque les test ne seont plus dans le meme fichier mettre : package iut.sae.polynome.test;

/**
 * Classe de test pour vérifier si la création de polynomes fonctionne.
 * Affiche une erreur si un test échoue.
 */
public class TestPolynome {

    /**
     * Lance les tests de base.
     */
    public void lancerTests() {
        System.out.println("Début des tests...");

        try {
            testCreationValide();
            System.out.println("Test création valide : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR sur le test de création valide : " + echecAnormal.getMessage());
        }

        try {
            testCreationInvalide();
            System.out.println("Test création invalide : OK (l'erreur a bien été détectée)");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR sur le test de création invalide : " + echecAnormal.getMessage());
        }
        try {
        	testLimitesValide();
        	 System.out.println("Test Limites valide : OK");
        } catch (Exception echecAnormal) {
        	System.err.println("ERREUR sur le test de limite valide : " + echecAnormal.getMessage());
        }
        try {
        	testLimitesInvalide();
        	 System.out.println("Test Limites invalide : OK");
        } catch (Exception echecAnormal) {
        	System.err.println("ERREUR sur le test de limite invalide : " + echecAnormal.getMessage());
        }

        System.out.println("Fin des tests.");
    }

    /**
     * Vérifie qu'on peut créer un polynôme normalement.
     * @throws Exception - capture une erreur si une combinaison est invalide
     */
    private void testCreationValide() throws Exception {
    	String[][] valide = {
    	//TODO inserer des valeurs de test
    			{"3x"}, 
    			{"3x^2"}, 
    			{"3x^10-9x^2-3"} 
    	};
    	for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
    		try {
    			String[] combinaison = valide[numeroTest];
    			new Polynome(combinaison[0]);
    		} catch (IllegalArgumentException attendue) {
    			throw new Exception("ERR combinaison invalide indice : " + numeroTest); 
    		}
    	}
    }
    
    /**
     * Vérifie que le programme détecte bien les cas erreurs.
     * @throws Exception - capture une erreur si une combinaison est valide
     */
    private void testCreationInvalide() throws Exception {
    	String[][] invalide = {
    	//TODO inserer des valeurs de test
    			{"3y"},    // OK : Erreur bien détecter
    			{""},      // OK : Erreur bien détecters
    			{"3x^2s"}, // OK : Erreur bien détecterS
    	};
    	for (int numeroTest = 0; numeroTest < invalide.length; numeroTest++) {
    		try {
    			String[] combinaison = invalide[numeroTest];
    			new Polynome(combinaison[0]);
    			throw new Exception("ERR combinaison valide indice : " + numeroTest);
    		} catch (IllegalArgumentException attendue) {
    			// C'est normal, le test a réussi car l'erreur a été détectée
    		}
    	}
    }
<<<<<<< HEAD
    // partie créées par Amaury
    /**
     * Test permettant de verifier la validité de la méthode Limites ici avec des valeurs valide
     */
    private void testLimitesValide() {
    	double[][] monomeTestValide = {
    			{2.0, 5.0, -2.0},
    			{-7.0, 3.0, 8.0},
    			{6.0, -12.0, 9.0},
    			{-30.0, 0.0, 1.0}
    	};
    	char[] limiteATester = {
    			'+',
    			'+',
    			'-',
    			'-'
    	};
    	String[] resultatAttendu = {
    			"+infini",
    			"-infini",
    			"-infini",
    			"+infini"
    	};
    	for (int noTest = 0; noTest < monomeTestValide.length; noTest++) {
	    	try {
    			Polynome test = new Polynome(monomeTestValide[noTest]);
    			String resultat = test.limite(limiteATester[noTest]);
    			resultat.compareTo(resultatAttendu[noTest]);
    			System.out.println("Test limite valide n°"+ noTest + " validé");
	    	} catch (IllegalArgumentException echecAnormal) {
	    		System.err.println("ERREUR sur le test de recherche de limite valide : " + echecAnormal.getMessage());
	    	}
    	}
    }
    
    /**
     * Test permettant de verifier la validité de la méthode Limites ici avec des valeurs erronée
     */
    private void testLimitesInvalide() {
    	double[][] monomeTestInvalide = {
    			{},
    			{},
    			{6.0, -12.0, 9.0},
    			{-30.0, 0.0, 1.0}
    	};
    	char[] limiteATester = {
    			'+',
    			'+',
    			'p',
    			7
    	};
    	for (int noTest = 0; noTest < monomeTestInvalide.length; noTest++) {
	    	try {
    			Polynome test = new Polynome(monomeTestInvalide[noTest]);
    			test.limite(limiteATester[noTest]);
    			System.err.println("Test limite invalide n°"+ noTest + " non validé (les valeurs ont été accepté)");
	    	} catch (IllegalArgumentException echecAnormal) {
	    		System.out.println("Test limite invalide n°"+ noTest + " ok");
	    	}
    	}
    }
    	
=======
    
>>>>>>> branch 'master' of https://github.com/arthurpro81/sae_202_205_project.git
}
