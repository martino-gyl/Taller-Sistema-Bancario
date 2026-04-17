package Integration;

public interface TransactionServiceImpl {
    ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto);
    ResultadoTransferencia recibirTransferencia(String cbuOrigen, String cbuDestino, double monto);
    void debitarCuenta(String cbuOrigen, double monto);
    void acreditarCuenta(String cbuDestino, double monto);
    boolean existeCuenta(String cbu);
    String getCodigoBanco();
}
