package musicplay.model;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import javax.swing.*;
import java.io.File;

public class MP3MetaReader {
    public static SongMeta readMeta(String filePath) {
        String title = null;
        String artist = null;
        ImageIcon cover = null;
        try {
            AudioFile audioFile = AudioFileIO.read(new File(filePath));
            Tag tag = audioFile.getTag();
            if (tag != null) {
                title = tag.getFirst(FieldKey.TITLE);
                artist = tag.getFirst(FieldKey.ARTIST);
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    byte[] imageData = artwork.getBinaryData();
                    cover = new ImageIcon(imageData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SongMeta(title, artist, cover);
    }

    public static class SongMeta {
        public final String title;
        public final String artist;
        public final ImageIcon cover;
        public SongMeta(String title, String artist, ImageIcon cover) {
            this.title = title;
            this.artist = artist;
            this.cover = cover;
        }
    }
} 