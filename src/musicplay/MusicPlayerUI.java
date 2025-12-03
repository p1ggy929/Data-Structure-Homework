package musicplay;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;
import java.awt.RenderingHints;
import musicplay.model.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import musicplay.datastructure.MyBinaryTree;
import musicplay.datastructure.MyBinaryTree.Node;
import musicplay.model.AudioPlayer;
import musicplay.model.HistoryManager;
import musicplay.model.MP3MetaReader;

public class MusicPlayerUI extends JFrame {
    // 歌单信息
    private JLabel playlistCoverLabel;
    private JTextField playlistTitleField;
    private JTextArea playlistDescArea;
    private JButton changeCoverButton;
    private JButton playAllButton;

    // 歌曲列表
    private JTable songTable;
    private DefaultTableModel songTableModel;

    // 底部播放栏
    private JLabel bottomCoverLabel;
    private JLabel bottomTitleLabel;
    private JLabel bottomArtistLabel;
    private JButton prevButton;
    private JButton playPauseButton;
    private JButton nextButton;
    private JButton modeButton;
    private JButton volumeButton;

    // 数据结构
    private PlaylistManager playlistManager;
    private HistoryManager historyManager;
    private AudioPlayer audioPlayer;

    // 其他
    private ImageIcon playlistCover;
    private boolean isPlaying = false;
    private boolean isRandomMode = false;
    private boolean isShowingHistory = false;
    private boolean isShowingHot = false;

    private JButton addSongButton;

    private JPanel mainPanel;

