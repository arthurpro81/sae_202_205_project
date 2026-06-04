/**
 * TestPolynome.java
 * IUT Toulouse Capitole — no copyright (copyleft)
 * Tests JUnit 6 de la classe Polynome.
 * Couvre toutes les methodes publiques sauf racine().
 * Structure : un @Test par methode testee, assertAll pour toutes les assertions.
 */
package iut.sae.polynome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Polynome")
public class TestPolynome {

    // Polynomes reinitialisees avant chaque test via @BeforeEach
    private Polynome p1;
    private Polynome p2;
    private Polynome p3;
    private Polynome pConstante;
    private Polynome pZero;
    private Polynome pX;
    private Polynome pNegX;

    @BeforeEach
    void init() {
        p1         = new Polynome("3x^2+2x+1");
        p2         = new Polynome("x^2+5x-3");
        p3         = new Polynome("-4x^3+2x^2");
        pConstante = new Polynome("42");
        pZero      = new Polynome("0");
        pX         = new Polynome("x");
        pNegX      = new Polynome("-x");
    }

    // =========================================================================
    // 1. CONSTRUCTEUR — formats valides
    // =========================================================================

    @ParameterizedTest(name = "Constructeur valide : \"{0}\"")
    @ValueSource(strings = {
        "x", "-x", "3x", "-3x", "1x", "-1x",
        "0", "1", "42", "-4",
        "x^2", "3x^2", "-3x^2", "x^10", "3x^10",
        "3x^2 + 2x + 1", "3x^2 - 2x + 1", "3x^2 - 2x - 1",
        "-3x^2 + 2x + 1", "-x + 3",
        "3x^2+2x+1", "3x^2-2x+1", "3x^10-9x^2-3",
        " 3x^2+2x", "-4x^2+6x+10",
        "-9+x^5-x+2+2x", "9x^2-6+x^3-7",
        "3x^2+3x^2",
        "5x-5x",
    })
    @DisplayName("Constructeur : formats valides")
    void testConstructeurValide(String input) {
        assertDoesNotThrow(() -> new Polynome(input));
    }

    // =========================================================================
    // 2. CONSTRUCTEUR — formats invalides
    // =========================================================================

    @ParameterizedTest(name = "Constructeur invalide : \"{0}\"")
    @ValueSource(strings = {
        "",
        "3y",
        "3x^2s",
        "abc",
        "3x^2 * 2",
        "3x^",
        "++3x",
        " ",
        "3x^2 + + 2",
    })
    @DisplayName("Constructeur : formats invalides")
    void testConstructeurInvalide(String input) {
        assertThrows(IllegalArgumentException.class, () -> new Polynome(input));
    }

    // =========================================================================
    // 3. CONSTRUCTEUR — canonisation (toString inclus car lie a la canonisation)
    // =========================================================================

    @Test
    @DisplayName("Constructeur : canonisation et toString")
    void testCanonisationEtToString() {
        assertAll(
            // tri par degres decroissants
            () -> assertEquals("x^5+x-7",    new Polynome("-9+x^5-x+2+2x").toString()),
            // simplification des degres doublons
            () -> assertEquals("6x^2",        new Polynome("3x^2+3x^2").toString()),
            // annulation totale
            () -> assertEquals("0",           new Polynome("5x-5x").toString()),
            // polynome deja canonise inchange
            () -> assertEquals("x^3+9x^2-13", new Polynome("9x^2-6+x^3-7").toString()),
            // espaces supprimes, format normalise
            () -> assertEquals("3x^2+2x+1",   new Polynome("3x^2 + 2x + 1").toString()),
            // polynome standard sans modification
            () -> assertEquals("x^2-2x+1",    new Polynome("x^2-2x+1").toString())
        );
    }

    // =========================================================================
    // 4. getMonomes()
    // =========================================================================

    @Test
    @DisplayName("getMonomes : decomposition correcte en monomes")
    void testGetMonomes() {
        assertAll(
            // polynome standard
            () -> assertEquals(new ArrayList<>(Arrays.asList("3x^2", "2x", "-1")),
                               new Polynome("3x^2+2x-1").getMonomes()),
            // monome unique x
            () -> assertEquals(new ArrayList<>(Arrays.asList("x")),
                               pX.getMonomes()),
            // constante seule
            () -> assertEquals(new ArrayList<>(Arrays.asList("42")),
                               pConstante.getMonomes()),
            // polynome nul apres canonisation
            () -> assertEquals(new ArrayList<>(Arrays.asList("0")),
                               new Polynome("3x-3x").getMonomes()),
            // monome negatif
            () -> assertEquals(new ArrayList<>(Arrays.asList("-x")),
                               pNegX.getMonomes())
        );
    }

