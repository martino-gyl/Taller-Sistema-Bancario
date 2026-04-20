package Integration;

import java.util.List;
import java.util.Map;

public class Mediator {
    private Map<String, ITransactionService> bancos;

    public Mediator(Map<String, ITransactionService> bancos) {
        this.bancos = bancos;
    }

    public ResultadoTransferencia transferir(String cbuOrigen, String cbuDestino, double monto) {
        try {
            ITransactionService origen = buscarBanco(cbuOrigen);
            ITransactionService destino = buscarBanco(cbuDestino);

            destino.validarCapacidadDeRecepcion(cbuDestino, monto);

            origen.extraerPorCbu(cbuOrigen, cbuDestino, monto);
            destino.depositarPorCbu(cbuOrigen, cbuDestino, monto);

            return new ResultadoTransferencia(true, "Transferencia Exitosa");
        } catch (Exception e) {
            return new ResultadoTransferencia(false, e.getMessage());
        }
    }
    private ITransactionService buscarBanco(String cbu) {
        String codigoBanco = CbuService.obtenerCodigoBanco(cbu);
        return bancos.get(codigoBanco);
    }

}