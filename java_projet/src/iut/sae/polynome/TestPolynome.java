/**
 * TestPolynome.java
 * IUT Toulouse Capitole — no copyright (copyleft)
 * Tests JUnit 6 de la classe Polynome.
 * Couvre toutes les méthodes publiques sauf racine().
 *
 * Dépendance Maven (pom.xml) :
 *   <dependency>
 *       <groupId>org.junit.jupiter</groupId>
 *       <artifactId>junit-jupiter</artifactId>
 *       <version>6.1.0</version>
 *       <scope>test</scope>
 *   </dependency>
 *
 * Différences JUnit 5 → JUnit 6 appliquées ici :
 *   - Versioning unifié : un seul artefact junit-jupiter en version 6.x
 *   - Les annotations (@Test, @ParameterizedTest, @CsvSource, etc.) sont inchangées
 *   - Java 17 minimum requis (déjà satisfait par JDK 25)
 *   - FastCSV remplace univocity-parsers pour @CsvSource (parsing plus strict)
 */
package iut.sae.polynome;

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

    // =========================================================================
    // 1. CONSTRUCTEUR — formats valides
    // =========================================================================

    @ParameterizedTest(name = "Constructeur valide : \"{0}\"")
    @ValueSource(strings = {
        // monômes simples
        "x", "-x", "3x", "-3x", "1x", "-1x",
        // constantes
        "0", "1", "42", "-4",
        // monômes avec exposant
        "x^2", "3x^2", "-3x^2", "x^10", "3x^10",
        // polynômes standards avec espaces
        "3x^2 + 2x + 1", "3x^2 - 2x + 1", "3x^2 - 2x - 1",
        "-3x^2 + 2x + 1", "-x + 3",
        // polynômes sans espaces
        "3x^2+2x+1", "3x^2-2x+1", "3x^10-9x^2-3",
        // polynômes avec espace initial ou signe initial
        " 3x^2+2x", "-4x^2+6x+10",
        // termes non canonisés (le constructeur doit les accepter)
        "-9+x^5-x+2+2x", "9x^2-6+x^3-7",
        "3x^2+3x^2",          // degrés doublons → simplification attendue
        "5x-5x",              // résultat nul → "0"
    })
    @DisplayName("Constructeur : formats valides")
    void testConstructeurValide(String input) {
        assertDoesNotThrow(() -> new Polynome(input),
            "Le polynôme \"" + input + "\" devrait être accepté");
    }

    // =========================================================================
    // 2. CONSTRUCTEUR — formats invalides
    // =========================================================================

    @ParameterizedTest(name = "Constructeur invalide : \"{0}\"")
    @ValueSource(strings = {
        "",           // chaîne vide
        "3y",         // variable non autorisée
        "3x^2s",      // caractère parasite
        "abc",        // que des lettres
        "3x^2 * 2",   // opérateur * interdit
        "3x^",        // ^ sans exposant
        "++3x",       // double opérateur
        " ",          // que des espaces
        "3x^2 + + 2", // double + consécutif
    })
    @DisplayName("Constructeur : formats invalides")
    void testConstructeurInvalide(String input) {
        assertThrows(IllegalArgumentException.class, () -> new Polynome(input),
            "Le polynôme \"" + input + "\" devrait lever une IllegalArgumentException");
    }

    // =========================================================================
    // 3. CONSTRUCTEUR — canonisation
    // =========================================================================

    @Test
    @DisplayName("Canonisation : tri par degrés décroissants")
    void testCanonicalisationTri() {
        assertEquals("x^5+x-7", new Polynome("-9+x^5-x+2+2x").toString());
    }

    @Test
    @DisplayName("Canonisation : simplification des degrés doublons")
    void testCanonicalisationSimplification() {
        assertEquals("6x^2", new Polynome("3x^2+3x^2").toString());
    }

    @Test
    @DisplayName("Canonisation : annulation totale → 0")
    void testCanonicalisationNul() {
        assertEquals("0", new Polynome("5x-5x").toString());
    }

    @Test
    @DisplayName("Canonisation : polynôme déjà canonisé inchangé")
    void testCanonicalisationDejaCanonise() {
        assertEquals("x^3+9x^2-13", new Polynome("9x^2-6+x^3-7").toString());
    }

    // =========================================================================
    // 4. toString()
    // =========================================================================

    @Test
    @DisplayName("toString : retourne le polynôme canonisé")
    void testToString() {
        assertEquals("3x^2+2x+1",  new Polynome("3x^2+2x+1").toString());
        assertEquals("3x^2+2x+1",  new Polynome("3x^2 + 2x + 1").toString());
        assertEquals("x^2-2x+1",   new Polynome("x^2-2x+1").toString());
        assertEquals("0",           new Polynome("3x-3x").toString());
    }

    // =========================================================================
    // 5. getMonomes()
    // =========================================================================

    @Test
    @DisplayName("getMonomes : liste correcte pour un polynôme standard")
    void testGetMonomes() {
        Polynome p = new Polynome("3x^2+2x-1");
        ArrayList<String> attendu = new ArrayList<>(Arrays.asList("3x^2", "2x", "-1"));
        assertEquals(attendu, p.getMonomes());
    }

    @Test
    @DisplayName("getMonomes : monôme unique x")
    void testGetMonomesX() {
        ArrayList<String> attendu = new ArrayList<>(Arrays.asList("x"));
        assertEquals(attendu, new Polynome("x").getMonomes());
    }

    @Test
    @DisplayName("getMonomes : constante")
    void testGetMonomesConstante() {
        ArrayList<String> attendu = new ArrayList<>(Arrays.asList("42"));
        assertEquals(attendu, new Polynome("42").getMonomes());
    }

    @Test
    @DisplayName("getMonomes : polynôme nul après canonisation")
    void testGetMonomesNul() {
        ArrayList<String> attendu = new ArrayList<>(Arrays.asList("0"));
        assertEquals(attendu, new Polynome("3x-3x").getMonomes());
    }

    // =========================================================================
    // 6. setMonomes()
    // =========================================================================

    @Test
    @DisplayName("setMonomes : remplacement d'un monôme à l'indice 0")
    void testSetMonomesIndice0() {
        Polynome p = new Polynome("3x^2+2x-1");
        p.setMonomes("5x^2", 0);
        assertEquals("5x^2", p.getMonomes().get(0));
    }

    @Test
    @DisplayName("setMonomes : remplacement d'un monôme au dernier indice")
    void testSetMonomesDernierIndice() {
        Polynome p = new Polynome("3x^2+2x-1");
        p.setMonomes("10", 2);
        assertEquals("10", p.getMonomes().get(2));
    }

    @Test
    @DisplayName("setMonomes : indice hors bornes lève IndexOutOfBoundsException")
    void testSetMonomesHorsBornes() {
        Polynome p = new Polynome("3x^2+2x-1");
        assertThrows(IndexOutOfBoundsException.class, () -> p.setMonomes("5x", 10));
    }

    // =========================================================================
    // 7. MaxDegres()
    // =========================================================================

    @ParameterizedTest(name = "MaxDegres({0}) = {1}")
    @CsvSource({
        "1,          0",
        "42,         0",
        "x,          1",
        "3x,         1",
        "-3x,        1",
        "x^2,        2",
        "3x^2,       2",
        "3x^10,      10",
        "3x^2+2x+1,  2",
        "-3x^2+2x+1, 2",
        "-x+3,       1",
        "3x^10-9x^2-3, 10",
        "5x^5+1,     5",
    })
    @DisplayName("MaxDegres : degré maximal correct")
    void testMaxDegres(String poly, int attendu) {
        assertEquals(attendu, new Polynome(poly).MaxDegres());
    }

    // =========================================================================
    // 8. coefficient()
    // =========================================================================

    @Test
    @DisplayName("coefficient : liste correcte pour un polynôme standard")
    void testCoefficient() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(3, 2, -1));
        assertEquals(attendu, new Polynome("3x^2+2x-1").coefficient());
    }

    @Test
    @DisplayName("coefficient : coefficient implicite 1 pour x")
    void testCoefficientImplicite1() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(1));
        assertEquals(attendu, new Polynome("x").coefficient());
    }

    @Test
    @DisplayName("coefficient : coefficient implicite -1 pour -x")
    void testCoefficientImpliciteNeg1() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(-1));
        assertEquals(attendu, new Polynome("-x").coefficient());
    }

    @Test
    @DisplayName("coefficient : constante seule")
    void testCoefficientConstante() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(42));
        assertEquals(attendu, new Polynome("42").coefficient());
    }

    @Test
    @DisplayName("coefficient : coefficients négatifs multiples")
    void testCoefficientNegatifs() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(-4, -3, -2));
        assertEquals(attendu, new Polynome("-4x^3-3x^2-2x").coefficient());
    }

    @Test
    @DisplayName("coefficient : coefficient implicite x^3")
    void testCoefficientImpliciteExposant() {
        ArrayList<Integer> attendu = new ArrayList<>(Arrays.asList(1, -1));
        assertEquals(attendu, new Polynome("x^3-x").coefficient());
    }

    // =========================================================================
    // 9. limite()
    // =========================================================================

    @ParameterizedTest(name = "limite({0}, ''+'' ) = {1} | limite({0}, ''-'') = {2}")
    @CsvSource({
        // coeff positif, degré pair
        "3x^2+7x,          +Infini, +Infini",
        "-17x^6+88x^3+1,   +Infini, +Infini",
        // coeff positif, degré impair
        "12x^5,            +Infini, -Infini",
        // coeff négatif, degré pair
        "-3x^2-7x+2,       -Infini, -Infini",
        "-x^4+5,           -Infini, -Infini",
        // coeff négatif, degré impair
        "-3x^3-7x+2,       -Infini, +Infini",
        "-x^25+7x^10-10,   -Infini, +Infini",
        // coeff implicite 1, degré impair
        "x^7+4x^2-4,       +Infini, -Infini",
        "x^2+x,            +Infini, +Infini",
        // monôme ax seul
        "777x+1,           +Infini, -Infini",
        "-10x-77,          -Infini, +Infini",
        // monôme x ou -x seul
        "x,                +Infini, -Infini",
        "-x,               -Infini, +Infini",
        "1x,               +Infini, -Infini",
        "-1x,              -Infini, +Infini",
    })
    @DisplayName("limite : résultat correct en + et -infini")
    void testLimite(String poly, String attenduPlus, String attenduMoins) {
        Polynome p = new Polynome(poly);
        assertEquals(attenduPlus,  p.limite('+'));
        assertEquals(attenduMoins, p.limite('-'));
    }

    @ParameterizedTest(name = "limite constante({0}) = {1}")
    @CsvSource({
        "10, 10",
        "-4, -4",
        "0,  0",
    })
    @DisplayName("limite : constante retourne sa valeur")
    void testLimiteConstante(String poly, String attendu) {
        Polynome p = new Polynome(poly);
        assertEquals(attendu, p.limite('+'));
        assertEquals(attendu, p.limite('-'));
    }

    @Test
    @DisplayName("limite : paramètre invalide lève IllegalArgumentException")
    void testLimiteParametreInvalide() {
        Polynome p = new Polynome("3x^2+2x");
        assertThrows(IllegalArgumentException.class, () -> p.limite('*'));
        assertThrows(IllegalArgumentException.class, () -> p.limite('0'));
        assertThrows(IllegalArgumentException.class, () -> p.limite('x'));
    }

    // =========================================================================
    // 10. derive()
    // =========================================================================

    @Test
    @DisplayName("derive(1) : cas Ax^b")
    void testDeriveAxb() {
        assertEquals("[6x]",    new Polynome("3x^2").derive(1).toString());
        assertEquals("[15x^2]", new Polynome("5x^3").derive(1).toString());
        assertEquals("[3x^2]",  new Polynome("x^3").derive(1).toString());
    }

    @Test
    @DisplayName("derive(1) : cas Ax")
    void testDeriveAx() {
        assertEquals("[7]", new Polynome("7x").derive(1).toString());
        assertEquals("[1]", new Polynome("x").derive(1).toString());
        assertEquals("[-1]", new Polynome("-x").derive(1).toString());
    }

    @Test
    @DisplayName("derive(1) : constante → liste vide")
    void testDeriveConstante() {
        assertEquals("[]", new Polynome("9").derive(1).toString());
        assertEquals("[]", new Polynome("0").derive(1).toString());
    }

    @Test
    @DisplayName("derive(1) : polynôme complet")
    void testDerivePolynomeComplet() {
        assertEquals("[6x, 2]",     new Polynome("3x^2+2x+1").derive(1).toString());
        assertEquals("[-12x^2, 4x]", new Polynome("-4x^3+2x^2").derive(1).toString());
    }

    @Test
    @DisplayName("derive(2) : dérivée seconde")
    void testDeriveDegre2() {
        assertEquals("[6]", new Polynome("3x^2+2x+1").derive(2).toString());
        assertEquals("[30x]", new Polynome("5x^3+2x").derive(2).toString());
    }

    @Test
    @DisplayName("derive(n) : dérivée d'ordre supérieur au degré → liste vide")
    void testDeriveOrdreSuperieur() {
        assertEquals("[]", new Polynome("3x^2+2x+1").derive(3).toString());
        assertEquals("[]", new Polynome("x").derive(2).toString());
    }

    @Test
    @DisplayName("derive : paramètre <= 0 lève IllegalArgumentException")
    void testDeriveParametreInvalide() {
        Polynome p = new Polynome("3x^2+2x");
        assertThrows(IllegalArgumentException.class, () -> p.derive(0));
        assertThrows(IllegalArgumentException.class, () -> p.derive(-1));
    }

    @Test
    @DisplayName("derive(1) : coefficient implicite -1")
    void testDeriveCoefficientImpliciteNegatif() {
        assertEquals("[-3x^2]", new Polynome("-x^3+5").derive(1).toString());
    }

    // =========================================================================
    // 11. addition()
    // =========================================================================

    @Test
    @DisplayName("addition : degrés communs")
    void testAdditionDegreCommun() {
        Polynome p1 = new Polynome("3x^2+2x+1");
        Polynome p2 = new Polynome("x^2+5x-3");
        assertEquals("4x^2+7x-2", p1.addition(p2));
    }

    @Test
    @DisplayName("addition : degrés disjoints")
    void testAdditionDegreDisjoint() {
        Polynome p1 = new Polynome("3x^2");
        Polynome p2 = new Polynome("2x");
        assertEquals("3x^2+2x", p1.addition(p2));
    }

    @Test
    @DisplayName("addition : termes qui s'annulent")
    void testAdditionAnnulation() {
        Polynome p1 = new Polynome("3x^2+2x");
        Polynome p2 = new Polynome("-3x^2-2x");
        assertEquals("0", p1.addition(p2));
    }

    @Test
    @DisplayName("addition : avec une constante")
    void testAdditionConstante() {
        Polynome p1 = new Polynome("3x^2");
        Polynome p2 = new Polynome("5");
        assertEquals("3x^2+5", p1.addition(p2));
    }

    @Test
    @DisplayName("addition : polynôme + lui-même")
    void testAdditionLuiMeme() {
        Polynome p = new Polynome("3x^2+2x");
        assertEquals("6x^2+4x", p.addition(p));
    }

    @Test
    @DisplayName("addition : polynôme + zéro")
    void testAdditionAvecZero() {
        Polynome p = new Polynome("3x^2+2x+1");
        Polynome zero = new Polynome("0");
        assertEquals("3x^2+2x+1", p.addition(zero));
    }

    @Test
    @DisplayName("addition : degrés différents, autrePolynome de degré supérieur")
    void testAdditionDegreSuperieur() {
        Polynome p1 = new Polynome("2x");
        Polynome p2 = new Polynome("x^3+x^2");
        assertEquals("x^3+x^2+2x", p1.addition(p2));
    }

    // =========================================================================
    // 12. soustraction()
    // =========================================================================

    @Test
    @DisplayName("soustraction : degrés communs")
    void testSoustractionDegreCommun() {
        Polynome p1 = new Polynome("3x^2+2x+1");
        Polynome p2 = new Polynome("x^2+5x-3");
        assertEquals("2x^2-3x+4", p1.soustraction(p2));
    }

    @Test
    @DisplayName("soustraction : résultat nul")
    void testSoustractionResultatNul() {
        Polynome p = new Polynome("3x^2+2x+1");
        assertEquals("0", p.soustraction(p));
    }

    @Test
    @DisplayName("soustraction : degrés disjoints")
    void testSoustractionDegreDisjoint() {
        Polynome p1 = new Polynome("3x^2");
        Polynome p2 = new Polynome("2x");
        assertEquals("3x^2-2x", p1.soustraction(p2));
    }

    @Test
    @DisplayName("soustraction : avec une constante")
    void testSoustractionConstante() {
        Polynome p1 = new Polynome("3x^2+5");
        Polynome p2 = new Polynome("5");
        assertEquals("3x^2", p1.soustraction(p2));
    }

    @Test
    @DisplayName("soustraction : signe inversé pour les termes non communs de autrePolynome")
    void testSoustractionSigneInverse() {
        Polynome p1 = new Polynome("x^2");
        Polynome p2 = new Polynome("x^2+3x");
        assertEquals("-3x", p1.soustraction(p2));
    }

    // =========================================================================
    // 13. multiplication()
    // =========================================================================

    @Test
    @DisplayName("multiplication : cas standard")
    void testMultiplicationStandard() {
        Polynome p1 = new Polynome("3x^2+2x");
        Polynome p2 = new Polynome("x+1");
        assertEquals("3x^3+5x^2+2x", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : par une constante")
    void testMultiplicationParConstante() {
        Polynome p1 = new Polynome("3x^2+2x+1");
        Polynome p2 = new Polynome("2");
        assertEquals("6x^2+4x+2", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : par zéro")
    void testMultiplicationParZero() {
        Polynome p1 = new Polynome("3x^2+2x+1");
        Polynome p2 = new Polynome("0");
        assertEquals("0", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : monôme × monôme")
    void testMultiplicationMonomes() {
        Polynome p1 = new Polynome("3x^2");
        Polynome p2 = new Polynome("2x^3");
        assertEquals("6x^5", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : coefficients négatifs")
    void testMultiplicationNegatifs() {
        Polynome p1 = new Polynome("-2x^2");
        Polynome p2 = new Polynome("3x");
        assertEquals("-6x^3", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : (x+1)(x-1) = x^2-1")
    void testMultiplicationIdentiteRemarquable() {
        Polynome p1 = new Polynome("x+1");
        Polynome p2 = new Polynome("x-1");
        assertEquals("x^2-1", p1.multiplication(p2));
    }

    @Test
    @DisplayName("multiplication : (x+1)^2 = x^2+2x+1")
    void testMultiplicationCarreDeSomme() {
        Polynome p = new Polynome("x+1");
        assertEquals("x^2+2x+1", p.multiplication(p));
    }

    // =========================================================================
    // 14. division()
    // =========================================================================

    @Test
    @DisplayName("division : exacte avec reste nul")
    void testDivisionExacte() {
        Polynome p1 = new Polynome("x^3+2x^2-5x-6");
        Polynome p2 = new Polynome("x-2");
        String[] result = p1.division(p2);
        assertEquals("x^2+4x+3", result[0]);
        assertEquals("0",         result[1]);
    }

    @Test
    @DisplayName("division : avec reste non nul")
    void testDivisionAvecReste() {
        Polynome p1 = new Polynome("x^3+x+1");
        Polynome p2 = new Polynome("x^2+1");
        String[] result = p1.division(p2);
        assertEquals("x",  result[0]);
        assertEquals("1",  result[1]);
    }

    @Test
    @DisplayName("division : monôme / monôme")
    void testDivisionMonomeParMonome() {
        Polynome p1 = new Polynome("6x^3");
        Polynome p2 = new Polynome("2x");
        String[] result = p1.division(p2);
        assertEquals("3x^2", result[0]);
        assertEquals("0",     result[1]);
    }

    @Test
    @DisplayName("division : constante / constante")
    void testDivisionConstantes() {
        Polynome p1 = new Polynome("6");
        Polynome p2 = new Polynome("2");
        String[] result = p1.division(p2);
        assertEquals("3", result[0]);
        assertEquals("0", result[1]);
    }

    @Test
    @DisplayName("division : degré diviseur > degré dividende lève IllegalArgumentException")
    void testDivisionDegreSuperieur() {
        Polynome p1 = new Polynome("x+1");
        Polynome p2 = new Polynome("x^2+1");
        assertThrows(IllegalArgumentException.class, () -> p1.division(p2));
    }

    @Test
    @DisplayName("division : vérification A = B×Q + R")
    void testDivisionVerification() {
        // (x^2+3x+2) ÷ (x+1) = (x+2) reste 0
        // vérif : (x+1)(x+2) = x^2+3x+2
        Polynome dividende = new Polynome("x^2+3x+2");
        Polynome diviseur  = new Polynome("x+1");
        String[] result    = dividende.division(diviseur);

        assertEquals("x+2", result[0]);
        assertEquals("0",   result[1]);

        // vérification algébrique : diviseur × quotient + reste == dividende
        Polynome quotient = new Polynome(result[0]);
        String produit    = diviseur.multiplication(quotient);
        assertEquals(dividende.toString(), new Polynome(produit).toString());
    }

    @Test
    @DisplayName("division : polynômes avec coefficients négatifs")
    void testDivisionCoefficientsNegatifs() {
        // (-x^2+1) ÷ (x-1) = (-x-1) reste 0
        Polynome p1 = new Polynome("-x^2+1");
        Polynome p2 = new Polynome("x-1");
        String[] result = p1.division(p2);
        assertEquals("0", result[1]);
        // vérif : (x-1)×(-x-1) = -x^2+1
        Polynome quotient = new Polynome(result[0]);
        assertEquals(
            new Polynome(p2.multiplication(quotient)).toString(),
            p1.toString()
        );
    }
}