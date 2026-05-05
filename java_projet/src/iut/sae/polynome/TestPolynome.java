package iut.sae.polynome;

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
        } catch (Exception e) {
            System.err.println("ERREUR sur le test de création valide : " + e.getMessage());
        }

        try {
            testCreationInvalide();
            System.out.println("Test création invalide : OK (l'erreur a bien été détectée)");
        } catch (Exception e) {
            System.err.println("ERREUR sur le test de création invalide : " + e.getMessage());
        }

        System.out.println("Fin des tests.");
    }

    /**
     * Vérifie qu'on peut créer un polynôme normalement.
     */
    private void testCreationValide() throws Exception {
        double[] c = {1.0, 2.0};
        Polynome p = new Polynome(c);
        if (p.getCoefficients() == null) {
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
        } catch (IllegalArgumentException e) {
            // C'est normal, le test a réussi car l'erreur a été détectée
        }
    }
}
