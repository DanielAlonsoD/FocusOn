package tablas;

public class Horario {
    private String id;
    private String titulo;
    private String diasSemana;
    private String horaInicio;
    private String horaFinal;

    public Horario(String titulo, String diasSemana, String horaInicio, String horaFinal) {
        this.titulo = titulo;
        this.diasSemana = diasSemana;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public Horario(String id, String titulo, String diasSemana, String horaIncio, String horaFinal) {
        this.id = id;
        this.titulo = titulo;
        this.diasSemana = diasSemana;
        this.horaInicio = horaIncio;
        this.horaFinal = horaFinal;
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

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }
}
