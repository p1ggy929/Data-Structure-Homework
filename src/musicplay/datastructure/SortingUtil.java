package musicplay.datastructure;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import musicplay.model.Song;

/**
 * 提供通用排序算法的工具类。
 * 这里的排序方法被设计为"纯函数"，它们不会修改传入的原始集合，而是返回一个排序后的新集合。
 */
public class SortingUtil {

    /**
     * 根据歌曲的播放次数对歌曲列表进行降序排序。每首歌的播放次数通过Song.getPlayCount()获取
     *
     * @param songs 需要排序的歌曲列表。
     * @return 按播放次数降序排序的歌曲列表。
     */
    public static List<Song> sortByPlayCount(List<Song> songs) {
        // 创建原始列表的副本，避免修改原始集合
        List<Song> sortedSongs = new ArrayList<>(songs);
        
        // 使用Collections.sort()和自定义Comparator按播放次数降序排序
        Collections.sort(sortedSongs, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                // 按播放次数降序排序
                return Integer.compare(song2.getPlayCount(), song1.getPlayCount());
            }
        });
        
        return sortedSongs;
    }
} 