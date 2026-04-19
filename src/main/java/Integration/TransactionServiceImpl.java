package Integration;

public interface TransactionServiceImpl {
    void extraerPorCbu(String cbu, double monto) throws Exception;
    void depositarPorCbu(String cbu, double monto) throws Exception;
    boolean esMiCbu(String cbu);
    void validarCapacidadDeRecepcion(String cbu, double monto) throws Exception;
}
