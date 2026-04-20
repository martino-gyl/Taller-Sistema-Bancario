package BancoMartino.servicios;

import BancoMartino.dominio.*;
import BancoMatias.entity.enums.EstadoTransaccion;
import Integration.ResultadoTransferencia;

import java.util.List;

public class AplicacionBanco {
    private final Banco banco;
    private final TransactionService transactionService;

    public AplicacionBanco(Banco banco) {
        this.banco = banco;
        this.transactionService = banco.getTransactionService();
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
            String passwordCuenta,
            TipoCuenta tipo,
            String dni,
            String nombre,
            String apellido,
            String email,
            String direccion
    ) {
        admin.darAltaCuenta(banco,
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

    public ResultadoTransferencia transferir(String numeroOrigen, String numeroDestino, double monto) {
        return transactionService.iniciarTransferencia(numeroOrigen, numeroDestino, monto);
    }

    public void depositar(Cuenta cuenta, double monto) {
        cuenta.depositar(monto);
    }

    public void extraer(Cuenta cuenta, double monto) {
        cuenta.extraer(monto);
    }

    public Cuenta buscarCuenta(String cbu) {
        Cuenta cuenta = banco.buscarCuentaPorCbu(cbu);

        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta inexistente.");
        }

        return cuenta;
    }

    public String resumenCuenta(Cuenta cuenta) {
        return "=== BALANCE CUENTA ===\n" +
                "CBU: " + cuenta.getCbu() + "\n" +
                "Tipo: " + cuenta.getTipo() + "\n" +
                "Titular: " + cuenta.getNombreCompleto() + "\n" +
                "DNI: " + cuenta.getDni() + "\n" +
                "Email: " + cuenta.getEmail() + "\n" +
                "Dirección: " + cuenta.getDireccion() + "\n" +
                "Saldo: $" + cuenta.getSaldo() + "\n" +
                "Sucursal: " + cuenta.getSucursal().getNombre();
    }

    public String resumenCuenta(String cbu) {
        return resumenCuenta(buscarCuenta(cbu));
    }

    public String transaccionesCuenta(String cbu) {
        List<Transaccion> transacciones = transactionService.obtenerTransaccionesPorCbu(cbu);

        List<Transaccion> transaccionesVisibles = transacciones.stream()
                .filter(t ->
                        t.getEstado() == EstadoTransaccion.EXITOSA ||
                                t.getEstado() == EstadoTransaccion.FALLIDA ||
                                t.getEstado() == EstadoTransaccion.RECHAZADA
                )
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("=== TRANSACCIONES DE LA CUENTA ").append(cbu).append(" ===\n");

        if (transaccionesVisibles.isEmpty()) {
            sb.append("La cuenta no tiene transacciones registradas.");
            return sb.toString();
        }

        int numero = 1;

        for (Transaccion transaccion : transaccionesVisibles) {
            boolean enviada = cbu.equals(transaccion.getCbuOrigen());
            String tipo = enviada ? "TRANSFERENCIA ENVIADA" : "TRANSFERENCIA RECIBIDA";
            String contraparte = enviada
                    ? transaccion.getCbuDestino()
                    : transaccion.getCbuOrigen();

            sb.append("\n")
                    .append(numero++)
                    .append(")\n")
                    .append("  Tipo: ").append(tipo).append("\n")
                    .append("  Cuenta contraparte: ").append(contraparte).append("\n")
                    .append("  Monto: $").append(String.format("%.2f", transaccion.getMonto())).append("\n")
                    .append("  Estado: ").append(transaccion.getEstado()).append("\n");

            if (transaccion.getMotivo() != null && !transaccion.getMotivo().isBlank()) {
                sb.append("  Motivo: ").append(transaccion.getMotivo()).append("\n");
            }
        }

        return sb.toString();
    }

    public String movimientosCuenta(String cbu) {
        Cuenta cuenta = buscarCuenta(cbu);

        StringBuilder sb = new StringBuilder();
        sb.append("=== MOVIMIENTOS DE LA CUENTA ").append(cuenta.getCbu()).append(" ===\n");

        if (cuenta.getMovimientos().isEmpty()) {
            sb.append("La cuenta no tiene movimientos registrados.");
            return sb.toString();
        }

        int numero = 1;

        for (Movimiento movimiento : cuenta.getMovimientos()) {
            sb.append("\n")
                    .append(numero++)
                    .append(")\n")
                    .append("  Fecha: ").append(movimiento.getFecha()).append("\n")
                    .append("  Tipo: ").append(movimiento.getTipo()).append("\n")
                    .append("  Monto: $").append(String.format("%.2f", movimiento.getMonto())).append("\n")
                    .append("  Detalle: ").append(movimiento.getDetalle()).append("\n");
        }

        return sb.toString();
    }
}