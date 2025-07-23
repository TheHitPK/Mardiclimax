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
        Image imagen16 = unmute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono16 = new ImageIcon(imagen16);
        
        ImageIcon mute = new ImageIcon(getClass().getResource("megafonoSinEscuchar.png"));
        Image imagen15 = mute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono15 = new ImageIcon(imagen15);
        
        JButton sonidoBtn = new JButton(icono16);     

        sonidoBtn.setBounds(300, 10, 24, 24);
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
            new int[]{375, 384, 393, 399, 414, 420, 420, 414, 411, 407, 409, 403, 398, 391, 382, 382, 379, 376, 379, 385, 381},
            21), ClimaFetcher.obtenerClima("Venezuela")));
        
        regionesOriginales.add(new PaisRegion("Surinam", new Polygon(
new int[]{369,366,370,370,377,380,375,372},
new int[]{405,410,415,421,418,406,405,407},
8),
ClimaFetcher.obtenerClima("Surinam")));
        
        regionesOriginales.add(new PaisRegion("Brasil", new Polygon(
new int[]{355,351,343,344,348,341,336,330,325,324,327,323,312,310,317,322,327,340,344,360,361,368,370,369,371,379,385,388,380,396,405,405,428,449,450,393,391,384,378,362,356},
new int[]{410,415,412,417,419,427,426,421,422,430,433,444,449,460,465,462,470,464,475,480,489,489,497,505,512,513,520,530,544,553,540,525,517,465,446,423,410,421,419,424,417},
41 ),
ClimaFetcher.obtenerClima("Brasil")));
        regionesOriginales.add(new PaisRegion("Panama", new Polygon(
new int[]{278,277,287,288,296,299,290,284},
new int[]{389,397,399,395,396,391,387,392},
8),
ClimaFetcher.obtenerClima("Panama")));
        
        regionesOriginales.add(new PaisRegion("Mexico", new Polygon(
new int[]{172,176,190,214,244,249,249,263,270,266,252,243,235,235,229,225,220,215,212,193,183},
new int[]{285,309,329,360,368,361,356,353,342,338,340,345,335,321,316,301,304,302,293,291,283},
21),
ClimaFetcher.obtenerClima("Mexico")));
        regionesOriginales.add(new PaisRegion("Honduras", new Polygon(
new int[]{258,262,274,277,271,264,261,261},
new int[]{369,364,363,367,368,378,377,372},
8),
ClimaFetcher.obtenerClima("Honduras")));
        regionesOriginales.add(new PaisRegion("Nicaragua", new Polygon(
new int[]{277,271,264,278,275,266},
new int[]{367,368,378,376,384,383},
6),
ClimaFetcher.obtenerClima("Nicaragua")));
        regionesOriginales.add(new PaisRegion("Niger ", new Polygon(
new int[]{591, 587, 586, 574, 581, 585, 588, 593, 597, 610, 621, 621, 627, 630
},
new int[]{357, 357, 369, 371, 382, 381, 377, 376, 380, 380, 373, 369, 363, 350},14),
ClimaFetcher.obtenerClima("Niger")));
        regionesOriginales.add(new PaisRegion("Groelandia", new Polygon(
new int[]{404,426,527,558,476},
new int[]{54,189,128,54,25},
5),
ClimaFetcher.obtenerClima("greenland")));
        regionesOriginales.add(new PaisRegion("nigeria", new Polygon(
new int[]{582,587,595,603,609,614,623,618,609,600,592,584,587},
new int[]{403,403,410,409,401,401,379,375,379,380,376,381,391},13),
ClimaFetcher.obtenerClima("nigeria")));
        regionesOriginales.add(new PaisRegion("Belice", new Polygon(
new int[]{258,258,260,263},
new int[]{356,361,360,354},
4),
ClimaFetcher.obtenerClima("Belize")));
        
        regionesOriginales.add(new PaisRegion("Ecuador", new Polygon(
                new int[]{290,284,283,285,285,291,302,301,296},
                new int[]{423,427,435,438,444,447,435,429,428},
                9),
                ClimaFetcher.obtenerClima("Ecuador")));
        regionesOriginales.add(new PaisRegion("Peru", new Polygon(
    new int[]{281,281,302,324,328,329,321,317,309,311,318,320,311,305,294,284},
    new int[]{442,448,486,500,494,478,463,466,459,449,444,440,438,430,446,445},
    16),
    ClimaFetcher.obtenerClima("Peru")));

regionesOriginales.add(new PaisRegion("Paraguay", new Polygon(
    new int[]{358,355,360,367,373,371,380,384,384,379,372,371,368},
    new int[]{504,515,520,521,525,534,534,527,520,513,513,506,503},
    13),
    ClimaFetcher.obtenerClima("Paraguay")));

regionesOriginales.add(new PaisRegion("Bolivia", new Polygon(
    new int[]{326,330,329,328,335,338,342,354,358,368,369,367,361,360,343,340,334},
    new int[]{470,480,495,500,517,518,514,514,504,503,496,489,489,480,475,466,466},
    17),
    ClimaFetcher.obtenerClima("Bolivia")));

regionesOriginales.add(new PaisRegion("Uruguay", new Polygon(
    new int[]{379,376,377,385,396},
    new int[]{545,548,563,565,555},
    5),
    ClimaFetcher.obtenerClima("Uruguay")));

regionesOriginales.add(new PaisRegion("Argentina", new Polygon(
    new int[]{340,335,331,337,335,343,346,350,357,364,383,377,375,390,385,380,372,373,365,360,353,343},
    new int[]{520,526,549,565,579,615,640,646,646,623,571,564,547,530,527,534,534,526,520,520,515,514},
    22),
    ClimaFetcher.obtenerClima("Argentina")));

regionesOriginales.add(new PaisRegion("Guyana", new Polygon(
    new int[]{359,355,356,356,360,368,369,365,369},
    new int[]{396,403,409,418,425,422,416,410,404},
    9),
    ClimaFetcher.obtenerClima("Guyana")));
regionesOriginales.add(new PaisRegion("tunez ", new Polygon(
new int[]{599,601,600,604,603,608,608,613,608,607},
new int[]{286,295,300,305,311,311,307,301,298,284},10),
ClimaFetcher.obtenerClima("tunez")));


regiones.add(new PaisRegion("libia", new Polygon(
new int[]{614,609,607,605,607,612,615,626,630,660,657,649,642,638,640,634,623},
new int[]{301,307,312,331,335,335,338,341,340,353,307,302,302,305,310,312,304},17),
ClimaFetcher.obtenerClima("libia")));









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
