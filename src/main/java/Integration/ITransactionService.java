package Integration;

public interface ITransactionService {
    void extraerPorCbu(String cbuOrigen, String cbuDestino, double monto) throws Exception;
    void depositarPorCbu(String cbuOrigen, String cbuDestino, double monto) throws Exception;
    void validarCapacidadDeRecepcion(String cbu, double monto) throws Exception;
}
