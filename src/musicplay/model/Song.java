package musicplay.model;

public class Song implements Comparable<Song> {
    private String title;
    private String artist;
    private String path;
    private int playCount = 0;
    private String album;
    
    public Song(String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.album = "未知专辑"; // 默认值
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getAlbum() {
        return album;
    }
    
    public void setAlbum(String album) {
        this.album = album;
    }
    
    public int getPlayCount() {
        return playCount;
    }
    
    public void incPlayCount() {
        playCount++;
    }
    
    @Override
    public String toString() {
        return title + " - " + artist;
    }

    @Override
    public int compareTo(Song other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Song other = (Song) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (artist == null) {
            if (other.artist != null)
                return false;
        } else if (!artist.equals(other.artist))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (playCount != other.playCount)
            return false;
        if (album == null) {
            if (other.album != null)
                return false;
        } else if (!album.equals(other.album))
            return false;
        return true;
    }
} 