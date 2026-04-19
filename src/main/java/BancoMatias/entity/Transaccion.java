package BancoMatias.entity;

import BancoMatias.entity.enums.EstadoTransaccion;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaccion {
    private UUID id;
    private String cbuOrigen;
    private String cbuDestino;
    private double monto;
    private EstadoTransaccion estado;
    private String motivo;
    private LocalDateTime fecha;

    public Transaccion(String origen, String destino, double monto, EstadoTransaccion estado) {
        this.id = UUID.randomUUID();
        this.cbuOrigen = origen;
        this.cbuDestino = destino;
        this.monto = monto;
        this.estado = estado;
        this.fecha = LocalDateTime.now();
    }

    public String getCbuOrigen() {
        return cbuOrigen;
    }

    public UUID getId() {
        return id;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }

    public void setCbuOrigen(String cbuOrigen) {
        this.cbuOrigen = cbuOrigen;
    }

    public String getCbuDestino() {
        return cbuDestino;
    }

    public void setCbuDestino(String cbuDestino) {
        this.cbuDestino = cbuDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }


    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}