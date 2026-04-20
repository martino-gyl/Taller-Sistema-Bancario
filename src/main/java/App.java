import Integration.MenuPrincipal;
import Integration.SistemaInicializador;


import java.util.*;

public class App {
    public static void main(String[] args) {
        MenuPrincipal menuPrincipal = new SistemaInicializador().crearMenuPrincipal();
        menuPrincipal.iniciar();
    }
}