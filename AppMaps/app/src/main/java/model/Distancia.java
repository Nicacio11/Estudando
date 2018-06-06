package model;

public class Distancia {
    private double distancia;
    private String unidade;

    public Distancia(double distancia, String unidade) {
        this.distancia = distancia;
        this.unidade = unidade;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


}