    // =========================================================================
    // 5. setMonomes()
    // =========================================================================

    @Test
    @DisplayName("setMonomes : remplacement a differents indices")
    void testSetMonomes() {
        assertAll(
            // remplacement a l'indice 0
            () -> {
                Polynome p = new Polynome("3x^2+2x-1");
                p.setMonomes("5x^2", 0);
                assertEquals("5x^2", p.getMonomes().get(0));
            },
            // remplacement au dernier indice
            () -> {
                Polynome p = new Polynome("3x^2+2x-1");
                p.setMonomes("10", 2);
                assertEquals("10", p.getMonomes().get(2));
            },
            // indice hors bornes leve IndexOutOfBoundsException
            () -> assertThrows(IndexOutOfBoundsException.class,
                () -> new Polynome("3x^2+2x-1").setMonomes("5x", 10))
        );
    }

    // =========================================================================
    // 6. MaxDegres()
    // =========================================================================

    @ParameterizedTest(name = "MaxDegres({0}) = {1}")
    @CsvSource({
        "1,            0",
        "42,           0",
        "x,            1",
        "3x,           1",
        "-3x,          1",
        "x^2,          2",
        "3x^2,         2",
        "3x^10,        10",
        "3x^2+2x+1,    2",
        "-3x^2+2x+1,   2",
        "-x+3,         1",
        "3x^10-9x^2-3, 10",
        "5x^5+1,       5",
    })
    @DisplayName("MaxDegres : degre maximal correct")
    void testMaxDegres(String poly, int attendu) {
        assertEquals(attendu, new Polynome(poly).MaxDegres());
    }

    // =========================================================================
    // 7. coefficient()
    // =========================================================================

    @Test
    @DisplayName("coefficient : extraction correcte des coefficients")
    void testCoefficient() {
        assertAll(
            // polynome standard
            () -> assertEquals(new ArrayList<>(Arrays.asList(3, 2, -1)),
                               new Polynome("3x^2+2x-1").coefficient()),
            // coefficient implicite 1 pour x
            () -> assertEquals(new ArrayList<>(Arrays.asList(1)),
                               pX.coefficient()),
            // coefficient implicite -1 pour -x
            () -> assertEquals(new ArrayList<>(Arrays.asList(-1)),
                               pNegX.coefficient()),
            // coefficients implicites dans un polynome
            () -> assertEquals(new ArrayList<>(Arrays.asList(1, -1)),
                               new Polynome("x^3-x").coefficient()),
            // constante seule
            () -> assertEquals(new ArrayList<>(Arrays.asList(42)),
                               pConstante.coefficient()),
            // coefficients negatifs multiples
            () -> assertEquals(new ArrayList<>(Arrays.asList(-4, -3, -2)),
                               new Polynome("-4x^3-3x^2-2x").coefficient())
        );
    }

    // =========================================================================
    // 8. limite()
    // =========================================================================

    @Test
    @DisplayName("limite : toutes les combinaisons de signe et parite")
    void testLimite() {
        assertAll(
            // coeff positif, degre pair → +Infini / +Infini
            () -> assertEquals("+Infini", new Polynome("3x^2+7x").limite('+')),
            () -> assertEquals("+Infini", new Polynome("3x^2+7x").limite('-')),
            // coeff negatif, degre pair → -Infini / -Infini
            () -> assertEquals("-Infini", new Polynome("-17x^6+88x^3+1").limite('+')),
            () -> assertEquals("-Infini", new Polynome("-x^4+5").limite('-')),
            // coeff positif, degre impair → +Infini / -Infini
            () -> assertEquals("+Infini", new Polynome("12x^5").limite('+')),
            () -> assertEquals("-Infini", new Polynome("12x^5").limite('-')),
            // coeff negatif, degre impair → -Infini / +Infini
            () -> assertEquals("-Infini", new Polynome("-3x^3-7x+2").limite('+')),
            () -> assertEquals("+Infini", new Polynome("-3x^3-7x+2").limite('-')),
            // coeff implicite 1, degre impair
            () -> assertEquals("+Infini", new Polynome("x^7+4x^2-4").limite('+')),
            () -> assertEquals("-Infini", new Polynome("x^7+4x^2-4").limite('-')),
            // monome x et -x seuls
            () -> assertEquals("+Infini", pX.limite('+')),
            () -> assertEquals("-Infini", pX.limite('-')),
            () -> assertEquals("-Infini", pNegX.limite('+')),
            () -> assertEquals("+Infini", pNegX.limite('-')),
            // constante retourne sa valeur
            () -> assertEquals("42", pConstante.limite('+')),
            () -> assertEquals("42", pConstante.limite('-')),
            () -> assertEquals("0",  pZero.limite('+')),
            () -> assertEquals("0",  pZero.limite('-')),
            // parametre invalide leve IllegalArgumentException
            () -> assertThrows(IllegalArgumentException.class, () -> p1.limite('*')),
            () -> assertThrows(IllegalArgumentException.class, () -> p1.limite('0')),
            () -> assertThrows(IllegalArgumentException.class, () -> p1.limite('x'))
        );
    }

