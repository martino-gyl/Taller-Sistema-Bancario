package Integration;

public class ResultadoTransferencia {
    public boolean fueExistoso;
    public String mensaje;

    public ResultadoTransferencia(boolean fueExistoso, String mensaje) {
        this.fueExistoso = fueExistoso;
        this.mensaje = mensaje;
    }
    public ResultadoTransferencia() {}
}