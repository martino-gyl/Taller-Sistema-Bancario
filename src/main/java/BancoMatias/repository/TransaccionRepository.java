package BancoMatias.repository;

import BancoMatias.entity.Transaccion;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class TransaccionRepository {
    private List<Transaccion> transacciones;

    public TransaccionRepository() {
        this.transacciones = new ArrayList<>();
    }


    public void save(Transaccion trans) {
        if (trans != null) {
            this.transacciones.add(trans);
        }
    }



    public List<Transaccion> findAll() {
        return transacciones;
    }


    public List<Transaccion> findByCbu(String cbu) {
        return transacciones.stream()
                .filter(t -> t.getCbuOrigen().equals(cbu) || t.getCbuDestino().equals(cbu))
                .toList();
    }
}