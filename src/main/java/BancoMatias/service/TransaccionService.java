package BancoMatias.service;

import BancoMatias.entity.UsuarioCliente;
import BancoMatias.repository.TransaccionRepository;
import BancoMatias.repository.UsuarioRepository;
import Integration.CbuService;
import Integration.ResultadoTransferencia;
import Integration.TransactionServiceImpl;
import Integration.Mediator;

import static BancoMatias.entity.Banco.CODIGO_BANCO_MATIAS;

public class TransaccionService implements TransactionServiceImpl {
    private UsuarioRepository usuarioRepo;
    private TransaccionRepository transaccionRepo;
    private Mediator mediador;

    public TransaccionService(UsuarioRepository usuarioRepo, TransaccionRepository transaccionRepo){
        this.usuarioRepo = usuarioRepo;
        this.transaccionRepo = transaccionRepo;
    }

    public boolean validarSaldo(String cbuOrigen, double monto){
        UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        return  cuentaOrigen.getSaldo() >= monto;
    }
    public boolean validarCuenta(String cbuOrigen){
        return usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen) != null;
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

    public boolean validarMonto(double monto){
        return monto>0;
    }

    @Override
    public void cargarMovimientoDeTransferenciaEnviada(String cbuOrigen, String cbuDestino, double monto) {

    }

    @Override
    public void cargarMovimientoDeTransferenciaRecibida(String cbuOrigen, String cbuDestino, double monto) {

    }

    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        ResultadoTransferencia resultado = new ResultadoTransferencia();

        resultado.fueExistoso = validarMonto(monto);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El monto tiene que ser mayor a 0.";
            return resultado;}

        resultado.fueExistoso = validarCuenta(cbuOrigen);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El cbu esta mal escrito o el usuario no existe";
            return resultado;}


        resultado.fueExistoso = validarSaldo(cbuOrigen, monto);
        if (!resultado.fueExistoso) { resultado.mensaje = "Saldo insuficiente para realizar la operacion.";
            return resultado;}

        return mediador.transferir(cbuOrigen, cbuDestino, monto);
    }

    @Override
    public ResultadoTransferencia recibirTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        ResultadoTransferencia resultado = new ResultadoTransferencia();

        resultado.fueExistoso = validarMonto(monto);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El monto tiene que ser mayor a 0.";
            return resultado;}

        resultado.fueExistoso = validarCuenta(cbuOrigen);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El cbu esta mal escrito o el usuario no existe";
            return resultado;}

        return resultado ;
    }

    @Override
    public String getCodigoBanco() {
        return CODIGO_BANCO_MATIAS;
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




}

