package BancoMartino.dominio;

import Integration.InterbankTransferable;

import java.util.List;

public class Admin implements InterbankTransferable {
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

    public void darAltaCuenta(
            Banco banco,
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

        if (banco.buscarCuentaPorEmail(email) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }

        if (sucursal.buscarCuentaPorDni(dni) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese dni");
        }

        String cbu = banco.generarCbu(sucursal);

        Cuenta cuenta = new Cuenta(
                cbu,
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
    }

    public void darBajaCuenta(String cbu) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        Cuenta cuenta = sucursal.buscarCuentaPorCbu(cbu);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no pertenece a esta sucursal");
        }

        sucursal.eliminarCuenta(cbu);
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

    public void transferir(String cbuOrigen, String cbuDestino, double monto) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        Cuenta origen = sucursal.buscarCuentaPorCbu(cbuOrigen);
        Cuenta destino = sucursal.buscarCuentaPorCbu(cbuDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Ambas cuentas deben pertenecer a esta sucursal");
        }

        if (origen == destino) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        if (monto > origen.getSaldo()) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta origen para transferir.");
        }

        origen.restarSaldo(monto);
        destino.sumarSaldo(monto);

        origen.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto,
                "Transferencia a cuenta cuyo email es " + destino.getEmail()
        );

        destino.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto,
                "Transferencia desde cuenta cuyo mail es " + origen.getEmail()
        );
    }

    @Override
    public void recibirTransferencia(String cbuDestino, double monto) {

    }
}