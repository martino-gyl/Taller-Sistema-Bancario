package BancoMartino.servicios;

import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Cuenta;
import BancoMatias.entity.enums.EstadoTransaccion;
import Integration.Mediator;
import Integration.ResultadoTransferencia;
import Integration.ITransactionService;

import java.util.List;

public class TransactionService implements ITransactionService {
    private Mediator mediador;
    private Banco banco;
    private TransaccionRepositorio transaccionRepositorio;

    public TransactionService(Banco banco, TransaccionRepositorio transaccionRepositorio){
        this.banco = banco;
        this.transaccionRepositorio = transaccionRepositorio;
    }

    public boolean saldoEsSuficiente(String cbuOrigen, double monto){
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        return  cuentaOrigen.getSaldo() >= monto;
    }
    public boolean cuentaEsValida(String cbuOrigen){
        return banco.buscarCuentaPorCbu(cbuOrigen) != null;
    }

    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        Transaccion intento = new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.PENDIENTE);
        ResultadoTransferencia resultado;

        try {
            if (!montoEsValido(monto)) throw new Exception("Monto debe ser mayor a 0.");
            if (!saldoEsSuficiente(cbuOrigen, monto)) throw new Exception("Saldo insuficiente.");
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
            transaccionRepositorio.guardar(intento);
        }
        return resultado;
    }

    public boolean montoEsValido(double monto){
        return monto>0;
    };


    @Override
    public void depositarPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        Cuenta cuentaDestino = banco.buscarCuentaPorCbu(cbuDestino);
        cuentaDestino.sumarSaldo(monto);
        transaccionRepositorio.guardar(new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.EXITOSA));
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        cuentaOrigen.restarSaldo(monto);
        transaccionRepositorio.guardar(new Transaccion(cbuOrigen, cbuDestino, monto, EstadoTransaccion.CONFIRMADA));
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

    public void setMediador(Mediator mediador) {
        this.mediador = mediador;
    }

    public List<Transaccion> obtenerTransaccionesPorCbu(String cbu) {
        return transaccionRepositorio.findByCbu(cbu);
    }

}
