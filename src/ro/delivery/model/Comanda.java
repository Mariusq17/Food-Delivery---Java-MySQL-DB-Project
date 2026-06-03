package ro.delivery.model;
import java.util.*;

public class Comanda {
    private static int counter = 1;
    private int idComanda;
    private Client client;
    private Restaurant restaurant;
    private List<Produs> produse;
    private Sofer sofer;
    private boolean finalizata;

    public Comanda(Client client, Restaurant restaurant, List<Produs> produse) {
        this.idComanda = counter++;
        this.client = client;
        this.restaurant = restaurant;
        this.produse = new ArrayList<>(produse);
        this.finalizata = false;
    }

    public int getIdComanda() { return idComanda; }
    public Client getClient() { return client; }
    public Sofer getSofer() { return sofer; }
    public void setSofer(Sofer sofer) { this.sofer = sofer; }
    public void setFinalizata(boolean finalizata) { this.finalizata = finalizata; }
    public boolean isFinalizata() { return finalizata; }
    public Restaurant getRestaurant() { return restaurant; }

    public double getTotal() {
        double total = 0;
        for (Produs p : produse) total += p.getPret();
        return total;
    }

    @Override
    public String toString() {
        String stare = finalizata ? "Finalizata" : (sofer != null ? "In curs de livrare" : "In asteptare");
        return "Comanda #" + idComanda + " | Client: " + client.getNume() + " | Restaurant: " + restaurant.getNume() + " | Total: " + getTotal() + " RON | Status: " + stare;
    }
}