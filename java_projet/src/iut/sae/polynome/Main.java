package iut.sae.polynome;

/**
 * Classe principale qui lance le programme et les tests.
 */
public class Main {

    public static void main(String[] args) {

        // Lancement des tests pour vérifier que tout marches
        TestPolynome testeur = new TestPolynome();
        testeur.lancerTests();
    }
}