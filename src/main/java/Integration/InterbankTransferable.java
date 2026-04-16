package Integration;

public interface InterbankTransferable {
    public void transferirPorCbu(String cbuOrigen, String cbuDestino, double monto);
    public void recibirTransferencia(String cbuDestino, double monto);
}
