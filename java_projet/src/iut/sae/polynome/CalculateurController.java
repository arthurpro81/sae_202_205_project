package iut.sae.polynome;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contrôleur de l'application de calcul de polynômes.
 * Gère l'interface, les calculs, la sauvegarde et l'importation.
 */
public class CalculateurController {

    // ================= POLYNÔME 1 =================
    @FXML private TextField poly1Input;
    @FXML private VBox poly1DetailsContainer;
    @FXML private Label poly1DegreeLabel;
    @FXML private Label poly1LimitsLabel;
    @FXML private Label poly1CoeffsLabel;
    @FXML private TextField poly1DerivOrder;
    @FXML private Button poly1SaveBtn;

    // --- P(x) poly1 ---
    @FXML private TextField poly1PxInput;
    @FXML private Button poly1PxBtn;
    @FXML private Label poly1PxLabel;

    // ================= COLONNE CENTRALE (OPÉRATIONS) =================
    @FXML private ComboBox<String> operationSelector;
    @FXML private Button calcBtn;

    // ================= BLOC RÉSULTAT =================
    @FXML private VBox calcResultContainer;
    @FXML private Label calcDegreeLabel;
    @FXML private Label calcLimitsLabel;
    @FXML private Label calcCoeffsLabel;
    @FXML private TextField calcDerivOrder;
    @FXML private Button calcSaveBtn;
    @FXML private Button backBtn;

    // --- P(x) résultat ---
    @FXML private TextField calcPxInput;
    @FXML private Button calcPxBtn;
    @FXML private Label calcPxLabel;

    // ================= POLYNÔME 2 =================
    @FXML private TextField poly2Input;
    @FXML private VBox poly2DetailsContainer;
    @FXML private Label poly2DegreeLabel;
    @FXML private Label poly2LimitsLabel;
    @FXML private Label poly2CoeffsLabel;
    @FXML private TextField poly2DerivOrder;
    @FXML private Button poly2SaveBtn;


    // --- P(x) poly2 ---
    @FXML private TextField poly2PxInput;
    @FXML private Button poly2PxBtn;
    @FXML private Label poly2PxLabel;

    // ================= BOUTON IMPORTER =================
    @FXML private Button importer;

    // Variables pour mémoriser le résultat courant de l'opération
    private String dernierResultatStr = "";
    private String operationActuelle = "";

    /**
     * Initialisation du contrôleur.
     */
    @FXML
    public void initialize() {
        operationSelector.getItems().addAll("+", "-", "*", "/");
        operationSelector.setValue("+");

        // Écouteurs temps réel
        poly1Input.textProperty().addListener((obs, old, newValue) -> mettreAJourDetailsPoly1());
        poly1DerivOrder.textProperty().addListener((obs, old, newValue) -> mettreAJourDetailsPoly1());
        poly2Input.textProperty().addListener((obs, old, newValue) -> mettreAJourDetailsPoly2());
        poly2DerivOrder.textProperty().addListener((obs, old, newValue) -> mettreAJourDetailsPoly2());
        calcDerivOrder.textProperty().addListener((obs, old, newValue) -> mettreAJourDetailsResultat());

        // Actions boutons calcul / retour
        calcBtn.setOnAction(event -> gererCalcul());
        backBtn.setOnAction(event -> gererRetour());

        // Actions sauvegarde
        poly1SaveBtn.setOnAction(event -> {
            Stage stage = (Stage) poly1SaveBtn.getScene().getWindow();
            sauvegarderPolynome(stage, "sauvegarde_poly1.txt", poly1Input.getText(),
                    poly1DegreeLabel.getText(), poly1LimitsLabel.getText(), poly1CoeffsLabel.getText());
        });
        poly2SaveBtn.setOnAction(event -> {
            Stage stage = (Stage) poly2SaveBtn.getScene().getWindow();
            sauvegarderPolynome(stage, "sauvegarde_poly2.txt", poly2Input.getText(),
                    poly2DegreeLabel.getText(), poly2LimitsLabel.getText(), poly2CoeffsLabel.getText());
        });
        calcSaveBtn.setOnAction(event -> {
            Stage stage = (Stage) calcSaveBtn.getScene().getWindow();
            sauvegarderPolynome(stage, "sauvegarde_resultat.txt", dernierResultatStr,
                    calcDegreeLabel.getText(), calcLimitsLabel.getText(), calcCoeffsLabel.getText());
        });

        // Actions P(x)
        poly1PxBtn.setOnAction(event -> calculerPx(poly1Input, poly1PxInput, poly1PxLabel));
        poly2PxBtn.setOnAction(event -> calculerPx(poly2Input, poly2PxInput, poly2PxLabel));
        calcPxBtn.setOnAction(event -> calculerPxResultat());

        // ACTION IMPORTER
        importer.setOnAction(event -> {
            Stage stage = (Stage) importer.getScene().getWindow();
            gererImportation(stage);
        });
    }

   

    // =========================================================
    //  P(x)
    // =========================================================

