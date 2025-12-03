package musicplay.model;

import musicplay.datastructure.MyStack;
import musicplay.datastructure.MyArrayList;
import java.util.List;

public class HistoryManager {
    private MyStack history;
    private static final int MAX_HISTORY_SIZE = 50;
    
    public HistoryManager() {
        history = new MyStack();
    }
    
    public void addToHistory(Song song) {
        // 从历史记录中移除已存在的相同歌曲，确保不重复，并将刚播放的歌曲置顶
        history.remove(song);

        // 如果历史记录达到上限，则移除最旧的条目（栈底的元素）
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.removeLast();
        }

        // 将当前播放的歌曲压入栈顶
        history.push(song);
    }
    
    public Song getLastPlayed() {
        if (history.isEmpty()) {
            return null;
        }
        return (Song) history.peek();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Song> getHistory() {
        MyArrayList result = new MyArrayList();
        MyStack temp = new MyStack();

        // 从 history 栈中依次弹出元素，加入到 result 列表和 temp 栈中
        // 这样 result 列表的顺序就是从新到旧
        while (!history.isEmpty()) {
            Song song = (Song) history.pop();
            result.add(song);
            temp.push(song);
        }

        // 从 temp 栈中依次弹出元素，压入 history 栈，以恢复 history 栈的原始顺序
        while (!temp.isEmpty()) {
            history.push(temp.pop());
        }

        return (List<Song>) result;
    }
    
    public void clearHistory() {
        history.clear();
    }
} 