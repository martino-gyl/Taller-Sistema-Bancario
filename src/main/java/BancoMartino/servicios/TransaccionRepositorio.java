package BancoMartino.servicios;

import java.util.ArrayList;
import java.util.List;

public class TransaccionRepositorio {
    private List<Transaccion> transacciones;

    public TransaccionRepositorio() {
        this.transacciones = new ArrayList<>();
    }

    public void guardar(Transaccion transaccion) {
        if (transaccion != null) {
            this.transacciones.add(transaccion);
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

