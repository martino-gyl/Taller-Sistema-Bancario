package Integration;

import BancoMartino.servicios.MenuBancario;
import BancoMatias.interfaz.Menu;

import java.util.Scanner;

public class MenuPrincipal {
    private final MenuBancario menuMartino;
    private final Menu menuMatias;
    private final Scanner scanner;

    public MenuPrincipal(MenuBancario menuMartino, Menu menuMatias) {
        this.menuMartino = menuMartino;
        this.menuMatias = menuMatias;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            try {
                System.out.println("""
                        === RED LINK / BANELCO ===
                        1) Entrar a Banco Martino
                        2) Entrar a Banco Matias
                        0) Apagar Sistema
                        """);

                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> menuMartino.iniciar();
                    case 2 -> menuMatias.mostrarMenu();
                    case 0 -> salir = true;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }
}