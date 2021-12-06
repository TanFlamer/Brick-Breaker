import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class GameSounds {

    private Clip bgm;
    private Clip soundEffect;

    public void setBgm(String filename){

        if(bgm!=null)
            bgm.stop();

        File song = new File("sounds/BGM/"+filename+".wav");

        try{
            Clip bgm = AudioSystem.getClip();
            bgm.open(AudioSystem.getAudioInputStream(song));
            this.bgm = bgm;
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        bgm.start();
    }

    public void playSoundEffect(String filename){

        File sound = new File("sounds/SoundEffects/"+filename+".wav");

        try{
            Clip soundEffect = AudioSystem.getClip();
            soundEffect.open(AudioSystem.getAudioInputStream(sound));
            this.soundEffect = soundEffect;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        soundEffect.start();
    }

    public Clip getBgm() {
        return bgm;
    }

    public Clip getSoundEffect() {
        return soundEffect;
    }
}
