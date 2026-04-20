package BancoMatias.interfaz;

import BancoMatias.entity.*;
import BancoMatias.entity.enums.EstadoTransaccion;
import BancoMatias.entity.enums.TipoDeCuenta;
import BancoMatias.service.SucursalService;
import BancoMatias.service.TransaccionService;
import BancoMatias.service.UsuarioClienteService;
import Integration.ResultadoTransferencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Scanner teclado;
    private UsuarioClienteService userService;
    private TransaccionService transService;
    private SucursalService sucService;
    private Sucursal sucursalActual;
    private Usuario sesionActiva;

    public Menu(UsuarioClienteService userService, TransaccionService transService, SucursalService sucService) {

        this.userService = userService;
        this.transService = transService;
        this.sucService = sucService;
        this.sucursalActual = null;
        this.sesionActiva = null;
        teclado = new Scanner(System.in);
    }


    public void mostrarMenu() throws Exception {
        boolean correr = true;
        while (correr) {
            System.out.println("\n-----Bienvenido a nuestro banco-----\n");

            ArrayList<Sucursal> listaSucursales = sucService.getTodasLasSucursales();
            mostrarLista("Elija la opcion deseada", listaSucursales);
            int opcion = teclado.nextInt();
            if (opcion > 0 && opcion <= listaSucursales.size()) {
                this.sucursalActual = listaSucursales.get(opcion - 1);
                System.out.println("Entrando a sucursal: " + sucursalActual.getNombre());
                correr = false;
            } else if (opcion == 0) {
                System.out.println("Saliendo...");
                correr = false;
            } else {
                System.out.println("Opción inválida.");
            }
        }
        while (sucursalActual != null) {
            if (sesionActiva == null) {
                // 1. Nadie logueado: Registro o Login
                menuInvitado();
            } else if (sesionActiva instanceof Admin) {
                // 2. Es Admin: Menú con superpoderes
                menuAdmin();
            } else {
                // 3. Es Cliente: Menú de operaciones bancarias
                menuCliente();
            }
        }



    }

    private void registrarUsuario(){
        System.out.println("Bienvenido al registro de usuarios.");
        System.out.println("Introduzca su nombre completo:");
        String nombre = teclado.nextLine();
        System.out.println("Introduzca su mail:");
        String mail = teclado.nextLine();
        System.out.println("Introduzca su password:");
        String password = teclado.nextLine();
        System.out.println("Introduzca su direccion:");
        String direccion = teclado.nextLine();

        mostrarOpcionesDeCuenta();
        int opcionTipoDeCuenta;
        do {
            System.out.println("Elija tipo de cuenta (1, 2 o 3):");
            opcionTipoDeCuenta = teclado.nextInt();
        } while (opcionTipoDeCuenta < 1 || opcionTipoDeCuenta > 3);

        TipoDeCuenta tipo = elegirTipoDeCuenta(opcionTipoDeCuenta);

        if(userService.altaUsuario(nombre,mail,password,direccion, tipo, sucursalActual)){
            System.out.println("El usuario " + nombre + " a sido dado de alta!");
        } else{
            System.out.println("Hubo un error, por favor volver a hacer el registro de usuario");
        }
    }
    private <T> void mostrarLista(String titulo, ArrayList<T> lista) {
        System.out.println("=== " + titulo + " ===");
        if (lista.isEmpty()) {
            System.out.println("No hay elementos para mostrar.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println((i + 1) + ") " + lista.get(i));
            }
        }
        System.out.println("0) Presione 0 para salir.");
        System.out.println("-------------------------");
    }

    public void mostrarOpcionesDeCuenta() {
        System.out.println("Seleccione el tipo de cuenta:");
        TipoDeCuenta[] opciones = TipoDeCuenta.values();

        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ") " + opciones[i]);
        }
    }

    private TipoDeCuenta elegirTipoDeCuenta(int opcion) {
        return switch (opcion) {
            case 1 -> TipoDeCuenta.CORRIENTE;
            case 2 -> TipoDeCuenta.AHORRO;
            case 3 -> TipoDeCuenta.SUELDO;
            default -> {
                System.out.println("Opción no válida, se asignó AHORRO por defecto.");
                yield TipoDeCuenta.AHORRO;
            }
        };
    }


        private void menuInvitado() {
            System.out.println("""
        --- SUCURSAL: """ + sucursalActual.getNombre() + """
        
        1) Registrar usuario
        2) Iniciar sesión Usuario
        3) Iniciar sesión Admin
        0) Salir de la sucursal
        """);
            int op = teclado.nextInt();
            teclado.nextLine();

            switch (op) {
                case 1 -> registrarUsuario();
                case 2 -> iniciarSesion();
                case 3 -> iniciarSesionAdmin();
                case 0 -> sucursalActual = null;
                default -> System.out.println("Opción inválida.");
            }
        }


    private void iniciarSesion() {
        System.out.println("Para iniciar sesion ingrese su mail y constrasenia");
        System.out.println("Mail:");
        String mail = teclado.nextLine();
        System.out.println("Password:");
        String password = teclado.nextLine();

        if (userService.validarUsuario(mail, password) != null){
            sesionActiva = userService.validarUsuario(mail, password);
        } else {
            System.out.println("El mail o el password no coinciden con un usuario en esta sucursal");
            System.out.println("Vas a volver al menu principal");
        }
    }
    private void iniciarSesionAdmin() {
        System.out.println("Para iniciar sesion escriba su user y password");
        System.out.println("User:");
        String mail = teclado.nextLine();
        System.out.println("Password:");
        String password = teclado.nextLine();

        if (userService.validarAdmin(mail, password, sucursalActual) != null){
            sesionActiva = userService.validarAdmin(mail, password, sucursalActual);
        } else {
            System.out.println("El mail o el password no coinciden con un admin en esta sucursal");
            System.out.println("Vas a volver al menu principal");
        }
    }

    private void menuCliente() throws Exception {
        System.out.println("""
            --- MENÚ CLIENTE (%s) ---
            1) Depositar dinero
            2) Retirar dinero
            3) Realizar una transferencia
            4) Mostrar datos de la cuenta
            5) Historial de transferencias
            6) Eliminar mi cuenta
            7) Cerrar sesión
            8) Salir de la sucursal
            """);

        int op = teclado.nextInt();
        teclado.nextLine(); // Limpiar buffer

        switch (op) {
            case 1 -> procesarDeposito();
            case 2 -> procesarRetiro();
            case 3 -> procesarTransferencia();
            case 4 -> System.out.println(sesionActiva); // Aprovechamos el toString
            case 5 -> historialDeTransferencias();
            case 6 -> solicitarBaja();
            case 7 -> {
                sesionActiva = null;
                System.out.println("Sesión cerrada.");
            }
            case 8 -> {
                sesionActiva = null;
                sucursalActual = null;
                System.out.println("Saliendo de la sucursal...");
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private void historialDeTransferencias() {

            UsuarioCliente cliente = (UsuarioCliente) sesionActiva;
            ArrayList<Transaccion> historial = cliente.getHistorialTransaccion();

            System.out.println("\n--- HISTORIAL DE TRANSFERENCIAS ---");

            if (historial == null || historial.isEmpty()) {
                System.out.println(" Aún no has realizado movimientos.");
            } else {
                for (Transaccion t : historial) {
                    // FILTRO: Solo imprimimos si NO es una confirmación interna
                    // Queremos ver: EXITOSA, FALLIDA o RECHAZADA
                    if (t.getEstado() != EstadoTransaccion.CONFIRMADA) {
                        System.out.println(t);
                    }
                }
            }
            System.out.println("------------------------------------");
    }


    private void solicitarBaja() {
    }

    private void procesarTransferencia() {
        System.out.println("--- TRANSFERENCIA POR CBU ---");
        String cbuOrigen = ((UsuarioCliente) sesionActiva).getCbu();

        System.out.println("Su CBU es: " + cbuOrigen);
        System.out.print("Ingrese el CBU del destinatario: ");
        String cbuDestino = teclado.nextLine();
        System.out.print("Ingrese el monto a transferir: ");
        double monto = teclado.nextDouble();
        teclado.nextLine(); //

        ResultadoTransferencia resultado = transService.iniciarTransferencia(cbuOrigen, cbuDestino, monto);

        if (resultado.fueExistoso) {
            System.out.println(resultado.mensaje);
        } else {
            System.err.println("Error: " + resultado.mensaje);
        }
    }

    private void procesarRetiro() { // Ya no necesita el "throws Exception" porque la atrapamos acá
        System.out.println("Su saldo actual es de $" + ((UsuarioCliente) sesionActiva).getSaldo());
        System.out.print("Ingrese cuanto desea retirar: ");
        double montoARetirar = teclado.nextDouble();

        try {
            transService.extraer((UsuarioCliente) sesionActiva, montoARetirar);

            System.out.println("Retiraste $" + montoARetirar + " con éxito.");
            System.out.println("Tu nuevo saldo es de $" + ((UsuarioCliente) sesionActiva).getSaldo());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void procesarDeposito() {
        System.out.println("Ingrese cuanto desea depositar a su cuenta.");
        double montoADepositar = teclado.nextDouble();
        if(transService.depositar((UsuarioCliente) sesionActiva, montoADepositar)){
            System.out.println("Depositaste $" + montoADepositar + " con exito.");
            System.out.println("Tu nuevo saldo en al cuente es de $" + ((UsuarioCliente) sesionActiva).getSaldo());
        }else{
            System.out.println("Algo salio mal, vuelva a intentarlo mas tarde.");
        }

    }

    private void menuAdmin() {
        System.out.println("""
        --- MODO ADMINISTRADOR (%s) ---
        1) Balance Total de la Sucursal
        2) Historial de movimientos de la Sucursal
        3) Listar TODOS los usuarios de esta sucursal
        4) Cerrar sesión
        5) Salir
        """);

        int op = teclado.nextInt();
        teclado.nextLine();

        switch (op) {
            case 1 -> mostrarBalanceSucursal();
            case 2 -> mostrarMovimientosSucursal();
            case 3 -> listarUsuariosActualesDeEstaSucursal();
            case 4 -> {
                sesionActiva = null;
                System.out.println("Sesión de administrador cerrada.");
            }
            case 5 -> {
                sesionActiva = null;
                sucursalActual = null;
                System.out.println("Saliendo de la sucursal...");
            }
            default -> System.out.println("⚠️ Opción no válida, intente de nuevo.");
        }
    }

    private void listarUsuariosActualesDeEstaSucursal(){
        System.out.println("\n--- LISTADO DE USUARIOS ACTUALES ---");
        ArrayList<UsuarioCliente> usuarios = sucursalActual.getUsuariosActivos();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en esta sucursal.");
        } else {
            for (UsuarioCliente u : usuarios) {
                // Usamos %-18s para que la etiqueta ocupe 18 espacios y todo lo demás se alinee
                System.out.printf("""
                 Nombre:          %s
                 CBU:             %s
                 Saldo en cuenta: $%.2f
                 Tipo de cuenta:  %s
                ------------------------------------
                """,
                        u.getName(),
                        u.getCbu(),
                        u.getSaldo(),
                        u.getTipoDeCuenta()
                );
            }
        }


    }
    private void mostrarMovimientosSucursal() {
        System.out.println("\n=== AUDITORÍA DE MOVIMIENTOS - SUCURSAL: " + sucursalActual.getNombre() + " ===");

        List<Transaccion> todas = transService.getHistorialGlobal();
        boolean huboMovimientos = false;

        for (Transaccion t : todas) {
            boolean origenEsLocal = sucursalActual.getUsuariosActivos().stream()
                    .anyMatch(u -> u.getCbu().equals(t.getCbuOrigen()));
            boolean destinoEsLocal = sucursalActual.getUsuariosActivos().stream()
                    .anyMatch(u -> u.getCbu().equals(t.getCbuDestino()));

            if (origenEsLocal || destinoEsLocal) {
                if (t.getEstado() != EstadoTransaccion.CONFIRMADA) {
                    System.out.println(t);
                }

                huboMovimientos = true;
            }
        }

        if (!huboMovimientos) {
            System.out.println("No se registraron movimientos en esta sucursal.");
        }
        System.out.println("--------------------------------------------------");
    }



    private void mostrarBalanceSucursal() {
        System.out.println("\n=== BALANCE SUCURSAL: " + sucursalActual.getNombre() + " ===");
        double total = 0;

        ArrayList<UsuarioCliente> clientes = sucursalActual.getUsuariosActivos();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados en esta sucursal.");
        } else {
            for (UsuarioCliente cliente : clientes) {
                total += cliente.getSaldo();
                System.out.printf("- %-20s | CBU: %s | Saldo: $%.2f%n",
                        cliente.getName(), cliente.getCbu(), cliente.getSaldo());
            }
            System.out.println("--------------------------------------------------");
            System.out.printf("TOTAL SUCURSAL: $%.2f%n", total);
        }
    }



    public TransaccionService getTransaccionService() {
        return transService;
    }
}