    /**
     * Calcule P(valeur) pour le polynôme saisi dans inputField,
     * avec la valeur saisie dans pxField. Affiche dans resultLabel.
     */
    private void calculerPx(TextField inputField, TextField pxField, Label resultLabel) {
        String texte = inputField.getText();
        if (texte == null || texte.trim().isEmpty()) {
            resultLabel.setText("• Aucun polynôme saisi.");
            return;
        }
        try {
            Polynome p = new Polynome(texte.trim());
            double valeurX = Double.parseDouble(pxField.getText().trim());
            double resultat = p.xEgale(valeurX);

            // Affichage propre : entier si la valeur est entière
            String affichage = (resultat == Math.floor(resultat) && !Double.isInfinite(resultat))
                    ? String.valueOf((long) resultat)
                    : String.valueOf(resultat);

            resultLabel.setText("• P(" + valeurX + ") = " + affichage);
        } catch (NumberFormatException e) {
            resultLabel.setText("• Valeur x invalide (nombre requis).");
        } catch (IllegalArgumentException e) {
            resultLabel.setText("• Erreur : " + e.getMessage());
        }
    }

    /**
     * Variante P(x) pour le panneau résultat.
     */
    private void calculerPxResultat() {
        if (dernierResultatStr == null || dernierResultatStr.isEmpty()) {
            calcPxLabel.setText("• Aucun résultat disponible.");
            return;
        }
        try {
            Polynome p = new Polynome(dernierResultatStr);
            double valeurX = Double.parseDouble(calcPxInput.getText().trim());
            double resultat = p.xEgale(valeurX);

            String affichage = (resultat == Math.floor(resultat) && !Double.isInfinite(resultat))
                    ? String.valueOf((long) resultat)
                    : String.valueOf(resultat);

            calcPxLabel.setText("• P(" + valeurX + ") = " + affichage);
        } catch (NumberFormatException e) {
            calcPxLabel.setText("• Valeur x invalide (nombre requis).");
        } catch (IllegalArgumentException e) {
            calcPxLabel.setText("• Erreur : " + e.getMessage());
        }
    }

    // =========================================================
    //  DÉTAILS TEMPS RÉEL
    // =========================================================

    private void mettreAJourDetailsPoly1() {
        remplirDetailsMonomnes(poly1Input, poly1DerivOrder, poly1DegreeLabel, poly1LimitsLabel, poly1CoeffsLabel);
    }

    private void mettreAJourDetailsPoly2() {
        remplirDetailsMonomnes(poly2Input, poly2DerivOrder, poly2DegreeLabel, poly2LimitsLabel, poly2CoeffsLabel);
    }

    private void remplirDetailsMonomnes(TextField input, TextField orderInput,
                                         Label degLabel, Label limLabel, Label coeffLabel) {
        String texte = input.getText();
        if (texte == null || texte.trim().isEmpty()) {
            degLabel.setText("• Degrés le plus haut :");
            limLabel.setText("• Limite (en plus et moins l'infini) :");
            coeffLabel.setText("• Coefficient :");
            return;
        }

        try {
            Polynome p = new Polynome(texte.trim());
            degLabel.setText("• Polynôme canonisé: " + p.toString() + "\n• Degré le plus haut : " + p.MaxDegres());
            limLabel.setText("• Limites : en +∞ : " + p.limite('+') + " | en -∞ : " + p.limite('-'));

            int ordre = 1;
            String ordreTexte = orderInput.getText();
            if (ordreTexte != null && !ordreTexte.trim().isEmpty()) {
                try {
                    ordre = Integer.parseInt(ordreTexte.trim());
                    if (ordre <= 0) ordre = 1;
                } catch (NumberFormatException e) { /* garder ordre=1 */ }
            }
            coeffLabel.setText("• Coefficients : " + p.coefficient()
                    + "\n• Dérivée (ordre " + ordre + ") : " + p.derive(ordre));
        } catch (IllegalArgumentException e) {
            degLabel.setText("• Format invalide");
            limLabel.setText("• Exemple requis : 3x^2+2x-1");
            coeffLabel.setText("");
        }
    }

    // =========================================================
    //  CALCUL OPÉRATION
    // =========================================================

