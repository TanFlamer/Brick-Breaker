package Main;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Public class GameSounds is responsible for adding audio to the game such as BGM and sound effects. There are 2 types
 * of audio in the game which are BGM which loop continuously and sound effects which only play once.
 *
 * @author TanZhunXian
 * @version 1.0
 * @since 28/11/2021
 */
public class GameSounds {

    /**
     * The BGM for the current screen.
     */
    private Clip bgm;
    /**
     * The sound effects for actions like collision.
     */
    private Clip soundEffect;

    /**
     * This method stops the current BGM and loads and plays a new BGM from file.
     * @param filename The name of the new BGM.
     */
    public void setBgm(String filename){

        if(bgm!=null)
            bgm.stop();

        File song = new File("sounds/BGM/"+filename+".wav");

        try{
            Clip bgm = AudioSystem.getClip();
            bgm.open(AudioSystem.getAudioInputStream(song));
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
            this.bgm = bgm;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        bgm.start();
    }

    /**
     * This method loads and plays the specified sound effect from file.
     * @param filename The name of the sound effect.
     */
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

    /**
     * This method returns the current BGM.
     * @return The current BGM is returned.
     */
    public Clip getBgm() {
        return bgm;
    }
}
