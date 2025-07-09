package app3d_3;



public class CoordenadasUtils {
    public static int convertirX(double longitud) {
        return (int) ((longitud + 180) * (1200.0 / 360.0));
    }

    public static int convertirY(double latitud) {
        return (int) ((90 - latitud) * (784.0 / 180.0));
    }
}
