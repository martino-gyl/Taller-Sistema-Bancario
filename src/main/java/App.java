import BancoMartino.dominio.Admin;
import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Sucursal;
import BancoMartino.dominio.TipoCuenta;
import BancoMartino.servicios.MenuBancario;


import BancoMatias.interfaz.Menu;
import BancoMatias.repository.SucursalRepository;
import BancoMatias.repository.TransaccionRepository;
import BancoMatias.repository.UsuarioRepository;
import BancoMatias.service.SucursalService;
import BancoMatias.service.TransaccionService;
import BancoMatias.service.UsuarioClienteService;
import Integration.Mediator;
import Integration.ITransactionService;

import java.util.*;

public class App {
    public static void main(String[] args) {
        UsuarioRepository userRepo = new UsuarioRepository();
        TransaccionRepository transRepo = new TransaccionRepository();
        SucursalRepository sucRepo = new SucursalRepository();

        TransaccionService transServiceMatias = new TransaccionService(userRepo, transRepo);
        UsuarioClienteService userServiceMatias = new UsuarioClienteService(userRepo);
        SucursalService sucServiceMatias = new SucursalService(sucRepo);

        Map<String, ITransactionService> listaBancoss = new HashMap<>();

        Banco bancoMartino = initBancoMartino();
        //BancoMartino.servicios.TransactionService serviceMartino = new BancoMartino.servicios.TransactionService(bancoMartino);

        listaBancoss.put(transServiceMatias.getCodigoBanco(), transServiceMatias);
        listaBancoss.put(bancoMartino.getCodigoBanco(), bancoMartino.getTransactionService());
        Mediator mediador = new Mediator(listaBancoss);

        transServiceMatias.setMediador(mediador);
        bancoMartino.setMediador(mediador);

        Menu menuMatias = new Menu(userServiceMatias, transServiceMatias, sucServiceMatias);
        MenuBancario menuMartino = new MenuBancario(bancoMartino);




        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("""
                === RED LINK / BANELCO ===
                1) Entrar a Banco Martino
                2) Entrar a Banco Matias
                0) Apagar Sistema
                """);

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> menuMartino.iniciar(); // Entra al bucle de Martino
                case 2 -> menuMatias.mostrarMenu(); // Entra al bucle de Matias
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
            // Al salir de cualquier menú con "Volver", regresamos a este switch
        }
    }

    private static Menu initBancoMatias(){
        BancoMatias.entity.Banco bancoPatagonia = BancoMatias.entity.Banco.getInstancia();
        UsuarioRepository userRepo = new UsuarioRepository();
        TransaccionRepository transRepo = new TransaccionRepository();
        SucursalRepository sucRepo = new SucursalRepository();
        SucursalService sucService = new SucursalService(sucRepo);
        TransaccionService transService = new TransaccionService(userRepo, transRepo);
        UsuarioClienteService userService = new UsuarioClienteService(userRepo);

        return new Menu(userService, transService, sucService);
    }
    private static Banco initBancoMartino(){
        Banco bancoMartino = new Banco("Banco Dino","102");

        Sucursal sucursal1 = new Sucursal("001", "Casa Central", "Av. Siempre Viva 123");
        Sucursal sucursal2 = new Sucursal("002", "Sucursal Norte", "Calle Norte 456");

        bancoMartino.agregarSucursal(sucursal1);
        bancoMartino.agregarSucursal(sucursal2);

        Admin admin1 = new Admin("Messi", "admin1", "1234");
        bancoMartino.asignarAdminASucursal("001", admin1);

        admin1.darAltaCuenta(bancoMartino,"1",TipoCuenta.CAJA_DE_AHORRO,"43","Martino","Simon","ma@","manuela pedraza");
        admin1.darAltaCuenta(bancoMartino,"2",TipoCuenta.CAJA_DE_AHORRO,"44","Lorenzo","Simon","lo@","manuela pedraza");
        return bancoMartino;
    }
}