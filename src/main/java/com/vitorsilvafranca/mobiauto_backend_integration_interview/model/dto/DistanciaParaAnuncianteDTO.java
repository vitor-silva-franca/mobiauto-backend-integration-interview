package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto;

public class DistanciaParaAnuncianteDTO {

    private Long lojistaId;
    private String nome;
    private Double distanciaKm;
    private Double duracaoMin;

    public DistanciaParaAnuncianteDTO(Long lojistaId, String nome, Double distanciaKm, Double duracaoMin) {
        this.lojistaId = lojistaId;
        this.nome = nome;
        this.distanciaKm = distanciaKm;
        this.duracaoMin = duracaoMin;
    }

    public Long getLojistaId() {
        return lojistaId;
    }

    public void setLojistaId(Long lojistaId) {
        this.lojistaId = lojistaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Double getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(Double duracaoMin) {
        this.duracaoMin = duracaoMin;
    }
}
