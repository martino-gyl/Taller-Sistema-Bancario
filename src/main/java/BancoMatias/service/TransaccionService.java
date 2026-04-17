package BancoMatias.service;

import BancoMatias.entity.Transaccion;
import BancoMatias.entity.UsuarioCliente;
import BancoMatias.repository.TransaccionRepository;
import BancoMatias.repository.UsuarioRepository;
import Integration.CbuService;
import Integration.BankIntegrable;
import Integration.Mediator;

import static BancoMatias.entity.Banco.CODIGO_BANCO_MATIAS;

public class TransaccionService implements BankIntegrable {
    private UsuarioRepository usuarioRepo;
    private TransaccionRepository transaccionRepo;
    private Mediator mediador;



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
        UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        UsuarioCliente cuentaDestino = usuarioRepo.buscarUsuarioClientePorCbu(cbuDestino);
        Transaccion transaccionPendiente = new Transaccion(cuentaOrigen, cuentaDestino, monto);


            cuentaOrigen.restarSaldo(monto);
            cuentaDestino.sumarSaldo(monto);
            usuarioRepo.agregarTransaccion(cuentaOrigen, transaccionPendiente);
            usuarioRepo.agregarTransaccion(cuentaDestino, transaccionPendiente);

    }

    @Override
    public void depositarPorCbu(String cbuDestino, double monto) {
        UsuarioCliente cuentaDestino = usuarioRepo.buscarUsuarioClientePorCbu(cbuDestino);
        cuentaDestino.sumarSaldo(monto);
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, double monto) {
        UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        cuentaOrigen.restarSaldo(monto);
    }

    @Override
    public boolean esMiCbu(String cbu) {
        return CbuService.obtenerCodigoBanco(cbu).equals(CODIGO_BANCO_MATIAS);
    }

    public Mediator getMediador() {
        return mediador;
    }

    public void setMediador(Mediator mediador) {
        this.mediador = mediador;
    }
//        public void transferir(UsuarioCliente cuentaOrigen, UsuarioCliente cuentaDestino, Double monto) throws SaldoInsuficienteException {
//
//            // Si la validación falla, lanzamos la excepción y el método se corta acá
//            if (!verificarSaldoParaRealizarTransaccionExitosa(cuentaOrigen, monto)) {
//                throw new SaldoInsuficienteException("Error: El saldo actual ($" + cuentaOrigen.getSaldo() + ") es insuficiente para transferir $" + monto);
//            }
//
//            // Si llegamos acá, es porque había saldo
//            Transaccion transaccionPendiente = new Transaccion(cuentaOrigen, cuentaDestino, monto);
//
//            cuentaOrigen.restarSaldo(monto);
//            cuentaDestino.sumarSaldo(monto);
//
//            usuarioRepo.agregarTransaccion(cuentaOrigen, transaccionPendiente);
//            usuarioRepo.agregarTransaccion(cuentaDestino, transaccionPendiente);
//        }



}

