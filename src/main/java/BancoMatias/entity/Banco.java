package BancoMatias.entity;

import BancoMatias.entity.enums.TipoDeCuenta;
import BancoMatias.service.TransaccionService;
import Integration.CbuService;

import java.util.ArrayList;
import java.util.Arrays;

public final class Banco {
    private static Banco instancia;
    private String nombre;
    public static final String CODIGO_BANCO_MATIAS = "101";
    private final ArrayList<Sucursal> sucursales;
    private TransaccionService transaccionService;

    private Banco() {
        sucursales = new ArrayList<>();

        inicializarSucursales();
        cargarUsuariosEnSucursales();
    }

    public static Banco getInstancia(){
        if (instancia == null) {
            instancia = new Banco();
        }
        return instancia;
    }

    private void inicializarSucursales(){
        Admin adminCaballito = new Admin("adminCab", "1234");
        Admin adminMataderos = new Admin("adminMat", "1234");
        Admin adminParquePatricios = new Admin("adminPar", "1234");
        sucursales.add(new Sucursal("Sucursal Caballito","201", adminCaballito, "Av. Rivadavia 5350", 0.0));
        sucursales.add(new Sucursal("Sucursal Mataderos","202", adminMataderos,"Av. Alberdi 6800", 0.0));
        sucursales.add(new Sucursal("Sucursal Parque Patricios","203", adminParquePatricios,"Av. Cordoba 2250", 0.0));
    }

    private void cargarUsuariosEnSucursales(){

        UsuarioCliente user1 = new UsuarioCliente("Jose Marmol","jose@gmail.com","pass1234", "calle 1", TipoDeCuenta.SUELDO);
        UsuarioCliente user2 = new UsuarioCliente("Axel Jota", "axel@gmail.com","pass1234","calle 2", TipoDeCuenta.CORRIENTE);
        UsuarioCliente user3 = new UsuarioCliente("Sofia Acevedo", "sofia@gmail.com","pass1234","calle 3", TipoDeCuenta.SUELDO);
        UsuarioCliente user4 = new UsuarioCliente("Tomas Benitez","tomas@gmail.com","pass1234", "calle 4", TipoDeCuenta.AHORRO);
        ArrayList<UsuarioCliente> listaUsuariosCaballito = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
        sucursales.get(0).setUsuariosActivos(listaUsuariosCaballito);

        String codSucursalCaballito = sucursales.get(0).getCodigo();
        user1.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalCaballito, (int) user1.getId()));
        user2.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalCaballito, (int) user2.getId()));
        user3.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalCaballito, (int) user3.getId()));
        user4.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalCaballito, (int) user4.getId()));

        UsuarioCliente user5 = new UsuarioCliente("Marcos Sastre","marcos@gmail.com", "pass1234","calle 5", TipoDeCuenta.SUELDO);
        UsuarioCliente user6 = new UsuarioCliente("Tatiana Rodriguez", "tatiana@gmail.com","pass1234","calle 6", TipoDeCuenta.CORRIENTE);
        UsuarioCliente user7 = new UsuarioCliente("Josue Jose","josue@gmail.com", "pass1234","calle 7", TipoDeCuenta.AHORRO);

        ArrayList<UsuarioCliente> listaUsuariosMataderos = new ArrayList<>(Arrays.asList(user5, user6, user7));
        sucursales.get(1).setUsuariosActivos(listaUsuariosMataderos);

        String codSucursalMataderos = sucursales.get(1).getCodigo();
        user5.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalMataderos, (int) user5.getId()));
        user6.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalMataderos, (int) user6.getId()));
        user7.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucursalMataderos, (int) user7.getId()));



        UsuarioCliente user9 = new UsuarioCliente("Matias Dworzak","matias@gmail.com", "pass1234","calle 8", TipoDeCuenta.AHORRO);
        UsuarioCliente user10 = new UsuarioCliente("Lucas Bravo", "lucas@gmail.com","pass1234","calle 9", TipoDeCuenta.AHORRO);
        UsuarioCliente user11 = new UsuarioCliente("Gustavo Narancio", "gustavo@gmail.com","pass1234","calle 10", TipoDeCuenta.SUELDO);
        UsuarioCliente user12 = new UsuarioCliente("Uri Yurquina", "uri@gmail.com","pass1234","calle 11", TipoDeCuenta.AHORRO);
        ArrayList<UsuarioCliente> listaUsuariosParquePatricios = new ArrayList<>(Arrays.asList(user9, user10, user11, user12));
        sucursales.get(2).setUsuariosActivos(listaUsuariosParquePatricios);

        String codSucParquePatricios = sucursales.get(1).getCodigo();
        user9.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucParquePatricios, (int) user9.getId()));
        user10.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucParquePatricios, (int) user10.getId()));
        user11.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucParquePatricios, (int) user11.getId()));
        user12.setCbu(CbuService.generarCbu(CODIGO_BANCO_MATIAS, codSucParquePatricios, (int) user12.getId()));

    }

    public ArrayList<Sucursal> getSucursales() {
        return sucursales;
    }



    public TransaccionService getServiceTransacciones() {
        return transaccionService;
    }

    public void setServiceTransacciones(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }
}


//    public void mostrarCuentas() {
//        System.out.println("-----Detalles de las cuentas del banco-----");
//        for (Entity.Sucursal sucursal : sucursales) {
//            System.out.println("-----Detalles de las cuentas de la sucursal " + sucursal.getNombre() + "-----");
//            sucursal.mostrarCuentas();
//        }
//    }