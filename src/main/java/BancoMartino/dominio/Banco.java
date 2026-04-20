package BancoMartino.dominio;

import BancoMartino.servicios.TransaccionRepositorio;
import BancoMartino.servicios.TransactionService;
import Integration.CbuService;
import Integration.Mediator;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nombre;
    private List<Sucursal> sucursales;
    private String codigoBanco;
    private CbuService cbuService;
    public final String CODIGO_BANCO_MARTINO = "102";
    private TransactionService transactionService;
    private TransaccionRepositorio transaccionRepositorio;

    public Banco(String nombre, String codigoBanco) {
        this.nombre = nombre;
        this.codigoBanco = codigoBanco;
        this.sucursales = new ArrayList<>();
        this.cbuService = new CbuService();
        this.transaccionRepositorio = new TransaccionRepositorio();
        this.transactionService = new TransactionService(this, transaccionRepositorio);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public String generarCbu(Sucursal sucursal) {
        return cbuService.generarCbu(
                codigoBanco,
                sucursal.getCodigo(),
                sucursal.obtenerSiguienteNumeroCuenta()
        );
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

    public Cuenta buscarCuentaPorCbu(String cbu) {
        for (Sucursal sucursal : sucursales) {
            Cuenta cuenta = sucursal.buscarCuentaPorCbu(cbu);
            if (cuenta != null) {
                return cuenta;
            }
        }
        return null;
    }

    public Cuenta buscarCuentaPorEmail(String email) {
        for (Sucursal sucursal : sucursales) {
            for (Cuenta cuenta : sucursal.getCuentas()) {
                if (cuenta.getEmail().equalsIgnoreCase(email)) {
                    return cuenta;
                }
            }
        }
        return null;
    }

    public Cuenta autenticarCuenta(String email, String password) {
        Cuenta cuenta = buscarCuentaPorEmail(email);

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

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public void setMediador(Mediator mediador) {
        transactionService.setMediador(mediador);
    }
}