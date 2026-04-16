package BancoMatias.entity;

import java.util.UUID;

public class Transaccion {
    private String id;
    private Usuario emisor;
    private Usuario destinatario;
    private Double monto;

    public boolean getTransaccionExitosa() {
        return transaccionExitosa;
    }

    public void setTransaccionExitosa(boolean transaccionExitosa) {
        this.transaccionExitosa = transaccionExitosa;
    }

    private boolean transaccionExitosa;

    public Transaccion(Usuario emisor, Usuario destinatario, Double monto) {
        this.id = UUID.randomUUID().toString();
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.monto = monto;
      //  this.transaccionExitosa = tieneSaldoParaRealizarLaOperacion(emisor, monto);
    }

//    public boolean tieneSaldoParaRealizarLaOperacion(Usuario cliente1, Double monto){
//        if(cliente1.getSaldo()>=monto) {
//            return true;}
//        return false;
//    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n {" +
                "id= '" + id + '\'' +
//                ", emisor= " + emisor.getName() +
//                ", destinatario= " + destinatario.getName() +
                ", monto= " + this.getMonto()+
                ", transaccionExitosa= " + this.getTransaccionExitosa() +
                '}';
    }
}
