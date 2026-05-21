/**
 * PolynomeTests.java                                        05/05/2026
 * IUT Toulouse Capitole - no copyright (copyleft)
 */
package iut.sae.polynome;

/**
 * Tests unitaires (sans JUnit) de iut.sae.polynome.Polynome
 * Pattern identique à DateTests.java :
 *   - Exception interne TestException
 *   - Fixture de polynômes valides partagée entre toutes les méthodes de test
 *   - Une méthode de test par comportement
 *   - Arrêt immédiat au premier échec avec message explicite
 *
 * Convention du tableau Monomes[] :
 *   index 0       = coefficient du monôme de plus haut degré
 *   index dernier = terme constant (a0)
 *   Ex : {3.0, 2.0, 1.0} représente 3x^2 + 2x + 1
 */
public class TestPolynome {


    private static class TestException extends RuntimeException {

        /** Constructeur sans message */
        public TestException() {
            super();
        }

        /**
         * Constructeur avec message décrivant la cause de l'échec
         * @param message texte décrivant l'échec
         */
        public TestException(String message) {
            super(message);
        }
    }

    /*
     *FIXTURE : jeu de polynômes valides partagé entre les tests d'instance
     *Initialisé à la construction — doit rester cohérent avec tous les tableaux
     *ATTENDU définis dans chaque méthode de test.
     *Chaque entrée est commentée avec sa représentation mathématique.
     *
     */


    private final Polynome[] FIXTURE_POLYNOMES_OK = {

        // [0]  1                                (polynôme constant non nul, degré 0)
        new Polynome(new double[]{ 1.0 }),

        // [1]  2x + 1                           (degré 1, coefficients positifs)
        new Polynome(new double[]{ 2.0, 1.0 }),

        // [2]  3x^2 + 2x + 1                   (degré 2, classique)
        new Polynome(new double[]{ 3.0, 2.0, 1.0 }),

        // [3]  -4x^3 - 2x + 5                  (degré 3, coefs négatifs, zéro intermédiaire)
        new Polynome(new double[]{ -4.0, 0.0, -2.0, 5.0 }),

        // [4]  x^4                              (degré 4, un seul coef non nul en tête)
        new Polynome(new double[]{ 1.0, 0.0, 0.0, 0.0, 0.0 }),

        // [5]  3.1x^5 + 2.7x^3 - 4.1x + 8.0   (exemple du sujet SAE, degré 5)
        new Polynome(new double[]{ 3.1, 0.0, 2.7, 0.0, -4.1, 8.0 }),

        // [6]  2x + 1  (avec zéros en tête)  {0.0, 0.0, 2.0, 1.0}
        //      => degré réel = 1 car les zéros en tête sont ignorés
        new Polynome(new double[]{ 0.0, 0.0, 2.0, 1.0 }),

        // [7]  polynôme nul  {0.0, 0.0, 0.0}
        //      => degré = -1  (convention pour le polynôme nul)
        new Polynome(new double[]{ 0.0, 0.0, 0.0 }),

        // [8]  -x + 3                           (coefficient négatif en tête)
        new Polynome(new double[]{ -1.0, 3.0 }),

        // [9]  x^2 - 1                          (racines réelles ±1, zéro intermédiaire)
        new Polynome(new double[]{ 1.0, 0.0, -1.0 }),
    };


    /** Crée un objet de test. */
    public TestPolynome() {
        super();
    }


    /**
     * Point d'entrée : crée un testeur et lance tous les tests.
     * @param args non utilisé
     */
    public static void main(String[] args) {
        new TestPolynome().lancerTests();
    }

