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
       
        
   
        Polynome polynomeTestAffichage = new Polynome("3x^2+3x^10-10");
        System.out.println(polynomeTestAffichage.toString());
        System.out.println(polynomeTestAffichage.getMonomes());
        System.out.println(polynomeTestAffichage.degres());
 
    }
}