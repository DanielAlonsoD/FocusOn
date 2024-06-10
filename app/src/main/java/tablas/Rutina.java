package tablas;

public class Rutina {
    private String id;
    private final String titulo;
    private final String diasSemana;
    private final String horaInicio;
    private final String horaFinal;

    public Rutina(String titulo, String diasSemana, String horaInicio, String horaFinal) {
        this.titulo = titulo;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public Rutina(String id, String titulo, String diasSemana, String horaIncio, String horaFinal) {
        this.id = id;
        this.titulo = titulo;
        this.diasSemana = diasSemana;
        this.horaInicio = horaIncio;
        this.horaFinal = horaFinal;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }
}
