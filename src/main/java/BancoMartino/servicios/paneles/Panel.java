package BancoMartino.servicios.paneles;

import servicios.AplicacionBanco;

import java.util.Scanner;

public abstract class Panel {
    private final Scanner scanner;
    private final AplicacionBanco aplicacion;

    public Panel(Scanner scanner, AplicacionBanco aplicacion) {
        this.scanner = scanner;
        this.aplicacion = aplicacion;
    }

    public abstract void mostrar();

    protected int leerInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un número entero válido.");
        }
    }

    protected double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe ingresar un número válido.");
        }
    }

    protected String leerTexto() {
        return scanner.nextLine().trim();
    }

    protected AplicacionBanco getAplicacion() {
        return aplicacion;
    }

    protected void mostrarError(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
