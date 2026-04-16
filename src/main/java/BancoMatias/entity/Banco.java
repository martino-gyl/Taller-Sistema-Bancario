package BancoMatias.entity;

import BancoMatias.entity.enums.TipoDeCuenta;
import BancoMatias.service.TransaccionService;

import java.util.ArrayList;
import java.util.Arrays;

public final class Banco {
    private static Banco instancia;
    private String nombre;
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
        sucursales.add(new Sucursal("Sucursal Caballito",adminCaballito, "Av. Rivadavia 5350", 0.0));
        sucursales.add(new Sucursal("Sucursal Mataderos", adminMataderos,"Av. Alberdi 6800", 0.0));
        sucursales.add(new Sucursal("Sucursal Parque Patricios", adminParquePatricios,"Av. Cordoba 2250", 0.0));
    }

    private void cargarUsuariosEnSucursales(){

        UsuarioCliente user1 = new UsuarioCliente("Jose Marmol","jose@gmail.com","pass1234", "calle 1", TipoDeCuenta.SUELDO);
        UsuarioCliente user2 = new UsuarioCliente("Axel Jota", "axel@gmail.com","pass1234","calle 2", TipoDeCuenta.CORRIENTE);
        UsuarioCliente user3 = new UsuarioCliente("Sofia Acevedo", "sofia@gmail.com","pass1234","calle 3", TipoDeCuenta.SUELDO);
        UsuarioCliente user4 = new UsuarioCliente("Tomas Benitez","tomas@gmail.com","pass1234", "calle 4", TipoDeCuenta.AHORRO);
        ArrayList<UsuarioCliente> listaUsuariosCaballito = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
        sucursales.get(0).setUsuariosActivos(listaUsuariosCaballito);

        UsuarioCliente user5 = new UsuarioCliente("Marcos Sastre","marcos@gmail.com", "pass1234","calle 5", TipoDeCuenta.SUELDO);
        UsuarioCliente user6 = new UsuarioCliente("Tatiana Rodriguez", "tatiana@gmail.com","pass1234","calle 6", TipoDeCuenta.CORRIENTE);
        UsuarioCliente user7 = new UsuarioCliente("Josue Jose","josue@gmail.com", "pass1234","calle 7", TipoDeCuenta.AHORRO);

        ArrayList<UsuarioCliente> listaUsuariosMataderos = new ArrayList<>(Arrays.asList(user5, user6, user7));
        sucursales.get(1).setUsuariosActivos(listaUsuariosMataderos);

        UsuarioCliente user9 = new UsuarioCliente("Matias Dworzak","matias@gmail.com", "pass1234","calle 8", TipoDeCuenta.AHORRO);
        UsuarioCliente user10 = new UsuarioCliente("Lucas Bravo", "lucas@gmail.com","pass1234","calle 9", TipoDeCuenta.AHORRO);
        UsuarioCliente user11 = new UsuarioCliente("Gustavo Narancio", "gustavo@gmail.com","pass1234","calle 10", TipoDeCuenta.SUELDO);
        UsuarioCliente user12 = new UsuarioCliente("Uri Yurquina", "uri@gmail.com","pass1234","calle 11", TipoDeCuenta.AHORRO);
        ArrayList<UsuarioCliente> listaUsuariosParquePatricios = new ArrayList<>(Arrays.asList(user9, user10, user11, user12));
        sucursales.get(2).setUsuariosActivos(listaUsuariosParquePatricios);

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