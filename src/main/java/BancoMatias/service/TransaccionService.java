package BancoMatias.service;

import BancoMatias.entity.Transaccion;
import BancoMatias.entity.UsuarioCliente;
import BancoMatias.repository.TransaccionRepository;
import BancoMatias.repository.UsuarioRepository;
import Integration.InterbankTransferable;

public class TransaccionService implements InterbankTransferable {
    private UsuarioRepository usuarioRepo;
    private TransaccionRepository transaccionRepo;

    public TransaccionService(UsuarioRepository usuarioRepo, TransaccionRepository transaccionRepo){
        this.usuarioRepo = usuarioRepo;
        this.transaccionRepo = transaccionRepo;
    }

    public boolean transferir(UsuarioCliente emisor, UsuarioCliente destinatario, Double monto) {
            Transaccion transaccionPendiente = new Transaccion(emisor, destinatario, monto);
            if (verificarSaldoParaRealizarTransaccionExitosa(emisor, monto)) {
                emisor.restarSaldo(monto);
                destinatario.sumarSaldo(monto);
                usuarioRepo.agregarTransaccion(emisor, transaccionPendiente);
                usuarioRepo.agregarTransaccion(destinatario, transaccionPendiente);

                return  true;
                   } else {
                return false;
            }

        }


    public boolean verificarSaldoParaRealizarTransaccionExitosa (UsuarioCliente emisor, double monto){
        return monto > 0 && emisor.getSaldo() >= monto;
    }
    public boolean depositar(UsuarioCliente user, Double monto) {
        if (user == null || monto == null || monto <= 0) {
            return false;
        }
        if (!usuarioRepo.existe(user)) {
            return false;
        }
        user.setSaldo(user.getSaldo() + monto);
        return usuarioRepo.save(user);
    }

    @Override
    public void transferirPorCbu(String cbuOrigen, String cbuDestino, double monto) {

    }

    @Override
    public void recibirTransferencia(String cbuDestino, double monto) {

    }
}

