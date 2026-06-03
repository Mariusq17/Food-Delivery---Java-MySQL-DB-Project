package ro.delivery.model;

public class Client extends Utilizator {
    private Adresa adresaLivrare;

    public Client(int id, String nume, Adresa adresaLivrare) {
        super(id, nume);
        this.adresaLivrare = adresaLivrare;
    }

    public Adresa getAdresaLivrare() {
        return adresaLivrare;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Client: " + nume + " | Adresa: " + adresaLivrare;
    }
}