package regalos.example.com.gestorderegalos.Amigos;

/**
 * Created by Alejandro on 13/11/2017.
 */

public class Amigo {
    private String fotoPerfil;
    private String nick;
    private String nombreComp;
    private boolean marcado = false;

    public Amigo() {

    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNombreComp() {
        return nombreComp;
    }

    public void setNombreComp(String nombreComp) {
        this.nombreComp = nombreComp;
    }

    public boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado){
        this.marcado = marcado;
    }
}
