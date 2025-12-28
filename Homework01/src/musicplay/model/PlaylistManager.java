package musicplay.model;

import musicplay.datastructure.MySearchList;
import musicplay.datastructure.MyBinaryTree;
import musicplay.datastructure.SortingUtil;
import musicplay.datastructure.RandomUtil;
import musicplay.datastructure.MyBinaryTree.Node;
import java.util.List;
import java.util.ArrayList;

public class PlaylistManager {
    private List<Song> mainPlaylist; // 存储原始添加顺序的列表
    private List<Song> playbackOrder; // 当前的播放顺序列表（可能是原始的，也可能是洗牌后的）
    private MySearchList searchList;
    // 核心数据结构：一个用于表示 歌手->专辑->歌曲 层级的二叉树
    private MyBinaryTree musicTree;
    private int currentIndex;
    private boolean isRandomMode;
    
    public PlaylistManager() {
        mainPlaylist = new ArrayList<>();
        playbackOrder = mainPlaylist; // 默认情况下，播放顺序和主列表一致
        searchList = new MySearchList();
        musicTree = new MyBinaryTree();
        // 初始化根节点，值为"所有歌手"，代表树的入口
        musicTree.setRoot(new Node("All Artists")); 
        currentIndex = -1;
        isRandomMode = false;
    }
    
    public String addSong(Song song) {
        mainPlaylist.add(song);
        searchList.addSong(song);

        // 如果不是随机模式，playbackOrder和mainPlaylist是同一个对象，所以新歌会自动包含进去
        // 如果是随机模式，新歌只被添加到了mainPlaylist，当前随机播放的列表不变，这符合一般播放器的逻辑
        
        Node root = musicTree.getRoot();

        // --- 查找或创建歌手节点 ---
        Node artistNode = findOrCreateChild(root, song.getArtist());
        if (artistNode == null) {
            return "歌手已满 (最多2位)";
        }

        // --- 查找或创建专辑节点 ---
        Node albumNode = findOrCreateChild(artistNode, song.getAlbum());
        if (albumNode == null) {
            return "该歌手的专辑已满 (最多2个)";
        }

        // --- 查找或创建歌曲节点 ---
        Node songNode = findOrCreateChild(albumNode, song);
        if (songNode == null) {
            return "该专辑的歌曲已满 (最多2首)";
        }

        if (currentIndex == -1) {
            currentIndex = 0;
        }
        return null; // 表示添加成功
    }

    /**
     * 辅助方法：在父节点下查找或创建子节点。
     * @param parent 父节点
     * @param value 子节点的值
     * @return 找到或创建的子节点，如果父节点的子节点已满则返回 null
     */
    private Node findOrCreateChild(Node parent, Object value) {
        // 检查左子节点
        if (parent.left == null) {
            parent.left = new Node(value);
            return parent.left;
        }
        if (getNodeValue(parent.left).equals(value.toString())) {
            return parent.left;
        }

        // 检查右子节点
        if (parent.right == null) {
            parent.right = new Node(value);
            return parent.right;
        }
        if (getNodeValue(parent.right).equals(value.toString())) {
            return parent.right;
        }

        return null; // 父节点的左右子节点都已存在且不匹配 -> 已满
    }

    /**
     * 辅助方法：安全地获取节点的值的字符串表示形式。
     */
    private String getNodeValue(Node node) {
        if (node.value instanceof Song) {
            return ((Song) node.value).getTitle();
        }
        return node.value.toString();
    }
    
    /**
     * 获取教学用的音乐二叉树实例。
     */
    public MyBinaryTree getMusicTree() {
        return musicTree;
    }
    
    public Song getCurrentSong() {
        if (playbackOrder.isEmpty() || currentIndex < 0 || currentIndex >= playbackOrder.size()) {
            return null;
        }
        return playbackOrder.get(currentIndex);
    }
    
    public Song nextSong() {
        if (playbackOrder.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex + 1) % playbackOrder.size();
        return getCurrentSong();
    }
    
    public Song previousSong() {
        if (playbackOrder.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex - 1 + playbackOrder.size()) % playbackOrder.size();
        return getCurrentSong();
    }
    
    public void toggleRandomMode() {
        isRandomMode = !isRandomMode;
        Song currentSong = getCurrentSong();

        if (isRandomMode) {
            // --- 进入随机模式 ---
            // 1. 使用洗牌算法创建一个新的随机顺序列表
            playbackOrder = (List<Song>) RandomUtil.shuffle(mainPlaylist);
        } else {
            // --- 退出随机模式 ---
            // 1. 将播放顺序切换回原始列表
            playbackOrder = mainPlaylist;
        }

        // 2. 无论切换到哪种模式，都找到当前歌曲在新列表中的位置，以保证播放的连续性
        if (currentSong != null) {
            currentIndex = playbackOrder.indexOf(currentSong);
        } else if (!playbackOrder.isEmpty()) {
            currentIndex = 0; // 如果之前没有歌曲，就从头开始
        } else {
            currentIndex = -1; // 如果列表为空，重置索引
        }
    }
    
    public boolean isRandomMode() {
        return isRandomMode;
    }
    
    public List<Song> searchSongs(String keyword) {
        return searchList.search(keyword);
    }
    
    public List<Song> getAllSongs() {
        return searchList.getAllSongs();
    }
    
    // 按播放次数降序返回所有歌曲
    public List<Song> getSongsByPlayCount() {
        List<Song> allSongs = getAllSongs();
        return SortingUtil.sortByPlayCount(allSongs);
    }
} 