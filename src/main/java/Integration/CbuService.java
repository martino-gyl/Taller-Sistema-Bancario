package Integration;

public class CbuService {

    public static String generarCbu(String codigoBanco, String codigoSucursal, int numeroCuenta) {
        String banco = completarConCeros(codigoBanco, 3);
        String sucursal = completarConCeros(codigoSucursal, 3);
        String cuenta = completarConCeros(String.valueOf(numeroCuenta), 6);

        return banco + sucursal + cuenta;
    }

    public static String obtenerCodigoBanco(String cbu) {
        validarCbu(cbu);
        return cbu.substring(0, 3);
    }

    public static String obtenerCodigoSucursal(String cbu) {
        validarCbu(cbu);
        return cbu.substring(3, 6);
    }

    public static String obtenerNumeroCuenta(String cbu) {
        validarCbu(cbu);
        return cbu.substring(6, 12);
    }

    public static void validarCbu(String cbu) {
        if (cbu == null || !cbu.matches("\\d{12}")) {
            throw new IllegalArgumentException("El CBU debe tener 12 dígitos.");
        }
    }
    private static String completarConCeros(String valor, int longitud) {
        return String.format("%" + longitud + "s", valor).replace(' ', '0');
    }
}