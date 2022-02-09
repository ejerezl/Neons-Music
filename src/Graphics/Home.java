package Graphics;

import App.App;
import Music.Album;
import Music.Library;
import Music.MediaPlayer;
import Music.Playlist;
import Users.Notifications.Notification;
import Users.User;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Class Home. Class that contains all the information to create the home (main) window of the app.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class Home extends JPanel{
    private App app;
    private static Home instance;
    private MidPanel mid;
    private ArrayList<Album> allAlbums;
    private ArrayList<Playlist> allPlaylists;
    private PlayerPanel player;
    private JPanel playlists;
    private JLabel oops2;
    private JScrollPane scrollPlaylist;
    private ArrayList<String> datospl;
    private DefaultTableModel model;
    private JTable playlistList;

    private DefaultTableModel modelpl;
    public Home(App app) {
        super();
        Home.instance = this;
        this.app = app;
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        mid = new MidPanel();

        JPanel avisos = new JPanel(new GridBagLayout());
        playlists = new JPanel(new GridBagLayout());
        JPanel albums = new JPanel(new GridBagLayout());
        player = new PlayerPanel();
        JPanel settings = new JPanel(new GridBagLayout());
        avisos.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        playlists.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        settings.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        albums.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        GridBagConstraints c1 = new GridBagConstraints();

        JLabel news = new JLabel("News from people you follow...");
        news.setForeground(Color.WHITE);
        c1.weightx = 0.5;
        c1.weighty = 0.5;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.PAGE_START;
        c1.insets = new Insets(50,0,0,10);

        avisos.add(news, c1);
        if(App.getLogged() == null){

            JLabel oops = new JLabel("Oops! You're not registered!");
            oops.setForeground(Color.WHITE);
            oops.setFont(new Font("Monospaced", Font.PLAIN, 14));

            JButton signUp = new JButton("Sign up");
            signUp.setPreferredSize(new Dimension(100, 40));
            signUp.addActionListener(new openLogin());



            c1.insets = new Insets(0,0,0,0);
            c1.gridy = 1;
            c1.anchor = GridBagConstraints.PAGE_START;

            avisos.add(oops, c1);

            c1.insets = new Insets(0,0,100,0);
            c1.anchor = GridBagConstraints.CENTER;

            avisos.add(signUp, c1);
        }else{
            if(App.getLogged().getNotifications().size() == 0){
                JLabel oops = new JLabel("No news over here");
                oops.setForeground(Color.WHITE);
                oops.setFont(new Font("Monospaced", Font.PLAIN, 14));
                c1.insets = new Insets(0,0,0,0);
                c1.gridy = 1;
                c1.anchor = GridBagConstraints.PAGE_START;

                avisos.add(oops, c1);
            }else{
                JTable notList;
                ArrayList<String> datosList = new ArrayList<>();
                for(Notification not: App.getLogged().getNotifications()){
                    datosList.add(not.toString());
                }
                DefaultTableModel modelNot = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                modelNot.addColumn("title", datosList.toArray());
                notList = new JTable(modelNot);
                notList.setRowHeight(30);
                notList.setTableHeader(null);
                notList.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                notList.setForeground(Color.white);
                notList.setFillsViewportHeight(true);
                JScrollPane scrollNot = new JScrollPane(notList);
                scrollNot.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                scrollNot.setForeground(Color.WHITE);
                c1.weightx = 0.3;
                c1.weighty = 0.3;
                c1.ipadx = 0;
                c1.ipady = 0;
                c1.gridx = 0;
                c1.gridy = 0;
                c1.fill = GridBagConstraints.BOTH;
                c1.insets = new Insets(30,0,0,0);
                avisos.add(scrollNot, c1);
            }

        }

        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 200;
        constraints.ipady = 600;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 3;

        avisos.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        avisos.setMinimumSize(new Dimension(200,600));

        this.add(avisos, constraints);

        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.ipadx = 0;
        constraints.ipady = 70;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;


        this.add(player, constraints);


        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.ipadx = 700;
        constraints.ipady = 600;
        constraints.gridheight = 3;
        constraints.gridwidth = 1;

        mid.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        this.add(mid, constraints);



        JLabel yourPlaylists = new JLabel("Your playlists...");
        yourPlaylists.setForeground(Color.WHITE);
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.NONE;
        c1.insets = new Insets(10,10,0,0);
        playlists.add(yourPlaylists, c1);

        JLabel plusIcon;
        JLabel plusIcon2;
        ImageIcon profile =  new ImageIcon("img/plus_opt.png");
        Image image = profile.getImage(); // transform it
        image = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        plusIcon = new JLabel (new ImageIcon(image));
        plusIcon2 = new JLabel(new ImageIcon(image));

        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.insets= new Insets(6, 0, 0 ,10);
        plusIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = JOptionPane.showInputDialog("Please write a name for the playlist");
                Playlist playlist = new Playlist(name, App.getLogged());
                App.getLogged().addPlaylist(playlist);
                if(App.demo){
                    JOptionPane.showMessageDialog(getParent(), "Playlist created! Now you can listen to it by clicking on its name.\n" +
                            "(Although currently it's empty...)", "Demo", JOptionPane.INFORMATION_MESSAGE);
                }
                updatePlaylists();
                validate();
                repaint();

            }

        });

        plusIcon2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomeFrame.getInstance().change("upload");
            }

        });
        albums.add(plusIcon2, c1);
        playlists.add(plusIcon, c1);
        //JScrollBar scroll1 = new JScrollBar();
        if(App.getLogged() == null){
            JLabel oops2 = new JLabel("Oops! You're not registered!");
            oops2.setForeground(Color.WHITE);
            oops2.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JButton signUp2 = new JButton("Sign up");
            signUp2.setPreferredSize(new Dimension(100, 40));
            signUp2.addActionListener(new openLogin());


            /*c1.anchor = GridBagConstraints.LINE_END;
            c1.fill = GridBagConstraints.VERTICAL;
            c1.insets = new Insets(5,0,5,0);
            playlists.add(scroll1, c1);*/

            c1.anchor = GridBagConstraints.CENTER;
            c1.insets = new Insets(0,0,0,0);
            playlists.add(oops2, c1);

            c1.insets = new Insets(65,0,0,0);
            playlists.add(signUp2, c1);
        }else{
            if(App.getLogged().getPlaylists().isEmpty()){
                oops2 = new JLabel("Oops! You don't have any playlist!");
                oops2.setForeground(Color.WHITE);
                oops2.setFont(new Font("Monospaced", Font.PLAIN, 14));
                c1.anchor = GridBagConstraints.CENTER;
                c1.insets = new Insets(0,0,0,0);
                playlists.add(oops2, c1);

                c1.anchor = GridBagConstraints.FIRST_LINE_END;
                datospl = new ArrayList<>();
                allPlaylists = App.getLogged().getPlaylists();
                for(Playlist playlist: allPlaylists){
                    datospl.add(playlist.getTitle());
                }
                modelpl = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                modelpl.addColumn("title", datospl.toArray());
                playlistList = new JTable(modelpl);
                playlistList.setRowHeight(30);
                playlistList.setTableHeader(null);
                playlistList.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                playlistList.setForeground(Color.white);
                playlistList.setFillsViewportHeight(true);
                playlistList.addMouseListener(new PopClickListenerPlaylist());
                scrollPlaylist = new JScrollPane(playlistList);
                scrollPlaylist.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                scrollPlaylist.setForeground(Color.WHITE);
                c1.weightx = 0.3;
                c1.weighty = 0.3;
                c1.ipadx = 0;
                c1.ipady = 0;
                c1.gridx = 0;
                c1.gridy = 0;
                c1.fill = GridBagConstraints.BOTH;
                c1.insets = new Insets(30,0,0,0);
                scrollPlaylist.setVisible(false);
                playlists.add(scrollPlaylist, c1);
            }else{
                c1.anchor = GridBagConstraints.FIRST_LINE_END;
                datospl = new ArrayList<>();
                allPlaylists = App.getLogged().getPlaylists();
                for(Playlist playlist: allPlaylists){
                    datospl.add(playlist.getTitle());
                }
                modelpl = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                modelpl.addColumn("title", datospl.toArray());
                playlistList = new JTable(modelpl);
                playlistList.setRowHeight(30);
                playlistList.setTableHeader(null);
                playlistList.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                playlistList.setForeground(Color.white);
                playlistList.setFillsViewportHeight(true);
                playlistList.addMouseListener(new PopClickListenerPlaylist());
                scrollPlaylist = new JScrollPane(playlistList);
                scrollPlaylist.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                scrollPlaylist.setForeground(Color.WHITE);
                c1.weightx = 0.3;
                c1.weighty = 0.3;
                c1.ipadx = 0;
                c1.ipady = 0;
                c1.gridx = 0;
                c1.gridy = 0;
                c1.fill = GridBagConstraints.BOTH;
                c1.insets = new Insets(30,0,0,0);

                playlists.add(scrollPlaylist, c1);
            }
        }


        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.ipadx = 250;
        constraints.ipady = 300;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridheight = 1;

        playlists.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        playlists.setMinimumSize(new Dimension(200, 300));

        this.add(playlists, constraints);

        JLabel yourAlbums = new JLabel("Your albums...");
        yourAlbums.setForeground(Color.WHITE);
        c1.insets = new Insets(10,10,0,0);
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.NONE;
        albums.add(yourAlbums, c1);
        if(App.getLogged() == null){
            //JScrollBar scroll2 = new JScrollBar();
            JLabel oops3 = new JLabel("Oops! You're not registered!");
            oops3.setForeground(Color.WHITE);
            oops3.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JButton signUp3 = new JButton("Sign up");
            signUp3.setPreferredSize(new Dimension(100, 40));
            signUp3.addActionListener(new openLogin());


            /*c1.fill = GridBagConstraints.VERTICAL;
            c1.anchor = GridBagConstraints.LINE_END;
            c1.insets = new Insets(5,0,5,0);

            albums.add(scroll2, c1);*/

            c1.anchor = GridBagConstraints.CENTER;
            c1.insets = new Insets(0,0,0,0);
            albums.add(oops3, c1);

            c1.insets = new Insets(65,0,0,0);
            albums.add(signUp3, c1);

        }else{
            if(Library.getInstance().getAlbums(App.getLogged()).size() == 0){
                JLabel oops3 = new JLabel("Looks like there's nothing here yet!");
                oops3.setForeground(Color.WHITE);
                oops3.setFont(new Font("Monospaced", Font.PLAIN, 14));
                JButton signUp3 = new JButton("Upload music");
                signUp3.setPreferredSize(new Dimension(150, 40));
                signUp3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        HomeFrame.getInstance().change("upload");
                    }
                });

                /*c1.fill = GridBagConstraints.VERTICAL;
                c1.anchor = GridBagConstraints.LINE_END;
                c1.insets = new Insets(5,0,5,0);

                albums.add(scroll2, c1);*/

                c1.anchor = GridBagConstraints.CENTER;
                c1.insets = new Insets(0,0,0,0);
                albums.add(oops3, c1);

                c1.insets = new Insets(65,0,0,0);
                albums.add(signUp3, c1);
            }else{
                JTable albumList;
                ArrayList<String> datos = new ArrayList<>();
                allAlbums = Library.getInstance().getAlbums(App.getLogged());
                for(Album album: allAlbums){
                    datos.add(album.getTitle());
                }
                model = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model.addColumn("title", datos.toArray());
                albumList = new JTable(model);
                albumList.setRowHeight(30);
                albumList.setTableHeader(null);
                albumList.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                albumList.setForeground(Color.white);
                albumList.setFillsViewportHeight(true);
                albumList.addMouseListener(new PopClickListenerAlbum());
                JScrollPane scrollAlbum = new JScrollPane(albumList);
                scrollAlbum.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                scrollAlbum.setForeground(Color.WHITE);
                c1.weightx = 0.3;
                c1.weighty = 0.3;
                c1.ipadx = 0;
                c1.ipady = 0;
                c1.gridx = 0;
                c1.gridy = 0;
                c1.fill = GridBagConstraints.BOTH;
                c1.insets = new Insets(30,0,0,0);

                albums.add(scrollAlbum, c1);

            }
        }
        c1.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.ipadx = 170;
        constraints.ipady = 300;
        constraints.gridx = 2;
        constraints.gridy = 1;

        albums.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        albums.setMinimumSize(new Dimension(200,300));

        this.add(albums, constraints);

        JLabel icon = new JLabel();
        profile =  new ImageIcon("img/settings_opt.png");
        image = profile.getImage(); // transform it
        image = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon.setIcon(new ImageIcon(image));
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomeFrame.getInstance().change("settings");


            }
        });
        JLabel logOutIcon = new JLabel();
        logOutIcon.setIcon(new ImageIcon("img/logout_opt.png"));
        logOutIcon.setPreferredSize(new Dimension(50, 50));
        logOutIcon.addMouseListener(new openLogin());
        logOutIcon.setPreferredSize(new Dimension(50, 50));

        JLabel profilePicture = new JLabel();
        if (App.getInstance().getLogged() == null) {
            profile =  new ImageIcon("img/Icon_White_opt.png");
            image = profile.getImage(); // transform it
            image = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        } else {
            profile =  new ImageIcon(App.getInstance().getLogged().getProfilePath());
            image = profile.getImage(); // transform it
            image = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        }
        profilePicture.setIcon(new ImageIcon(image));

        JLabel user = new JLabel(App.getLogged() == null ? "Anonymous":App.getLogged().getArtisticname());
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Monospaced", Font.PLAIN, 16));

        c1.insets = new Insets(4,10,0,0);
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        settings.add(icon, c1);

        c1.insets = new Insets(4,35,0,0);
        settings.add(profilePicture, c1);

        c1.insets = new Insets(0,10,0,0);
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        settings.add(logOutIcon, c1);

        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.CENTER;
        c1.fill = GridBagConstraints.NONE;
        c1.insets = new Insets(0,0,0,30);

        settings.add(user, c1);

        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 150;

        settings.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        settings.setMinimumSize(new Dimension(130,100));


        this.add(settings, constraints);
        this.setMinimumSize(new Dimension(1300, 700));
        this.setVisible(true);

    }

    /**
     * Private class openLogin. Used when logout to set the login window.
     */
    private class openLogin extends MouseAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            exit(e);
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            exit(e);
        }
        void exit(AWTEvent e){
            if(MediaPlayer.getInstance() != null){
                MediaPlayer.getInstance().stopPlaying();
            }
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            App.setLogged(null);
            new LoginWindow();
        }
    }

    /**
     * Private class PopClickListenerAlbum. Used to change panels to the one of the album you've chosen.
     */
    private class PopClickListenerAlbum extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                mid.change(allAlbums.get(row));
            }

        }
    }

    /**
     * Private class PopClickListenerPlaylist. Used to change panels to the one of the playlist you've chosen.
     */
    private class PopClickListenerPlaylist extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                mid.change(allPlaylists.get(row));
            }

        }
    }

    public void updatePlaylists(){
        playlists.remove(scrollPlaylist);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        datospl = new ArrayList<>();
        allPlaylists = App.getLogged().getPlaylists();
        for(Playlist playlist: allPlaylists){
            datospl.add(playlist.getTitle());
        }
        modelpl = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelpl.addColumn("title", datospl.toArray());
        playlistList = new JTable(modelpl);
        playlistList.setRowHeight(30);
        playlistList.setTableHeader(null);
        playlistList.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        playlistList.setForeground(Color.white);
        playlistList.setFillsViewportHeight(true);
        playlistList.addMouseListener(new PopClickListenerPlaylist());
        scrollPlaylist = new JScrollPane(playlistList);
        scrollPlaylist.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        scrollPlaylist.setForeground(Color.WHITE);
        c1.weightx = 0.3;
        c1.weighty = 0.3;
        c1.ipadx = 0;
        c1.ipady = 0;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.fill = GridBagConstraints.BOTH;
        c1.insets = new Insets(30,0,0,0);
        playlists.add(scrollPlaylist, c1);
        validate();
        repaint();
    }

    public PlayerPanel getPlayer(){
        return player;
    }
    public static Home getInstance(){
        return instance;
    }
}
