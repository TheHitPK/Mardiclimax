package app3d_3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pablo
 */
public class DatosGraficos {

    // Estado de carga y datos estáticos compartidos
    private static boolean datosCargados = false;
    private static Map<String, Float> mapaTemperaturas = new HashMap<>();
    private static List<Map.Entry<String, Float>> calientes;
    private static List<Map.Entry<String, Float>> frios;

    // Lista de países para consultar temperatura
    private List<String> paises = Arrays.asList(
        "Kabul", "Tirana", "Berlin", "Andorra", "Luanda", "Antigua", "Riyadh", "Algiers", "Argentina",
        "Yerevan", "Canberra", "Vienna", "Baku", "Nassau", "Dhaka", "Bridgetown", "Manama", "Brussels",
        "Belmopan", "Porto-Novo", "Minsk", "Myanmar", "Sucre", "Sarajevo", "Gaborone", "Brasilia", "Brunei",
        "Sofia", "Ouagadougou", "Bujumbura", "Thimphu", "Praia", "Cambodia", "Cameroon", "Ottawa", "Doha",
        "Ndjamena", "Santiago", "Beijing", "Nicosia", "Bogota", "Moroni", "Pyongyang", "Seoul",
        "Yamoussoukro", "9.93333,-84.08333", "Zagreb", "Havana", "Copenhagen", "Roseau", "Quito", "Cairo",
        "13.6894,-89.1872", "Dubai", "Asmara", "Bratislava", "Ljubljana", "Madrid", "Washington", "Tallinn",
        "Mbabane", "Ethiopia", "Suva", "Manila", "Helsinki", "Paris", "Libreville", "Banjul", "Tbilisi",
        "Accra", "Athens", "Grenada", "Guatemala", "Conakry", "Bissau", "Malabo", "Georgetown", "Haiti",
        "Tegucigalpa", "Budapest", "Delhi", "Jakarta", "Baghdad", "Tehran", "Dublin", "Reykjavik", "Majuro",
        "Honiara", "Jerusalem", "Rome", "Kingston", "Tokyo", "Amman", "Astana", "Nairobi", "Bishkek",
        "Tarawa", "Kuwait", "Vientiane", "Maseru", "Riga", "Beirut", "Monrovia", "Tripoli", "Vaduz",
        "Vilnius", "Luxembourg", "Antananarivo", "Malaysia", "Lilongwe", "Male", "Bamako", "Valletta",
        "Rabat", "Mauritius", "Nouakchott", "Mexico", "Palikir", "Chisinau", "Monaco", "Ulaanbaatar",
        "Podgorica", "Maputo", "Windhoek", "Nauru", "Kathmandu", "Managua", "Niamey", "Abuja", "Oslo",
        "Wellington", "Muscat", "Amsterdam", "Islamabad", "Palau", "Panama", "Papua", "Asuncion", "Lima",
        "Warsaw", "Lisbon", "London", "Bangui", "Prague", "Brazzaville", "Kinshasa", "18.4834,-69.9296",
        "Kigali", "Bucharest", "Moscow", "Apia", "Basseterre", "Belgrade", "Kingstown", "Castries",
        "0.3302,6.7333", "Dakar", "Victoria", "Freetown", "Singapore", "Damascus", "Mogadishu", "Colombo",
        "Pretoria", "Khartoum", "Juba", "Stockholm", "Bern", "Paramaribo", "Bangkok", "Dodoma", "Dushanbe",
        "Dili", "Togo", "Tonga", "Trinidad", "Tunis", "Ashgabat", "Ankara", "Tuvalu", "Kyiv", "Kampala",
        "Montevideo", "Tashkent", "Vanuatu", "Vatican", "Caracas", "Hanoi", "Sanaa", "Djibouti", "Lusaka",
        "Harare"
    );

    // Constructor principal con carga de datos climáticos
    public DatosGraficos() {
    if (!datosCargados) {
        long inicioSecuencial = System.currentTimeMillis(); // 🕒 Comienza

        for (String pais : paises) {
            ClimaInfo clima = ClimaFetcher.obtenerClima(pais);
            mapaTemperaturas.put(pais, clima.getTemperatura());
        }

        long finSecuencial = System.currentTimeMillis(); // 🕒 Termina
        System.out.println("⏱ Tiempo secuencial: " + (finSecuencial - inicioSecuencial) + " ms");

        // 🧵 Llama a la API en paralelo
        long inicioParalelo = System.currentTimeMillis(); // 🕒 Comienza

        paises.parallelStream().forEach(pais -> {
            ClimaInfo clima = ClimaFetcher.obtenerClima(pais);
            synchronized (mapaTemperaturas) {
            mapaTemperaturas.put(pais, clima.getTemperatura());
            }
        });

        long finParalelo = System.currentTimeMillis(); // 🕒 Termina
        System.out.println("⚡ Tiempo paralelo: " + (finParalelo - inicioParalelo) + " ms");


        // 🔥 Obtener los 10 más calientes
        calientes = mapaTemperaturas.entrySet()
            .stream()
            .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
            .limit(10)
            .collect(Collectors.toList());

        // ❄️ Obtener los 10 más fríos
        frios = mapaTemperaturas.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(10)
            .collect(Collectors.toList());

        datosCargados = true;
    }
}


    // Accesores para listas ordenadas
    public List<Map.Entry<String, Float>> getCalientes() {
        return calientes;
    }

    public void setCalientes(List<Map.Entry<String, Float>> calientes) {
        DatosGraficos.calientes = calientes;
    }

    public List<Map.Entry<String, Float>> getFrios() {
        return frios;
    }

    public void setFrios(List<Map.Entry<String, Float>> frios) {
        DatosGraficos.frios = frios;
    }

    // Métodos compatibles (pueden eliminarse si no los usas)
    public List<Map.Entry<String, Float>> probarPaisesCalientes() {
        return getCalientes();
    }

    public List<Map.Entry<String, Float>> probarPaisesFrios() {
        return getFrios();
    }
}
