package ro.delivery.model;

public class Recenzie {
    private Client client;
    private int nota;
    private String comentariu;

    public Recenzie(Client client, int nota, String comentariu) {
        this.client = client;
        this.nota = nota;
        this.comentariu = comentariu;
    }
    @Override
    public String toString() { return nota + "/5 - " + comentariu + " (de la " + client.getNume() + ")"; }
}