package app3d_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Menú Principal");
        setSize(900, 500);
        setResizable(false);
        setLocationRelativeTo(null);
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        
        ImageIcon unmute = new ImageIcon(getClass().getResource("icons8-megáfono-40.png"));
        Image imagen16 = unmute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono16 = new ImageIcon(imagen16);
        
        ImageIcon mute = new ImageIcon(getClass().getResource("megafonoSinEscuchar.png"));
        Image imagen15 = mute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono15 = new ImageIcon(imagen15);
        
        JButton sonidoBtn = new JButton(icono16);     

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("fondonuevo1.jpg")));
        fondo.setLayout(null);
        
        sonidoBtn.setBounds(100, 5, 24, 24);
        fondo.add(sonidoBtn);
        
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

        // Título animado
        TituloAnimadoPanel animado = new TituloAnimadoPanel();
        animado.setBounds(0, 20, 900, 180); // Aumentado para evitar recorte
        fondo.add(animado);

        // Panel de botones principales
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBounds(0, 310, 900, 70);

        JButton btnEstadisticas = crearBotonModerno("Estadísticas");
        JButton btnMapa = crearBotonModerno("Mapamundi");
        JButton btnCerrarSesion = crearBotonModerno("Cerrar Sesión");

        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnMapa);
        panelBotones.add(btnCerrarSesion);
        fondo.add(panelBotones);

        // Botón "Info" tipo menú contextual
        JButton btnInfo = new JButton("Info");
        btnInfo.setBounds(10, 5, 70, 25);
        btnInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnInfo.setFocusPainted(false);
        btnInfo.setBackground(new Color(240, 240, 240));
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Menú emergente
        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemVersion = new JMenuItem("Versión");
        itemVersion.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Versión: Fase beta", "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem itemDesarrolladores = new JMenuItem("Desarrolladores");
        itemDesarrolladores.addActionListener(e -> {
            String desarrolladores = "Armando Paris. armandoparis222@gmail.com"
                    + "\nEduardo Chirinos. eduardoortigoza86@gmail.com"
                    + "\nManuel Ocando. manuelocandofaria@gmail.com"
                    + "\nPablo Quintero. pablot1804@gmail.com"
                    + "\nRicardo Iriarte. ririarte@gmail.com                                 ";
            JOptionPane.showMessageDialog(this, desarrolladores, "Desarrolladores", JOptionPane.INFORMATION_MESSAGE);
        });

        popup.add(itemVersion);
        popup.add(itemDesarrolladores);

        btnInfo.addActionListener(e -> {
            popup.show(btnInfo, 0, btnInfo.getHeight());
        });

        fondo.add(btnInfo); // Agregamos el botón al fondo

        setContentPane(fondo);

        // Acciones de botones principales
        btnMapa.addActionListener(e -> {
            dispose();
            JFrame frame = new JFrame("Mapa Climático 2D");
            frame.setSize(1200, 784);
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(new MapaPanel(true));
            frame.setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            JFrame loginFrame = new JFrame("Planeta Tierra 3D - Login");
            loginFrame.setContentPane(new App3D_3(loginFrame));
            loginFrame.setSize(900, 500);
            loginFrame.setResizable(false);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setVisible(true);
        });

        btnEstadisticas.addActionListener(e -> {
    dispose();
    JFrame frame = new JFrame("Gráfico de Barras con Java 2D");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 500);
    frame.setUndecorated(true);            
    frame.setLocationRelativeTo(null);  

    // ---------- Fondo con imagen JPG ----------
    // Asegúrate que "fondo.jpg" esté en el mismo paquete o en src/
    ImageIcon fondoIcon = new ImageIcon(getClass().getResource("stats.jpg"));
    JLabel background = new JLabel(fondoIcon);
    background.setLayout(new BorderLayout()); // o null si quieres coordenadas absolutas
    frame.setContentPane(background); // Establecer el JLabel como fondo
    // ------------------------------------------

    Graficos2D panel = new Graficos2D();
    panel.setOpaque(false); // Para que no tape la imagen de fondo

    JButton paisesCalientes = new JButton("Capitales mas Calientes");
    JButton paisesFrios = new JButton("Capitales mas Frios");

    panel.setPreferredSize(new Dimension(800, 500)); // espacio suficiente horizontal
    panel.add(paisesCalientes);
    panel.add(paisesFrios);

    JButton volverBtn = new JButton("Volver al menú");

    volverBtn.addActionListener(volver -> {
        SwingUtilities.getWindowAncestor(panel).dispose();
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    });

    panel.add(volverBtn);

    frame.add(panel, BorderLayout.SOUTH); // lo colocamos en la parte inferior del fondo
    frame.setVisible(true);

    paisesCalientes.addActionListener(press -> panel.dibujoRepaintCaliente());
    paisesFrios.addActionListener(press -> panel.dibujoRepaintFrio());

    //agregar boton de estadisticas de caliente

    // aquí usamos scroll automático

    // aquí usamos scroll automático

});
    }

    // Método para crear botones estilizados
    private JButton crearBotonModerno(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setForeground(Color.DARK_GRAY);
        boton.setBackground(new Color(255, 255, 255, 230));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(220, 240, 255));
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(255, 255, 255, 230));
            }
        });

        return boton;
    }
}
