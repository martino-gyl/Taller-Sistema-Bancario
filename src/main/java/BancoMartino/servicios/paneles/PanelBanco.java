package BancoMartino.servicios.paneles;

import BancoMartino.dominio.Sucursal;
import BancoMartino.servicios.AplicacionBanco;

import java.util.List;
import java.util.Scanner;


public class PanelBanco extends Panel {

    public PanelBanco(Scanner scanner, AplicacionBanco aplicacion) {
        super(scanner, aplicacion);
    }

    @Override
    public void mostrar() {
        boolean volver = false;

        while (!volver) {
            try {
                System.out.println("\n--- PANEL BANCO ---");
                System.out.println("1. Listar sucursales");
                System.out.println("2. Crear sucursal");
                System.out.println("3. Asignar admin a sucursal");
                System.out.println("4. Ver balance total del banco");
                System.out.println("5. Ver balance de una sucursal");
                System.out.println("6. Volver");
                System.out.print("Opción: ");

                int opcion = leerInt();

                switch (opcion) {
                    case 1 -> listarSucursales();
                    case 2 -> crearSucursal();
                    case 3 -> asignarAdmin();
                    case 4 -> System.out.println("Balance total del banco: $" + getAplicacion().balanceBanco());
                    case 5 -> verBalanceSucursal();
                    case 6 -> volver = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void listarSucursales() {
        List<Sucursal> sucursales = getAplicacion().listarSucursales();

        if (sucursales.isEmpty()) {
            System.out.println("No hay sucursales registradas.");
            return;
        }

        System.out.println("\n--- SUCURSALES ---");
        for (Sucursal sucursal : sucursales) {
            String admin = (sucursal.getAdmin() == null)
                    ? "Sin admin"
                    : sucursal.getAdmin().getUsuario();

            System.out.println(
                    "Código: " + sucursal.getCodigo() +
                            " | Nombre: " + sucursal.getNombre() +
                            " | Dirección: " + sucursal.getDireccion() +
                            " | Admin: " + admin
            );
        }
    }

    private void crearSucursal() {
        System.out.print("Código sucursal: ");
        String codigo = leerTexto();

        System.out.print("Nombre sucursal: ");
        String nombre = leerTexto();

        System.out.print("Dirección sucursal: ");
        String direccion = leerTexto();

        getAplicacion().crearSucursal(codigo, nombre, direccion);
        System.out.println("Sucursal creada correctamente.");
    }

    private void asignarAdmin() {
        System.out.print("Código sucursal: ");
        String codigo = leerTexto();

        System.out.print("Nombre admin: ");
        String nombre = leerTexto();

        System.out.print("Usuario admin: ");
        String usuario = leerTexto();

        System.out.print("Contraseña admin: ");
        String password = leerTexto();

        getAplicacion().asignarAdmin(codigo, nombre, usuario, password);
        System.out.println("Admin asignado correctamente.");
    }

    private void verBalanceSucursal() {
        System.out.print("Código sucursal: ");
        String codigo = leerTexto();

        double balance = getAplicacion().balanceSucursal(codigo);
        System.out.println("Balance sucursal: $" + balance);
    }
}
