package musicplay.model;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;

public class MP3Player {
    private Player player;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private boolean isPlaying;
    private long pausePosition;
    private long totalLength;
    private String currentFile;
    
    public MP3Player() {
        isPlaying = false;
        pausePosition = 0;
        totalLength = 0;
    }
    
    public void play(String filePath) {
        try {
            stop();
            currentFile = filePath;
            File file = new File(filePath);
            fis = new FileInputStream(file);
            totalLength = file.length();
            bis = new BufferedInputStream(fis);
            player = new Player(bis);
            
            // Start playback in a new thread
            new Thread(() -> {
                try {
                    isPlaying = true;
                    player.play();
                    isPlaying = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void pause() {
        if (player != null && isPlaying) {
            try {
                player.close();
                isPlaying = false;
                // Note: JLayer doesn't support direct pause, so we need to restart from the beginning
                // when resuming
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void resume() {
        if (!isPlaying && currentFile != null) {
            play(currentFile);
        }
    }
    
    public void stop() {
        if (player != null) {
            try {
                player.close();
                if (bis != null) bis.close();
                if (fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPlaying = false;
            pausePosition = 0;
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public long getPosition() {
        // Note: JLayer doesn't provide direct position information
        // This is a placeholder implementation
        return pausePosition;
    }
    
    public long getLength() {
        return totalLength;
    }
} 