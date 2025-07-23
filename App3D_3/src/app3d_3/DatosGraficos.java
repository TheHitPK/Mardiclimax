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

    private static final Map<String, String> coordenadasPaises;

static {
    Map<String, String> mapa = new HashMap<>();
    mapa.put("Afganistán", "34.28,69.11");
    mapa.put("Albania", "41.18,19.49");
    mapa.put("Alemania", "52.52,13.405");
    mapa.put("Andorra", "42.31,1.32");
    mapa.put("Angola", "-8.83,13.15");
    mapa.put("Antigua y Barbuda", "17.127,-61.846");
    mapa.put("Arabia", "24.71,46.67");
    mapa.put("Argelia", "36.75,3.04");
    mapa.put("Argentina", "-34.6037,-58.3816");
    mapa.put("Armenia", "40.18,44.31");
    mapa.put("Australia", "-35.28,149.13");
    mapa.put("Austria", "48.20,16.37");
    mapa.put("Azerbaiyán", "40.38,49.89");
    mapa.put("Bahamas", "25.05,-77.35");
    mapa.put("Bangladés", "23.81,90.41");
    mapa.put("Barbados", "13.10,-59.61");
    mapa.put("Baréin", "26.23,50.58");
    mapa.put("Bélgica", "50.85,4.35");
    mapa.put("Belice", "17.25,-88.77");
    mapa.put("Benín", "6.50,2.60");
    mapa.put("Bielorrusia", "53.90,27.57");
    mapa.put("Birmania", "19.75,96.10");
    mapa.put("Bolivia", "-16.50,-68.15");
    mapa.put("Bosnia y Herzegovina", "43.85,18.36");
    mapa.put("Botsuana", "-24.65,25.91");
    mapa.put("Brasil", "-15.78,-47.93");
    mapa.put("Brunéi", "4.89,114.94");
    mapa.put("Bulgaria", "42.70,23.32");
    mapa.put("Burkina Faso", "12.37,-1.52");
    mapa.put("Burundi", "-3.38,29.36");
    mapa.put("Cabo Verde", "14.92,-23.51");
    mapa.put("Camboya", "11.55,104.92");
    mapa.put("Camerún", "3.87,11.52");
    mapa.put("Canadá", "45.42,-75.69");
    mapa.put("Catar", "25.28,51.53");
    mapa.put("Chad", "12.11,15.04");
    mapa.put("Chile", "-33.45,-70.66");
    mapa.put("China", "39.90,116.40");
    mapa.put("Chipre", "35.17,33.36");
    mapa.put("Colombia", "4.61,-74.08");
    mapa.put("Comoras", "-11.69,43.26");
    mapa.put("Corea del Norte", "39.03,125.75");
    mapa.put("Corea del Sur", "37.55,126.98");
    mapa.put("Costa de Marfil", "6.82,-5.28");
    mapa.put("Costa Rica", "9.93,-84.08");
    mapa.put("Croacia", "45.81,15.98");
    mapa.put("Cuba", "23.13,-82.36");
    mapa.put("Dinamarca", "55.67,12.57");
    mapa.put("Dominica", "15.30,-61.38");
    mapa.put("República Dominicana", "18.48,-69.93");
    mapa.put("Ecuador", "-0.23,-78.52");
    mapa.put("Egipto", "30.04,31.24");
    mapa.put("El Salvador", "13.69,-89.19");
    mapa.put("Emiratos Árabes Unidos", "24.47,54.37");
    mapa.put("Eritrea", "15.33,38.93");
    mapa.put("Eslovaquia", "48.15,17.11");
    mapa.put("Eslovenia", "46.05,14.51");
    mapa.put("España", "40.4168,-3.7038");
    mapa.put("Estados Unidos", "38.90,-77.04");
    mapa.put("Estonia", "59.43,24.75");
    mapa.put("Esuatini", "-26.32,31.13");
    mapa.put("Etiopía", "9.03,38.74");
    mapa.put("Fiji", "-18.14,178.44");
    mapa.put("Filipinas", "14.60,120.98");
    mapa.put("Finlandia", "60.17,24.94");
    mapa.put("Francia", "48.8566,2.3522");
    mapa.put("Gabón", "0.39,9.45");
    mapa.put("Gambia", "13.45,-16.58");
    mapa.put("Georgia", "41.71,44.79");
    mapa.put("Ghana", "5.56,-0.20");
    mapa.put("Grecia", "37.98,23.73");
    mapa.put("Guatemala", "14.63,-90.52");
    mapa.put("Guinea", "9.54,-13.68");
    mapa.put("Guinea-Bisáu", "11.86,-15.59");
    mapa.put("Guyana", "6.80,-58.16");
    mapa.put("Haití", "18.54,-72.34");
    mapa.put("Honduras", "14.08,-87.21");
    mapa.put("Hungría", "47.50,19.04");
    mapa.put("India", "28.61,77.20");
    mapa.put("Indonesia", "-6.20,106.85");
    mapa.put("Irak", "33.31,44.36");
    mapa.put("Irán", "35.69,51.42");
    mapa.put("Irlanda", "53.35,-6.26");
    mapa.put("Islandia", "64.13,-21.90");
    mapa.put("Israel", "31.77,35.21");
    mapa.put("Italia", "41.90,12.49");
    mapa.put("Jamaica", "18.01,-76.79");
    mapa.put("Japón", "35.6895,139.6917");
    mapa.put("Jordania", "31.95,35.93");
    mapa.put("Kazajistán", "51.16,71.43");
    mapa.put("Kenia", "-1.29,36.82");
    mapa.put("Kirguistán", "42.87,74.59");
    mapa.put("Kiribati", "1.33,173.00");
    mapa.put("Kuwait", "29.37,47.98");
    mapa.put("Laos", "17.97,102.61");
    mapa.put("Letonia", "56.95,24.11");
    mapa.put("Líbano", "33.89,35.50");
    mapa.put("Liberia", "6.31,-10.80");
    mapa.put("Libia", "32.89,13.19");
    mapa.put("Liechtenstein", "47.14,9.52");
    mapa.put("Lituania", "54.69,25.28");
    mapa.put("Luxemburgo", "49.61,6.13");
    mapa.put("Madagascar", "-18.91,47.54");
    mapa.put("Malasia", "3.14,101.69");
    mapa.put("Malaui", "-13.96,33.78");
    mapa.put("Maldivas", "4.17,73.51");
    mapa.put("Malí", "12.65,-8.00");
    mapa.put("Malta", "35.89,14.51");
    mapa.put("Marruecos", "34.02,-6.84");
    mapa.put("Mauricio", "-20.16,57.50");
    mapa.put("Mauritania", "18.08,-15.98");
    mapa.put("México", "19.43,-99.13");
    mapa.put("Micronesia", "6.92,158.18");
    mapa.put("Moldavia", "47.01,28.85");
    mapa.put("Mónaco", "43.73,7.42");
    mapa.put("Mongolia", "47.92,106.92");
    mapa.put("Montenegro", "42.43,19.26");
    mapa.put("Mozambique", "-25.96,32.58");
    mapa.put("Namibia", "-22.56,17.08");
    mapa.put("Nauru", "-0.53,166.92");
    mapa.put("Nepal", "27.70,85.32");
    mapa.put("Nicaragua", "12.13,-86.25");
    mapa.put("Níger", "13.52,2.11");
    mapa.put("Nigeria", "9.07,7.48");
    mapa.put("Noruega", "59.91,10.75");
    mapa.put("NewZealand", "-41.29,174.78");
    mapa.put("Omán", "23.60,58.54");
    mapa.put("Países Bajos", "52.37,4.89");
    mapa.put("Pakistán", "33.68,73.05");
    mapa.put("Palaos", "7.50,134.62");
    mapa.put("Panamá", "8.98,-79.52");
    mapa.put("Papúa Nueva Guinea", "-9.44,147.18");
    mapa.put("Paraguay", "-25.30,-57.58");
    mapa.put("Perú", "-12.04,-77.03");
    mapa.put("Polonia", "52.23,21.01");
    mapa.put("Portugal", "38.72,-9.14");
    mapa.put("Reino Unido", "51.51,-0.13");
    mapa.put("República Centroafricana", "4.37,18.58");
    mapa.put("República Checa", "50.08,14.43");
    mapa.put("República del Congo", "-4.27,15.29");
    mapa.put("República Democrática del Congo", "-4.32,15.31");
    mapa.put("República Dominicana", "18.48,-69.93");
    mapa.put("Ruanda", "-1.95,30.06");
    mapa.put("Rumanía", "44.43,26.10");
    mapa.put("Rusia", "55.75,37.62");
    mapa.put("Samoa", "-13.83,-171.75");
    mapa.put("San Cristóbal y Nieves", "17.30,-62.72");
    mapa.put("San Marino", "43.93,12.45");
    mapa.put("San Vicente y las Granadinas", "13.16,-61.23");
    mapa.put("Santa Lucía", "14.01,-60.99");
    mapa.put("Santo Tomé y Príncipe", "0.33,6.73");
    mapa.put("Senegal", "14.69,-17.44");
    mapa.put("Serbia", "44.82,20.46");
    mapa.put("Seychelles", "-4.62,55.45");
    mapa.put("Sierra Leona", "8.48,-13.23");
    mapa.put("Singapur", "1.29,103.85");
    mapa.put("Siria", "33.51,36.29");
    mapa.put("Somalia", "2.04,45.34");
    mapa.put("Sri Lanka", "6.93,79.85");
    mapa.put("Sudáfrica", "-25.74,28.19");
    mapa.put("Sudán", "15.50,32.56");
    mapa.put("South Sudan", "4.85,31.58");
    mapa.put("Suecia", "59.33,18.07");
    mapa.put("Suiza", "46.95,7.45");
    mapa.put("Surinam", "5.83,-55.17");
    mapa.put("Tailandia", "13.75,100.50");
    mapa.put("Tanzania", "-6.16,35.75");
    mapa.put("Tayikis", "38.56,68.78");
    mapa.put("Timor", "-8.56,125.57");
    mapa.put("Togo", "6.13,1.22");
    mapa.put("Tonga", "-21.14,-175.20");
    mapa.put("Trinidad", "10.66,-61.52");
    mapa.put("Túnez", "36.80,10.18");
    mapa.put("Turkmenis", "37.95,58.38");
    mapa.put("Turquía", "39.93,32.86");
    mapa.put("Tuvalu", "-8.52,179.20");
    mapa.put("Ucrania", "50.45,30.52");
    mapa.put("Uganda", "0.31,32.58");
    mapa.put("Uruguay", "-34.90,-56.19");
    mapa.put("Uzbekis", "41.31,69.24");
    mapa.put("Vanuatu", "-17.73,168.32");
    mapa.put("Vaticano", "41.90,12.45");
    mapa.put("Venezuela", "10.48,-66.90");
    mapa.put("Vietnam", "21.03,105.85");
    mapa.put("Yemen", "15.35,44.20");
    mapa.put("Yibuti", "11.59,43.15");
    mapa.put("Zambia", "-15.41,28.28");
    mapa.put("Zimbabue", "-17.83,31.05");

    coordenadasPaises = Collections.unmodifiableMap(mapa);
}


    // Constructor principal con carga de datos climáticos
    public DatosGraficos() {
    if (!datosCargados) {
        long inicioParalelo = System.currentTimeMillis();

        coordenadasPaises.entrySet().parallelStream().forEach(entry -> {
            ClimaInfo clima = ClimaFetcher.obtenerClima(entry.getValue());
            synchronized (mapaTemperaturas) {
                mapaTemperaturas.put(entry.getKey(), clima.getTemperatura());
            }
        });

        long finParalelo = System.currentTimeMillis();
        System.out.println("⚡ Tiempo paralelo: " + (finParalelo - inicioParalelo) + " ms");

        calientes = mapaTemperaturas.entrySet()
            .stream()
            .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
            .limit(10)
            .collect(Collectors.toList());

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
