package BancoMartino.servicios;

import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Cuenta;
import BancoMartino.dominio.TipoMovimiento;
import Integration.CbuService;
import Integration.Mediator;
import Integration.ResultadoTransferencia;
import Integration.ITransactionService;

public class TransactionService implements ITransactionService {
    private Mediator mediador;
    private Banco banco;

    public TransactionService(Banco banco){
        this.banco = banco;
    }

    public boolean saldoEsSuficiente(String cbuOrigen, double monto){
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        return  cuentaOrigen.getSaldo() >= monto;
    }
    public boolean cuentaEsValida(String cbuOrigen){
        return banco.buscarCuentaPorCbu(cbuOrigen) != null;
    }

    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        ResultadoTransferencia validacionOrigenTransferencia = validarTransferenciaDesdeOrigen(cbuOrigen, monto);
        if (validacionOrigenTransferencia != null) return validacionOrigenTransferencia;

        return mediador.transferir(cbuOrigen, cbuDestino, monto);
    }

    private ResultadoTransferencia validarTransferenciaDesdeOrigen(String cbuOrigen, double monto) {
        ResultadoTransferencia resultado = new ResultadoTransferencia();

        if (!montoEsValido(monto)) {
            resultado.fueExistoso = false;
            resultado.mensaje = "El monto tiene que ser mayor a 0.";
            return resultado;
        }

        if (!cuentaEsValida(cbuOrigen)) {
            resultado.fueExistoso = cuentaEsValida(cbuOrigen);
            resultado.mensaje = "El cbu esta mal escrito o el usuario no existe";
            return resultado;
        }

        if (!saldoEsSuficiente(cbuOrigen, monto)) {
            resultado.fueExistoso = false;
            resultado.mensaje = "Saldo insuficiente para realizar la operacion.";
            return resultado;
        }
        return null;
    }

    public boolean montoEsValido(double monto){
        return monto>0;
    };



    public String getCodigoBanco() {
        return banco.CODIGO_BANCO_MARTINO;
    }

    public void cargarMovimientoDeTransferenciaRecibida(String cbuOrigen,String cbuDestino,double monto){
        Cuenta cuentaDestino = banco.buscarCuentaPorCbu(cbuDestino);
        cuentaDestino.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto,
                "Transferencia recibida de cuenta cuyo CBU es " + cbuOrigen
        );
    }

    public void cargarMovimientoDeTransferenciaEnviada(String cbuOrigen,String cbuDestino,double monto){
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        cuentaOrigen.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto,
                "Transferencia enviada a cuenta cuyo CBU es " + cbuDestino
        );
    }

    @Override
    public void depositarPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        Cuenta cuentaDestino = banco.buscarCuentaPorCbu(cbuDestino);
        cuentaDestino.sumarSaldo(monto);
        cargarMovimientoDeTransferenciaRecibida(cbuOrigen, cbuDestino, monto);
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, String cbuDestino, double monto) {
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        cuentaOrigen.restarSaldo(monto);
        cargarMovimientoDeTransferenciaEnviada(cbuOrigen, cbuDestino, monto);
    }


    public boolean esMiCbu(String cbu) {
        return CbuService.obtenerCodigoBanco(cbu).equals(banco.CODIGO_BANCO_MARTINO);
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
}
