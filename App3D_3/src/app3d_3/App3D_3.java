// App3D_3.java
package app3d_3;

import java.awt.*;
import java.sql.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;


public class App3D_3 extends JPanel {
    public App3D_3(JFrame parentFrame) {
        InicializadorBD.inicializar();
        setLayout(new BorderLayout());

        Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas3D.setPreferredSize(new Dimension(500, 500));

        JPanel panel3D = new JPanel(new BorderLayout());
        panel3D.setBackground(new Color(5, 10, 30));
        panel3D.add(canvas3D, BorderLayout.CENTER);

        JPanel panelLogin = new LoginPanel(parentFrame);

        add(panel3D, BorderLayout.WEST);
        add(panelLogin, BorderLayout.CENTER);

        SimpleUniverse universo = new SimpleUniverse(canvas3D);
        universo.getViewingPlatform().setNominalViewingTransform();
        BranchGroup escena = crearEscena();
        escena.compile();
        universo.addBranchGraph(escena);
    }

    private BranchGroup crearEscena() {
        BranchGroup root = new BranchGroup();
        BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        TextureLoader starLoader = new TextureLoader(getClass().getResource("stars.jpg"), null);
        Background fondo = new Background(starLoader.getImage());
        fondo.setApplicationBounds(limites);
        root.addChild(fondo);

        TransformGroup rotador = new TransformGroup();
        rotador.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(rotador);

        Appearance apariencia = new Appearance();
        TextureLoader loader = new TextureLoader(getClass().getResource("earth.jpg"), null);
        Texture textura = loader.getTexture();
        apariencia.setTexture(textura);

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);

        Material material = new Material();
        material.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
        apariencia.setMaterial(material);

        Sphere planeta = new Sphere(0.45f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS, 64, apariencia);
        rotador.addChild(planeta);

        Alpha alpha = new Alpha(-1, 10000);
        RotationInterpolator rotacion = new RotationInterpolator(alpha, rotador);
        rotacion.setSchedulingBounds(limites);
        root.addChild(rotacion);

        DirectionalLight luz = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
        luz.setInfluencingBounds(limites);
        root.addChild(luz);

        return root;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Planeta Tierra 3D - Login");
            App3D_3 contenido = new App3D_3(ventana);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setContentPane(contenido);
            ventana.setSize(900, 500);
            ventana.setResizable(false);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            
        });
    }
}

// ConexionBD.java
class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/clima";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

