import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class GameSounds {

    private Clip clip;

    public GameSounds(String filename){
        File sound = new File(filename+".wav");

        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Clip getClip() {
        return clip;
    }
}