    // =========================================================================
    // 9. derive()
    // =========================================================================

    @Test
    @DisplayName("derive : toutes les regles de derivation")
    void testDerive() {
        assertAll(
            // Ax^b → A*b x^(b-1)
            () -> assertEquals("[6x]",    new Polynome("3x^2").derive(1).toString()),
            () -> assertEquals("[15x^2]", new Polynome("5x^3").derive(1).toString()),
            () -> assertEquals("[3x^2]",  new Polynome("x^3").derive(1).toString()),
            // Ax → A
            () -> assertEquals("[7]",  new Polynome("7x").derive(1).toString()),
            () -> assertEquals("[1]",  pX.derive(1).toString()),
            () -> assertEquals("[-1]", pNegX.derive(1).toString()),
            // coefficient implicite negatif
            () -> assertEquals("[-3x^2]", new Polynome("-x^3+5").derive(1).toString()),
            // constante → liste vide
            () -> assertEquals("[]", new Polynome("9").derive(1).toString()),
            () -> assertEquals("[]", pZero.derive(1).toString()),
            // polynome complet
            () -> assertEquals("[6x, 2]",      p1.derive(1).toString()),
            () -> assertEquals("[-12x^2, 4x]", p3.derive(1).toString()),
            // derivee d'ordre 2
            () -> assertEquals("[6]",   p1.derive(2).toString()),
            () -> assertEquals("[30x]", new Polynome("5x^3+2x").derive(2).toString()),
            // ordre superieur au degre → liste vide
            () -> assertEquals("[]", p1.derive(3).toString()),
            () -> assertEquals("[]", pX.derive(2).toString()),
            // parametre invalide
            () -> assertThrows(IllegalArgumentException.class, () -> p1.derive(0)),
            () -> assertThrows(IllegalArgumentException.class, () -> p1.derive(-1))
        );
    }

    // =========================================================================
    // 10. addition()
    // =========================================================================

    @Test
    @DisplayName("addition : tous les cas")
    void testAddition() {
        assertAll(
            // degres communs → coefficients additionnes
            () -> assertEquals("4x^2+7x-2", p1.addition(p2)),
            // degres disjoints → juxtaposition
            () -> assertEquals("3x^2+2x", new Polynome("3x^2").addition(new Polynome("2x"))),
            // termes qui s'annulent → 0
            () -> assertEquals("0", new Polynome("3x^2+2x").addition(new Polynome("-3x^2-2x"))),
            // avec une constante
            () -> assertEquals("3x^2+5", new Polynome("3x^2").addition(new Polynome("5"))),
            // polynome + lui-meme
            () -> assertEquals("6x^2+4x", new Polynome("3x^2+2x").addition(new Polynome("3x^2+2x"))),
            // polynome + zero
            () -> assertEquals("3x^2+2x+1", p1.addition(pZero)),
            // autrePolynome de degre superieur
            () -> assertEquals("x^3+x^2+2x", new Polynome("2x").addition(new Polynome("x^3+x^2")))
        );
    }

    // =========================================================================
    // 11. soustraction()
    // =========================================================================

    @Test
    @DisplayName("soustraction : tous les cas")
    void testSoustraction() {
        assertAll(
            // degres communs → coefficients soustraits
            () -> assertEquals("2x^2-3x+4", p1.soustraction(p2)),
            // resultat nul
            () -> assertEquals("0", p1.soustraction(p1)),
            // degres disjoints
            () -> assertEquals("3x^2-2x", new Polynome("3x^2").soustraction(new Polynome("2x"))),
            // avec une constante
            () -> assertEquals("3x^2", new Polynome("3x^2+5").soustraction(new Polynome("5"))),
            // signe inverse pour les termes non communs
            () -> assertEquals("-3x", new Polynome("x^2").soustraction(new Polynome("x^2+3x")))
        );
    }

