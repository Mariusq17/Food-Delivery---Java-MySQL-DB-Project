package ro.delivery.service;

import ro.delivery.model.*;
import ro.delivery.repository.*;
import java.util.*;

public class DeliveryService {
    private RestaurantRepository restRepo = new RestaurantRepository();
    private ClientRepository clientRepo = new ClientRepository();
    private SoferRepository soferRepo = new SoferRepository();
    private ProdusRepository produsRepo = new ProdusRepository();
    private AdminRepository adminRepo = new AdminRepository();
    private ComandaRepository comandaRepo = new ComandaRepository();
    private AuditService audit = AuditService.getInstance();

    // ADMIN & AUTH
    public boolean autentificareAdmin(String u, String p) { return adminRepo.login(u, p); }
    public Client loginClient(String n) { return clientRepo.findByName(n); }
    public Sofer loginSofer(String n) { return soferRepo.findByName(n); }

    // RESTAURANTE & PRODUSE
    public void adaugaRestaurant(Restaurant r) { restRepo.insert(r); audit.logAction("C_Restaurant"); }
    public List<Restaurant> getRestaurante() { return restRepo.findAll(); }
    public Restaurant getRestaurantById(int id) { return restRepo.findById(id); }
    public void updateRestaurant(int id, String n) { restRepo.update(id, n); audit.logAction("U_Restaurant"); }
    public void stergeRestaurant(int id) { restRepo.delete(id); audit.logAction("D_Restaurant"); }

    public void adaugaProdus(Produs p, int idR) { produsRepo.insert(p, idR); audit.logAction("C_Produs"); }
    public List<Produs> getProduse(int idR) { return produsRepo.findByRestaurant(idR); }

    public void afiseazaRestaurante() {
        System.out.println("\n--- Restaurante Disponibile ---");
        restRepo.findAll().forEach(System.out::println);
    }

    public void afiseazaMeniu(int idR) {
        List<Produs> lp = produsRepo.findByRestaurant(idR);
        for(int i=0; i<lp.size(); i++) System.out.println((i+1) + ". " + lp.get(i).getDenumire() + " (" + lp.get(i).getPret() + " RON)");
    }

    // SOFERI & CLIENTI (ADMIN CRUD)
    public List<Sofer> getSoferi() { return soferRepo.findAll(); }
    public Sofer getSoferById(int id) { return soferRepo.findById(id); }
    public void adaugaSofer(Sofer s) { soferRepo.insert(s); }
    public void updateSofer(int id, String n, String v) { soferRepo.update(id, n, v); }
    public void stergeSofer(int id) { soferRepo.delete(id); }

    public List<Client> getClienti() { return clientRepo.findAll(); }
    public Client getClientById(int id) { return clientRepo.findById(id); }
    public void adaugaClient(Client c, String o, String s) { clientRepo.insert(c, o, s); }
    public void updateClient(int id, String n, String o, String s) { clientRepo.update(id, n, o, s); }
    public void stergeClient(int id) { clientRepo.delete(id); }

    // COMENZI
    public void plaseazaComanda(int idC, int idR, double tot) { comandaRepo.create(idC, idR, tot); audit.logAction("C_Comanda"); }
    public void istoricClient(int idC) { comandaRepo.afiseazaIstoricClient(idC); }
    public void comenziPending() { comandaRepo.afiseazaComenziDisponibile(); }
    public void comenziActiveSofer(int idS) { comandaRepo.afiseazaComenziSofer(idS); }
    public void preiaComanda(int idC, int idS) { comandaRepo.preiaComanda(idC, idS); audit.logAction("Preluare_Comanda"); }
    public void finalizeazaComanda(int idC, int idS) { comandaRepo.finalizeazaComanda(idC, idS); audit.logAction("Finalizare_Comanda"); }
    public void adminToateComenzile() { comandaRepo.afiseazaToateComenzile(); }
}