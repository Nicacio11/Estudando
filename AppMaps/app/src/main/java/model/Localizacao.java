package model;

public class Localizacao {
    private int id;
    private Double latitude;
    private Double longitude;
    private String descricao;

    public Localizacao() {
    }

    public Localizacao(Double latitude, Double longitude, String descricao) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = descricao;
    }

    public Localizacao(int id, Double latitude, Double longitude, String descricao) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
