package Integration;

public interface TransactionServiceImpl {
    ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto);
    ResultadoTransferencia recibirTransferencia(String cbuOrigen, String cbuDestino, double monto);
    void depositarPorCbu(String cbuOrigen, double monto);
    void extraerPorCbu(String cbuDestino, double monto);
    boolean existeCuenta(String cbu);
    String getCodigoBanco();
}
