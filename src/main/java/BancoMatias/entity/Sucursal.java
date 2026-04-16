package BancoMatias.entity;

import java.util.ArrayList;

public class Sucursal {
    private String nombre;
    private String direccion;
    private Double saldoTotal;
    private Admin administrador;
    private String codigo;
    private ArrayList<UsuarioCliente> usuariosActivos;
    private ArrayList<UsuarioCliente> usuariosDadosDeBaja;

    public Sucursal(String nombre,String codigo, Admin administrador, String direccion, Double saldoTotal){
        this.nombre = nombre;
        this.direccion = direccion;
        this.codigo = codigo;
        this.saldoTotal = saldoTotal;
        this.usuariosActivos = new ArrayList<>();
        this.usuariosDadosDeBaja = new ArrayList<>();
        this.administrador = administrador;
    }

    public ArrayList<UsuarioCliente> getUsuariosDadosDeBaja() {
        return usuariosDadosDeBaja;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setUsuariosDadosDeBaja(ArrayList<UsuarioCliente> usuariosDadosDeBaja) {
        this.usuariosDadosDeBaja = usuariosDadosDeBaja;
    }

    public ArrayList<UsuarioCliente> getUsuariosActivos() {
        return usuariosActivos;
    }

    public void setUsuariosActivos(ArrayList<UsuarioCliente> usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public Admin getAdministrador() {
        return administrador;
    }

    public Double getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  nombre ;
    }
}
