// PaisRegion.java
package app3d_3;

import java.awt.Polygon;

public class PaisRegion {
    private String nombre;
    private Polygon region;
    private ClimaInfo clima;

    public PaisRegion(String nombre, Polygon region, ClimaInfo clima) {
        this.nombre = nombre;
        this.region = region;
        this.clima = clima;
    }

    public String getNombre() { return nombre; }
    public Polygon getRegion() { return region; }
    public ClimaInfo getClima() { return clima; }
}
