package musicplay.datastructure;

import java.util.ArrayList;
import java.util.List;
import musicplay.model.Song;

/**
 * 使用二分查找的自定义搜索列表实现。
 * 列表会始终按歌曲标题排序。
 * 内部包含纯粹的二分查找算法作为私有辅助方法。
 */
public class MySearchList {
    private List<Song> songs;

    public MySearchList() {
        this.songs = new ArrayList<>();
    }
    
    
    /**
     * 二分查找，用于寻找任意一个标题以前缀开头的歌曲。
     * @param songs 已排序的歌曲列表
     * @param keyword 搜索关键词 (前缀)
     * @return 任意一个匹配的歌曲的索引，如果未找到则返回 -1
     */
    private static int binarySearchForAny(List<Song> songs, String keyword) {
    	// TODO 待实现
        return -1; // 未找到
    }
    

    /**
     * 向列表中添加一首歌曲，并保持列表按标题排序。
     * @param song 要添加的歌曲
     */
    public void addSong(Song song) {
        int insertionPoint = findInsertionPoint(this.songs, song);
        songs.add(insertionPoint, song);
    }

    /**
     * 根据关键词搜索歌曲。
     * @param keyword 搜索关键词
     * @return 匹配的歌曲列表
     */
    public List<Song> search(String keyword) {
        List<Song> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }

        int anyMatchIndex = binarySearchForAny(this.songs, keyword);

        if (anyMatchIndex != -1) {
            String lowerCaseKeyword = keyword.toLowerCase();
            
            int firstMatchIndex = anyMatchIndex;
            while (firstMatchIndex > 0 && songs.get(firstMatchIndex - 1).getTitle().toLowerCase().startsWith(lowerCaseKeyword)) {
                firstMatchIndex--;
            }
            
            for (int i = firstMatchIndex; i < songs.size(); i++) {
                Song currentSong = songs.get(i);
                if (currentSong.getTitle().toLowerCase().startsWith(lowerCaseKeyword)) {
                    result.add(currentSong);
                } else {
                    break;
                }
            }
        }
        
        return result;
    }

    /**
     * 获取列表中的所有歌曲。
     * @return 所有歌曲的列表，按标题排序
     */
    public List<Song> getAllSongs() {
        return new ArrayList<>(songs);
    }
    
    /**
     * 清空列表。
     */
    public void clear() {
        songs.clear();
    }

    // --- 内部私有的、纯粹的二分查找算法 ---

    

    /**
     * 在一个已按标题排序的歌曲列表中，使用二分查找找到新歌曲的正确插入点。
     * @param songs 已排序的歌曲列表
     * @param song 要插入的歌曲
     * @return 该歌曲的正确插入点索引
     */
    private static int findInsertionPoint(List<Song> songs, Song song) {
        int low = 0;
        int high = songs.size() - 1;
        int insertionPoint = songs.size();

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Song midVal = songs.get(mid);
            int cmp = midVal.getTitle().compareToIgnoreCase(song.getTitle());

            if (cmp < 0) {
                low = mid + 1;
            } else {
                insertionPoint = mid;
                high = mid - 1;
            }
        }
        return insertionPoint;
    }
} 