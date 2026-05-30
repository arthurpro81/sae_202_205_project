/**
 * Main.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

import java.util.Scanner;

//TODO mettre le prog Main à part des autres programmes

/**
 * Classe principale qui lance le programme et les tests.
 */
public class Main {

    public static void main(String[] args) {

        // Lancement des tests pour vérifier que tout marches
        TestPolynome testeur = new TestPolynome();
        testeur.lancerTests();
       
        Scanner entreeUti = new Scanner(System.in);
        
        System.out.print("Entrez un polynome : ");
        String saisiePoly = entreeUti.nextLine();
        entreeUti.close();
        
        Polynome polynomeUti = new Polynome(saisiePoly);
        
        System.out.println("Affichage du polynome : " + polynomeUti.toString());
        System.out.println("Affichage de sa liste de monomes : " + polynomeUti.getMonomes() + "\n");
        System.out.println("Affichage de sont degres : " + polynomeUti.MaxDegres());
        System.out.println("Affichage de sa liste de ses coefficient : " + polynomeUti.coefficient());
        System.out.println("Affichage de sa limite en + : " + polynomeUti.limite('+'));
        System.out.println("Affichage de sa limite en - : " + polynomeUti.limite('-'));
        System.out.println("Affichage de sa dérivé f': " + polynomeUti.derive(1));
        System.out.println("Affichage de sa dérivé f''''': " + polynomeUti.derive(5));
        
    }
}