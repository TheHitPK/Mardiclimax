package app3d_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class TituloAnimadoPanel extends JPanel {
    private float escala = 0.0f;
    private float alpha = 0.0f;
    private final Timer timer;
    private Font fuentePersonalizada;

    public TituloAnimadoPanel() {
        setOpaque(false);
        cargarFuentePersonalizada();

        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escala += 0.02f;
                alpha += 0.02f;

                if (escala >= 1.0f) escala = 1.0f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    timer.stop();
                }
                repaint();
            }
        });
        timer.start();
    }

    private void cargarFuentePersonalizada() {
        try {
            fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("Graduate-Regular.ttf")).deriveFont(Font.BOLD, 95f); // ← más pequeño
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuentePersonalizada);
        } catch (FontFormatException | IOException e) {
            fuentePersonalizada = new Font("Segoe UI", Font.BOLD, 90);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 180);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String texto = "VENTARRÓN";  // ← nuevo título

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Font fuente = fuentePersonalizada.deriveFont(95f * escala); // ← escala también ajustada
        g2.setFont(fuente);

        FontMetrics fm = g2.getFontMetrics();
        int textoAncho = fm.stringWidth(texto);
        int textoAlto = fm.getHeight();

        int x = (getWidth() - textoAncho) / 2;
        int y = (getHeight() + textoAlto) / 2 + 10;

        // Sombra azul muy oscura
        for (int i = 6; i > 0; i--) {
            g2.setColor(new Color(0, 30, 90, 25));
            g2.drawString(texto, x + i, y + i);
        }

        // Gradiente azul oscuro → azul grisáceo
        GradientPaint grad = new GradientPaint(
            x, y - textoAlto, new Color(0, 70, 130),
            x + textoAncho, y, new Color(0, 100, 150)
        );
        g2.setPaint(grad);
        g2.drawString(texto, x, y);

        // Brillo blanco leve
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(2f));
        g2.drawString(texto, x, y);
    }
}
