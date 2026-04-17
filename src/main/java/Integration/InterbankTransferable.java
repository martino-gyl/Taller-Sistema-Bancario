package Integration;

public interface InterbankTransferable {
    public void transferirPorCbu(String cbuOrigen, String cbuDestino, double monto);
    public void depositarPorCbu(String cbuDestino, double monto);
    public void extraerPorCbu(String cbuOrigen, double monto);
    boolean esMiCbu(String cbu);
}
