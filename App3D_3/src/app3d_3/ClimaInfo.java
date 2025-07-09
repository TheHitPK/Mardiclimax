package app3d_3;



public class ClimaInfo {
    private float temperatura;
    private String descripcion;
    private float precipitacion;

    public ClimaInfo(float temperatura, String descripcion, float precipitacion) {
        this.temperatura = temperatura;
        this.descripcion = descripcion;
        this.precipitacion = precipitacion;
    }

    public float getTemperatura() { return temperatura; }
    public String getDescripcion() { return descripcion; }
    public float getPrecipitacion() { return precipitacion; }
}
