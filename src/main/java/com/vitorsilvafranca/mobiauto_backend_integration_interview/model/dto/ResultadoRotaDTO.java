package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto;

public class ResultadoRotaDTO {

    private final double distanciaKm;
    private final double duracaoMin;

    public ResultadoRotaDTO(double distanciaKm, double duracaoMin) {
        this.distanciaKm = distanciaKm;
        this.duracaoMin = duracaoMin;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public double getDuracaoMin() {
        return duracaoMin;
    }

    @Override
    public String toString() {
        return String.format("Distância: %.2f km, Tempo estimado: %.0f minutos", distanciaKm, duracaoMin);
    }
}