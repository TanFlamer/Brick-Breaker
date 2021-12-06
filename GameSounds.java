import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class GameSounds {

    public GameSounds(String filename){

        File sound = new File(filename+".wav");

        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
