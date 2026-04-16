package BancoMartino.servicios;

import dominio.Banco;
import servicios.paneles.*;


import java.util.Scanner;

public class MenuBancario {
    private final Scanner scanner;
    private final Panel panelBanco;
    private final Panel panelAdmin;
    private final Panel panelCuenta;

    public MenuBancario(Banco banco) {
        Scanner scannerCompartido = new Scanner(System.in);
        AplicacionBanco aplicacion = new AplicacionBanco(banco);

        this.scanner = scannerCompartido;
        this.panelBanco = new PanelBanco(scannerCompartido, aplicacion);
        this.panelAdmin = new PanelAdmin(scannerCompartido, aplicacion);
        this.panelCuenta = new PanelCuenta(scannerCompartido, aplicacion);
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            try {
                System.out.println("\n--- MENÚ PRINCIPAL ---");
                System.out.println("1. Panel Banco");
                System.out.println("2. Panel Admin");
                System.out.println("3. Panel Cuenta");
                System.out.println("4. Salir");
                System.out.print("Opción: ");

                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> panelBanco.mostrar();
                    case 2 -> panelAdmin.mostrar();
                    case 3 -> panelCuenta.mostrar();
                    case 4 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}