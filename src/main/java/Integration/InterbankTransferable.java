package Integration;

public interface InterbankTransferable {
    public void transferir(String cbuOrigen, String cbuDestino, double monto);
    public void recibirTransferencia(String cbuDestino, double monto);
}
