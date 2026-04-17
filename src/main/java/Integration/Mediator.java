package Integration;

import java.util.Dictionary;
import java.util.List;

public class Mediator {
    private Dictionary<String, TransactionServiceImpl> transactionServices;

    public Mediator(Dictionary<String, TransactionServiceImpl> transactionServices) {
        this.transactionServices = transactionServices;
    }

    public ResultadoTransferencia transferir(String cbuOrigen, String cbuDestino, double monto) {

        String codigoBancarioOrigen = CbuService.obtenerCodigoBanco(cbuOrigen);
        String codigoBancarioDestino = CbuService.obtenerCodigoBanco(cbuDestino);

        TransactionServiceImpl transactionServiceOrigen = transactionServices.get(codigoBancarioOrigen);
        TransactionServiceImpl transactionServiceDestino = transactionServices.get(codigoBancarioDestino);

        if (transactionServiceOrigen == null || transactionServiceDestino == null) {
            throw new IllegalArgumentException("TransactionService no está iniciado");
        }

        ResultadoTransferencia resultadoDestino =
                transactionServiceDestino.recibirTransferencia(cbuOrigen,cbuDestino,monto);

        if (!resultadoDestino.fueExistoso) {
            return resultadoDestino;
        }

        transactionServiceOrigen.extraerPorCbu(cbuOrigen, monto);
        transactionServiceDestino.depositarPorCbu(cbuDestino, monto);

        return resultadoDestino;
    }

}