// LoginPanel.java
class LoginPanel extends JPanel {
    private final JFrame parentFrame;

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setBackground(new Color(5, 10, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 20);

        JLabel titulo = new JLabel("Inicio de Sesión");
        titulo.setFont(fuenteTitulo);
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        JLabel usuarioLbl = new JLabel("Usuario:");
        usuarioLbl.setForeground(Color.WHITE);
        usuarioLbl.setFont(fuente);
        JTextField usuarioTxt = new JTextField(15);

        JLabel passLbl = new JLabel("Contraseña:");
        passLbl.setForeground(Color.WHITE);
        passLbl.setFont(fuente);
        JPasswordField passTxt = new JPasswordField(15);

        JButton loginBtn = new JButton("Iniciar Sesión");
        JButton registroBtn = new JButton("Registrarse");

        loginBtn.setFont(fuente);
        registroBtn.setFont(fuente);
        
        ImageIcon unmute = new ImageIcon(getClass().getResource("icons8-megáfono-40.png"));
        Image imagen16 = unmute.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon icono16 = new ImageIcon(imagen16);
        
        ImageIcon mute = new ImageIcon(getClass().getResource("megafonoSinEscuchar.png"));
        Image imagen15 = mute.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon icono15 = new ImageIcon(imagen15);
        
        JButton sonidoBtn = new JButton(icono16);
        add(sonidoBtn);

        
          
        
        sonidoBtn.setBounds(10, 10, 20, 20);
        
        Sonido musica= Sonido.getInstancia();
        musica.reproducirSonido();


    sonidoBtn.addActionListener(e -> {
        if (musica.estaSonando()) {
        sonidoBtn.setIcon(icono15);
        musica.detenerSonido(); // mute
        } else {
        sonidoBtn.setIcon(icono16);
        musica.reanudarSonido(); // unmute
        }
    });

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; add(usuarioLbl, gbc);
        gbc.gridx = 1; add(usuarioTxt, gbc);
        gbc.gridy = 2; gbc.gridx = 0; add(passLbl, gbc);
        gbc.gridx = 1; add(passTxt, gbc);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2; add(loginBtn, gbc);
        gbc.gridy = 4; add(registroBtn, gbc);

loginBtn.addActionListener(e -> {
    String user = usuarioTxt.getText().trim();
    String pass = new String(passTxt.getPassword()).trim();

    if (user.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, rellena los campos.");
        return;
    }

    try (Connection conn = ConexionBD.conectar()) {
        String sql = "SELECT nombre FROM usuario WHERE nickname = ? AND contrasena = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user);
        stmt.setString(2, pass);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String nombre = rs.getString("nombre");
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + nombre + "!");

            // Simula carga con Timer y abre el menú principal
            Timer timer = new Timer(2000, evt -> {
                parentFrame.dispose();
                SwingUtilities.invokeLater(() -> {
                    new MenuPrincipal().setVisible(true);
                });
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error en la conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});


        registroBtn.addActionListener(e -> {
            new RegistroDialog(parentFrame).setVisible(true);
        });
    }
    
}


// RegistroDialog.java
class RegistroDialog extends JDialog {
    public RegistroDialog(JFrame parent) {
        super(parent, "Registro de Usuario", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fuente = new Font("Segoe UI", Font.PLAIN, 13);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 16);

        JLabel titulo = new JLabel("Registro de Usuario");
        titulo.setFont(fuenteTitulo);
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel nombreLbl = new JLabel("Nombre:");
        nombreLbl.setForeground(Color.WHITE);
        nombreLbl.setFont(fuente);
        panel.add(nombreLbl, gbc);

        gbc.gridx = 1;
        JTextField nombreTxt = new JTextField(15);
        panel.add(nombreTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel apellidoLbl = new JLabel("Apellido:");
        apellidoLbl.setForeground(Color.WHITE);
        apellidoLbl.setFont(fuente);
        panel.add(apellidoLbl, gbc);

        gbc.gridx = 1;
        JTextField apellidoTxt = new JTextField(15);
        panel.add(apellidoTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel usuarioLbl = new JLabel("Usuario:");
        usuarioLbl.setForeground(Color.WHITE);
        usuarioLbl.setFont(fuente);
        panel.add(usuarioLbl, gbc);

        gbc.gridx = 1;
        JTextField usuarioTxt = new JTextField(15);
        panel.add(usuarioTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passLbl = new JLabel("Contraseña:");
        passLbl.setForeground(Color.WHITE);
        passLbl.setFont(fuente);
        panel.add(passLbl, gbc);

        gbc.gridx = 1;
        JPasswordField passTxt = new JPasswordField(15);
        panel.add(passTxt, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel nacionalidadLbl = new JLabel("Nacionalidad:");
        nacionalidadLbl.setForeground(Color.WHITE);
        nacionalidadLbl.setFont(fuente);
        panel.add(nacionalidadLbl, gbc);

        gbc.gridx = 1;
        JComboBox<String> nacionalidadBox = new JComboBox<>(new String[] {
            "Canada", "USA", "Mexico", "Colombia", "Venezuela", "Brazil", "Chile", "Argentina", "Uruguay",
            "Alemania", "Francia", "Italia", "Rusia", "China", "Jap\u00f3n", "Australia"
        });
        panel.add(nacionalidadBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registrarBtn = new JButton("Registrar");
        registrarBtn.setFont(fuente);
        panel.add(registrarBtn, gbc);

        registrarBtn.addActionListener(e -> {
            String nombre = nombreTxt.getText().trim();
            String apellido = apellidoTxt.getText().trim();
            String user = usuarioTxt.getText().trim();
            String pass = new String(passTxt.getPassword()).trim();
            String nacionalidad = (String) nacionalidadBox.getSelectedItem();

            if (nombre.isEmpty() || apellido.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = ConexionBD.conectar()) {
                String sql = "INSERT INTO usuario (nickname, contrasena, pais, nombre, apellido) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.setString(3, nacionalidad);
                stmt.setString(4, nombre);
                stmt.setString(5, apellido);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Usuario registrado con \u00e9xito: " + user, "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