    /**
     * Lance tous les tests unitaires dans l'ordre.
     * S'arrête au premier échec et affiche la cause précise.
     */
    public void lancerTests() {
        try {
            testEchecsConstructeur();
            System.out.println("testEchecsConstructeur    : OK");

            testValidesConstructeur();
            System.out.println("testValidesConstructeur   : OK");

            testGetMonomes();
            System.out.println("testGetMonomes            : OK");

            testDegres();
            System.out.println("testDegres                : OK");

            testToString();
            System.out.println("testToString              : OK");

            // --- TDD : décommenter quand les méthodes seront implémentées ---
            // testCoefficient();
            // testRacines();
            // testLimites();
            // testValeurEn();
            // testDerivee();
            // testAddition();
            // testMultiplicationScalaire();

            System.out.println("\nTous les tests ont réussi.");

        } catch (TestException echecTest) {
            System.err.println("\nECHEC : " + echecTest.getMessage());
        }
    }

    
    /**
     * Vérifie que le constructeur lève bien IllegalArgumentException
     * pour chaque entrée invalide (null ou tableau vide).
     * @throws TestException si un tableau invalide est accepté sans exception
     */
    private void testEchecsConstructeur() {

        // null : testé séparément car ne peut pas figurer dans un double[][]
        try {
            new Polynome(null);
            throw new TestException(
                "testEchecsConstructeur - tableau null accepté sans exception !");
        } catch (IllegalArgumentException attendue) {
            // OK
        }

        // Tous les autres cas invalides sous forme de tableau 2D
        double[][] invalides = {
            {},   // tableau vide
        };

        for (int noJeuInvalide = 0; noJeuInvalide < invalides.length; noJeuInvalide++) {
            try {
                new Polynome(invalides[noJeuInvalide]);
                throw new TestException(
                    "testEchecsConstructeur - jeu invalide n°" + noJeuInvalide
                    + " accepté sans exception : "
                    + java.util.Arrays.toString(invalides[noJeuInvalide]));
            } catch (IllegalArgumentException attendue) {
                // OK
            }
        }
    }

    /**
     * Vérifie que le constructeur accepte sans exception les mêmes tableaux
     * que la fixture (tous doivent être valides).
     * @throws TestException si une instanciation valide lève une exception
     */
    private void testValidesConstructeur() {

        double[][] valides = {
            { 1.0 },
            { 2.0, 1.0 },
            { 3.0, 2.0, 1.0 },
            { -4.0, 0.0, -2.0, 5.0 },
            { 1.0, 0.0, 0.0, 0.0, 0.0 },
            { 3.1, 0.0, 2.7, 0.0, -4.1, 8.0 },
            { 0.0, 0.0, 2.0, 1.0 },
            { 0.0, 0.0, 0.0 },
            { -1.0, 3.0 },
            { 1.0, 0.0, -1.0 },
        };

        for (int noJeuValide = 0; noJeuValide < valides.length; noJeuValide++) {
            try {
                new Polynome(valides[noJeuValide]);
            } catch (IllegalArgumentException echecAnormal) {
                throw new TestException(
                    "testValidesConstructeur - jeu valide n°" + noJeuValide
                    + " rejeté à tort : " + echecAnormal.getMessage());
            }
        }
    }


    /**
     * Vérifie que getMonomes() retourne un tableau de valeurs identiques
     * (même longueur, mêmes coefficients) à celui passé au constructeur.
     * @throws TestException si les tableaux diffèrent
     */
    private void testGetMonomes() {

        double[][] attendus = {
            { 1.0 },
            { 2.0, 1.0 },
            { 3.0, 2.0, 1.0 },
            { -4.0, 0.0, -2.0, 5.0 },
            { 1.0, 0.0, 0.0, 0.0, 0.0 },
            { 3.1, 0.0, 2.7, 0.0, -4.1, 8.0 },
            { 0.0, 0.0, 2.0, 1.0 },
            { 0.0, 0.0, 0.0 },
            { -1.0, 3.0 },
            { 1.0, 0.0, -1.0 },
        };

        for (int noPolynome = 0; noPolynome < attendus.length; noPolynome++) {
            if (!java.util.Arrays.equals(attendus[noPolynome],
                                         FIXTURE_POLYNOMES_OK[noPolynome].getMonomes())) {
                throw new TestException(
                    "testGetMonomes - fixture n°" + noPolynome
                    + " " + java.util.Arrays.toString(FIXTURE_POLYNOMES_OK[noPolynome].getMonomes())
                    + " => attendu = " + java.util.Arrays.toString(attendus[noPolynome])
                    + ", obtenu = "
                    + java.util.Arrays.toString(FIXTURE_POLYNOMES_OK[noPolynome].getMonomes()));
            }
        }
    }

    

