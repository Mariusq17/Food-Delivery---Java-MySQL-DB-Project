package ro.delivery.main;

import ro.delivery.config.DatabaseConfiguration;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    /**
     * Reseteaza complet tabelele si contoarele ID la 1.
     * Folosita doar in modul de test automat.
     */
    public static void curataBazaDeDate() {
        System.out.println("\n>>> Pregatire mediu de test: Resetare totala baza de date...");
        try (Connection conn = DatabaseConfiguration.getConnection();
             Statement stmt = conn.createStatement()) {

            // Dezactivam constrangerile pentru a putea folosi TRUNCATE pe tabele legate
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            stmt.execute("TRUNCATE TABLE produse");
            stmt.execute("TRUNCATE TABLE comenzi");
            stmt.execute("TRUNCATE TABLE restaurante");
            stmt.execute("TRUNCATE TABLE clienti");
            stmt.execute("TRUNCATE TABLE soferi");
            stmt.execute("TRUNCATE TABLE admins");

            // Re-inseram adminul default fara de care nu ne putem loga
            stmt.execute("INSERT INTO admins (username, password) VALUES ('admin', 'admin123')");

            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println(">>> Baza de date a fost resetata. ID-urile incep de la 1.");

        } catch (Exception e) {
            System.err.println("(!) Eroare la curatarea bazei de date: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Scanner folosit doar pentru alegerea modului de pornire
        Scanner inputInitial = new Scanner(System.in);

        System.out.println("==============================================");
        System.out.println("      SISTEM GESTIUNE FOOD DELIVERY v2.0");
        System.out.println("==============================================");
        System.out.println("Selectati modul de pornire:");
        System.out.println("1. MANUAL (Pastreaza datele existente)");
        System.out.println("2. TEST AUTOMAT (Sterge tot si ruleaza script)");
        System.out.print("\nOptiune: ");

        String alegere = inputInitial.nextLine();

        if (alegere.equals("2")) {
            // Curatam baza de date DOAR daca se alege testul automat
            curataBazaDeDate();

            // Scriptul simuleaza un flux complet de utilizare
            String script =
                    // [ADMIN] - Adaugare date de baza
                    "3\nadmin\nadmin123\n" +
                            "1\n2\nPizza Hut\n" +
                            "5\n1\nPizza Pepperoni\n35\n" +
                            "5\n1\nApa Minerala\n5\n" +
                            "0\n0\n" +
                            // [CLIENT] - Inregistrare si comanda
                            "1\n2\nClient_Test\nBucuresti\nCalea Victoriei\n" +
                            "2\n1\n1\n2\n0\n" +
                            "3\n" + // Vezi istoricul (Status: WAIT)
                            "0\n0\n" +
                            // [SOFER] - Inregistrare si livrare
                            "2\n2\nSofer_Test\nMotocicleta\n" +
                            "2\n1\n" + // Preia comanda ID 1
                            "4\n1\n" + // Finalizeaza livrarea
                            "0\n0\n" +
                            // [ADMIN] - Verificare finala raport
                            "3\nadmin\nadmin123\n4\n0\n0\n";

            // Injectam scriptul in fluxul de intrare al sistemului
            System.setIn(new ByteArrayInputStream(script.getBytes()));
            System.out.println("\n[INFO] Rulare script automat activata...");
        } else {
            System.out.println("\n[INFO] Mod manual activat. Datele din DB sunt pastrate.");
        }

        // Pornim aplicatia principala
        // Daca am injectat scriptul, TerminalApp va citi din el.
        // Daca nu, va citi normal de la tastatura.
        TerminalApp app = new TerminalApp();
        app.start();

        System.out.println("\n--- Program finalizat. Toate datele au fost salvate in MySQL. ---");
    }
}