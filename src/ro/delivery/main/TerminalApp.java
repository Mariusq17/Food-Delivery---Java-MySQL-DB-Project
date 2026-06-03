package ro.delivery.main;

import ro.delivery.model.*;
import ro.delivery.service.DeliveryService;
import java.util.*;

public class TerminalApp {
    private DeliveryService service;
    private Scanner scanner;

    public TerminalApp() {
        this.service = new DeliveryService();
        this.scanner = new Scanner(System.in);
    }

    private int citesteIntreg() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine()); }
            catch (Exception e) { System.out.print("(!) Introduceti un numar: "); }
        }
    }

    private String proceseazaInputUpdate(String nume, String original, boolean obligatoriu) {
        while (true) {
            System.out.print(nume + " [" + original + "]: ");
            String in = scanner.nextLine();
            if (in.isEmpty()) return original;
            if (in.trim().isEmpty()) {
                if (obligatoriu) { System.out.println("Camp obligatoriu!"); continue; }
                return "";
            }
            return in.trim();
        }
    }

    public void start() {
        while (true) {
            System.out.println("\n=== FOOD DELIVERY SYSTEM ===");
            System.out.println("1. Portal Client | 2. Portal Sofer | 3. ADMIN PANEL | 0. Iesire");
            String opt = scanner.nextLine();
            if (opt.equals("1")) portalClient();
            else if (opt.equals("2")) portalSofer();
            else if (opt.equals("3")) loginAdmin();
            else if (opt.equals("0")) break;
        }
    }

    // --- PORTAL CLIENT ---
    private void portalClient() {
        System.out.print("\n1. Login | 2. Register | 0. Inapoi: ");
        String opt = scanner.nextLine();
        if (opt.equals("1")) {
            System.out.print("Nume: "); Client c = service.loginClient(scanner.nextLine());
            if (c != null) meniuClient(c); else System.out.println("Nu exista!");
        } else if (opt.equals("2")) {
            System.out.print("Nume: "); String n = scanner.nextLine();
            System.out.print("Oras: "); String o = scanner.nextLine();
            System.out.print("Strada: "); String s = scanner.nextLine();
            service.adaugaClient(new Client(0, n, new Adresa(o, s)), o, s);
            meniuClient(service.loginClient(n));
        }
    }

    private void meniuClient(Client c) {
        while (true) {
            System.out.println("\n--- CLIENT: " + c.getNume() + " ---");
            System.out.println("1. Restaurante | 2. Comanda NOUA | 3. Istoric | 0. Logout");
            String opt = scanner.nextLine();
            if (opt.equals("1")) service.afiseazaRestaurante();
            else if (opt.equals("2")) flowComanda(c);
            else if (opt.equals("3")) service.istoricClient(c.getId());
            else if (opt.equals("0")) break;
        }
    }

    private void flowComanda(Client c) {
        service.afiseazaRestaurante();
        System.out.print("ID Restaurant: "); int idR = citesteIntreg();
        Restaurant r = service.getRestaurantById(idR);
        if (r == null) return;
        service.afiseazaMeniu(idR);
        List<Produs> meniu = service.getProduse(idR);
        double total = 0;
        while (true) {
            System.out.print("Nr. produs (0=Gata): "); int p = citesteIntreg();
            if (p == 0) break;
            if (p > 0 && p <= meniu.size()) { total += meniu.get(p-1).getPret(); System.out.println("Adaugat!"); }
        }
        if (total > 0) { service.plaseazaComanda(c.getId(), idR, total); System.out.println("Comanda plasata!"); }
    }

    // --- PORTAL SOFER ---
    private void portalSofer() {
        System.out.print("\n1. Login | 2. Register | 0. Inapoi: ");
        String opt = scanner.nextLine();
        if (opt.equals("1")) {
            System.out.print("Nume: "); Sofer s = service.loginSofer(scanner.nextLine());
            if (s != null) meniuSofer(s); else System.out.println("Nu exista!");
        } else if (opt.equals("2")) {
            System.out.print("Nume: "); String n = scanner.nextLine();
            System.out.print("Vehicul: "); String v = scanner.nextLine();
            service.adaugaSofer(new Sofer(0, n, v));
            meniuSofer(service.loginSofer(n));
        }
    }

    private void meniuSofer(Sofer s) {
        while (true) {
            System.out.println("\n--- SOFER: " + s.getNume() + " ---");
            System.out.println("1. Comenzi Noi | 2. Preia | 3. In curs | 4. Finalizeaza | 0. Logout");
            String opt = scanner.nextLine();
            if (opt.equals("1")) service.comenziPending();
            else if (opt.equals("2")) { System.out.print("ID (0=Cancel): "); int id = citesteIntreg(); if(id!=0) service.preiaComanda(id, s.getId()); }
            else if (opt.equals("3")) service.comenziActiveSofer(s.getId());
            else if (opt.equals("4")) { System.out.print("ID (0=Cancel): "); int id = citesteIntreg(); if(id!=0) service.finalizeazaComanda(id, s.getId()); }
            else if (opt.equals("0")) break;
        }
    }

    // --- ADMIN PANEL ---
    private void loginAdmin() {
        for(int i=3; i>0; i--) {
            System.out.print("User: "); String u = scanner.nextLine();
            System.out.print("Pass: "); String p = scanner.nextLine();
            if (service.autentificareAdmin(u, p)) { meniuAdmin(); return; }
            System.out.println("Gresit! Ramase: " + (i-1));
        }
    }

    private void meniuAdmin() {
        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Restaurante | 2. Soferi | 3. Clienti | 4. TOATE COMENZILE | 0. Logout");
            String opt = scanner.nextLine();
            if (opt.equals("1")) subMeniuRestaurante();
            else if (opt.equals("2")) subMeniuSoferi();
            else if (opt.equals("3")) subMeniuClienti();
            else if (opt.equals("4")) service.adminToateComenzile();
            else if (opt.equals("0")) break;
        }
    }

    private void subMeniuRestaurante() {
        while (true) {
            System.out.println("\n1. Lista | 2. Adauga | 3. Update | 4. Sterge | 5. Adauga Produs | 0. Inapoi");
            String opt = scanner.nextLine();
            if (opt.equals("0")) break;
            if (opt.equals("1")) service.afiseazaRestaurante();
            else if (opt.equals("2")) { System.out.print("Nume: "); service.adaugaRestaurant(new Restaurant(scanner.nextLine())); }
            else if (opt.equals("3")) {
                System.out.print("ID: "); int id = citesteIntreg(); Restaurant r = service.getRestaurantById(id);
                if(r != null) service.updateRestaurant(id, proceseazaInputUpdate("Nume", r.getNume(), true));
            } else if (opt.equals("4")) { System.out.print("ID: "); service.stergeRestaurant(citesteIntreg()); }
            else if (opt.equals("5")) {
                System.out.print("ID Rest: "); int id = citesteIntreg(); System.out.print("Produs: "); String n = scanner.nextLine();
                System.out.print("Pret: "); double pr = citesteDouble(); service.adaugaProdus(new Produs(n, pr), id);
            }
        }
    }

    private void subMeniuSoferi() {
        while (true) {
            System.out.println("\n1. Lista | 2. Update | 3. Sterge | 0. Inapoi");
            String opt = scanner.nextLine();
            if (opt.equals("0")) break;
            if (opt.equals("1")) service.getSoferi().forEach(System.out::println);
            else if (opt.equals("2")) {
                System.out.print("ID: "); int id = citesteIntreg(); Sofer s = service.getSoferById(id);
                if(s != null) service.updateSofer(id, proceseazaInputUpdate("Nume", s.getNume(), true), proceseazaInputUpdate("Vehicul", s.getVehicul(), true));
            } else if (opt.equals("3")) { System.out.print("ID: "); service.stergeSofer(citesteIntreg()); }
        }
    }

    private void subMeniuClienti() {
        while (true) {
            System.out.println("\n1. Lista | 2. Update | 3. Sterge | 0. Inapoi");
            String opt = scanner.nextLine();
            if (opt.equals("0")) break;
            if (opt.equals("1")) service.getClienti().forEach(System.out::println);
            else if (opt.equals("2")) {
                System.out.print("ID: "); int id = citesteIntreg(); Client c = service.getClientById(id);
                if(c != null) service.updateClient(id, proceseazaInputUpdate("Nume", c.getNume(), true), proceseazaInputUpdate("Oras", c.getAdresaLivrare().getOras(), false), proceseazaInputUpdate("Strada", c.getAdresaLivrare().getStrada(), false));
            } else if (opt.equals("3")) { System.out.print("ID: "); service.stergeClient(citesteIntreg()); }
        }
    }

    private double citesteDouble() {
        while (true) { try { return Double.parseDouble(scanner.nextLine()); } catch (Exception e) { System.out.print("(!) Pret valid: "); } }
    }
}