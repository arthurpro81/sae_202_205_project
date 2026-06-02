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
       
        Scanner entreeUti = new Scanner(System.in);
        
        System.out.print("Entrez un polynome : ");
        String saisiePoly = entreeUti.nextLine();
        entreeUti.close();
        
        Polynome polynomeUti = new Polynome(saisiePoly);
        Polynome polyCalc = new Polynome("9x^2-6+x^3-7");		//STUB de test
        String[] resultDivision = polynomeUti.division(polyCalc);
        
        System.out.println("Affichage du polynome Saisie: " + polynomeUti.toString());
        System.out.println("Affichage du polynome Calc: " + polyCalc.toString() + "\n");
        
        System.out.println("Affichage de sa liste de monomes : " + polynomeUti.getMonomes() + "\n");
        System.out.println("Affichage de sont degres : " + polynomeUti.MaxDegres());
        System.out.println("Affichage de sa liste de ses coefficient : " + polynomeUti.coefficient());
        
        System.out.println("Affichage de sa limite en + : " + polynomeUti.limite('+'));
        System.out.println("Affichage de sa limite en - : " + polynomeUti.limite('-'));
        
        System.out.println("Affichage de sa dérivé f': " + polynomeUti.derive(1));
        System.out.println("Affichage de sa dérivé f''''': " + polynomeUti.derive(5) + "\n");
        
        System.out.println("Addition Poly saisie + PolyCacl : " + polynomeUti.addition(polyCalc));
        System.out.println("Soustraction Poly saisie - PolyCacl : " + polynomeUti.soustraction(polyCalc));
        System.out.println("Multiplication Poly saisie * PolyCacl : " + polynomeUti.multiplication(polyCalc));
        System.out.println("Division Poly saisie / PolyCacl [Quotient | Reste] : "
        				   + "[" + resultDivision[0] + " | " + resultDivision[1] + "]");
    }
}