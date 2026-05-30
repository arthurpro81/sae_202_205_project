/**
 * TestPolynome.java											05/05/2026
 * IUT Toulouse Captiole no copyright(copyleft )
 */
package iut.sae.polynome;

//lorsque les test ne seont plus dans le meme fichier mettre : package iut.sae.polynome.test;

/**
 * Classe de test pour vérifier si la création de polynomes fonctionne.
 * Affiche une erreur si un test échoue.
 */
public class TestPolynome {

    /**
     * Lance les tests de base.
     */
    public void lancerTests() {
        System.out.println("Début des tests...");

        try {
        	//TODO faire les tests des méthodes coefficient(), getMonomes(), setMonomes(), toString()
            testCreationValide();
            testLimite();
            testDerive();
            testMaxDegres();
            //testRacine();
            System.out.println("Test création valide : OK");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR sur le test de création valide : " + echecAnormal.getMessage());
        }

        try {
            testCreationInvalide();
            System.out.println("Test création invalide : OK (l'erreur a bien été détectée)");
        } catch (Exception echecAnormal) {
            System.err.println("ERREUR sur le test de création invalide : " + echecAnormal.getMessage());
        }

        System.out.println("Fin des tests.");
    }

    /**
     * Vérifie qu'on peut créer un polynôme normalement.
     * @throws Exception - capture une erreur si une combinaison est invalide
     */
    private void testCreationValide() throws Exception {
    	String[][] valide = {
    	//TODO inserer des valeurs de test
    			{"3x"}, 
    			{"3x^2"}, 
    			{"3x^10-9x^2-3"} 
    	};
    	for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
    		try {
    			String[] combinaison = valide[numeroTest];
    			new Polynome(combinaison[0]);
    		} catch (IllegalArgumentException attendue) {
    			throw new Exception("ERR combinaison invalide indice : " + numeroTest); 
    		}
    	}
    }
    
    /**
     * Vérifie que le programme détecte bien les cas erreurs.
     * @throws Exception - capture une erreur si une combinaison est valide
     */
    private void testCreationInvalide() throws Exception {
    	String[][] invalide = {
    	//TODO inserer des valeurs de test
    			{"3y"},    // OK : Erreur bien détecter
    			{""},      // OK : Erreur bien détecters
    			{"3x^2s"}, // OK : Erreur bien détecterS
    	};
    	for (int numeroTest = 0; numeroTest < invalide.length; numeroTest++) {
    		try {
    			String[] combinaison = invalide[numeroTest];
    			new Polynome(combinaison[0]);
    			throw new Exception("ERR combinaison valide indice : " + numeroTest);
    		} catch (IllegalArgumentException attendue) {
    			// C'est normal, le test a réussi car l'erreur a été détectée
    		}
    	}
    }
    
    /**
     * Vérifie le bon fonctionnement de la méthode Limite et retourne le bon résultat
     * @throws Exception - capture une erreur si le résultat retourné n'est pas le bon
     */
    private void testLimite() throws Exception {
    	String[][] limiteValide = {
    			// test avec valeurs standard
    			{"3x^2+7x"}, // positif et exposant paire
    			{"-17x^6+88x^3+1"}, // negatif et exposant paire
    			{"12x^5"}, // positif et exposant impaire
    			{"-3x^3-7x+2"}, // negatif et exposant impaire
    			// test avec valeur particulière
    			{"x^7+4x^2-4"}, // constante égale à 1, exposant paire
    			{"-x^4+5"}, // constante égale à -1, exposant paire
    			{"777x+1"}, // positif pas d'exposant
    			{"-10x-77"}, //negatif pas d'exposant
    			{"x^2+x"}, // constante égale à 1, exposant impaire
    			{"-x^25+7x^10-10"}, // constante égale à -1, exposant impaire
    			// cas monome uniquement x ou -x
    			{"x"},
    			{"-x"},
    			{"1x"}, // equvalant à x mais avec plus de précision
    			{"-1x"}, // equivalant à -x mais avec plus de  précision
    			// cas monome constante
    			{"10"},
    			{"-4"},
    			{"0"},
    	};
    	String[] resultatAttendu = {
    			// resultat valeur standard
    			"+Infini",
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			// resultat valeur particulière
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			"+Infini",
    			"+Infini",
    			"-Infini",
    			"+Infini",
    			// resultat cas x ou -x
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			"+Infini",
    			"-Infini",
    			"-Infini",
    			"+Infini",
    			// resultat constante
    			"10",
    			"10",
    			"-4",
    			"-4",
    			"0",
    			"0"
    	};
    	boolean correct = true;
    	for (int numeroTest = 0; numeroTest < limiteValide.length; numeroTest++) {
    		try {
    			Polynome valeurATester = new Polynome(limiteValide[numeroTest][0]);
    			String resultatRecuperer = valeurATester.limite('+');
    			boolean comparaison; 
    			comparaison = resultatRecuperer.equals(resultatAttendu[numeroTest*2]);
    			correct = comparaison == true;
    			resultatRecuperer = valeurATester.limite('-'); 
    			comparaison = resultatRecuperer.equals(resultatAttendu[numeroTest*2+1]);
    			correct = comparaison == true;
    			System.out.println("test limite n°" + numeroTest + " en +Infini et -Infini passé et resultat est " + correct);
    		} catch (IllegalArgumentException nonAttendu){
    			System.out.println("ERR limite n'a pas pu être calculé pour le test n°" + numeroTest);
    		}
    	}
    }
    
    /**
     * Vérifie que la méthode derive() fonctionne correctement.
     * @throws Exception - capture une erreur si le résultat est incorrect
     */
    private void testDerive() throws Exception {

        String[][] valide = {

            // polynome        derive attendu
            {"3x^2",           "[6x]"},
            {"5x^3",           "[15x^2]"},
            {"7x",             "[7]"},
            {"9",              "[]"},
            {"x",              "[1]"},
            {"3x^2+2x+1",      "[6x, 2]"},
            {"-4x^3+2x^2",     "[-12x^2, 4x]"}
        };

        for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
            try {
                String[] combinaison = valide[numeroTest];
                Polynome polynomeTest = new Polynome(combinaison[0]);
                String resultatObtenu = polynomeTest.derive(1).toString();
                String resultatAttendu = combinaison[1];
                if (!resultatObtenu.equals(resultatAttendu)) {
                    throw new Exception(
                        "ERR derive incorrect indice : " + numeroTest
                        + " attendu = " + resultatAttendu
                        + " obtenu = " + resultatObtenu
                    );
                }
            } catch (IllegalArgumentException attendue) {
                throw new Exception("ERR combinaison invalide indice : " + numeroTest);
            }
        }
    }
    
    /**
     * Vérifie que la méthode racines() fonctionne correctement.
     * @throws Exception - capture une erreur si le résultat est incorrect
     */
    private void testRacines() throws Exception {

        String[][] valide = {

            // polynome                     racines attendues
            {"x-1",                         "[1.0]"},
            {"x+2",                         "[-2.0]"},
            {"x^2-4",                       "[-2.0, 2.0]"},
            {"x^2-2x+1",                    "[1.0]"},          // racine double
            {"x^2+1",                       "[]"},             // aucune racine réelle
            {"x^2-5x+6",                    "[2.0, 3.0]"},
            {"x^3-6x^2+11x-6",              "[1.0, 2.0, 3.0]"},
            {"2x-8",                        "[4.0]"},
            {"x^2",                         "[0.0]"},
            {"x^3",                         "[0.0]"},
            

            {"999999x^2-999999",            "[-1.0, 1.0]"},
            {"x^2-1000000",                 "[-1000.0, 1000.0]"},
            {"x^4-16",                      "[-2.0, 2.0]"},
            {"x^6-x^3",                     "[0.0, 1.0]"},
            {"5x^2+0x-20",                  "[-2.0, 2.0]"},
            
            {"x^2-2",                       "[-1.4142, 1.4142]"}, // 2 racines de type double
            {"2x-1",                        "[0.5]"},             // 1 racines de type double
            {"x^2-3",                       "[-1.7320, 1.7320]"}  // 2 racines de type double (positve et négative)
        };

        for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
            try {
                String[] combinaison = valide[numeroTest];
                Polynome polynomeTest = new Polynome(combinaison[0]);
                String resultatObtenu = polynomeTest.racines().toString();
                String resultatAttendu = combinaison[1];

                if (!resultatObtenu.equals(resultatAttendu)) {
                    throw new Exception(
                        "ERR racines incorrect indice : " + numeroTest
                        + " attendu = " + resultatAttendu
                        + " obtenu = " + resultatObtenu
                    );
                }

            } catch (IllegalArgumentException attendue) {
                throw new Exception("ERR combinaison invalide indice : " + numeroTest);
            }
        }
    }
    
	private void testMaxDegres() throws Exception {
	    String[][] valide = { 
	        { "1" }, { "42" }, { "x" }, { "3x" }, { "-3x" }, { "x^2" }, { "3x^2" }, { "3x^10" },
	        { "3x^2 + 2x + 1" }, { "3x^2+2x+1" }, { "3x^2 - 2x + 1" }, { "3x^2 - 2x - 1" }, { "-3x^2 + 2x + 1" },
	        { "-x + 3" }, { "3x^10 - 9x^2 - 3" }, { "5x^5 + 1" } 
	    };

	    int[] resultatAttendu = {
	        0, 0, 1, 1, 1, 2, 2, 10,
	        2, 2, 2, 2, 2,
	        1, 10, 5
	    };

	    boolean correct;

	    for (int numeroTest = 0; numeroTest < valide.length; numeroTest++) {
	        try {
	            String[] combinaison = valide[numeroTest];
	            
	            Polynome valeurATester = new Polynome(combinaison[0]);
	            
	            int resultatRecupere = valeurATester.MaxDegres(); 
	            
	            boolean comparaison = (resultatRecupere == resultatAttendu[numeroTest]);
	            correct = comparaison; 
	            
	            if (correct) {
	                System.out.println("test degresMax n°" + numeroTest + " passé et le resultat est " + correct);
	            } else {
	                System.out.println("ECHEC test degresMax n°" + numeroTest + " sur " + combinaison[0] + 
	                                   " | Attendu : " + resultatAttendu[numeroTest] + " | Obtenu : " + resultatRecupere);
	            }

	        } catch (IllegalArgumentException echecAnormal) {
	            throw new Exception("ERR combinaison valide rejetée à tort, indice : " + numeroTest + " -> "
	                    + valide[numeroTest][0]);
	        }
	    }
	}
}