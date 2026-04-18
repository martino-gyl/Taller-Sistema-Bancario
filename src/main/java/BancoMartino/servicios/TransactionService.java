package BancoMartino.servicios;

import BancoMartino.dominio.Banco;
import BancoMartino.dominio.Cuenta;
import BancoMartino.dominio.TipoMovimiento;
import BancoMatias.entity.UsuarioCliente;
import Integration.CbuService;
import Integration.Mediator;
import Integration.ResultadoTransferencia;
import Integration.TransactionServiceImpl;

import static BancoMatias.entity.Banco.CODIGO_BANCO_MATIAS;

public class TransactionService implements TransactionServiceImpl {
    private Mediator mediador;
    private Banco banco;

    public TransactionService(Banco banco){
        this.banco = banco;
    }
    @Override
    public boolean validarSaldo(String cbuOrigen, double monto){
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        return  cuentaOrigen.getSaldo() >= monto;
    }
    @Override
    public boolean validarCuenta(String cbuOrigen){
        return banco.buscarCuentaPorCbu(cbuOrigen) != null;
    }
    @Override
    public ResultadoTransferencia iniciarTransferencia(String cbuOrigen, String cbuDestino, double monto) {
        ResultadoTransferencia resultado = getResultadoTransferencia(cbuOrigen, monto);
        if (resultado != null) return resultado;

        return mediador.transferir(cbuOrigen, cbuDestino, monto);
    }

    private ResultadoTransferencia getResultadoTransferencia(String cbuOrigen, double monto) {
        ResultadoTransferencia resultado = new ResultadoTransferencia();

        resultado.fueExistoso = validarMonto(monto);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El monto tiene que ser mayor a 0.";
            return resultado;
        }

        resultado.fueExistoso = validarCuenta(cbuOrigen);
        if (!resultado.fueExistoso) {
            resultado.mensaje = "El cbu esta mal escrito o el usuario no existe";
            return resultado;
        }


        resultado.fueExistoso = validarSaldo(cbuOrigen, monto);
        if (!resultado.fueExistoso) { resultado.mensaje = "Saldo insuficiente para realizar la operacion.";
            return resultado;
        }
        return null;
    }

    @Override
    public boolean validarMonto(double monto){
        return monto>0;
    };

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
        return banco.CODIGO_BANCO_MARTINO;
    }

    @Override
    public void cargarMovimientoDeTransferenciaRecibida(String cbuOrigen,String cbuDestino,double monto){
        Cuenta cuentaDestino = banco.buscarCuentaPorCbu(cbuDestino);
        cuentaDestino.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto,
                "Transferencia recibida de cuenta cuyo CBU es " + cbuOrigen
        );
    }

    @Override
    public void cargarMovimientoDeTransferenciaEnviada(String cbuOrigen,String cbuDestino,double monto){
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        cuentaOrigen.registrarMovimiento(
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto,
                "Transferencia enviada a cuenta cuyo CBU es " + cbuDestino
        );
    }

    @Override
    public void depositarPorCbu(String cbuDestino, double monto) {
        Cuenta cuentaDestino = banco.buscarCuentaPorCbu(cbuDestino);
        cuentaDestino.sumarSaldo(monto);
    }

    @Override
    public void extraerPorCbu(String cbuOrigen, double monto) {
        Cuenta cuentaOrigen = banco.buscarCuentaPorCbu(cbuOrigen);
        cuentaOrigen.restarSaldo(monto);
    }

    @Override
    public boolean esMiCbu(String cbu) {
        return CbuService.obtenerCodigoBanco(cbu).equals(banco.CODIGO_BANCO_MARTINO);
    }

}
