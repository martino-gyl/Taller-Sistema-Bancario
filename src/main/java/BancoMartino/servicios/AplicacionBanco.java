package BancoMartino.servicios;

import dominio.*;

import java.util.List;

public class AplicacionBanco {
    private final Banco banco;

    public AplicacionBanco(Banco banco) {
        this.banco = banco;
    }

    public Admin loginAdmin(String codigoSucursal, String usuario, String password) {
        Admin admin = banco.autenticarAdmin(codigoSucursal, usuario, password);

        if (admin == null) {
            throw new IllegalArgumentException("Credenciales de administrador inválidas.");
        }

        return admin;
    }

    public Cuenta loginCuenta(String numeroCuenta, String password) {
        Cuenta cuenta = banco.autenticarCuenta(numeroCuenta, password);

        if (cuenta == null) {
            throw new IllegalArgumentException("Credenciales de cuenta inválidas.");
        }

        return cuenta;
    }

    public List<Sucursal> listarSucursales() {
        return banco.getSucursales();
    }

    public void crearSucursal(String codigo, String nombre, String direccion) {
        banco.agregarSucursal(new Sucursal(codigo, nombre, direccion));
    }

    public void asignarAdmin(String codigoSucursal, String nombreAdmin, String usuarioAdmin, String passwordAdmin) {
        banco.asignarAdminASucursal(
                codigoSucursal,
                new Admin(nombreAdmin, usuarioAdmin, passwordAdmin)
        );
    }

    public double balanceBanco() {
        return banco.calcularBalanceTotal();
    }

    public double balanceSucursal(String codigoSucursal) {
        Sucursal sucursal = banco.buscarSucursalPorCodigo(codigoSucursal);

        if (sucursal == null) {
            throw new IllegalArgumentException("Sucursal inexistente.");
        }

        return sucursal.calcularBalanceSucursal();
    }

    public double balanceSucursal(Admin admin) {
        return admin.verBalanceSucursal();
    }

    public List<Cuenta> listarCuentas(Admin admin) {
        return admin.listarCuentas();
    }

    public void crearCuenta(
            Admin admin,
            String numeroCuenta,
            String passwordCuenta,
            TipoCuenta tipo,
            String dni,
            String nombre,
            String apellido,
            String email,
            String direccion
    ) {
        admin.darAltaCuenta(
                numeroCuenta,
                passwordCuenta,
                tipo,
                dni,
                nombre,
                apellido,
                email,
                direccion
        );
    }

    public void eliminarCuenta(Admin admin, String numeroCuenta) {
        admin.darBajaCuenta(numeroCuenta);
    }

    public void transferir(Admin admin, String numeroOrigen, String numeroDestino, double monto) {
        admin.transferir(numeroOrigen, numeroDestino, monto);
    }

    public void depositar(Cuenta cuenta, double monto) {
        cuenta.depositar(monto);
    }

    public void extraer(Cuenta cuenta, double monto) {
        cuenta.extraer(monto);
    }

    public Cuenta buscarCuenta(String numeroCuenta) {
        Cuenta cuenta = banco.buscarCuentaPorNumero(numeroCuenta);

        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta inexistente.");
        }

        return cuenta;
    }

    public String resumenCuenta(Cuenta cuenta) {
        return "=== BALANCE CUENTA ===\n" +
                "Número: " + cuenta.getNumero() + "\n" +
                "Tipo: " + cuenta.getTipo() + "\n" +
                "Titular: " + cuenta.getNombreCompleto() + "\n" +
                "DNI: " + cuenta.getDni() + "\n" +
                "Email: " + cuenta.getEmail() + "\n" +
                "Dirección: " + cuenta.getDireccion() + "\n" +
                "Saldo: $" + cuenta.getSaldo() + "\n" +
                "Sucursal: " + cuenta.getSucursal().getNombre();
    }

    public String resumenCuenta(String numeroCuenta) {
        return resumenCuenta(buscarCuenta(numeroCuenta));
    }

    public String movimientosCuenta(String numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);

        StringBuilder sb = new StringBuilder();
        sb.append("=== MOVIMIENTOS ===\n");

        if (cuenta.getMovimientos().isEmpty()) {
            sb.append("La cuenta no tiene movimientos.");
            return sb.toString();
        }

        for (Movimiento movimiento : cuenta.getMovimientos()) {
            sb.append(movimiento.getFecha())
                    .append(" | ")
                    .append(movimiento.getTipo())
                    .append(" | $")
                    .append(movimiento.getMonto())
                    .append(" | ")
                    .append(movimiento.getDetalle())
                    .append("\n");
        }

        return sb.toString();
    }
}