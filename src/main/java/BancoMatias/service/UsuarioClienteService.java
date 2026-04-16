package BancoMatias.service;

import BancoMatias.entity.*;
import BancoMatias.entity.enums.TipoDeCuenta;
import BancoMatias.repository.UsuarioRepository;

import java.util.ArrayList;

public class UsuarioClienteService {

    private UsuarioRepository usuarioRepo;

    public UsuarioClienteService(UsuarioRepository usuarioRepo){
        this.usuarioRepo = usuarioRepo;

    }


    public boolean altaUsuario(String nombre, String mail, String password, String direccion, TipoDeCuenta tipo, Sucursal sucursalActual) {
        if (nombre != null && mail != null && password != null) {
        Usuario user = new UsuarioCliente(nombre, mail, password, direccion, tipo);
        return usuarioRepo.altaUsuarioCliente((UsuarioCliente) user, sucursalActual);
        }
        return false;
    }

    public UsuarioCliente buscarUsuarioClientePorMail(String mail){

        return (UsuarioCliente) usuarioRepo.buscarUsuarioActivoPorMail( mail);
    }
    public Usuario validarUsuario(String mail, String password){
        Usuario userBuscado = usuarioRepo.buscarUsuarioActivoPorMail(mail);
        if(userBuscado != null && userBuscado.getPassword().equals(password)){
            return userBuscado;
        }
        return null;
    }

    public Admin validarAdmin(String mail, String password, Sucursal suc){
        Admin admin = suc.getAdministrador();

        if (admin != null && admin.getMail().equals(mail) && admin.getPassword().equals(password)){
            return admin;
        }
        return null;
    }

//    public void bajaUsuario(UsuarioCliente user) {
//        if (user.getEstaActivado()) {
//            sucursal.getUsuariosActivos().remove(user);
//            sucursal.getUsuariosDadosDeBaja().add(user);
//            user.setEstaActivado(false);
//            System.out.println("El usuario " + user.getName() + " a sido dado de baja!");
//        }

        public ArrayList<Transaccion> obtenerHistorial(String mail) {
            UsuarioCliente user = (UsuarioCliente) usuarioRepo.buscarUsuarioActivoPorMail(mail);
           if(usuarioRepo.existe(user)){
               return user.getHistorialTransaccion();}
           return null;
        }


}

//    public void listarUsuarios() {
//        int i;
//        for (i = 0; i < sucursal.getUsuariosActivos().size(); i++) {
//            System.out.println(sucursal.getUsuariosActivos().get(i).getName());
//        }
//    }
//
//    public void listarUsuariosConSusBalances() {
//        int i;
//        for (i = 0; i < sucursal.getUsuariosActivos().size(); i++) {
//            System.out.println("Nombre: " + sucursal.getUsuariosActivos().get(i).getName() + "\nBalance: $" + sucursal.getUsuariosActivos().get(i).getSaldo());
//            System.out.println("-----------------------------");
//        }
//    }

