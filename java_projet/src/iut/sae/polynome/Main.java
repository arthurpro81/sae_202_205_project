/**
 * Main.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft)
 */
package iut.sae.polynome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//import java.util.Scanner;

/**
 * Classe principale qui lance le programme et les tests.
 */

/*
public class Main {

    public static void main(String[] args) {
        
        Scanner entreeUti = new Scanner(System.in);
        
        System.out.print("Entrez un polynome : ");
        String saisiePoly = entreeUti.nextLine();
        entreeUti.close();
        
        Polynome polynomeUti = new Polynome(saisiePoly);
        Polynome polyCalc = new Polynome("x-7");		//STUB de test
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
        System.out.println("P(x) x = " + polynomeUti.xEgale(2));
        System.out.println("racine -50|50: " + polynomeUti.racine(-50, 50));
    	
    }
}*/

/**
 * Classe principale qui initialise et lance l'interface graphique du calculateur.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du conteneur graphique depuis le fichier FXML
        	// Remplacer l'ancien chargement par un chemin absolu depuis la racine des packages
        	FXMLLoader loader = new FXMLLoader(Main.class.getResource("/iut/sae/polynome/CalculateurPolynome.fxml"));            Parent root = loader.load();

            // Configuration de la fenêtre principale (Stage)
            primaryStage.setTitle("Calculateur de Polynômes - SAE");
            primaryStage.setScene(new Scene(root, 1000, 750));
            
            // Définition de tailles minimales pour éviter que l'affichage ne se casse
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(650);
            
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur critique lors du chargement de l'interface FXML : ");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Démarre le cycle de vie de l'application JavaFX
        launch(args);
    }
}