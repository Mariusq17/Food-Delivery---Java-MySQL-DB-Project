package ro.delivery.model;
import java.util.*;

public class Restaurant {
    private int id; // Adaugat pentru sincronizare DB
    private String nume;
    private Set<Produs> meniu;

    public Restaurant(int id, String nume) {
        this.id = id;
        this.nume = nume;
        this.meniu = new TreeSet<>();
    }
    // Constructor secundar pentru creare rapida
    public Restaurant(String nume) { this(0, nume); }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public Set<Produs> getMeniu() { return meniu; }
    public void adaugaProdus(Produs p) { meniu.add(p); }

    @Override
    public String toString() { return "ID: " + id + " | Restaurant: " + nume; }
}