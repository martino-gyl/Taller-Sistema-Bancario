package BancoMartino.dominio;

import Integration.CbuService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cuenta {
    private String cbu;
    private String password;
    private Sucursal sucursal;
    private double saldo;
    private TipoCuenta tipo;

    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;

    private List<Movimiento> movimientos;

    public Cuenta(
            String cbu,
            String password,
            Sucursal sucursal,
            TipoCuenta tipo,
            String dni,
            String nombre,
            String apellido,
            String email,
            String direccion
    ) {
        this.cbu = cbu;
        this.password = password;
        this.sucursal = sucursal;
        this.tipo = tipo;
        this.saldo = 0;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = direccion;
        this.movimientos = new ArrayList<>();
    }

    public String getCbu() {
        return cbu;
    }

    public TipoCuenta getTipo() {
        return tipo;
    }

    public String getDni() {
        return dni;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getSaldo() {
        return saldo;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }

    public void depositar(double monto) {
        sumarSaldo(monto);
        registrarMovimiento(TipoMovimiento.DEPOSITO, monto, "Depósito en cuenta");
    }

    public void extraer(double monto) {
        if (monto > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        restarSaldo(monto);
        registrarMovimiento(TipoMovimiento.EXTRACCION, monto, "Extracción de cuenta");
    }

    public void sumarSaldo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        saldo += monto;
    }

    public void restarSaldo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        saldo -= monto;
    }

    public void registrarMovimiento(TipoMovimiento tipo, double monto, String detalle) {
        movimientos.add(new Movimiento(tipo, monto, detalle));
    }

    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }
}