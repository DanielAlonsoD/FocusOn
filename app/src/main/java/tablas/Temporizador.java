package tablas;

public class Temporizador {
    private String id;
    private String titulo;
    private String trabajo;
    private String descanso;

    public Temporizador(String titulo, String trabajo, String descanso) {
        this.titulo = titulo;
        this.trabajo = trabajo;
        this.descanso = descanso;
    }

    public Temporizador(String id, String titulo, String trabajo, String descanso) {
        this.id = id;
        this.titulo = titulo;
        this.trabajo = trabajo;
        this.descanso = descanso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getDescanso() {
        return descanso;
    }

    public void setDescanso(String descanso) {
        this.descanso = descanso;
    }
}
