package BancoMartino.servicios.paneles;

import dominio.Admin;
import dominio.Cuenta;
import dominio.TipoCuenta;
import servicios.AplicacionBanco;

import java.util.List;
import java.util.Scanner;

public class PanelAdmin extends Panel {

    public PanelAdmin(Scanner scanner, AplicacionBanco aplicacion) {
        super(scanner, aplicacion);
    }

    @Override
    public void mostrar() {
        Admin admin = loginAdmin();
        boolean volver = false;

        while (!volver) {
            try {
                System.out.println("\n--- PANEL ADMIN SUCURSAL " + admin.getSucursal().getCodigo() + " ---");
                System.out.println("1. Listar cuentas");
                System.out.println("2. Crear cuenta");
                System.out.println("3. Eliminar cuenta");
                System.out.println("4. Ver balance de la sucursal");
                System.out.println("5. Ver balance de una cuenta");
                System.out.println("6. Ver movimientos de una cuenta");
                System.out.println("7. Transferir entre cuentas de la sucursal");
                System.out.println("8. Volver");
                System.out.print("Opción: ");

                int opcion = leerInt();

                switch (opcion) {
                    case 1 -> listarCuentas(admin);
                    case 2 -> crearCuenta(admin);
                    case 3 -> eliminarCuenta(admin);
                    case 4 -> System.out.println("Balance sucursal: $" + getAplicacion().balanceSucursal(admin));
                    case 5 -> verBalanceCuenta();
                    case 6 -> verMovimientosCuenta();
                    case 7 -> transferir(admin);
                    case 8 -> volver = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                mostrarError(e);
            }
        }
    }

    private Admin loginAdmin() {
        System.out.println("\n--- LOGIN ADMIN ---");
        System.out.print("Código de sucursal: ");
        String codigoSucursal = leerTexto();

        System.out.print("Usuario: ");
        String usuario = leerTexto();

        System.out.print("Contraseña: ");
        String password = leerTexto();

        return getAplicacion().loginAdmin(codigoSucursal, usuario, password);
    }

    private void listarCuentas(Admin admin) {
        List<Cuenta> cuentas = getAplicacion().listarCuentas(admin);

        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas en la sucursal.");
            return;
        }

        System.out.println("\n--- CUENTAS DE LA SUCURSAL ---");
        for (Cuenta cuenta : cuentas) {
            System.out.println(
                    "Número: " + cuenta.getNumero() +
                            " | Tipo: " + cuenta.getTipo() +
                            " | Titular: " + cuenta.getNombreCompleto() +
                            " | DNI: " + cuenta.getDni() +
                            " | Saldo: $" + cuenta.getSaldo()
            );
        }
    }

    private void crearCuenta(Admin admin) {
        System.out.print("DNI titular: ");
        String dni = leerTexto();

        System.out.print("Nombre titular: ");
        String nombre = leerTexto();

        System.out.print("Apellido titular: ");
        String apellido = leerTexto();

        System.out.print("Email titular: ");
        String email = leerTexto();

        System.out.print("Dirección titular: ");
        String direccion = leerTexto();

        System.out.print("Número de cuenta: ");
        String numeroCuenta = leerTexto();

        System.out.print("Contraseña cuenta: ");
        String passwordCuenta = leerTexto();

        TipoCuenta tipo = leerTipoCuenta();

        getAplicacion().crearCuenta(
                admin,
                numeroCuenta,
                passwordCuenta,
                tipo,
                dni,
                nombre,
                apellido,
                email,
                direccion
        );

        System.out.println("Cuenta creada correctamente.");
    }

    private void eliminarCuenta(Admin admin) {
        System.out.print("Número de cuenta a eliminar: ");
        String numeroCuenta = leerTexto();

        getAplicacion().eliminarCuenta(admin, numeroCuenta);
        System.out.println("Cuenta eliminada correctamente.");
    }

    private void transferir(Admin admin) {
        System.out.print("Número de cuenta origen: ");
        String numeroOrigen = leerTexto();

        System.out.print("Número de cuenta destino: ");
        String numeroDestino = leerTexto();

        System.out.print("Monto a transferir: ");
        double monto = leerDouble();

        getAplicacion().transferir(admin, numeroOrigen, numeroDestino, monto);
        System.out.println("Transferencia realizada correctamente.");
    }

    private void verBalanceCuenta() {
        System.out.print("Número de cuenta: ");
        String numeroCuenta = leerTexto();

        System.out.println("\n" + getAplicacion().resumenCuenta(numeroCuenta));
    }

    private void verMovimientosCuenta() {
        System.out.print("Número de cuenta: ");
        String numeroCuenta = leerTexto();

        System.out.println("\n" + getAplicacion().movimientosCuenta(numeroCuenta));
    }

    private TipoCuenta leerTipoCuenta() {
        System.out.println("Tipo de cuenta:");
        System.out.println("1. Caja de ahorro");
        System.out.println("2. Cuenta corriente");
        System.out.print("Opción: ");

        int opcionTipo = leerInt();

        if (opcionTipo == 1) {
            return TipoCuenta.CAJA_DE_AHORRO;
        }
        if (opcionTipo == 2) {
            return TipoCuenta.CUENTA_CORRIENTE;
        }

        throw new IllegalArgumentException("Tipo de cuenta inválido.");
    }
}
