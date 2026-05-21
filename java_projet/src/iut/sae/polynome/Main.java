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
       
        
        //test
        Polynome polynomeTestAffichage = new Polynome("3x^3+2x^2");
        System.out.println("Affichage du polynome : " + polynomeTestAffichage.toString());
        System.out.println("Affichage de sa liste de monomes : " + polynomeTestAffichage.getMonomes());
        System.out.println("Affichage de sont degres : " + polynomeTestAffichage.degres());
        System.out.println("Affichage de sa liste de ses coefficient : " + polynomeTestAffichage.coefficient());
        System.out.println("Affichage de sa limite en + : " + polynomeTestAffichage.limite('+'));
        System.out.println("Affichage de sa limite en - : " + polynomeTestAffichage.limite('-'));
        System.out.println("Affichage de sa dérivé : " + polynomeTestAffichage.derive(1));
    }
}