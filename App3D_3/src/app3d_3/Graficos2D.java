
package app3d_3;


import javax.swing.*;
import java.awt.*;

public class Graficos2D extends JPanel {

    // Datos de ejemplo
    private int[] valores = {50, 120, 80, 100, 60, 90, 110, 70, 130, 85};
    private String[] etiquetas = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct"};


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
       

        int anchoBarra = 40;
        int espacio = 20;
        int totalAncho = valores.length * anchoBarra + (valores.length - 1) * espacio;
        int x = (getWidth() - totalAncho) / 2;      
        int baseY = getHeight() - 50;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        for (int i = 0; i < valores.length; i++) {
            int altura = valores[i];

            // Dibujar barra
            g2.setColor(Color.BLUE);
            g2.fillRect(x, baseY - altura, anchoBarra, altura);

            // Dibujar etiqueta
            g2.setColor(Color.BLACK);
            g2.drawString(etiquetas[i], x + 5, baseY + 15);

            x += anchoBarra + espacio;
        }
    }

}
