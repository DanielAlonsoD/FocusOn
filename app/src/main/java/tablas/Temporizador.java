package tablas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Temporizador implements Parcelable {
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

    protected Temporizador(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        trabajo = in.readString();
        descanso = in.readString();
    }

    public static final Creator<Temporizador> CREATOR = new Creator<Temporizador>() {
        @Override
        public Temporizador createFromParcel(Parcel in) {
            return new Temporizador(in);
        }

        @Override
        public Temporizador[] newArray(int size) {
            return new Temporizador[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(titulo);
        dest.writeString(trabajo);
        dest.writeString(descanso);
    }
}
