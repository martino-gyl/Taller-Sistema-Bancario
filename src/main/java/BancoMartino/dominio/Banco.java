package BancoMartino.dominio;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nombre;
    private List<Sucursal> sucursales;

    public Banco(String nombre) {
        this.nombre = nombre;
        this.sucursales = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void agregarSucursal(Sucursal sucursal) {
        if (buscarSucursalPorCodigo(sucursal.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una sucursal con ese código");
        }
        sucursales.add(sucursal);
    }

    public Sucursal buscarSucursalPorCodigo(String codigo) {
        for (Sucursal sucursal : sucursales) {
            if (sucursal.getCodigo().equals(codigo)) {
                return sucursal;
            }
        }
        return null;
    }

    public void asignarAdminASucursal(String codigoSucursal, Admin admin) {
        Sucursal sucursal = buscarSucursalPorCodigo(codigoSucursal);

        if (sucursal == null) {
            throw new IllegalArgumentException("Sucursal inexistente");
        }

        sucursal.setAdmin(admin);
    }

    public double calcularBalanceTotal() {
        double total = 0;
        for (Sucursal sucursal : sucursales) {
            total += sucursal.calcularBalanceSucursal();
        }
        return total;
    }

    public Cuenta buscarCuentaPorNumero(String numero) {
        for (Sucursal sucursal : sucursales) {
            Cuenta cuenta = sucursal.buscarCuentaPorNumero(numero);
            if (cuenta != null) {
                return cuenta;
            }
        }
        return null;
    }

    public Cuenta obtenerCuentaPorId(String idCuenta) {
        return buscarCuentaPorNumero(idCuenta);
    }

    public Cuenta autenticarCuenta(String numero, String password) {
        Cuenta cuenta = buscarCuentaPorNumero(numero);

        if (cuenta != null && cuenta.validarPassword(password)) {
            return cuenta;
        }

        return null;
    }

    public Admin autenticarAdmin(String codigoSucursal, String usuario, String password) {
        Sucursal sucursal = buscarSucursalPorCodigo(codigoSucursal);

        if (sucursal == null || sucursal.getAdmin() == null) {
            return null;
        }

        Admin admin = sucursal.getAdmin();

        if (admin.getUsuario().equals(usuario) && admin.validarPassword(password)) {
            return admin;
        }

        return null;
    }
}