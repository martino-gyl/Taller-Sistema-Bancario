import BancoMartino.dominio.Admin;
import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Sucursal;
import BancoMartino.dominio.TipoCuenta;
import BancoMartino.servicios.MenuBancario;

public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco("Banco Dino");

        Sucursal sucursal1 = new Sucursal("001", "Casa Central", "Av. Siempre Viva 123");
        Sucursal sucursal2 = new Sucursal("002", "Sucursal Norte", "Calle Norte 456");

        banco.agregarSucursal(sucursal1);
        banco.agregarSucursal(sucursal2);

        Admin admin1 = new Admin("Messi", "admin1", "1234");
        banco.asignarAdminASucursal("001", admin1);

        admin1.darAltaCuenta("1","1",TipoCuenta.CAJA_DE_AHORRO,"43","Martino","Simon","ma@","manuela pedraza");
        admin1.darAltaCuenta("2","2",TipoCuenta.CAJA_DE_AHORRO,"44","Lorenzo","Simon","lo@","manuela pedraza");
        MenuBancario menu = new MenuBancario(banco);
        menu.iniciar();
    }
}