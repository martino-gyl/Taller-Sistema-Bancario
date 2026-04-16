package BancoMartino.dominio;

import java.time.LocalDateTime;

public class Movimiento {
    private final TipoMovimiento tipo;
    private final double monto;
    private final LocalDateTime fecha;
    private final String detalle;

    public Movimiento(TipoMovimiento tipo, double monto, String detalle) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.detalle = detalle;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getDetalle() {
        return detalle;
    }
}