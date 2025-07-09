/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app3d_3;

/**
 *
 * @author lzzow
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ClimaFetcher {

    private static final String API_KEY = "053d4abe5046409794812307253006";

    public static ClimaInfo obtenerClima(String pais) {
        try {
            // WeatherAPI permite pasar países como ubicación (ej. "Argentina", "Canada")
            String urlStr = "https://api.weatherapi.com/v1/current.json?key=" + API_KEY +
                            "&q=" + pais + "&lang=es";

            HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject current = json.getJSONObject("current");

            float temp = current.getFloat("temp_c");
            String descripcion = current.getJSONObject("condition").getString("text");
            float precipitacion = current.getFloat("precip_mm");

            return new ClimaInfo(temp, descripcion, precipitacion);

        } catch (Exception e) {
            System.err.println("Error al obtener clima de " + pais + ": " + e.getMessage());
            return new ClimaInfo(0, "Desconocido", 0);
        }
    }
}