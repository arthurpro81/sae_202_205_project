/**
 * TestPolynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//lorsque les test ne seont plus dans le meme fichier mettre : package iut.sae.polynome.test;

import java.lang.*;

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
			testDegreClassique();
			System.out.println("Test degré classique : OK");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR sur degré classique : " + echecAnormal.getMessage());
		}

		try {
			testDegreZerosDebut();
			System.out.println("Test degré avec zéros au début : OK");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR sur degré avec zéros au début : " + echecAnormal.getMessage());
		}

		try {
			testDegreConstante();
			System.out.println("Test degré constante : OK");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR sur degré constante : " + echecAnormal.getMessage());
		}

		try {
			testDegrePolynomeNul();
			System.out.println("Test degré polynôme nul : OK");
		} catch (Exception echecAnormal) {
			System.err.println("ERREUR sur degré polynôme nul : " + echecAnormal.getMessage());
		}

		System.out.println("Fin des tests.");
	}

	/**
	 * Vérifie qu'on peut créer un polynôme normalement.
	 */
	private void testCreationValide() throws Exception {
		double[] monomesTest = { 1.0, 2.0 };
		Polynome polynomeTest = new Polynome(monomesTest);
		if (polynomeTest.getMonomes() == null) {
			throw new Exception("Le polynôme a été créé mais les coefficients sont nuls !");
		}
	}

	/**
	 * Vérifie que le programme détecte bien une erreur si on donne un tableau vide.
	 */
	private void testCreationInvalide() throws Exception {
		try {
			new Polynome(null);
			throw new Exception("Le programme aurait dû afficher une erreur pour un tableau nul !");
		} catch (IllegalArgumentException attendue) {
			// C'est normal, le test a réussi car l'erreur a été détectée
		}
	}

	/**
	 * Vérifie le calcul du degré pour un polynôme classique sans zéros au début.
	 */
	private void testDegreClassique() throws Exception {
		// Représente 3x^2 + 2x + 1
		double[] monomesTest = { 3.0, 2.0, 1.0 };
		Polynome p = new Polynome(monomesTest);
		if (p.degres() != 2) {
			throw new Exception("Attendu 2, obtenu " + p.degres());
		}
	}

	/**
	 * Vérifie que les zéros situés au début du tableau (ex: 0x^3 + 2x^2) sont bien
	 * ignorés.
	 */
	private void testDegreZerosDebut() throws Exception {
		// Représente 0x^3 + 2x^2 + 0x + 1
		double[] monomesTest = { 0.0, 2.0, 0.0, 1.0 };
		Polynome p = new Polynome(monomesTest);
		if (p.degres() != 2) {
			throw new Exception("Attendu 2, obtenu " + p.degres());
		}
	}

	/**
	 * Vérifie le calcul du degré pour une simple constante.
	 */
	private void testDegreConstante() throws Exception {
		// Représente la constante 5.0
		double[] monomesTest = { 5.0 };
		Polynome p = new Polynome(monomesTest);
		if (p.degres() != 0) {
			throw new Exception("Le degré d'une constante doit être 0, obtenu " + p.degres());
		}
	}

	/**
	 * Vérifie le calcul du degré pour le fameux polynôme nul (N(X) = 0.0).
	 */
	private void testDegrePolynomeNul() throws Exception {
		// Représente 0x^2 + 0x + 0
		double[] monomesTest = { 0.0, 0.0, 0.0 };
		Polynome p = new Polynome(monomesTest);
		if (p.degres() != -1) {
			throw new Exception("Le degré du polynôme nul doit être -1, obtenu " + p.degres());
		}
	}
}
