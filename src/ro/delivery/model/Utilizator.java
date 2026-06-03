package ro.delivery.model;

public abstract class Utilizator {
    protected int id;
    protected String nume;

    public Utilizator(int id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
}