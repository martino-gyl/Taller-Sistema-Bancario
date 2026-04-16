package BancoMartino.dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sucursal {
    private String codigo;
    private String nombre;
    private String direccion;
    private Admin admin;
    private List<Cuenta> cuentas;

    public Sucursal(String codigo, String nombre, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.cuentas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        if (admin != null) {
            admin.asignarSucursal(this);
        }
    }

    public int siguienteNumeroCuenta(){
        return cuentas.size()+1;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void registrarCuenta(Cuenta cuenta) {
        if (buscarCuentaPorCbu(cuenta.getCbu()) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese número en la sucursal");
        }
        cuentas.add(cuenta);
    }

    public void eliminarCuenta(String cbu) {
        Iterator<Cuenta> iterator = cuentas.iterator();

        while (iterator.hasNext()) {
            Cuenta cuenta = iterator.next();
            if (cuenta.getCbu().equals(cbu)) {
                iterator.remove();
                return;
            }
        }
    }

    public Cuenta buscarCuentaPorCbu(String cbu) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getCbu().equals(cbu)) {
                return cuenta;
            }
        }
        return null;
    }

    public Cuenta buscarCuentaPorDni(String dni) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getDni().equals(dni)) {
                return cuenta;
            }
        }
        return null;
    }

    public double calcularBalanceSucursal() {
        double total = 0;
        for (Cuenta cuenta : cuentas) {
            total += cuenta.getSaldo();
        }
        return total;
    }
}