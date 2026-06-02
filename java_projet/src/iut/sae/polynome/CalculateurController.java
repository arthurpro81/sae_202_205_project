package iut.sae.polynome;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CalculateurController {

    // ================= POLYNÔME 1 =================
    @FXML private TextField poly1Input;
    @FXML private VBox poly1DetailsContainer;
    @FXML private Label poly1DegreeLabel;
    @FXML private Label poly1LimitsLabel;
    @FXML private Label poly1CoeffsLabel;
    @FXML private TextField poly1DerivOrder;
    @FXML private Button poly1SaveBtn;

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

    // ================= POLYNÔME 2 =================
    @FXML private TextField poly2Input;
    @FXML private VBox poly2DetailsContainer;
    @FXML private Label poly2DegreeLabel;
    @FXML private Label poly2LimitsLabel;
    @FXML private Label poly2CoeffsLabel;
    @FXML private TextField poly2DerivOrder;
    @FXML private Button poly2SaveBtn;

    // ================= BOUTON IMPORTER =================
    @FXML private Button importer;

    // Variables pour mémoriser le résultat courant de l'opération
    private String dernierResultatStr = "";
    private String operationActuelle = "";

    @FXML
    public void initialize() {
        // 1. Initialisation du sélecteur d'opération
        operationSelector.getItems().addAll("+", "-", "*", "/");
        operationSelector.setValue("+");

        // 2. Écouteurs pour mettre à jour les détails du Polynôme 1 en temps réel
        poly1Input.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mettreAJourDetailsPoly1();
            }
        });
        poly1DerivOrder.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mettreAJourDetailsPoly1();
            }
        });

        // 3. Écouteurs pour mettre à jour les détails du Polynôme 2 en temps réel
        poly2Input.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mettreAJourDetailsPoly2();
            }
        });
        poly2DerivOrder.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mettreAJourDetailsPoly2();
            }
        });

        // 4. Écouteur pour l'ordre de dérivation du résultat global
        calcDerivOrder.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mettreAJourDetailsResultat();
            }
        });

        // 5. Actions des boutons principaux
        calcBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                gererCalcul();
            }
        });
        backBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                gererRetour();
            }
        });

        // Actions secondaires (Sauvegardes / Import)
        poly1SaveBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Sauvegarde du Polynôme 1 : " + poly1Input.getText());
            }
        });
        poly2SaveBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Sauvegarde du Polynôme 2 : " + poly2Input.getText());
            }
        });
        calcSaveBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Sauvegarde du Résultat : " + dernierResultatStr);
            }
        });
        importer.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("Action d'importation lancée");
            }
        });
    }

    /**
     * Calcule et affiche les propriétés du Polynôme 1.
     */
    private void mettreAJourDetailsPoly1() {
        remplirDetailsMonomes(poly1Input, poly1DerivOrder, poly1DegreeLabel, poly1LimitsLabel, poly1CoeffsLabel);
    }

    /**
     * Calcule et affiche les propriétés du Polynôme 2.
     */
    private void mettreAJourDetailsPoly2() {
        remplirDetailsMonomes(poly2Input, poly2DerivOrder, poly2DegreeLabel, poly2LimitsLabel, poly2CoeffsLabel);
    }

    /**
     * Centralise la logique d'extraction et de calcul des propriétés pour les colonnes latérales.
     */
    private void remplirDetailsMonomes(TextField input, TextField orderInput, Label degLabel, Label limLabel, Label coeffLabel) {
        String texte = input.getText();
        if (texte == null || texte.trim().isEmpty()) {
            degLabel.setText("• son degrés le plus haut");
            limLabel.setText("• ses limite (en plus et moins l'infini)");
            coeffLabel.setText("• ses coefficients");
            return;
        }

        try {
            Polynome p = new Polynome(texte.trim());
            degLabel.setText("• Degré le plus haut : " + p.MaxDegres());
            limLabel.setText("• Limites : en +∞ : " + p.limite('+') + " | en -∞ : " + p.limite('-'));

            // Récupération de l'ordre de la dérivée
            int ordre = 1;
            String ordreTexte = orderInput.getText();
            if (ordreTexte != null && !ordreTexte.trim().isEmpty()) {
                try {
                    ordre = Integer.parseInt(ordreTexte.trim());
                    if (ordre <= 0) ordre = 1; // Sécurité de dérivation
                } catch (NumberFormatException e) {
                    // Si l'utilisateur saisit du texte non numérique, on garde 1 par défaut
                }
            }

            coeffLabel.setText("• Coefficients : " + p.coefficient() + "\n• Dérivée (ordre " + ordre + ") : " + p.derive(ordre));
        } catch (IllegalArgumentException e) {
            degLabel.setText("• Format invalide");
            limLabel.setText("• Exemple requis : 3x^2+2x-1");
            coeffLabel.setText("");
        }
    }

    /**
     * Déclenche le calcul entre le Polynôme 1 et le Polynôme 2 lors du clic sur "effectuer le calcul".
     */
    private void gererCalcul() {
        String saisie1 = poly1Input.getText();
        String saisie2 = poly2Input.getText();

        if (saisie1 == null || saisie1.trim().isEmpty() || saisie2 == null || saisie2.trim().isEmpty()) {
            return; // Saisies incomplètes
        }

        try {
            Polynome p1 = new Polynome(saisie1.trim());
            Polynome p2 = new Polynome(saisie2.trim());
            operationActuelle = operationSelector.getValue();

            // Rendre visible le conteneur du résultat (gère l'espace avec 'managed')
            calcResultContainer.setVisible(true);
            calcResultContainer.setManaged(true);

            if ("/".equals(operationActuelle)) {
                // Gestion spécifique de la division (renvoie un tableau [Quotient, Reste])
                calcDerivOrder.setDisable(true); // Désactive la dérivée car le résultat comporte deux entités
                String[] divisionResultat = p1.division(p2);
                calcDegreeLabel.setText("• Quotient : " + divisionResultat[0]);
                calcLimitsLabel.setText("• Reste : " + divisionResultat[1]);
                calcCoeffsLabel.setText("• Opération : Division Euclidienne");
                dernierResultatStr = "Q: " + divisionResultat[0] + " R: " + divisionResultat[1];
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

    /**
     * Met à jour l'affichage des propriétés du polynôme obtenu en résultat.
     */
    private void mettreAJourDetailsResultat() {
        if ("/".equals(operationActuelle) || dernierResultatStr.isEmpty()) {
            return;
        }

        try {
            Polynome pRes = new Polynome(dernierResultatStr);
            calcDegreeLabel.setText("• Degré le plus haut : " + pRes.MaxDegres());
            calcLimitsLabel.setText("• Limites : en +∞ : " + pRes.limite('+') + " | en -∞ : " + pRes.limite('-'));

            int ordre = 1;
            String ordreTexte = calcDerivOrder.getText();
            if (ordreTexte != null && !ordreTexte.trim().isEmpty()) {
                try {
                    ordre = Integer.parseInt(ordreTexte.trim());
                    if (ordre <= 0) ordre = 1;
                } catch (NumberFormatException e) {
                    // Reste à 1 si invalide
                }
            }
            calcCoeffsLabel.setText("• Coefficients : " + pRes.coefficient() + "\n• Dérivée (ordre " + ordre + ") : " + pRes.derive(ordre));
        } catch (IllegalArgumentException e) {
            calcDegreeLabel.setText("• Polynôme résultant : " + dernierResultatStr);
            calcLimitsLabel.setText("");
            calcCoeffsLabel.setText("");
        }
    }

    /**
     * Masque le conteneur de résultat lors du clic sur le bouton "retour".
     */
    private void gererRetour() {
        calcResultContainer.setVisible(false);
        calcResultContainer.setManaged(false);
    }
}