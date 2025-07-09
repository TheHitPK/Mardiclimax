package app3d_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapaPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private BufferedImage mapa;
    private List<PaisRegion> regiones = new ArrayList<>();
    private List<PaisRegion> regionesOriginales = new ArrayList<>();
    private PaisRegion hoverRegion = null;
    private double escala = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private final double ESCALA_MIN = Math.max(1200.0 / 1200, 700.0 / 784);
    private final double ESCALA_MAX = 3.0;

    private JButton zoomInBtn, zoomOutBtn, volverBtn,muteBtn;
    private Point dragStart;

    public MapaPanel() {
        this(false);
    }

    public MapaPanel(boolean conBotonVolver) {
        try {
            mapa = ImageIO.read(new File("mapa_base.png"));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen del mapa.");
        }
        ImageIcon unmute = new ImageIcon(getClass().getResource("icons8-megáfono-40.png"));
        Image imagen16 = unmute.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon icono16 = new ImageIcon(imagen16);
        
        ImageIcon mute = new ImageIcon(getClass().getResource("megafonoSinEscuchar.png"));
        Image imagen15 = mute.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon icono15 = new ImageIcon(imagen15);
        
        JButton sonidoBtn = new JButton(icono16);     

        sonidoBtn.setBounds(200, 10, 20, 20);
        add(sonidoBtn);
        
        Sonido musica = Sonido.getInstancia();
        
        sonidoBtn.addActionListener(e -> {
        if (musica.estaSonando()) {
        sonidoBtn.setIcon(icono15);
        musica.detenerSonido(); // mute
        } else {
        sonidoBtn.setIcon(icono16);
        musica.reanudarSonido(); // unmute
        }
    });

        regionesOriginales.add(new PaisRegion("Estados Unidos", new Polygon(
            new int[]{173, 163, 187, 276, 288, 308, 300, 303, 356, 357, 293, 269, 235, 225, 217, 210, 194},
            new int[]{277, 237, 196, 210, 218, 233, 245, 250, 229, 238, 320, 301, 314, 296, 298, 286, 284},
            17), ClimaFetcher.obtenerClima("USA")));

        regionesOriginales.add(new PaisRegion("Venezuela", new Polygon(
            new int[]{320,312,315,329,331,335,340,346,343,343,350,355,354,358,349,342,330,325,321,320,316},
            new int[]{365, 374, 383, 389, 404, 410, 410, 404, 401, 397, 399, 393, 388, 381, 372, 372, 369, 366, 369, 375, 371},
            21), ClimaFetcher.obtenerClima("Venezuela")));

        setLayout(null);

        // Zoom +
        zoomInBtn = new JButton("+");
        zoomInBtn.setBounds(10, 10, 50, 30);
        zoomInBtn.addActionListener(e -> {
            if (escala < ESCALA_MAX) {
                escala *= 1.2;
                if (escala > ESCALA_MAX) escala = ESCALA_MAX;
                limitarOffset();
                repaint();
            }
        });
        add(zoomInBtn);

        // Zoom -
        zoomOutBtn = new JButton("-");
        zoomOutBtn.setBounds(70, 10, 50, 30);
        zoomOutBtn.addActionListener(e -> {
            if (escala > ESCALA_MIN) {
                escala /= 1.2;
                if (escala < ESCALA_MIN) escala = ESCALA_MIN;
                limitarOffset();
                repaint();
            }
        });
        add(zoomOutBtn);

        // Botón "Volver"
        if (conBotonVolver) {
            volverBtn = new JButton("Volver al menú");
            volverBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            volverBtn.setBounds(130, 10, 150, 30);
            volverBtn.setBackground(new Color(255, 255, 255, 220));
            volverBtn.setFocusPainted(false);
            volverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            volverBtn.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
            });

            add(volverBtn);
        }

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    private void limitarOffset() {
        int panelW = getWidth();
        int panelH = getHeight();
        int mapaW = (int)(mapa.getWidth() * escala);
        int mapaH = (int)(mapa.getHeight() * escala);

        if (offsetX < 0) offsetX = 0;
        if (offsetX > mapaW - panelW) offsetX = Math.max(mapaW - panelW, 0);

        if (offsetY < 0) offsetY = 0;
        if (offsetY > mapaH - panelH) offsetY = Math.max(mapaH - panelH, 0);
    }

    private Polygon escalarPoligono(Polygon p) {
        int[] xs = new int[p.npoints];
        int[] ys = new int[p.npoints];
        for (int i = 0; i < p.npoints; i++) {
            xs[i] = (int) (p.xpoints[i] * escala - offsetX);
            ys[i] = (int) (p.ypoints[i] * escala - offsetY);
        }
        return new Polygon(xs, ys, p.npoints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (mapa != null) {
            int w = (int)(mapa.getWidth() * escala);
            int h = (int)(mapa.getHeight() * escala);
            g2.drawImage(mapa, (int)-offsetX, (int)-offsetY, w, h, this);
        }

        regiones.clear();
        for (PaisRegion original : regionesOriginales) {
            Polygon escalado = escalarPoligono(original.getRegion());
            ClimaInfo clima = original.getClima();
            String nombre = original.getNombre();
            regiones.add(new PaisRegion(nombre, escalado, clima));

            g2.setColor(new Color(0, 100, 255, 80));
            g2.fillPolygon(escalado);
            g2.setColor(new Color(0, 100, 255));
            g2.drawPolygon(escalado);
        }

        if (hoverRegion != null) {
            Polygon r = hoverRegion.getRegion();
            Rectangle bounds = r.getBounds();
            int tooltipX = bounds.x + bounds.width / 2 - 110;
            int tooltipY = bounds.y - 95;
            if (tooltipY < 10) tooltipY = bounds.y + bounds.height + 10;

            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(tooltipX, tooltipY, 220, 85, 10, 10);

            g2.setColor(Color.WHITE);
            g2.drawString("País: " + hoverRegion.getNombre(), tooltipX + 10, tooltipY + 20);
            g2.drawString("Clima: " + hoverRegion.getClima().getDescripcion(), tooltipX + 10, tooltipY + 35);
            g2.drawString("Temperatura: " + hoverRegion.getClima().getTemperatura() + " °C", tooltipX + 10, tooltipY + 50);
            g2.drawString("Precipitación: " + hoverRegion.getClima().getPrecipitacion() + " mm", tooltipX + 10, tooltipY + 65);
        }

        // Husos horarios
        g2.setColor(new Color(255, 0, 0, 100));
        double gradosPorHuso = 15.0;
        double pixelesPorGrado = (mapa.getWidth() * escala) / 360.0;
        double centroX = (mapa.getWidth() * escala) / 2.0;

        for (int huso = -12; huso <= 12; huso++) {
            int x = (int)(centroX + huso * gradosPorHuso * pixelesPorGrado - offsetX);
            g2.drawLine(x, 0, x, getHeight());
        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        Optional<PaisRegion> match = regiones.stream()
            .filter(r -> r.getRegion().contains(e.getPoint()))
            .findFirst();
        hoverRegion = match.orElse(null);
        repaint();
    }

    @Override public void mousePressed(MouseEvent e) { dragStart = e.getPoint(); }

    @Override public void mouseDragged(MouseEvent e) {
        if (dragStart != null && escala > ESCALA_MIN) {
            int dx = e.getX() - dragStart.x;
            int dy = e.getY() - dragStart.y;
            offsetX -= dx;
            offsetY -= dy;
            limitarOffset();
            dragStart = e.getPoint();
            repaint();
        }
    }

    @Override public void mouseReleased(MouseEvent e) { dragStart = null; }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) { hoverRegion = null; repaint(); }

    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0 && escala < ESCALA_MAX) {
            escala *= 1.1;
            if (escala > ESCALA_MAX) escala = ESCALA_MAX;
        } else if (notches > 0 && escala > ESCALA_MIN) {
            escala /= 1.1;
            if (escala < ESCALA_MIN) escala = ESCALA_MIN;
        }
        limitarOffset();
        repaint();
    }
}
