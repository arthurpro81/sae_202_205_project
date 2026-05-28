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
			testDegresInvalide();
			System.out.println("Test  de degres Invalide : OK ");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR sur le test de degres invalide : " + echecAnormal.getMessage());
		}

		try {
			testDegresValide();
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

	private void testDegresInvalide() throws Exception {
		String[][] invalide = {
				// variable invalide
				{ "3y" }, { "3y^2 + 1" },

				// chaine vide
				{ "" },

				// Caractere pas present dans un polynome
				{ "3x^2 + ?" }, { "3x^2 + $5" }, { "3x^2 + 2x!" }, { "3x^2 + 2.5x" },

				// format degres invalide
				{ "3x^2s" }, { "3x^" }, { "3x^^2" }, { "3x^2^1" },

				// opérateurs invalides
				{ "++2x" }, { "3x^2 * 2" }, { "3x^2 / 2" }, { "3x^2 % 2" },

				// erreur de structure des opereteur
				{ "+ 3x^2" }, { "3x^2 +" }, { "3x^2 + + 2" }, { "(3x^2 + 2)" }, };
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

	private void testDegresValide() throws Exception {
		String[][] valide = { { "1" }, { "42" }, { "x" }, { "3x" }, { "-3x" }, { "x^2" }, { "3x^2" }, { "3x^10" },
				{ "3x^2 + 2x + 1" }, { "3x^2+2x+1" }, { "3x^2 - 2x + 1" }, { "3x^2 - 2x - 1" }, { "-3x^2 + 2x + 1" },
				{ "-x + 3" }, { "3x^10 - 9x^2 - 3" }, { "5x^5 + 1" }, };

		for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
			try {
				String[] combinaison = valide[numeroTest];
				new Polynome(combinaison[0]);
				// Si on arrive ici sans exception : OK, c'est ce qu'on veut
			} catch (IllegalArgumentException echecAnormal) {
				// Si une exception est levée sur une chaîne valide : ERREUR
				throw new Exception("ERR combinaison valide rejetée à tort, indice : " + numeroTest + " -> "
						+ valide[numeroTest][0]);
			}
		}
	}

}