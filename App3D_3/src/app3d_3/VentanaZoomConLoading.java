package app3d_3;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.net.URL;

public class VentanaZoomConLoading extends JFrame {

    private final JProgressBar progressBar;
    private final String[] mensajes = {
        "Cargando...",
        "Cargando mapa...",
        "Cargando datos desde la API...",
        "Cargando estadísticas..."
    };
    private int mensajeIndex = 0;
    private Timer mensajeTimer;

    public VentanaZoomConLoading() {
        setTitle("Vista Ampliada del Planeta");
        setLayout(new BorderLayout());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Canvas3D
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add("Center", canvas);

        // Barra de carga
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(mensajes[mensajeIndex]);
        add(progressBar, BorderLayout.SOUTH);

        // Iniciar temporizador de mensajes
        iniciarCambioMensajes();

        // Universo y escena
        SimpleUniverse universo = new SimpleUniverse(canvas);
        universo.getViewingPlatform().setNominalViewingTransform();
        BranchGroup escena = crearEscenaZoom();
        universo.addBranchGraph(escena);

        setVisible(true);
    }

    private void iniciarCambioMensajes() {
        mensajeTimer = new Timer(1700, e -> {
            mensajeIndex = (mensajeIndex + 1) % mensajes.length;
            progressBar.setString(mensajes[mensajeIndex]);
        });
        mensajeTimer.start();
    }

    private BranchGroup crearEscenaZoom() {
        BranchGroup root = new BranchGroup();
        BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Fondo de estrellas
        URL fondoUrl = getClass().getResource("loading.jpg");
        if (fondoUrl != null) {
            TextureLoader starLoader = new TextureLoader(fondoUrl, null);
            Background fondo = new Background(starLoader.getImage());
            fondo.setApplicationBounds(limites);
            root.addChild(fondo);
        }

        // Grupo raíz transformable
        TransformGroup grupoMovimiento = new TransformGroup();
        grupoMovimiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(grupoMovimiento);

        // Grupo rotación del planeta
        TransformGroup rotador = new TransformGroup();
        rotador.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        grupoMovimiento.addChild(rotador);

        // Apariencia del planeta
        Appearance apariencia = new Appearance();
        URL tierraUrl = getClass().getResource("1.jpeg");
        if (tierraUrl != null) {
            TextureLoader loader = new TextureLoader(tierraUrl, null);
            Texture textura = loader.getTexture();
            apariencia.setTexture(textura);

            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            apariencia.setTextureAttributes(texAttr);

              Material material = new Material();
              material.setAmbientColor(new Color3f(0.3f, 0.3f, 0.3f));  // luz base
material.setDiffuseColor(new Color3f(0.8f, 0.8f, 0.8f));  // luz directa (mantiene visibilidad)
material.setSpecularColor(new Color3f(0f, 0f, 0f));       // SIN reflejo
material.setShininess(1.0f);    
        }

        // Esfera del planeta con tamaño reducido
        Sphere planeta = new Sphere(0.3f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS, 64, apariencia);
        rotador.addChild(planeta);

        // Animación de rotación
        Alpha alphaRotacion = new Alpha(-1, 8000);
        RotationInterpolator rotacion = new RotationInterpolator(alphaRotacion, rotador);
        rotacion.setSchedulingBounds(limites);
        root.addChild(rotacion);

        // Animación de movimiento lateral (más lento)
        Alpha alphaMovimiento = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
                0, 0, 6000, 0, 0, 6000, 0, 0);
        Transform3D ejeX = new Transform3D();
        ejeX.setTranslation(new Vector3f(-0.5f, 0.0f, 0.0f));
        PositionInterpolator movimiento = new PositionInterpolator(alphaMovimiento, grupoMovimiento, ejeX, -0.5f, 0.5f);
        movimiento.setSchedulingBounds(limites);
        root.addChild(movimiento);

        // Luz
        DirectionalLight luz = new DirectionalLight(new Color3f(4f, 4f, 4f), new Vector3f(-1f, -0.3f, -1f));
        luz.setInfluencingBounds(limites);
        root.addChild(luz);

        return root;
    }

    public static void mostrarVentana() {
        SwingUtilities.invokeLater(() -> new VentanaZoomConLoading());
    }
}
