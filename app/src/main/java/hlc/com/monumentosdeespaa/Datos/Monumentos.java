package hlc.com.monumentosdeespaa.Datos;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Sergio on 10/02/2018.
 */

public class Monumentos {

    private int id_monumentos;
    private String nombre;
    private Date anno;
    private String localidad;
    private String provincia;
    private String comunidad;
    private LatLng latLng;

    public Monumentos(int id_monumentos, String nombre, Date anno, String localidad, String provincia, String comunidad, double lat, double lng) {
        this.id_monumentos = id_monumentos;
        this.nombre = nombre;
        this.anno = anno;
        this.localidad = localidad;
        this.provincia = provincia;
        this.comunidad = comunidad;
        this.latLng = new LatLng(lat, lng);
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
