package hlc.com.monumentosdeespaa.Datos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Clase que representa a los monumetos de la vista de la base de datos
 * Created by Sergio on 10/02/2018.
 */

public class Monumentos implements Parcelable{

    private int id_monumentos;
    private String nombre;
    private Date anno;
    private String localidad;
    private String provincia;
    private String comunidad;
    private double latitud;
    private double longitud;

    @Override
    public String toString() {
        return id_monumentos + " " + nombre + " " + anno + " " + localidad + " " + provincia + " " + comunidad + " " + latitud + " " + longitud;
    }

    public Monumentos(int id_monumentos, String nombre, Date anno, String localidad, String provincia, String comunidad, double lat, double lng) {
        this.id_monumentos = id_monumentos;
        this.nombre = nombre;
        this.anno = anno;
        this.localidad = localidad;
        this.provincia = provincia;
        this.comunidad = comunidad;
        this.latitud = lat;
        this.longitud = lng;
    }

    protected Monumentos(Parcel in) {
        id_monumentos = in.readInt();
        nombre = in.readString();
        localidad = in.readString();
        provincia = in.readString();
        comunidad = in.readString();
        latitud = in.readDouble();
        longitud = in.readDouble();
    }

    public int getId_monumentos() {
        return id_monumentos;
    }

    public void setId_monumentos(int id_monumentos) {
        this.id_monumentos = id_monumentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getAnno() {
        return anno;
    }

    public void setAnno(Date anno) {
        this.anno = anno;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComunidad() {
        return comunidad;
    }

    public void setComunidad(String comunidad) {
        this.comunidad = comunidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public LatLng getLatLng() {
        return new LatLng(latitud,longitud);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_monumentos);
        dest.writeString(nombre);
        dest.writeString(localidad);
        dest.writeString(provincia);
        dest.writeString(comunidad);
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
    }

    public static final Parcelable.Creator<Monumentos> CREATOR
            = new Parcelable.Creator<Monumentos>() {
        public Monumentos createFromParcel(Parcel in) {
            return new Monumentos(in);
        }

        public Monumentos[] newArray(int size) {
            return new Monumentos[size];
        }
    };

}