    private void gererCalcul() {
        String saisie1 = poly1Input.getText();
        String saisie2 = poly2Input.getText();

        if (saisie1 == null || saisie1.trim().isEmpty()
                || saisie2 == null || saisie2.trim().isEmpty()) {
            return;
        }

        try {
            Polynome p1 = new Polynome(saisie1.trim());
            Polynome p2 = new Polynome(saisie2.trim());
            operationActuelle = operationSelector.getValue();

            calcResultContainer.setVisible(true);
            calcResultContainer.setManaged(true);

            calcPxLabel.setText("• P(x) :");

            if ("/".equals(operationActuelle)) {
                calcDerivOrder.setDisable(true);
                calcDegreeLabel.setText("• Calcul en cours...");
                calcLimitsLabel.setText("");
                calcCoeffsLabel.setText("");

                Task<String[]> task = new Task<>() {
                    @Override
                    protected String[] call() throws Exception {
                        return p1.division(p2);
                    }
                };

                task.setOnSucceeded(event -> {
                    String[] divisionResultat = task.getValue();
                    calcDegreeLabel.setText("• Quotient : " + divisionResultat[0]);
                    calcLimitsLabel.setText("• Reste : " + divisionResultat[1]);
                    calcCoeffsLabel.setText("• Opération : Division Euclidienne");
                    dernierResultatStr = divisionResultat[0]; // on retient le quotient pour P(x)/racines
                });

                task.setOnFailed(event -> {
                    Throwable ex = task.getException();
                    calcDegreeLabel.setText("• Erreur lors du calcul");
                    calcLimitsLabel.setText("• " + (ex != null ? ex.getMessage() : "Erreur inconnue"));
                    calcCoeffsLabel.setText("");
                });

                new Thread(task).start();

            } else {
                calcDerivOrder.setDisable(false);
                if ("+".equals(operationActuelle)) {
                    dernierResultatStr = p1.addition(p2);
                } else if ("-".equals(operationActuelle)) {
                    dernierResultatStr = p1.soustraction(p2);
                } else if ("*".equals(operationActuelle)) {
                    dernierResultatStr = p1.multiplication(p2);
                }
                mettreAJourDetailsResultat();
            }
        } catch (Exception e) {
            calcResultContainer.setVisible(true);
            calcResultContainer.setManaged(true);
            calcDegreeLabel.setText("• Erreur lors du calcul");
            calcLimitsLabel.setText("• " + e.getMessage());
            calcCoeffsLabel.setText("");
        }
    }

    private void mettreAJourDetailsResultat() {
        if ("/".equals(operationActuelle) || dernierResultatStr.isEmpty()) {
            return;
        }

        try {
            Polynome pRes = new Polynome(dernierResultatStr);
            calcDegreeLabel.setText("• Polynome canonisé: " + pRes.toString()
                    + "\n• Degré le plus haut : " + pRes.MaxDegres());
            calcLimitsLabel.setText("• Limites : en +∞ : " + pRes.limite('+')
                    + " | en -∞ : " + pRes.limite('-'));

            int ordre = 1;
            String ordreTexte = calcDerivOrder.getText();
            if (ordreTexte != null && !ordreTexte.trim().isEmpty()) {
                try {
                    ordre = Integer.parseInt(ordreTexte.trim());
                    if (ordre <= 0) ordre = 1;
                } catch (NumberFormatException e) { /* garder ordre=1 */ }
            }
            calcCoeffsLabel.setText("• Coefficients : " + pRes.coefficient()
                    + "\n• Dérivée (ordre " + ordre + ") : " + pRes.derive(ordre));
        } catch (IllegalArgumentException e) {
            calcDegreeLabel.setText("• Polynôme résultant : " + dernierResultatStr);
            calcLimitsLabel.setText("");
            calcCoeffsLabel.setText("");
        }
    }

    private void gererRetour() {
        calcResultContainer.setVisible(false);
        calcResultContainer.setManaged(false);
        dernierResultatStr = "";
        operationActuelle = "";
        calcPxLabel.setText("• P(x) :");
    }

    // =========================================================
    //  IMPORTATION / SAUVEGARDE
    // =========================================================

    /**
     * Gère l'importation de polynômes depuis un fichier .txt
     * Format attendu : [polynome1] [polynome2] (séparés par un espace)
     */
    private void gererImportation(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importer des polynômes");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextLine()) {
                    String ligne = scanner.nextLine().trim();
                    if (!ligne.isEmpty()) {
                        String[] parts = ligne.split("\\s+");
                        if (parts.length >= 1) {
                            try {
                                new Polynome(parts[0]);
                                poly1Input.setText(parts[0]);
                                if (parts.length >= 2) {
                                    new Polynome(parts[1]);
                                    poly2Input.setText(parts[1]);
                                } else {
                                    poly2Input.setText("");
                                }
                                System.out.println("Importation réussie.");
                            } catch (IllegalArgumentException e) {
                                System.err.println("Format de polynôme invalide dans le fichier.");
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("Fichier non trouvé : " + e.getMessage());
            }
        }
    }

    private void sauvegarderPolynome(Stage stage, String nomFichierSuggest, String polynome,
                                      String degre, String limites, String coeffs) {
        if (polynome == null || polynome.trim().isEmpty()) {
            System.out.println("Rien à sauvegarder.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le polynôme");
        fileChooser.setInitialFileName(nomFichierSuggest);

        File dossierInitial = new File(System.getProperty("user.dir") + "/Sauvegarde_Polynome");
        if (dossierInitial.exists() && dossierInitial.isDirectory()) {
            fileChooser.setInitialDirectory(dossierInitial);
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (PrintStream fichier = new PrintStream(file)) {
                fichier.println(polynome);
                fichier.println(degre);
                fichier.println(limites);
                fichier.println(coeffs);
                fichier.println("==============================");
                System.out.println("Sauvegarde réussie dans : " + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.err.println("Erreur de sauvegarde : " + e.getMessage());
            }
        }
    }
}