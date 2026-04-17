package Integration;

import java.util.List;

public class Mediator {
    private List<BankIntegrable> bancos;

    public Mediator(List<BankIntegrable> bancos) {
        this.bancos = bancos;
    }

    public void transferir(String cbuOrigen, String cbuDestino, double monto) {

        BankIntegrable bancoOrigen = buscarBanco(cbuOrigen);
        BankIntegrable bancoDestino = buscarBanco(cbuDestino);

        if (bancoOrigen == null || bancoDestino == null) {
            return;
        }

        bancoOrigen.extraerPorCbu(cbuOrigen, monto);
        bancoDestino.depositarPorCbu(cbuDestino, monto);

        System.out.println("✅ Transferencia procesada: " + cbuOrigen + " -> " + cbuDestino);
    }

    private BankIntegrable buscarBanco(String cbu) {
        return bancos.stream()
                .filter(b -> b.esMiCbu(cbu))
                .findFirst()
                .orElse(null);
    }
}