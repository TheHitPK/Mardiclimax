package app3d_3;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.image.TextureLoader;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.net.URL;

public class VentanaZoomConLoading extends JFrame {

    public VentanaZoomConLoading() {
        setTitle("Vista Ampliada del Planeta");
        setLayout(new BorderLayout());
        setSize(500, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Canvas3D para renderizado 3D
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add("Center", canvas);

        // Barra de carga
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Cargando...");
        add(progressBar, BorderLayout.SOUTH);

        // Crear universo y escena 3D
        SimpleUniverse universo = new SimpleUniverse(canvas);
        universo.getViewingPlatform().setNominalViewingTransform();
        BranchGroup escena = crearEscenaZoom();
        universo.addBranchGraph(escena);

        setVisible(true);
    }

    private BranchGroup crearEscenaZoom() {
        BranchGroup root = new BranchGroup();
        BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Fondo de estrellas
        URL fondoUrl = getClass().getResource("stars.jpg");
        if (fondoUrl != null) {
            TextureLoader starLoader = new TextureLoader(fondoUrl, null);
            Background fondo = new Background(starLoader.getImage());
            fondo.setApplicationBounds(limites);
            root.addChild(fondo);
        }

        // Grupo transformable (rotación)
        TransformGroup rotador = new TransformGroup();
        rotador.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(rotador);

        // Textura del planeta
        Appearance apariencia = new Appearance();
        URL tierraUrl = getClass().getResource("earth.jpg");
        if (tierraUrl != null) {
            TextureLoader loader = new TextureLoader(tierraUrl, null);
            Texture textura = loader.getTexture();
            apariencia.setTexture(textura);

            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            apariencia.setTextureAttributes(texAttr);

            Material material = new Material();
            material.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
            apariencia.setMaterial(material);
        }

        // Esfera (zoom: radio más grande)
        Sphere planeta = new Sphere(0.8f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS, 64, apariencia);
        rotador.addChild(planeta);

        // Rotación animada
        Alpha alpha = new Alpha(-1, 10000);
        RotationInterpolator rotacion = new RotationInterpolator(alpha, rotador);
        rotacion.setSchedulingBounds(limites);
        root.addChild(rotacion);

        // Luz direccional
        DirectionalLight luz = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
        luz.setInfluencingBounds(limites);
        root.addChild(luz);

        return root;
    }
    
    

    // Método para lanzar desde otro lugar
    public static void mostrarVentana() {
        SwingUtilities.invokeLater(() -> new VentanaZoomConLoading());
    }
}
