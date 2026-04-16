package Integration;

public class CbuService {

    public static String generarCbu(String codigoBanco, String codigoSucursal, int numeroCuenta) {
        return codigoBanco + codigoSucursal + numeroCuenta;
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
}