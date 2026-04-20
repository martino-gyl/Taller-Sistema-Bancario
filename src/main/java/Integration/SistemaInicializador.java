package Integration;

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

import java.util.HashMap;
import java.util.Map;



public class SistemaInicializador {

    public MenuPrincipal crearMenuPrincipal() {
        Banco bancoMartino = crearBancoMartino();
        MenuBancario menuMartino = new MenuBancario(bancoMartino);

        Menu menuMatias = crearMenuMatias();
        TransaccionService transServiceMatias = crearTransaccionServiceMatias(menuMatias);

        Mediator mediador = crearMediator(bancoMartino, transServiceMatias);

        transServiceMatias.setMediador(mediador);
        bancoMartino.setMediador(mediador);

        return new MenuPrincipal(menuMartino, menuMatias);
    }

    private Menu crearMenuMatias() {
        UsuarioRepository userRepo = new UsuarioRepository();
        TransaccionRepository transRepo = new TransaccionRepository();
        SucursalRepository sucRepo = new SucursalRepository();

        TransaccionService transServiceMatias = new TransaccionService(userRepo, transRepo);
        UsuarioClienteService userServiceMatias = new UsuarioClienteService(userRepo);
        SucursalService sucServiceMatias = new SucursalService(sucRepo);

        return new Menu(userServiceMatias, transServiceMatias, sucServiceMatias);
    }

    private TransaccionService crearTransaccionServiceMatias(Menu menuMatias) {
        return menuMatias.getTransaccionService();
    }

    private Mediator crearMediator(Banco bancoMartino, TransaccionService transServiceMatias) {
        Map<String, ITransactionService> serviciosPorBanco = new HashMap<>();

        serviciosPorBanco.put(transServiceMatias.getCodigoBanco(), transServiceMatias);
        serviciosPorBanco.put(bancoMartino.getCodigoBanco(), bancoMartino.getTransactionService());

        return new Mediator(serviciosPorBanco);
    }

    private Banco crearBancoMartino() {
        Banco bancoMartino = new Banco("Banco Dino", "102");

        Sucursal sucursal1 = new Sucursal("001", "Casa Central", "Av. Siempre Viva 123");
        Sucursal sucursal2 = new Sucursal("002", "Sucursal Norte", "Calle Norte 456");

        bancoMartino.agregarSucursal(sucursal1);
        bancoMartino.agregarSucursal(sucursal2);

        Admin admin1 = new Admin("Messi", "admin1", "1234");
        bancoMartino.asignarAdminASucursal("001", admin1);

        admin1.darAltaCuenta(
                bancoMartino,
                "1",
                TipoCuenta.CAJA_DE_AHORRO,
                "43",
                "Martino",
                "Simon",
                "ma@",
                "Manuela Pedraza"
        );

        admin1.darAltaCuenta(
                bancoMartino,
                "2",
                TipoCuenta.CAJA_DE_AHORRO,
                "44",
                "Lorenzo",
                "Simon",
                "lo@",
                "Manuela Pedraza"
        );

        return bancoMartino;
    }
}