    // =========================================================================
    // 12. multiplication()
    // =========================================================================

    @Test
    @DisplayName("multiplication : tous les cas")
    void testMultiplication() {
        assertAll(
            // distributivite standard
            () -> assertEquals("3x^3+5x^2+2x",
                new Polynome("3x^2+2x").multiplication(new Polynome("x+1"))),
            // par une constante
            () -> assertEquals("6x^2+4x+2", p1.multiplication(new Polynome("2"))),
            // par zero
            () -> assertEquals("0", p1.multiplication(pZero)),
            // monome x monome
            () -> assertEquals("6x^5", new Polynome("3x^2").multiplication(new Polynome("2x^3"))),
            // coefficients negatifs
            () -> assertEquals("-6x^3", new Polynome("-2x^2").multiplication(new Polynome("3x"))),
            // identite (a+b)(a-b) = a^2-b^2
            () -> assertEquals("x^2-1",
                new Polynome("x+1").multiplication(new Polynome("x-1"))),
            // identite (a+b)^2 = a^2+2ab+b^2
            () -> assertEquals("x^2+2x+1",
                new Polynome("x+1").multiplication(new Polynome("x+1")))
        );
    }

    // =========================================================================
    // 13. division()
    // =========================================================================

    @Test
    @DisplayName("division : tous les cas")
    void testDivision() {
        assertAll(
            // division exacte : quotient et reste nul
            () -> {
                String[] r = new Polynome("x^3+2x^2-5x-6").division(new Polynome("x-2"));
                assertEquals("x^2+4x+3", r[0]);
                assertEquals("0",         r[1]);
            },
            // division avec reste non nul
            () -> {
                String[] r = new Polynome("x^3+x+1").division(new Polynome("x^2+1"));
                assertEquals("x", r[0]);
                assertEquals("1", r[1]);
            },
            // monome / monome
            () -> {
                String[] r = new Polynome("6x^3").division(new Polynome("2x"));
                assertEquals("3x^2", r[0]);
                assertEquals("0",     r[1]);
            },
            // constante / constante
            () -> {
                String[] r = new Polynome("6").division(new Polynome("2"));
                assertEquals("3", r[0]);
                assertEquals("0", r[1]);
            },
            () -> {
                Polynome dividende = new Polynome("x^2+3x+2");
                Polynome diviseur  = new Polynome("x+1");
                String[] r         = dividende.division(diviseur);
                assertEquals("x+2", r[0]);
                assertEquals("0",   r[1]);
                assertEquals(dividende.toString(),
                    new Polynome(diviseur.multiplication(new Polynome(r[0]))).toString());
            },
            // coefficients negatifs : verification A = B x Q + R
            () -> {
                Polynome p1 = new Polynome("-x^2+1");
                Polynome p2 = new Polynome("x-1");
                String[] r  = p1.division(p2);
                assertEquals("0", r[1]);
                assertEquals(p1.toString(),
                    new Polynome(p2.multiplication(new Polynome(r[0]))).toString());
            },
            // degre diviseur > degre dividende leve IllegalArgumentException
            () -> assertThrows(IllegalArgumentException.class,
                () -> new Polynome("x+1").division(new Polynome("x^2+1")))
        );
    }
    
