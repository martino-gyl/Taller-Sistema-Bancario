package Integration;

public interface TransactionServiceImpl {
    void extraerPorCbu(String cbuOrigen, String cbuDestino, double monto) throws Exception;
    void depositarPorCbu(String cbuOrigen, String cbuDestino, double monto) throws Exception;
    boolean esMiCbu(String cbu);
    void validarCapacidadDeRecepcion(String cbu, double monto) throws Exception;
}
