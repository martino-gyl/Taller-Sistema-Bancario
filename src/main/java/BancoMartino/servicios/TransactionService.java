package BancoMartino.servicios;

import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Cuenta;
import BancoMartino.dominio.TipoMovimiento;
import BancoMatias.entity.UsuarioCliente;
import Integration.Mediator;
import Integration.ResultadoTransferencia;
import Integration.TransactionServiceImpl;

public class TransactionService implements TransactionServiceImpl {
    private Mediator mediador;

    public TransactionService(Banco banco){
        this.banco = banco;
    }

    @Override
    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        // validar
        return mediador.transferir(cbuOrigen, cbuDestino, monto);
    };

    @Override
    public ResultadoTransferencia recibirTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        // validar
        return mediador.transferir(cbuOrigen, cbuDestino, monto);
    };

    @Override
    public void depositarPorCbu(String cbuDestino, double monto) {
        UsuarioCliente cuentaDestino = .buscarUsuarioClientePorCbu(cbuDestino);
        cuentaDestino.sumarSaldo(monto);
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, double monto) {
        UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        cuentaOrigen.restarSaldo(monto);
    }

        public void transferir(String cbuOrigen, String cbuDestino, double monto) {
        if (sucursal == null) {
            throw new IllegalStateException("El admin no tiene sucursal asignada");
        }

        Cuenta origen = sucursal.buscarCuentaPorCbu(cbuOrigen);
        Cuenta destino = sucursal.buscarCuentaPorCbu(cbuDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Ambas cuentas deben pertenecer a esta sucursal");
        }

        if (origen == destino) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        if (monto > origen.getSaldo()) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta origen para transferir.");
        }

        origen.restarSaldo(monto);
        destino.sumarSaldo(monto);

        origen.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto,
                "Transferencia a cuenta cuyo email es " + destino.getEmail()
        );

        destino.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto,
                "Transferencia desde cuenta cuyo mail es " + origen.getEmail()
        );
    }
}
