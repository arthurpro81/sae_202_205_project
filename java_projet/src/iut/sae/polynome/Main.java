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

        Polynome polynomeTestAffichage = new Polynome("3x^2 + 3x^10 - 23x^2 - 112");
        System.out.println("Affichage du polynome : " + polynomeTestAffichage.toString());
        System.out.println("Affichage de sa liste de monomes : " + polynomeTestAffichage.getMonomes());
        System.out.println("Affichage de sont degres : " + polynomeTestAffichage.degres());
        System.out.println("Affichage de s liste de ses coefficient : " + polynomeTestAffichage.coefficient());
    }
}