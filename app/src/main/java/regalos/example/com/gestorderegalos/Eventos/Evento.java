package regalos.example.com.gestorderegalos.Eventos;

/**
 * Created by Alejandro on 19/10/2017.
 */

public class Evento {
    private String id;
    private String nombreEvento;
    private String dineroFalta;
    private int maxProgres;
    private int minProgres;
    private String creador;
    private boolean privilegios;

    public Evento() {

    }

    public boolean getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(boolean privilegios) {
        this.privilegios = privilegios;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getId() {
        return id;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public String getDineroFalta() {
        return dineroFalta;
    }

    public int getMaxProgres(){
        return maxProgres;
    }

    public int getMinProgres(){
        return minProgres;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public void setDineroFalta(String dineroFalta) {
        this.dineroFalta = dineroFalta;
    }

    public void setMaxProgres(int maxProgres){
        this.maxProgres = maxProgres;
    }

    public void setMinProgres(int minProgres){
        this.minProgres = minProgres;
    }

}
