package iut.sae.polynome;

/**
 * Classe Polynome
 * Permet de créer un polynome à partir d'un tableau de coefficients.
 */
public class Polynome {

    /** Tableau des coefficients du polynome. */
    private double[] coefficients;

    /**
     * Constructeur : crée un polynôme à partir d'un tableau de coefficients.
     * @param coeffs Tableau de coefficients (ex: {3.0, 2.0, 1.0} pour 3x^2 + 2x + 1)
     * @throws IllegalArgumentException si le tableau est nul ou vide.
     */
    public Polynome(double[] coeffs) {
        if (coeffs == null || coeffs.length == 0) {
            throw new IllegalArgumentException("Erreur : Le tableau de coefficients ne peut pas être vide !");
        }
        this.coefficients = coeffs;
    }

    /**
     * Retourne les coefficients du polynôme.
     * @return le tableau de coefficients.
     */
    public double[] getCoefficients() {
        return this.coefficients;
    }

    /**
     * Affiche le polynôme sous forme de texte simple.
     */
    @Override
    public String toString() {
        String resultat = "";
        for (int parcour = 0; parcour < coefficients.length; parcour++) {
            resultat += coefficients[parcour];
        }
        return "Polynome[" + resultat + "]";
    }
}
