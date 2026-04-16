package BancoMartino.servicios.paneles;

import BancoMartino.dominio.Cuenta;
import BancoMartino.servicios.AplicacionBanco;

import java.util.Scanner;

public class PanelCuenta extends Panel {

    public PanelCuenta(Scanner scanner, AplicacionBanco aplicacion) {
        super(scanner, aplicacion);
    }

    @Override
    public void mostrar() {
        Cuenta cuenta = loginCuenta();
        boolean volver = false;

        while (!volver) {
            try {
                System.out.println("\n--- PANEL CUENTA " + cuenta.getNumero() + " ---");
                System.out.println("1. Depositar");
                System.out.println("2. Extraer");
                System.out.println("3. Ver saldo");
                System.out.println("4. Ver balance");
                System.out.println("5. Ver movimientos");
                System.out.println("6. Volver");
                System.out.print("Opción: ");

                int opcion = leerInt();

                switch (opcion) {
                    case 1 -> depositar(cuenta);
                    case 2 -> extraer(cuenta);
                    case 3 -> System.out.println("Saldo actual: $" + cuenta.getSaldo());
                    case 4 -> System.out.println("\n" + getAplicacion().resumenCuenta(cuenta));
                    case 5 -> System.out.println("\n" + getAplicacion().movimientosCuenta(cuenta.getNumero()));
                    case 6 -> volver = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                mostrarError(e);
            }
        }
    }

    private Cuenta loginCuenta() {
        System.out.println("\n--- LOGIN CUENTA ---");
        System.out.print("Número de cuenta: ");
        String numeroCuenta = leerTexto();

        System.out.print("Contraseña: ");
        String password = leerTexto();

        return getAplicacion().loginCuenta(numeroCuenta, password);
    }

    private void depositar(Cuenta cuenta) {
        System.out.print("Monto a depositar: ");
        double monto = leerDouble();

        getAplicacion().depositar(cuenta, monto);
        System.out.println("Depósito realizado.");
    }

    private void extraer(Cuenta cuenta) {
        System.out.print("Monto a extraer: ");
        double monto = leerDouble();

        getAplicacion().extraer(cuenta, monto);
        System.out.println("Extracción realizada.");
    }
}