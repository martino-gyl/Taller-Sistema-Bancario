package BancoMatias.repository;

import BancoMatias.entity.Banco;
import BancoMatias.entity.Sucursal;

import java.util.ArrayList;

public class SucursalRepository {
    private Banco Bancodb = Banco.getInstancia();

    public ArrayList<Sucursal> getTodasLasSucursales(){
        return Bancodb.getSucursales();
    }

}