    // 自定义网易云风格按钮
    private static class CloudPlayButton extends JButton {
        private Color normalColor = new Color(255, 58, 58);
        private Color hoverColor = new Color(230, 40, 40);
        public CloudPlayButton(String text) {
            super(text);
            setFont(new Font("微软雅黑", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBackground(normalColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setPreferredSize(new Dimension(160, 36));
            setIcon(new ImageIcon(drawPlayIcon(20, 20, Color.WHITE)));
            setIconTextGap(10);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(normalColor);
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            super.paintComponent(g);
            g2.dispose();
        }
        @Override
        public void paintBorder(Graphics g) {}
        private Image drawPlayIcon(int w, int h, Color color) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int[] x = {4, w-4, 4};
            int[] y = {4, h/2, h-4};
            g2.setColor(color);
            g2.fillPolygon(x, y, 3);
            g2.dispose();
            return img;
        }
    }

    // 自定义圆角边框
    class RoundedBorder extends LineBorder {
        private int radius;
        public RoundedBorder(Color color, int thickness, int radius) {
            super(color, thickness, true);
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }

    public MusicPlayerUI() {
        playlistManager = new PlaylistManager();
        historyManager = new HistoryManager();
        audioPlayer = new AudioPlayer();
        // 设置默认封面图
        java.net.URL coverUrl = getClass().getResource("/icons/cover.jpeg");
        if (coverUrl != null) {
            playlistCover = new ImageIcon(coverUrl);
        } else {
            playlistCover = new ImageIcon(new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB));
        }
        // 设置全局背景色
        UIManager.put("Panel.background", new Color(250,246,247));
        UIManager.put("Table.background", new Color(250,246,247));
        UIManager.put("TableHeader.background", new Color(250,246,247));
        UIManager.put("ScrollPane.background", new Color(250,246,247));
        UIManager.put("TextArea.background", new Color(250,246,247));
        UIManager.put("TextField.background", new Color(250,246,247));
        UIManager.put("Viewport.background", new Color(250,246,247));
        initUI();
    }

    private void initUI() {
        setTitle("f230001 & f230002 音乐播放器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250,246,247));

        // 左侧网易云风格菜单栏
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.Y_AXIS));
        leftMenu.setBackground(new Color(245,245,247));
        leftMenu.setPreferredSize(new Dimension(180, 0));
        leftMenu.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(180, 40));
        logoPanel.setPreferredSize(new Dimension(180, 40));
        JLabel logoImg = new JLabel(loadIcon("/icons/net-ease.png", 24));
        JLabel logoText = new JLabel("云音乐");
        logoText.setFont(new Font("微软雅黑", Font.BOLD, 18));
        logoText.setForeground(new Color(220, 40, 40));
        logoPanel.add(logoImg);
        logoPanel.add(logoText);
        leftMenu.add(logoPanel);
        leftMenu.add(Box.createVerticalStrut(30));
        JButton btnPlaylist = new JButton("我的歌单");
        btnPlaylist.setIcon(loadIcon("/icons/playlist.png", 20));
        btnPlaylist.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnPlaylist.setIconTextGap(10);
        JButton btnHistory = new JButton("最近播放");
        btnHistory.setIcon(loadIcon("/icons/history.png", 20));
        btnHistory.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHistory.setIconTextGap(10);
        JButton btnHot = new JButton("最热歌曲");
        btnHot.setIcon(loadIcon("/icons/hot.png", 20));
        btnHot.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHot.setIconTextGap(10);
//        JButton btnFavorite = new JButton("喜欢的音乐");
//        btnFavorite.setIcon(loadIcon("/icons/favorite.png", 20));
//        btnFavorite.setHorizontalTextPosition(SwingConstants.RIGHT);
//        btnFavorite.setIconTextGap(10);
//        JButton btnDownload = new JButton("下载管理");
//        btnDownload.setIcon(loadIcon("/icons/download.png", 20));
//        btnDownload.setHorizontalTextPosition(SwingConstants.RIGHT);
//        btnDownload.setIconTextGap(10);
        JButton btnArtistInfo = new JButton("歌手信息");
        btnArtistInfo.setIcon(loadIcon("/icons/stats.png", 20));
        btnArtistInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnArtistInfo.setIconTextGap(10);
        btnPlaylist.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnHistory.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnHot.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnArtistInfo.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnPlaylist.setFocusPainted(false);
        btnHistory.setFocusPainted(false);
        btnHot.setFocusPainted(false);
        btnArtistInfo.setFocusPainted(false);
        btnPlaylist.setBackground(new Color(245,245,247));
        btnHistory.setBackground(new Color(245,245,247));
        btnHot.setBackground(new Color(245,245,247));
        btnArtistInfo.setBackground(new Color(245,245,247));
        btnPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPlaylist.setHorizontalAlignment(SwingConstants.LEFT);
        btnPlaylist.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
        btnHistory.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHistory.setHorizontalAlignment(SwingConstants.LEFT);
        btnHistory.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
        btnHot.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHot.setHorizontalAlignment(SwingConstants.LEFT);
        btnHot.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
//        btnFavorite.setAlignmentX(Component.LEFT_ALIGNMENT);
//        btnFavorite.setHorizontalAlignment(SwingConstants.LEFT);
//        btnFavorite.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
//        btnDownload.setAlignmentX(Component.LEFT_ALIGNMENT);
//        btnDownload.setHorizontalAlignment(SwingConstants.LEFT);
//        btnDownload.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
        btnArtistInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnArtistInfo.setHorizontalAlignment(SwingConstants.LEFT);
        btnArtistInfo.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 0));
        leftMenu.add(btnPlaylist);
        leftMenu.add(btnHistory);
        leftMenu.add(btnHot);
        leftMenu.add(Box.createVerticalStrut(10));
        leftMenu.add(btnArtistInfo);
        leftMenu.add(Box.createVerticalGlue());
        add(leftMenu, BorderLayout.WEST);

        // 右侧主内容区 mainPanel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250,246,247));

        // 顶部歌单信息区
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        topPanel.setBackground(new Color(250,246,247));
        // 封面
        playlistCoverLabel = new JLabel();
        playlistCoverLabel.setIcon(fitCenterIcon(playlistCover, 180, 180));
        playlistCoverLabel.setBorder(null);
        playlistCoverLabel.setHorizontalAlignment(JLabel.CENTER);
        playlistCoverLabel.setVerticalAlignment(JLabel.CENTER);
        playlistCoverLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setBackground(new Color(250,246,247));
        coverPanel.add(playlistCoverLabel, BorderLayout.CENTER);
        // 歌单介绍
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(250,246,247));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 与封面图保持32px间距
        // 标题
        JLabel titleLabel = new JLabel("云村流行热播 Ciara全新remix单曲来袭");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        // 描述
        JTextArea descArea = new JTextArea("每周五锁定我 全球最新R&B作品一网打尽 五月天最新单曲");
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        descArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        descArea.setRows(3);
        descArea.setBackground(new Color(250,246,247));
        descArea.setEditable(true);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(350, 70));
        descScroll.setBorder(null);
        descScroll.getViewport().setBackground(new Color(250,246,247));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descScroll);
        infoPanel.add(Box.createVerticalGlue());
        // 播放全部和添加歌曲按钮在同一行，底部对齐
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBackground(new Color(250,246,247));
        playAllButton = new CloudPlayButton("播放全部");
        playAllButton.addActionListener(e -> playAllSongs());
        playAllButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        addSongButton = new JButton("＋ 添加歌曲");
        addSongButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        addSongButton.setForeground(new Color(255, 58, 58));
        addSongButton.setBackground(new Color(250,246,247));
        addSongButton.setFocusPainted(false);
        addSongButton.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        addSongButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        addSongButton.addActionListener(e -> addNewSong());
        btnPanel.add(playAllButton);
        btnPanel.add(Box.createHorizontalStrut(8));
        btnPanel.add(addSongButton);
        btnPanel.add(Box.createHorizontalGlue());
        // 搜索框
        int searchWidth = 220;
        int searchHeight = 32;
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        searchField.setForeground(new Color(192,176,176));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        searchField.setText("搜索");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("搜索")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(60,60,60));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("搜索");
                    searchField.setForeground(new Color(192,176,176));
                }
            }
        });
        JLabel searchIcon = new JLabel(loadIcon("/icons/search.png", 16));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setPreferredSize(new Dimension(searchWidth, searchHeight));
        searchPanel.setMaximumSize(new Dimension(searchWidth, searchHeight));
        searchPanel.setBackground(new Color(250,246,247));
        searchPanel.setBorder(new RoundedBorder(new Color(230,230,230), 1, 18));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        btnPanel.add(searchPanel);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(btnPanel);

        // 搜索逻辑
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void doSearch() {
                String text = searchField.getText().trim().toLowerCase();
                if (text.isEmpty() || text.equals("搜索")) {
                    updateSongTable();
                    return;
                }
                java.util.List<Song> filtered = playlistManager.searchSongs(text);
                updateSongTable(filtered);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
        });

        topPanel.add(coverPanel, BorderLayout.WEST);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 歌单列表上方工具栏
        JPanel tableTopPanel = new JPanel(new BorderLayout());
        tableTopPanel.setBackground(new Color(250,246,247));
        // 歌曲列表区
        String[] columns = {"#", "标题", "歌手", "专辑", "时长", "播放次数"};
        songTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        songTable = new JTable(songTableModel);
        songTable.setRowHeight(28);
        songTable.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songTable.setShowGrid(false);
        songTable.setIntercellSpacing(new Dimension(0, 0));
        Color tableBg = new Color(255,253,253); // 更浅的粉白色
        songTable.setBackground(tableBg);
        songTable.setBorder(null);
        songTable.setFocusable(false);
        songTable.setSelectionBackground(new Color(255, 235, 238));
        songTable.setSelectionForeground(Color.BLACK);
        songTable.setShowHorizontalLines(false);
        songTable.setShowVerticalLines(false);
        // 隐藏JTable表头
        songTable.setTableHeader(null);
        songTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 使用 rowAtPoint 和 columnAtPoint 更精确
                int row = songTable.rowAtPoint(e.getPoint());
                if (row == -1) return;

                if (e.getClickCount() == 2) { // 双击播放
                    playSongByIndex(row);
                } else if (e.getClickCount() == 1) { // 单击歌手列显示统计
                    int column = songTable.columnAtPoint(e.getPoint());
                    if (column == 2) { // 歌手列
                        String artist = (String)songTable.getValueAt(row, 2);
                        showArtistStats(artist);
                    }
                }
            }
        });
        JScrollPane tableScroll = new JScrollPane(songTable);
        tableScroll.setBorder(BorderFactory.createMatteBorder(1,0,1,0,new Color(240,230,230)));
        tableScroll.getViewport().setBackground(tableBg);
        // 极简网易云风格表头
        JPanel customHeader = new JPanel(new GridLayout(1, 6));
        String[] headers = {"#", "标题", "歌手", "专辑", "时长", "播放次数"};
        for (String h : headers) {
            JTextField field = new JTextField(h);
            field.setEditable(false);
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setFont(new Font("微软雅黑", Font.BOLD, 14));
            field.setForeground(new Color(192,176,176));
            field.setBackground(tableBg);
            field.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            customHeader.add(field);
        }
        customHeader.setBackground(tableBg);
        // 歌单列表整体面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(tableBg);
        tablePanel.add(tableTopPanel, BorderLayout.NORTH);
        tablePanel.add(customHeader, BorderLayout.CENTER);
        tablePanel.add(tableScroll, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // 底部播放栏
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        bottomPanel.setBackground(new Color(250,246,247));
        // 封面
        bottomCoverLabel = new JLabel();
        bottomCoverLabel.setPreferredSize(new Dimension(60, 60));
        bottomCoverLabel.setBorder(null);
        // 歌曲信息
        JPanel infoBottomPanel = new JPanel();
        infoBottomPanel.setLayout(new BoxLayout(infoBottomPanel, BoxLayout.Y_AXIS));
        infoBottomPanel.setBackground(new Color(250,246,247));
        bottomTitleLabel = new JLabel("未播放");
        bottomTitleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        bottomArtistLabel = new JLabel("");
        bottomArtistLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        infoBottomPanel.add(bottomTitleLabel);
        infoBottomPanel.add(bottomArtistLabel);
        JPanel leftBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftBottomPanel.setBackground(new Color(250,246,247));
        leftBottomPanel.add(bottomCoverLabel);
        leftBottomPanel.add(infoBottomPanel);
        // 控制按钮
        prevButton = new JButton(loadIcon("/icons/prev.png", 24));
        playPauseButton = new JButton(loadIcon("/icons/play.png", 24));
        nextButton = new JButton(loadIcon("/icons/next.png", 24));
        modeButton = new JButton(loadIcon("/icons/repeat.png", 20));
        volumeButton = new JButton(loadIcon("/icons/volume.png", 20));
        
        // 设置按钮样式
        prevButton.setPreferredSize(new Dimension(40, 40));
        playPauseButton.setPreferredSize(new Dimension(40, 40));
        nextButton.setPreferredSize(new Dimension(40, 40));
        modeButton.setPreferredSize(new Dimension(36, 36));
        volumeButton.setPreferredSize(new Dimension(36, 36));
        
        prevButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        playPauseButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        nextButton.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        modeButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        volumeButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        prevButton.setFocusPainted(false);
        playPauseButton.setFocusPainted(false);
        nextButton.setFocusPainted(false);
        modeButton.setFocusPainted(false);
        volumeButton.setFocusPainted(false);
        prevButton.setBackground(new Color(250,246,247));
        playPauseButton.setBackground(new Color(250,246,247));
        nextButton.setBackground(new Color(250,246,247));
        modeButton.setBackground(new Color(250,246,247));
        volumeButton.setBackground(new Color(250,246,247));
        prevButton.setBorder(null);
        playPauseButton.setBorder(null);
        nextButton.setBorder(null);
        modeButton.setBorder(null);
        volumeButton.setBorder(null);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        controlPanel.setBackground(new Color(250,246,247));
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(modeButton);
        controlPanel.add(volumeButton);
        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // 事件绑定
        prevButton.addActionListener(e -> playPreviousSong());
        playPauseButton.addActionListener(e -> togglePlayPause());
        nextButton.addActionListener(e -> playNextSong());
        modeButton.addActionListener(e -> togglePlayMode());
        volumeButton.addActionListener(e -> adjustVolume());

        // 右键添加歌曲
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addSongItem = new JMenuItem("添加歌曲");
        addSongItem.addActionListener(e -> addNewSong());
        popupMenu.add(addSongItem);
        
        songTable.setComponentPopupMenu(popupMenu);
        tableScroll.setComponentPopupMenu(popupMenu);

        // 菜单栏事件
        btnPlaylist.addActionListener(e -> {
            isShowingHistory = false;
            isShowingHot = false;
            updateSongTable();
            btnPlaylist.setForeground(new Color(255,58,58));
            btnHistory.setForeground(Color.DARK_GRAY);
            btnHot.setForeground(Color.DARK_GRAY);
        });
        btnHistory.addActionListener(e -> {
            isShowingHistory = true;
            isShowingHot = false;
            updateSongTable();
            btnPlaylist.setForeground(Color.DARK_GRAY);
            btnHistory.setForeground(new Color(255,58,58));
            btnHot.setForeground(Color.DARK_GRAY);
        });
        btnHot.addActionListener(e -> {
            isShowingHistory = false;
            isShowingHot = true;
            updateSongTable();
            btnPlaylist.setForeground(Color.DARK_GRAY);
            btnHistory.setForeground(Color.DARK_GRAY);
            btnHot.setForeground(new Color(255,58,58));
        });
        // 默认高亮歌单
        btnPlaylist.setForeground(new Color(255,58,58));
        btnHistory.setForeground(Color.DARK_GRAY);
        btnHot.setForeground(Color.DARK_GRAY);

        // 添加歌手信息按钮的事件处理
        btnArtistInfo.addActionListener(e -> showArtistInfoStats());

        // 初始化
        updateSongTable();
        updateBottomBar(null);
    }

    private void choosePlaylistCover() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png", "bmp", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            playlistCover = new ImageIcon(file.getAbsolutePath());
            playlistCoverLabel.setIcon(fitCenterIcon(playlistCover, 180, 180));
        }
    }

    private void playAllSongs() {
        if (playlistManager.getAllSongs().size() > 0) {
            playSongByIndex(0);
            songTable.setRowSelectionInterval(0, 0);
        }
    }

    private void playSongByIndex(int index) {
        List<Song> songs;
        if (isShowingHistory) {
            songs = new java.util.ArrayList<>(historyManager.getHistory());
        } else if (isShowingHot) {
            songs = playlistManager.getSongsByPlayCount();
        } else {
            songs = playlistManager.getAllSongs();
        }
        if (index >= 0 && index < songs.size()) {
            Song song = songs.get(index);
            historyManager.addToHistory(song);
            song.incPlayCount();
            audioPlayer.stop();
            audioPlayer.play(song.getPath());
            isPlaying = true;
            updateBottomBar(song);
            playPauseButton.setIcon(loadIcon("/icons/pause.png", 24));
        }
    }

    private void playPreviousSong() {
        Song song = playlistManager.previousSong();
        if (song != null) {
            historyManager.addToHistory(song);
            song.incPlayCount();
            audioPlayer.stop();
            audioPlayer.play(song.getPath());
            isPlaying = true;
            updateBottomBar(song);
            playPauseButton.setIcon(loadIcon("/icons/pause.png", 24));
            selectSongInTable(song);
        }
    }

    private void playNextSong() {
        Song song = playlistManager.nextSong();
        if (song != null) {
            historyManager.addToHistory(song);
            song.incPlayCount();
            audioPlayer.stop();
            audioPlayer.play(song.getPath());
            isPlaying = true;
            updateBottomBar(song);
            playPauseButton.setIcon(loadIcon("/icons/pause.png", 24));
            selectSongInTable(song);
        }
    }

    private void togglePlayPause() {
        if (isPlaying) {
            audioPlayer.pause();
            isPlaying = false;
            playPauseButton.setIcon(loadIcon("/icons/play.png", 24));
        } else {
            Song currentSong = playlistManager.getCurrentSong();
            if (currentSong != null) {
                audioPlayer.resume();
                isPlaying = true;
                playPauseButton.setIcon(loadIcon("/icons/pause.png", 24));
            }
        }
    }

    private void togglePlayMode() {
        isRandomMode = !isRandomMode;
        playlistManager.toggleRandomMode();
        if (isRandomMode) {
            modeButton.setIcon(loadIcon("/icons/shuffle.png", 20));
        } else {
            modeButton.setIcon(loadIcon("/icons/repeat.png", 20));
        }
    }

    private void adjustVolume() {
        int value = JOptionPane.showOptionDialog(this, new JSlider(JSlider.HORIZONTAL, 0, 100, 100),
                "调节音量", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        // 实际音量调整已在滑块监听中实现
    }

    private void addNewSong() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("音频文件", "mp3", "wav"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            String title = file.getName();
            String artist = "";
            String album = "";
            // 自动读取MP3标签
            if (filePath.toLowerCase().endsWith(".mp3")) {
                MP3MetaReader.SongMeta meta = MP3MetaReader.readMeta(filePath);
                if (meta.title != null && !meta.title.isEmpty()) title = meta.title;
                if (meta.artist != null && !meta.artist.isEmpty()) artist = meta.artist;
                // 封面不在此处显示
            }
            title = (String)JOptionPane.showInputDialog(this, "歌曲标题:", "添加歌曲", JOptionPane.PLAIN_MESSAGE, null, null, title);
            artist = (String)JOptionPane.showInputDialog(this, "歌手:", "添加歌曲", JOptionPane.PLAIN_MESSAGE, null, null, artist);
            album = (String)JOptionPane.showInputDialog(this, "专辑:", "添加歌曲", JOptionPane.PLAIN_MESSAGE, null, null, album);
            if (title != null && artist != null) {
                Song song = new Song(title, artist, filePath);
                song.setAlbum(album); // 设置专辑信息
                
                String errorMessage = playlistManager.addSong(song);
                if (errorMessage != null) {
                    JOptionPane.showMessageDialog(this, errorMessage, "添加失败", JOptionPane.WARNING_MESSAGE);
                }

                updateSongTable();
                // 自动滚动到新加的歌曲
                int lastRow = songTableModel.getRowCount() - 1;
                if (lastRow >= 0) songTable.scrollRectToVisible(songTable.getCellRect(lastRow, 0, true));
            }
        }
    }

    private void updateSongTable() {
        songTableModel.setRowCount(0);
        List<Song> songs;
        if (isShowingHistory) {
            songs = new java.util.ArrayList<>(historyManager.getHistory());
        } else if (isShowingHot) {
            songs = playlistManager.getSongsByPlayCount();
        } else {
            songs = playlistManager.getAllSongs();
        }
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            songTableModel.addRow(new Object[]{i + 1, s.getTitle(), s.getArtist(), "", "", s.getPlayCount()});
        }
    }

    private void updateSongTable(java.util.List<Song> customList) {
        songTableModel.setRowCount(0);
        java.util.List<Song> songs = customList;
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            songTableModel.addRow(new Object[]{i + 1, s.getTitle(), s.getArtist(), "", "", s.getPlayCount()});
        }
    }

    private void updateBottomBar(Song song) {
        if (song == null) {
            bottomCoverLabel.setIcon(fitCenterIcon(playlistCover, 60, 60));
            bottomTitleLabel.setText("未播放");
            bottomArtistLabel.setText("");
        } else {
            bottomCoverLabel.setIcon(fitCenterIcon(playlistCover, 60, 60));
            bottomTitleLabel.setText(song.getTitle());
            bottomArtistLabel.setText(song.getArtist());
        }
    }

    private void selectSongInTable(Song song) {
        List<Song> songs = playlistManager.getAllSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(song)) {
                songTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return null;
        // 高质量缩放
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(icon.getImage(), 0, 0, w, h, null);
        g2.dispose();
        // 圆角处理
        BufferedImage rounded = makeRoundedCorner(scaled, w, h, w / 8);
        return new ImageIcon(rounded);
    }

    // 工具方法：生成圆角图片
    private BufferedImage makeRoundedCorner(Image img, int w, int h, int arc) {
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return output;
    }

    // 生成正圆形图标
    private ImageIcon resizeIconCircle(ImageIcon icon, int size) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return null;
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(icon.getImage(), 0, 0, size, size, null);
        g2.dispose();
        // 圆形裁剪
        BufferedImage circle = makeCircleImage(scaled, size);
        return new ImageIcon(circle);
    }

    // 工具方法：生成正圆形图片
    private BufferedImage makeCircleImage(Image img, int size) {
        BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();
        return output;
    }

    // 加载并缩放图标
    private ImageIcon loadIcon(String path, int size) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return null;
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // 等比缩放居中显示图片，只缩小不放大
    private ImageIcon fitCenterIcon(ImageIcon icon, int w, int h) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return null;
        int imgW = icon.getIconWidth();
        int imgH = icon.getIconHeight();
        double scale = Math.min(1.0, Math.min((double)w/imgW, (double)h/imgH)); // 只缩小不放大
        int drawW = (int)(imgW * scale);
        int drawH = (int)(imgH * scale);
        int x = (w - drawW) / 2;
        int y = (h - drawH) / 2;
        BufferedImage canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(250,246,247,0)); // 背景透明
        g2.fillRect(0, 0, w, h);
        g2.drawImage(icon.getImage(), x, y, drawW, drawH, null);
        g2.dispose();
        return new ImageIcon(canvas);
    }

    /**
     * 显示歌手和音乐收藏树的统计信息
     */
    private void showArtistInfoStats() {
        MyBinaryTree musicTree = playlistManager.getMusicTree();
        StringBuilder stats = new StringBuilder();

        stats.append("--- 音乐收藏二叉树统计 ---\n");
        stats.append("树的深度: ").append(musicTree.getDepth()).append("\n");
        stats.append("歌曲总数 (叶子节点数): ").append(musicTree.getLeafCount()).append("\n\n");

        stats.append("树的结构 (前序遍历):\n");
        appendTreeStructure(stats, musicTree.getRoot(), 0);
        
        JOptionPane.showMessageDialog(this, 
            new JScrollPane(new JTextArea(stats.toString())),
            "歌手信息统计",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 递归辅助方法，用于生成带缩进的树结构字符串。
     */
    private void appendTreeStructure(StringBuilder builder, Node node, int indent) {
        if (node == null) return;
        
        for (int i = 0; i < indent; i++) {
            builder.append("  "); // 2个空格作为缩进单位
        }
        
        if (node.value instanceof Song) {
            builder.append("└ 歌曲: ").append(((Song)node.value).getTitle()).append("\n");
        } else {
            builder.append("└ 节点: ").append(node.value.toString()).append("\n");
        }

        appendTreeStructure(builder, node.left, indent + 1);
        appendTreeStructure(builder, node.right, indent + 1);
    }

    /**
     * 显示歌手统计信息
     */
    private void showArtistStats(String artist) {
        // 这个功能在新模型下需要重新设计，暂时禁用
        JOptionPane.showMessageDialog(this, "此功能在新模型下待定。", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MusicPlayerUI().setVisible(true);
        });
    }
} 