    /**
     * Vérifie la méthode degres() sur chaque polynôme de la fixture.
     * Conventions :
     *   - degré = longueur du tableau - 1 - nombre de zéros en tête ignorés
     *   - polynôme nul (tous coefficients à 0.0) => -1
     *   - les zéros en tête de tableau sont ignorés
     * @throws TestException si le degré calculé ne correspond pas à l'attendu
     */
    private void testDegres() {

        final int[] DEGRES_ATTENDUS = {
             0,  // [0]  {1.0}                        => 1
             1,  // [1]  {2.0, 1.0}                   => 2x + 1
             2,  // [2]  {3.0, 2.0, 1.0}              => 3x^2 + 2x + 1
             3,  // [3]  {-4.0, 0.0, -2.0, 5.0}       => -4x^3 - 2x + 5
             4,  // [4]  {1.0, 0.0, 0.0, 0.0, 0.0}    => x^4
             5,  // [5]  {3.1, 0.0, 2.7, 0.0, -4.1, 8.0}
             1,  // [6]  {0.0, 0.0, 2.0, 1.0}         => zéros en tête ignorés
            -1,  // [7]  {0.0, 0.0, 0.0}              => polynôme nul
             1,  // [8]  {-1.0, 3.0}                  => -x + 3
             2,  // [9]  {1.0, 0.0, -1.0}             => x^2 - 1
        };

        for (int noPolynome = 0; noPolynome < DEGRES_ATTENDUS.length; noPolynome++) {
            if (DEGRES_ATTENDUS[noPolynome] != FIXTURE_POLYNOMES_OK[noPolynome].degres()) {
                throw new TestException(
                    "testDegres - fixture n°" + noPolynome
                    + " " + java.util.Arrays.toString(FIXTURE_POLYNOMES_OK[noPolynome].getMonomes())
                    + " => attendu = " + DEGRES_ATTENDUS[noPolynome]
                    + ", obtenu = " + FIXTURE_POLYNOMES_OK[noPolynome].degres());
            }
        }
    }

    /**
     * Vérifie la méthode toString() sur chaque polynôme de la fixture.
     * Format (puissances décroissantes, termes nuls omis) :
     *   - terme constant seul   : "7.0"
     *   - terme en X            : "2.0X"
     *   - terme en X^n (n >= 2) : "3.0X^2"
     *   - séparateur positif    : " + "
     *   - séparateur négatif    : " - "  (le signe est absorbé dans le séparateur)
     *   - polynôme nul          : "0.0"
     * @throws TestException si l'affichage ne correspond pas à l'attendu
     */
    private void testToString() {

        final String[] STRINGS_ATTENDUS = {
            "1.0",                                  
            "2.0X + 1.0",                           
            "3.0X^2 + 2.0X + 1.0",                 
            "-4.0X^3 - 2.0X + 5.0",                
            "3.1X^5 + 2.7X^3 - 4.1X + 8.0",      
            "2.0X + 1.0",                           
            "0.0",                                  
            "-1.0X + 3.0",                          
            "1.0X^2 - 1.0",                         
        };

        for (int noPolynome = 0; noPolynome < STRINGS_ATTENDUS.length; noPolynome++) {
            String obtenu = FIXTURE_POLYNOMES_OK[noPolynome].toString();
            if (!STRINGS_ATTENDUS[noPolynome].equals(obtenu)) {
                throw new TestException(
                    "testToString - fixture n°" + noPolynome
                    + " " + java.util.Arrays.toString(FIXTURE_POLYNOMES_OK[noPolynome].getMonomes())
                    + " => attendu = '" + STRINGS_ATTENDUS[noPolynome]
                    + "', obtenu = '" + obtenu + "'");
            }
        }
    }

