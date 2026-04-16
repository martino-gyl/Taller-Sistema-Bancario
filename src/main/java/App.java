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


public class App {
    public static void main(String[] args) {
        MenuBancario menuMartino = new MenuBancario(initBancoMartino());
        Menu menuMatias = initBancoMatias();
        menuMartino.iniciar();

        menuMatias.mostrarMenu();
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
        Banco bancoMartino = new Banco("Banco Dino");

        Sucursal sucursal1 = new Sucursal("001", "Casa Central", "Av. Siempre Viva 123");
        Sucursal sucursal2 = new Sucursal("002", "Sucursal Norte", "Calle Norte 456");

        bancoMartino.agregarSucursal(sucursal1);
        bancoMartino.agregarSucursal(sucursal2);

        Admin admin1 = new Admin("Messi", "admin1", "1234");
        bancoMartino.asignarAdminASucursal("001", admin1);

        admin1.darAltaCuenta("1","1",TipoCuenta.CAJA_DE_AHORRO,"43","Martino","Simon","ma@","manuela pedraza");
        admin1.darAltaCuenta("2","2",TipoCuenta.CAJA_DE_AHORRO,"44","Lorenzo","Simon","lo@","manuela pedraza");
        return bancoMartino;
    }
}