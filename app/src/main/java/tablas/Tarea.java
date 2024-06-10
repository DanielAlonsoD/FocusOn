package tablas;

import java.util.ArrayList;

public class Tarea {
    private String id;
    private String nombre;
    private boolean realizado;
    private ArrayList<Subtarea> subtareas;

    public Tarea(String nombre, boolean realizado, ArrayList<Subtarea> subtareas) {
        this.nombre = nombre;
        this.realizado = realizado;
        this.subtareas = subtareas;
    }

    public Tarea(String id, String nombre, boolean realizado, ArrayList<Subtarea> subtareas) {
        this.id = id;
        this.nombre = nombre;
        this.realizado = realizado;
        this.subtareas = subtareas;
    }

    public String getId() {
        return id;
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

    public ArrayList<Subtarea> getSubtareas() {
        return subtareas;
    }

    public void setSubtareas(ArrayList<Subtarea> subtareas) {
        this.subtareas = subtareas;
    }
}
