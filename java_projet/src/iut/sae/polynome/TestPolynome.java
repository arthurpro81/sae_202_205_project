/**
 * TestPolynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//lorsque les test ne seont plus dans le meme fichier mettre : package iut.sae.polynome.test;

/**
 * Classe de test pour vérifier si la création de polynomes fonctionne. Affiche
 * une erreur si un test échoue.
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
			testDegresMax();
			System.out.println("Test degrés valides : OK");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR degrés valides : " + echecAnormal.getMessage());
		}
		
	}

	/**
	 * Vérifie qu'on peut créer un polynôme normalement.
	 * 
	 * @throws Exception - capture une erreur si une combinaison est invalide
	 */
	private void testCreationValide() throws Exception {
		String[][] valide = {
				// TODO inserer des valeurs de test
				{ "3x" }, { "3x^2" }, { "3x^10-9x^2-3" } };
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
	 * 
	 * @throws Exception - capture une erreur si une combinaison est valide
	 */
	private void testCreationInvalide() throws Exception {
		String[][] invalide = {
				// TODO inserer des valeurs de test
				{ "3y" }, // OK : Erreur bien détecter
				{ "" }, // OK : Erreur bien détecters
				{ "3x^2s" }, // OK : Erreur bien détecterS
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


	private void testDegresMax() throws Exception {
	    String[][] valide = { 
	        { "1" }, { "42" }, { "x" }, { "3x" }, { "-3x" }, { "x^2" }, { "3x^2" }, { "3x^10" },
	        { "3x^2 + 2x + 1" }, { "3x^2+2x+1" }, { "3x^2 - 2x + 1" }, { "3x^2 - 2x - 1" }, { "-3x^2 + 2x + 1" },
	        { "-x + 3" }, { "3x^10 - 9x^2 - 3" }, { "5x^5 + 1" } 
	    };

	    int[] resultatAttendu = {
	        0, 0, 1, 1, 1, 2, 2, 10,
	        2, 2, 2, 2, 2,
	        1, 10, 5
	    };

	    boolean correct = true;

	    for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
	        try {
	            String[] combinaison = valide[numeroTest];
	            
	            Polynome valeurATester = new Polynome(combinaison[0]);
	            
	            int resultatRecupere = valeurATester.degres(); 
	            
	            boolean comparaison = (resultatRecupere == resultatAttendu[numeroTest]);
	            correct = comparaison; 
	            
	            if (correct) {
	                System.out.println("test degresMax n°" + numeroTest + " passé et le resultat est " + correct);
	            } else {
	                System.out.println("ECHEC test degresMax n°" + numeroTest + " sur " + combinaison[0] + 
	                                   " | Attendu : " + resultatAttendu[numeroTest] + " | Obtenu : " + resultatRecupere);
	            }

	        } catch (IllegalArgumentException echecAnormal) {
	            throw new Exception("ERR combinaison valide rejetée à tort, indice : " + numeroTest + " -> "
	                    + valide[numeroTest][0]);
	        }
	    }
	}

}