package BancoMatias.entity;

public class Usuario {
    private long id;
    private String password;
    private String mail;
    private static int contadorId = 1;

    public Usuario(String mail, String password) {
        this.id = contadorId++;
        this.mail = mail;
        this.password = password;

    }





    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static int getContadorId() {
        return contadorId;
    }

    public static void setContadorId(int contadorId) {
        Usuario.contadorId = contadorId;
    }
}
