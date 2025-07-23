package app3d_3;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Graficos2D extends JPanel {    
    public static DatosGraficos paisesTemp = new DatosGraficos();
    private int mostrarCalientes = 0; // Toggle para mostrar temperaturas calientes o frías
    

    public void dibujoRepaintCaliente() {
        mostrarCalientes = 1;
        repaint(); // Solicita redibujado
    }
    
    public void dibujoRepaintFrio() {
        mostrarCalientes = 2;
        repaint(); // Solicita redibujado
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        
        

        int anchoBarra = 40;
        int espacio = 30;
        int margenIzquierdo = 60;
        int margenDerecho = 60;

        // ⚙️ Escalado visual para hacer barras más grandes
        int escalaY = 5;

        // Límites de temperatura
        int maxTemp = 50;
        int minTemp = -10;
        int pasoTemp = 10;

        // Eje base para 0 °C
        int baseY = getHeight() / 2 + 100;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setStroke(new BasicStroke(1f));

        
        
        // 🔍 Lógica para decidir qué dibujar
        if (mostrarCalientes==1) {
            // 📏 Dibujar líneas de referencia y etiquetas de temperatura
        for (int temp = minTemp; temp <= maxTemp; temp += pasoTemp) {
            int y = baseY - (temp * escalaY); // Aplicando escala

            g2.setColor(Color.white);
            g2.drawLine(margenIzquierdo, y, getWidth() - margenDerecho, y);

            g2.setColor(temp < 0 ? Color.RED : Color.white);
            g2.drawString(temp + "°C", 20, y + 5);
        }
            dibujarCalientes(anchoBarra, espacio, g2, paisesTemp, escalaY, baseY);
            
        } else if(mostrarCalientes==2) {
            // 📏 Dibujar líneas de referencia y etiquetas de temperatura
        for (int temp = minTemp; temp <= maxTemp; temp += pasoTemp) {
            int y = baseY - (temp * escalaY); // Aplicando escala

            g2.setColor(Color.white);
            g2.drawLine(margenIzquierdo, y, getWidth() - margenDerecho, y);

            g2.setColor(temp < 0 ? Color.RED : Color.white);
            g2.drawString(temp + "°C", 20, y + 5);
        }
            dibujarFrios(anchoBarra, espacio, g2, paisesTemp, escalaY, baseY);
        }
        
             
    }
    
    public void dibujarFrios(int anchoBarra, int espacio,Graphics2D g2,DatosGraficos paisesTemp, int escalaY,int baseY){ // 📊 Dibujar barras de temperaturas por ciudad
        paisesTemp.probarPaisesFrios();
        int totalBarras = paisesTemp.getFrios().size();
        int totalAncho = totalBarras * anchoBarra + (totalBarras - 1) * espacio;
        int x = (getWidth() - totalAncho) / 2;

        for (Map.Entry<String, Float> entry : paisesTemp.getFrios()) {
            String ciudad = entry.getKey();
            float temperatura = entry.getValue();

            int alturaVisual = (int) (temperatura * escalaY);

            if (temperatura >= 0) {
                g2.setColor(Color.BLUE);
                g2.fillRect(x, baseY - alturaVisual, anchoBarra, alturaVisual);
            } else {
                g2.setColor(Color.RED);
                g2.fillRect(x, baseY, anchoBarra, -alturaVisual);
            }

            g2.setColor(Color.white);
            g2.drawString(ciudad, x + 3, baseY + 25); // Etiqueta ciudad

            x += anchoBarra + espacio;
        }
    }
    
    public void dibujarCalientes(int anchoBarra, int espacio,Graphics2D g2,DatosGraficos paisesTemp, int escalaY,int baseY){ // 📊 Dibujar barras de temperaturas por ciudad
        paisesTemp.probarPaisesCalientes();
        int totalBarras = paisesTemp.getCalientes().size();
        int totalAncho = totalBarras * anchoBarra + (totalBarras - 1) * espacio;
        int x = (getWidth() - totalAncho) / 2;

        for (Map.Entry<String, Float> entry : paisesTemp.getCalientes()) {
            String ciudad = entry.getKey();
            float temperatura = entry.getValue();

            int alturaVisual = (int) (temperatura * escalaY);

            if (temperatura >= 40) {
                g2.setColor(Color.RED);
                g2.fillRect(x, baseY - alturaVisual, anchoBarra, alturaVisual);
            } else if(temperatura >=0 ) {
                g2.setColor(Color.YELLOW);
                g2.fillRect(x, baseY, anchoBarra, -alturaVisual);
            } else{
                g2.setColor(Color.cyan);
                g2.fillRect(x, baseY, anchoBarra, -alturaVisual);
            }

            g2.setColor(Color.white);
            g2.drawString(ciudad, x + 3, baseY + 20); // Etiqueta ciudad

            x += anchoBarra + espacio;
        }
    }
}
