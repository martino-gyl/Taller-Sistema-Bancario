package BancoMartino.dominio;

import java.util.List;

public class Admin {
    private String nombre;
    private String usuario;
    private String password;
    private Sucursal sucursal;

    public Admin(String nombre, String usuario, String password) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }

    public void asignarSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public Cuenta darAltaCuenta(
            String numeroCuenta,
            String passwordCuenta,
            TipoCuenta tipo,
            String dni,
            String nombre,
            String apellido,
            String email,
            String direccion
    ) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        if (sucursal.buscarCuentaPorNumero(numeroCuenta) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese número en la sucursal");
        }

        if (sucursal.buscarCuentaPorDni(dni) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta asociada a ese DNI en la sucursal");
        }

        Cuenta cuenta = new Cuenta(
                numeroCuenta,
                passwordCuenta,
                sucursal,
                tipo,
                dni,
                nombre,
                apellido,
                email,
                direccion
        );

        sucursal.registrarCuenta(cuenta);
        return cuenta;
    }

    public void darBajaCuenta(String numeroCuenta) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        Cuenta cuenta = sucursal.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no pertenece a esta sucursal");
        }

        sucursal.eliminarCuenta(numeroCuenta);
    }

    public List<Cuenta> listarCuentas() {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        return sucursal.getCuentas();
    }

    public double verBalanceSucursal() {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        return sucursal.calcularBalanceSucursal();
    }

    public void transferir(String numeroOrigen, String numeroDestino, double monto) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        Cuenta origen = sucursal.buscarCuentaPorNumero(numeroOrigen);
        Cuenta destino = sucursal.buscarCuentaPorNumero(numeroDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Ambas cuentas deben pertenecer a esta sucursal");
        }

        if (origen == destino) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        origen.restarSaldo(monto);
        destino.sumarSaldo(monto);

        origen.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto,
                "Transferencia a cuenta " + destino.getNumero()
        );

        destino.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto,
                "Transferencia desde cuenta " + origen.getNumero()
        );
    }
}