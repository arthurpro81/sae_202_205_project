/**
 * TestPolynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//lorsque les test ne seont plus dans le meme fichier mettre : package iut.sae.polynome.test;

import java.lang.*;

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

        System.out.println("Fin des tests.");
    }

    /**
     * Vérifie qu'on peut créer un polynôme normalement.
     */
    private void testCreationValide() throws Exception {
        double[] monomesTest = {1.0, 2.0};
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
         * Teste la création de polynômes avec différents jeux de racines
         */
        private void testRacinesValides() {
            // Jeu de données : {coeffLeader, racine1, mult1, racine2, mult2...}
            // Pour simplifier en BUT1, on va tester des cas fixes
            
            System.out.println("Test de création :");

            // Cas 1 : (x - 2)
            Polynome p1 = new Polynome(1.0, new double[]{2.0}, new int[]{1});
            afficherResultat("P(x) = x - 2", p1.toString().contains("2.0") && p1.toString().contains("1.0"));

            // Cas 2 : (x - 1)^2 = x^2 - 2x + 1
            Polynome p2 = new Polynome(1.0, new double[]{1.0}, new int[]{2});
            afficherResultat("P(x) = (x - 1)^2", p2.toString().contains("1.0") && p2.toString().contains("2.0"));

            // Cas 3 : 2(x - 3)(x + 3) = 2x^2 - 18
            Polynome p3 = new Polynome(2.0, new double[]{3.0, -3.0}, new int[]{1, 1});
            afficherResultat("P(x) = 2(x-3)(x+3)", p3.toString().contains("18.0"));
            
            // Cas 4 : 4(x - 1)^7
            Polynome p4 = new Polynome(4.0, new double[]{1.0}, new int[]{7});
            afficherResultat("P(x) = 4(x - 1)^7", p4.toString().contains("4.0x^7"));
        }

        /**
         * Vérifie que P(racine) est bien égal à 0
         */
        private void testEvaluationSurRacines() {
            // On définit des racines à tester
            double[][] lesRacines = {
                {2.0},          // Test 1
                {1.0},          // Test 2
                {3.0, -3.0},    // Test 3
                {1.0}           // Test 4
            };
            
            int[][] lesMult = {
                {1},
                {2},
                {1, 1},
                {7}
            };

            double[] lesCoeffsLeaders = {1.0, 1.0, 2.0,4.0};

            for (int i = 0; i < lesRacines.length; i++) {
                Polynome p = new Polynome(lesCoeffsLeaders[i], lesRacines[i], lesMult[i]);
                
                System.out.println("Vérification du polynôme : " + p);
                
                for (double racine : lesRacines[i]) {
                    double result = p.calculer(racine);
                    // En informatique, avec les double, on teste si c'est très proche de 0
                    boolean ok = Math.abs(result) < 1e-9; 
                    afficherResultat("  Vérification racine " + racine, ok);
                    if (!ok) {
                        System.out.println("    ECHEC : P(" + racine + ") = " + result);
                    }
                }
            }
        }

        /**
         * Utilitaire pour afficher proprement le succès ou l'échec
         */
        private void afficherResultat(String nomTest, boolean succes) {
            if (succes) {
                System.out.println("[ OK ] " + nomTest);
            } else {
                System.out.println("[ERREUR] " + nomTest);
            }
        }
    }
}