    // =========================================================================
    // 15. racine()
    // =========================================================================

@Disabled("Méthode racine() non implémentée — TODO : Suite de Sturm + Hörner")
@Test
@DisplayName("racine : tous les cas dans un encadrement [-10, 10]")
void testRacine() {
    assertAll(
        //Cas sans racine réelle 
        // x^2+1 -> aucune racine réelle
        () -> assertEquals(new ArrayList<>(),
                           new Polynome("x^2+1").racine(-10, 10)),

        //Racine en 0
        // x -> racine en 0
        () -> assertEquals(new ArrayList<>(Arrays.asList(0.0)),
                           new Polynome("x").racine(-10, 10)),
        // x^2 -> racine double en 0
        () -> assertEquals(new ArrayList<>(Arrays.asList(0.0)),
                           new Polynome("x^2").racine(-10, 10)),
        // x^3 -> racine triple en 0
        () -> assertEquals(new ArrayList<>(Arrays.asList(0.0)),
                           new Polynome("x^3").racine(-10, 10)),

        //Racine entière positive
        // x-1 -> racine en 1
        () -> assertEquals(new ArrayList<>(Arrays.asList(1.0)),
                           new Polynome("x-1").racine(-10, 10)),
        // 2x-8 -> racine en 4
        () -> assertEquals(new ArrayList<>(Arrays.asList(4.0)),
                           new Polynome("2x-8").racine(-10, 10)),

        //Racine entière négative
        // x+2 -> racine en -2
        () -> assertEquals(new ArrayList<>(Arrays.asList(-2.0)),
                           new Polynome("x+2").racine(-10, 10)),

        //Racine double
        // x^2-2x+1 = (x-1)^2 -> racine double en 1
        () -> assertEquals(new ArrayList<>(Arrays.asList(1.0)),
                           new Polynome("x^2-2x+1").racine(-10, 10)),

        //Deux racines entières
        // x^2-4 -> racines en -2 et 2
        () -> assertEquals(new ArrayList<>(Arrays.asList(-2.0, 2.0)),
                           new Polynome("x^2-4").racine(-10, 10)),
        // x^2-5x+6 = (x-2)(x-3) -> racines en 2 et 3
        () -> assertEquals(new ArrayList<>(Arrays.asList(2.0, 3.0)),
                           new Polynome("x^2-5x+6").racine(-10, 10)),

        //Trois racines entières
        // x^3-6x^2+11x-6 = (x-1)(x-2)(x-3) -> racines en 1, 2 et 3
        () -> assertEquals(new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0)),
                           new Polynome("x^3-6x^2+11x-6").racine(-10, 10)),

        //Racines décimales
        // x^2-2 -> racines en -1.4142 et 1.4142 (arrondi 4 décimales)
        () -> assertEquals(new ArrayList<>(Arrays.asList(-1.4142, 1.4142)),
                           new Polynome("x^2-2").racine(-10, 10)),
        // 2x-1 → racine en 0.5
        () -> assertEquals(new ArrayList<>(Arrays.asList(0.5)),
                           new Polynome("2x-1").racine(-10, 10)),
        // x^2-3 -> racines en -1.7320 et 1.7320
        () -> assertEquals(new ArrayList<>(Arrays.asList(-1.7320, 1.7320)),
                           new Polynome("x^2-3").racine(-10, 10)),

        //Encadrement excluant certaines racines
        // x^2-4 -> racines en -2 et 2, encadrement [0, 10] -> seulement 2
        () -> assertEquals(new ArrayList<>(Arrays.asList(2.0)),
                           new Polynome("x^2-4").racine(0, 10)),
        // x^2-4 → encadrement [-10, 0] → seulement -2
        () -> assertEquals(new ArrayList<>(Arrays.asList(-2.0)),
                           new Polynome("x^2-4").racine(-10, 0)),
        // x^2-4 -> encadrement [3, 10] -> aucune racine dans cet encadrement
        () -> assertEquals(new ArrayList<>(),
                           new Polynome("x^2-4").racine(3, 10)),

        //Encadrement = point exact (racine sur la borne)
        // x-5 -> racine en 5, encadrement [5, 10] -> racine trouvée
        () -> assertEquals(new ArrayList<>(Arrays.asList(5.0)),
                           new Polynome("x-5").racine(5, 10)),

        //Encadrement invalide -> IllegalArgumentException
        () -> assertThrows(IllegalArgumentException.class,
                           () -> new Polynome("x^2-4").racine(5, -5)),
        () -> assertThrows(IllegalArgumentException.class,
                           () -> new Polynome("x").racine(10, 0)),

        //Constante sans racine
        // 42 -> polynôme constant non nul → aucune racine
        () -> assertEquals(new ArrayList<>(),
                           new Polynome("42").racine(-10, 10)),

        //Polynôme nul -> toute valeur est racine (cas dégénéré)
        // 0 -> cas particulier, comportement à définir selon l'implémentation
        () -> assertNotNull(new Polynome("0").racine(-10, 10)),

        //Grands coefficients
        // 999999x^2-999999 = 999999(x-1)(x+1) → racines en -1 et 1
        () -> assertEquals(new ArrayList<>(Arrays.asList(-1.0, 1.0)),
                           new Polynome("999999x^2-999999").racine(-10, 10)),
        // x^2-1000000 -> racines en -1000 et 1000, encadrement [-1001, 1001]
        () -> assertEquals(new ArrayList<>(Arrays.asList(-1000.0, 1000.0)),
                           new Polynome("x^2-1000000").racine(-1001, 1001))
    );
} 
}