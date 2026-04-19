package BancoMartino.servicios.paneles;

import BancoMartino.dominio.Cuenta;
import BancoMartino.servicios.AplicacionBanco;
import Integration.ResultadoTransferencia;

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
                System.out.println("\n--- PANEL CUENTA " + cuenta.getEmail() + " ---");
                System.out.println("1. Depositar");
                System.out.println("2. Extraer");
                System.out.println("3. Transferir");
                System.out.println("4. Ver saldo");
                System.out.println("5. Ver balance");
                System.out.println("6. Ver movimientos");
                System.out.println("7. Volver");
                System.out.print("Opción: ");

                int opcion = leerInt();

                switch (opcion) {
                    case 1 -> depositar(cuenta);
                    case 2 -> extraer(cuenta);
                    case 3 -> transferir(cuenta);
                    case 4 -> System.out.println("Saldo actual: $" + cuenta.getSaldo());
                    case 5 -> System.out.println("\n" + getAplicacion().resumenCuenta(cuenta));
                    case 6 -> System.out.println("\n" + getAplicacion().movimientosCuenta(cuenta.getCbu()));
                    case 7 -> volver = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                mostrarError(e);
            }
        }
    }

    private Cuenta loginCuenta() {
        System.out.println("\n--- LOGIN CUENTA ---");
        System.out.print("Email: ");
        String email = leerTexto();

        System.out.print("Contraseña: ");
        String password = leerTexto();

        return getAplicacion().loginCuenta(email, password);
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

    private void transferir(Cuenta cuenta) {
        System.out.print("CBU destino: ");
        String cbuDestino = leerTexto();
        System.out.print("Monto a transferir: ");
        double monto = leerDouble();
        String cbuOrigen = cuenta.getCbu();
        ResultadoTransferencia resultado = getAplicacion().transferir(cbuOrigen, cbuDestino, monto);
        if(resultado.fueExistoso){
            System.out.println("Transferencia realizada correctamente.");
        } else {
            System.out.println("La transferencia no se pudo concretar, tuvo un error: " + resultado.mensaje);
        }

    }
}