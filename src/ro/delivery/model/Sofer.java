package ro.delivery.model;

public class Sofer extends Utilizator {
    private String vehicul;

    public Sofer(int id, String nume, String vehicul) {
        super(id, nume);
        this.vehicul = vehicul;
    }

    public String getVehicul() { return vehicul; }

    @Override
    public String toString() {
        return "ID: " + id + " | Sofer: " + nume + " (Vehicul: " + vehicul + ")";
    }
}