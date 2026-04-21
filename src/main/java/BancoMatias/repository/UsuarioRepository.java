package BancoMatias.repository;

import BancoMatias.entity.*;
import Integration.CbuService;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private Banco bandoDb = Banco.getInstancia();

    public Usuario buscarUsuarioActivoPorMail(String mail) {
        for (Sucursal suc : bandoDb.getSucursales()) {
            for (Usuario user : suc.getUsuariosActivos()) {
                if (user.getMail().equals(mail)) return user;
            }
        }
        return null;
    }
    public UsuarioCliente buscarUsuarioClientePorCbu(String cbu) {
        for (Sucursal suc : bandoDb.getSucursales()) {
            if (suc.getCodigo().equals(CbuService.obtenerCodigoSucursal(cbu))) {
                for (UsuarioCliente user : suc.getUsuariosActivos()) {
                    if (user.getCbu().equals(cbu)) {
                        return user;
                    }
                }
            }
        } return null;
    }

    public boolean save(Usuario user) {
        if (user == null) return false;
        return true;}

    public boolean altaUsuarioCliente(UsuarioCliente user, Sucursal suc) {
        if (suc == null || user == null) return false;

        suc.getUsuariosActivos().add(user);
        user.setEstaActivado(true);
        String nuevoCbu = Integration.CbuService.generarCbu(
                Banco.CODIGO_BANCO_MATIAS,
                suc.getCodigo(),
                (int) user.getId()
        );
        user.setCbu(nuevoCbu);

        return true;
    }

    public boolean existe(Usuario user) {
        if (user == null) return false;

        return buscarUsuarioActivoPorMail(user.getMail()) != null;
    }

    public ArrayList<Transaccion> obtenerHistorialDeTransaccionesDelUsuario (UsuarioCliente user){
//        System.out.println("-----------------------------");
//        System.out.println("Historial de transacciones del usuario " + user.getName() + ": \n");
        return user.getHistorialTransaccion();
    }

    public void agregarTransaccion(UsuarioCliente user, Transaccion transaccion) {
        user.getHistorialTransaccion().add(transaccion);

    }
}
