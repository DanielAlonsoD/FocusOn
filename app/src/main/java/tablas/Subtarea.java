package tablas;

public class Subtarea {
    private String id;
    private String nombre;
    private boolean realizado;

    public Subtarea(String id, String nombre, boolean realizado) {
        this.id = id;
        this.nombre = nombre;
        this.realizado = realizado;
    }

    public Subtarea(String nombre, boolean realizado) {
        this.nombre = nombre;
        this.realizado = realizado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }
}
