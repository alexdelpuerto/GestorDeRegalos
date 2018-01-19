package regalos.example.com.gestorderegalos.Pagos;


/**
 * Created by Alejandro on 19/10/2017.
 */

public class Dinero {

    private String usuario;
    private String nombreRegalo;
    private double precio;
    private int numUsuarios;
    private int id_regalo;

    public Dinero() {

    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setNombreRegalo(String nombreRegalo) {
        this.nombreRegalo = nombreRegalo;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setNumUsuarios(int numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public void setId_regalo(int id_regalo) {
        this.id_regalo = id_regalo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombreRegalo() {
        return nombreRegalo;
    }

    public double getPrecio() {
        return precio;
    }

    public int getNumUsuarios() {
        return numUsuarios;
    }

    public int getId_regalo() {
        return id_regalo;
    }
}
