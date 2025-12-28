package musicplay.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    private MP3Player mp3Player;
    private boolean isPlaying;
    private long pausePosition;
    private String currentFile;
    private boolean isMP3;
    
    public AudioPlayer() {
        isPlaying = false;
        pausePosition = 0;
        mp3Player = new MP3Player();
    }
    
    public void play(String filePath) {
        try {
            stop();
            currentFile = filePath;
            File audioFile = new File(filePath);
            String extension = getFileExtension(filePath).toLowerCase();
            
            if (extension.equals("mp3")) {
                isMP3 = true;
                mp3Player.play(filePath);
                isPlaying = true;
            } else {
                isMP3 = false;
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();
                
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                
                clip.open(audioStream);
                clip.start();
                isPlaying = true;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void pause() {
        if (isMP3) {
            mp3Player.pause();
        } else if (clip != null && isPlaying) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
        isPlaying = false;
    }
    
    public void resume() {
        if (isMP3) {
            mp3Player.resume();
        } else if (clip != null && !isPlaying) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
        }
        isPlaying = true;
    }
    
    public void stop() {
        if (isMP3) {
            mp3Player.stop();
        } else if (clip != null) {
            clip.stop();
            clip.close();
        }
        isPlaying = false;
        pausePosition = 0;
    }
    
    public boolean isPlaying() {
        if (isMP3) {
            return mp3Player.isPlaying();
        }
        return isPlaying;
    }
    
    public void setVolume(float volume) {
        if (!isMP3 && clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
        // Note: JLayer doesn't support volume control
    }
    
    public long getPosition() {
        if (isMP3) {
            return mp3Player.getPosition();
        } else if (clip != null) {
            return clip.getMicrosecondPosition();
        }
        return 0;
    }
    
    public long getLength() {
        if (isMP3) {
            return mp3Player.getLength();
        } else if (clip != null) {
            return clip.getMicrosecondLength();
        }
        return 0;
    }
    
    private String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }
} 