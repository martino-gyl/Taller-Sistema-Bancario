package Integration;

import java.util.Dictionary;
import java.util.List;

public class Mediator {
    private List<TransactionServiceImpl> bancos;

    public Mediator(List<TransactionServiceImpl> bancos) {
        this.bancos = bancos;
    }

//    public ResultadoTransferencia transferir(String cbuOrigen, String cbuDestino, double monto) {
//
//        TransactionServiceImpl transactionServiceOrigen = buscarBanco(cbuOrigen);
//        TransactionServiceImpl transactionServiceDestino = buscarBanco(cbuDestino);
//        if (transactionServiceOrigen == null || transactionServiceDestino == null) {
//            throw new IllegalArgumentException("TransactionService no está iniciado");
//        }
//        ResultadoTransferencia resultadoDestino =
//                transactionServiceDestino.recibirTransferencia(cbuOrigen,cbuDestino,monto);
//
//        if (!resultadoDestino.fueExistoso) {
//            return resultadoDestino;
//        }
//        transactionServiceOrigen.extraerPorCbu(cbuOrigen, monto);
//        transactionServiceOrigen.cargarMovimientoDeTransferenciaEnviada(cbuOrigen,cbuDestino,monto);
//        transactionServiceDestino.depositarPorCbu(cbuDestino, monto);
//        transactionServiceDestino.cargarMovimientoDeTransferenciaRecibida(cbuOrigen,cbuDestino,monto);
//
//        return resultadoDestino;
//    }
    public ResultadoTransferencia transferir(String cbuOrigen, String cbuDestino, double monto) {
        try {
            TransactionServiceImpl origen = buscarBanco(cbuOrigen);
            TransactionServiceImpl destino = buscarBanco(cbuDestino);

            destino.validarCapacidadDeRecepcion(cbuDestino, monto);
            origen.extraerPorCbu(cbuOrigen, monto);
            destino.depositarPorCbu(cbuDestino, monto);

            return new ResultadoTransferencia(true, "Transferencia Exitosa");
        } catch (Exception e) {
            return new ResultadoTransferencia(false, e.getMessage());
        }
    }
    private TransactionServiceImpl buscarBanco(String cbu) {
        return bancos.stream()
                .filter(b -> b.esMiCbu(cbu))
                .findFirst()
                .orElse(null);
    }

}