package BancoMatias.service;

import BancoMatias.entity.Transaccion;
import BancoMatias.entity.UsuarioCliente;
import BancoMatias.entity.enums.EstadoTransaccion;
import BancoMatias.repository.TransaccionRepository;
import BancoMatias.repository.UsuarioRepository;
import Integration.CbuService;
import Integration.ResultadoTransferencia;
import Integration.ITransactionService;
import Integration.Mediator;

import java.util.ArrayList;
import java.util.List;

import static BancoMatias.entity.Banco.CODIGO_BANCO_MATIAS;

public class TransaccionService implements ITransactionService {
    private UsuarioRepository usuarioRepo;
    private TransaccionRepository transaccionRepo;
    private Mediator mediador;

    public TransaccionService(UsuarioRepository usuarioRepo, TransaccionRepository transaccionRepo){
        this.usuarioRepo = usuarioRepo;
        this.transaccionRepo = transaccionRepo;
    }
    public boolean montoEsValido(double monto){
        return monto>0;
    }
    public boolean validarSaldo(String cbuOrigen, double monto){
        UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        return  cuentaOrigen.getSaldo() >= monto;
    }
    public boolean cuentaEsValida(String cbuOrigen){
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
    public void extraer(UsuarioCliente user, Double monto) throws Exception {
        if (monto <= 0) {throw new Exception("El monto tiene que ser mayor que 0");}
        if (!usuarioRepo.existe(user)) {throw new Exception("El usuario no existe en este banco");}
        if (user.getSaldo() < monto) {throw new Exception("Saldo insuficiente. Saldo actual: $" + user.getSaldo());}
        user.setSaldo(user.getSaldo() - monto);
        usuarioRepo.save(user);
    }

    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        Transaccion intento = new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.PENDIENTE);
        ResultadoTransferencia resultado;

        try {
            if (!montoEsValido(monto)) throw new Exception("Monto debe ser mayor a 0.");
            if (!validarSaldo(cbuOrigen, monto)) throw new Exception("Saldo insuficiente.");
            if(!cuentaEsValida(cbuOrigen)) throw new Exception("El CBU es inválido o el usuario no existe en este banco.");
            resultado = mediador.transferir(cbuOrigen, cbuDestino, monto);

            if (resultado.fueExistoso) {
                intento.setEstado(EstadoTransaccion.EXITOSA);
            } else {
                intento.setEstado(EstadoTransaccion.FALLIDA);
                intento.setMotivo(resultado.mensaje);
            }

        } catch (Exception e) {
            resultado = new ResultadoTransferencia(false, e.getMessage());
            intento.setEstado(EstadoTransaccion.RECHAZADA);
            intento.setMotivo(e.getMessage());
        } finally {
            transaccionRepo.save(intento);
            UsuarioCliente cuentaOrigen = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
            if(cuentaOrigen != null) { cuentaOrigen.getHistorialTransaccion().add(intento);}

        }
        return resultado;
    }


    public String getCodigoBanco() {
        return CODIGO_BANCO_MATIAS;
    }


    @Override
    public void depositarPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        UsuarioCliente cuenta = usuarioRepo.buscarUsuarioClientePorCbu(cbuDestino);
        cuenta.sumarSaldo(monto);
        Transaccion t = new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.EXITOSA);
        cuenta.getHistorialTransaccion().add(t);
        transaccionRepo.save(t);
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        UsuarioCliente cuenta = usuarioRepo.buscarUsuarioClientePorCbu(cbuOrigen);
        cuenta.restarSaldo(monto);
        Transaccion t = new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.CONFIRMADA);
        cuenta.getHistorialTransaccion().add(t);
        transaccionRepo.save(t);
    }

    public boolean esMiCbu(String cbu) {
        return CbuService.obtenerCodigoBanco(cbu).equals(CODIGO_BANCO_MATIAS);
    }

    public Mediator getMediador() {
        return mediador;
    }
    public List<Transaccion> getHistorialGlobal() {
        return transaccionRepo.findAll(); // El repo ya lo tiene
    }
    public void setMediador(Mediator mediador) {
        this.mediador = mediador;
    }



    @Override
    public void validarCapacidadDeRecepcion(String cbu, double monto) throws Exception {

        if (!montoEsValido(monto)) {
            throw new Exception("El monto tiene que ser mayor a 0.");
        }
        if (!cuentaEsValida(cbu)) {
            throw new Exception("El CBU es inválido o el usuario no existe en este banco.");
        }
    }


}