    // =========================================================================
    // SQUELETTES TDD — à décommenter quand les méthodes seront implémentées
    // =========================================================================

    /*
     * Vérifie coefficient(int n) : retourne le coefficient de X^n.
     *
    private void testCoefficient() {
        // Ex : {3.0, 2.0, 1.0}.coefficient(2) == 3.0
        // Ex : {3.0, 2.0, 1.0}.coefficient(0) == 1.0
        // Ex : {3.0, 2.0, 1.0}.coefficient(5) == 0.0  (degré dépassé)
        // TODO : définir les cas et les valeurs attendues
        throw new TestException("testCoefficient : non implémenté");
    }
    */

    /*
     * Vérifie racines() : retourne le tableau des racines réelles.
     * Un polynôme peut n'avoir aucune racine réelle => tableau vide attendu.
     *
    private void testRacines() {
        // Ex : {1.0, 0.0, -1.0}  (x^2-1)    => racines = {-1.0, 1.0}
        // Ex : {1.0, 0.0}        (x)         => racines = {0.0}
        // Ex : {1.0}             (constante)  => racines = {}
        // TODO
        throw new TestException("testRacines : non implémenté");
    }
    */

    /*
     * Vérifie les limites en -∞ et +∞ de la fonction polynomiale associée.
     * Le comportement asymptotique est celui du monôme de plus haut degré.
     * Ex : {3.0, 2.0, 1.0}  (3x^2)  => limite en ±∞  = +∞
     * Ex : {-4.0, 0.0, -2.0, 5.0}   => limite en -∞  = +∞, limite en +∞ = -∞
     * Ex : polynôme nul              => limites = 0.0
     * Suggestion : représenter ±∞ avec Double.POSITIVE_INFINITY / NEGATIVE_INFINITY
     *
    private void testLimites() {
        // TODO
        throw new TestException("testLimites : non implémenté");
    }
    */

    /*
     * Vérifie valeurEn(double x) : évalue p(x).
     * Suggestion : utiliser la méthode de Horner pour l'efficacité.
     * Ex : {3.0, 2.0, 1.0} en x=2  =>  3*4 + 2*2 + 1 = 17.0
     * Ex : {3.0, 2.0, 1.0} en x=0  =>  1.0  (terme constant)
     *
    private void testValeurEn() {
        // TODO
        throw new TestException("testValeurEn : non implémenté");
    }
    */

    /*
     * Vérifie derivee() : retourne le polynôme dérivé P'.
     * Ex : {3.0, 2.0, 1.0}  (3x^2+2x+1) => P' = {6.0, 2.0}  (6x+2)
     * Ex : {5.0}             (constante)  => P' = {0.0}
     *
    private void testDerivee() {
        // TODO
        throw new TestException("testDerivee : non implémenté");
    }
    */

    /*
     * Vérifie addition(Polynome autre) : retourne P + Q.
     * Ex : {2.0, 1.0} + {1.0, 0.0, 3.0} => {1.0, 2.0, 4.0}
     *
    private void testAddition() {
        // TODO
        throw new TestException("testAddition : non implémenté");
    }
    */

    /*
     * Vérifie multiplicationScalaire(double scalaire) : retourne scalaire * P.
     * Ex : {3.0, 2.0, 1.0} * 2.0 => {6.0, 4.0, 2.0}
     * Ex : {3.0, 2.0, 1.0} * 0.0 => {0.0}  (polynôme nul)
     *
    private void testMultiplicationScalaire() {
        // TODO
        throw new TestException("testMultiplicationScalaire : non implémenté");
    }
    */
}