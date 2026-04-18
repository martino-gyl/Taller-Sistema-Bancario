package Integration;

public interface TransactionServiceImpl {
    ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto);
    ResultadoTransferencia recibirTransferencia(String cbuOrigen, String cbuDestino, double monto);
    void depositarPorCbu(String cbuOrigen, double monto);
    void extraerPorCbu(String cbuDestino, double monto);
    boolean esMiCbu(String cbu);
    String getCodigoBanco();
    public boolean cuentaEsValida(String cbuOrigen);
    public boolean saldoEsSuficiente(String cbuOrigen, double monto);
    public boolean montoEsValido(double monto);
    public void cargarMovimientoDeTransferenciaEnviada(String cbuOrigen,String cbuDestino,double monto);
    public void cargarMovimientoDeTransferenciaRecibida(String cbuOrigen,String cbuDestino,double monto);
}
