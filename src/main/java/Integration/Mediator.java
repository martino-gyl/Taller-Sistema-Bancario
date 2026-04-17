package Integration;

import java.util.Dictionary;
import java.util.List;

public class Mediator {
    private Dictionary<TransactionService> bancos;


    public Mediator(List<TransactionService> bancos) {
        this.bancos = bancos;
    }

    public void transferir(String cbuOrigen, String cbuDestino, double monto) {

        TransactionService bancoOrigen = buscarBanco(cbuOrigen);
        TransactionService bancoDestino = buscarBanco(cbuDestino);

        if (bancoOrigen == null || bancoDestino == null) {
            return;
        }

        bancoOrigen.extraerPorCbu(cbuOrigen, monto);
        bancoDestino.depositarPorCbu(cbuDestino, monto);

        System.out.println("✅ Transferencia procesada: " + cbuOrigen + " -> " + cbuDestino);
    }

    private TransactionService buscarBanco(String cbu) {
        return bancos.stream()
                .filter(b -> b.esMiCbu(cbu))
                .findFirst()
                .orElse(null);
    }
}