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
    /**
     * Vérifie le bon fonctionnement de la méthode Limite et retourne le bon résultat
     * @throws Exception - capture une erreur si le résultat retourné n'est pas le bon
     */
    private void testLimiteValide() throws Exception {
    	String[][] limiteValide = {
    			// test avec valeurs standard
    			{"3x^2+7x"},
    			{"-17x^6+88x^3+1"},
    			{"12x^5"},
    			{"-3x^3-7x+2"},
    			// test avec valeur particulière
    			{"x^7+4x^2-4"},
    			{"-x^4+5"},
    			{"777x+1"},
    			{"-10x-77"},
    			{"x^2+x"},
    			{"-x^25+7x^10-10"},
    			// cas monome uniquement x ou -x
    			{"x"},
    			{"-x"},
    			// cas monome constante
    			{"10"},
    			{"-4"},
    			{"0"}
    			
    	};
    }
}
