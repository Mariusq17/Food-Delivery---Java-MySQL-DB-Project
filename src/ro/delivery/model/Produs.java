package ro.delivery.model;

public class Produs implements Comparable<Produs> {
    private String denumire;
    private double pret;

    public Produs(String denumire, double pret) {
        this.denumire = denumire;
        this.pret = pret;
    }
    public String getDenumire() { return denumire; }
    public double getPret() { return pret; }

    @Override
    public int compareTo(Produs p) {
        int pretCompare = Double.compare(this.pret, p.pret);
        if (pretCompare != 0) return pretCompare;
        return this.denumire.compareTo(p.denumire);
    }

    @Override
    public String toString() { return denumire + " - " + pret + " RON"; }
}