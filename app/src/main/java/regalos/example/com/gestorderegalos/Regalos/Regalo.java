package regalos.example.com.gestorderegalos.Regalos;

/**
 * Created by Alejandro on 27/11/2017.
 */

public class Regalo {
    private String id;
    private String nombre;
    private String descripcion;
    private String foto;
    private String precio;
    private String evento;
    private String comprador;
    private boolean comprado;

    public Regalo() {}

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public String getPrecio() {
        return precio;
    }

    public String getEvento() {
        return evento;
    }

    public String getComprador() {
        return comprador;
    }

    public boolean getComprado() {
        return comprado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
