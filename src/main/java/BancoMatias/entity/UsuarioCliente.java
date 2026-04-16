package BancoMatias.entity;

import BancoMatias.entity.enums.TipoDeCuenta;

import java.util.ArrayList;

public class UsuarioCliente extends Usuario{
    private String name;
    private String direccion;
    private TipoDeCuenta tipoDeCuenta;
    private Double saldo;
    private ArrayList<Transaccion> historialTransaccion;
    private boolean estaActivado;


    // 101 500 1234

    public UsuarioCliente(String name, String mail, String password, String direccion, TipoDeCuenta tipoDeCuenta) {
        super(mail, password);
        this.name = name;
        this.direccion = direccion;
        this.tipoDeCuenta = tipoDeCuenta;
        this.saldo = 100.0;
        this.historialTransaccion = new ArrayList<>();
        this.estaActivado = true;
    }


    public void restarSaldo(double monto){
        this.saldo -= monto;
    }
    public void sumarSaldo(double monto){this.saldo += monto;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public TipoDeCuenta getTipoDeCuenta() {
        return tipoDeCuenta;
    }

    public void setTipoDeCuenta(TipoDeCuenta tipoDeCuenta) {
        this.tipoDeCuenta = tipoDeCuenta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public ArrayList<Transaccion> getHistorialTransaccion() {
        return historialTransaccion;
    }

    public void setHistorialTransaccion(ArrayList<Transaccion> historialTransaccion) {
        this.historialTransaccion = historialTransaccion;
    }

    public boolean isEstaActivado() {
        return estaActivado;
    }

    public void setEstaActivado(boolean estaActivado) {
        this.estaActivado = estaActivado;
    }
}
