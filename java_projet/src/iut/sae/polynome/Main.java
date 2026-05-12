/**
 * Main.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//TODO mettre le prog Main à part des autres programmes

/**
 * Classe principale qui lance le programme et les tests.
 */
public class Main {

    public static void main(String[] args) {

        // Lancement des tests pour vérifier que tout marches
        TestPolynome testeur = new TestPolynome();
        testeur.lancerTests();
       
        // Test methode ToString FIXME elle marche pas - voir dans classe Polynome
        
        double[] monomesTestAffichage = {1.0, 2.0, 3.0};
        Polynome polynomeTestAffichage = new Polynome(monomesTestAffichage);
        
        polynomeTestAffichage.toString();
        
    }
}