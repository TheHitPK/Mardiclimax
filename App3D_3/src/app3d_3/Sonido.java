package app3d_3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class Sonido {
    private static Sonido instancia;

    private Sonido() {}

    public static Sonido getInstancia() {
        if (instancia == null) {
            instancia = new Sonido();
        }
        return instancia;
    }
    
    private javax.sound.sampled.Clip clip;
    
     public void reproducirSonido() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("ambient-soundscapes-003-space-atmosphere-303242.wav");
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            this.clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("❌ Error de sonido: " + e.getMessage());
        }
    }
   public void detenerSonido() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void reanudarSonido() {
        if (clip != null) {
            clip.start();
        }
    }

    public void cerrar() {
        if (clip != null) {
            clip.close();
        }
    }

    public boolean estaSonando() {
        return clip != null && clip.isRunning();
    }
    
}
