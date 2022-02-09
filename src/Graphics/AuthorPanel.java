package Graphics;

import App.App;
import Music.Album;
import Music.Library;
import Music.MediaPlayer;
import Music.Song;
import Users.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class AuthorPanel. Class that contains all the information to make the author window.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AuthorPanel extends JPanel {
    JTable songs;
    JTable albums;
    ArrayList<Song> top3;
    SortedSet<Album> sortedAlbums;
    MidPanel midPanel;

    JScrollPane infoScrollPane;
    public AuthorPanel(User artist, MidPanel midPanel) {
        super();
        this.setLayout(new GridBagLayout());
        this.midPanel = midPanel;

        JLabel artistName = new JLabel(artist == null ? "":artist.getArtisticname());
        JLabel profilePicture;
        ImageIcon profile;
        Image image;
        JPanel infoPanel = new JPanel(new GridBagLayout());
        JLabel topsongs = new JLabel("TOP SONGS");
        JLabel albumSingles = new JLabel("ALBUM AND SINGLES");
        GridBagConstraints constraints = new GridBagConstraints();


        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10, 10, 0, 0);


        infoPanel.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        artistName.setFont(new Font("Monospaced", Font.BOLD, 25));
        artistName.setForeground(Color.WHITE);

        topsongs.setFont(new Font("Monospaced", Font.BOLD, 25));
        topsongs.setForeground(Color.WHITE);
        albumSingles.setFont(new Font("Monospaced", Font.BOLD, 25));
        albumSingles.setForeground(Color.WHITE);

        //Set profile picture
        profile =  new ImageIcon(artist == null? "img/user_opt.png":artist.getProfilePath());
        image = profile.getImage(); // transform it
        image = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        profilePicture = new JLabel (new ImageIcon(image));

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(70,10,0,0);
        this.add(profilePicture, constraints);

        constraints.insets = new Insets(68, 150, 0,0);
        this.add(artistName, constraints);
        if(App.getLogged() != null) {
            JToggleButton toggleButton;

            if (App.getLogged().getFollowing().contains(artist)) {
                toggleButton = new JToggleButton("UNFOLLOW");
                toggleButton.setSelected(false);

            } else {
                toggleButton = new JToggleButton("FOLLOW");
                toggleButton.setSelected(true);
            }

            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (toggleButton.isSelected()) {
                        toggleButton.setText("FOLLOW");

                        App.getLogged().unfollow(artist);

                        if (App.demo) {
                            JOptionPane.showMessageDialog(getParent(), "You just unfollowed this author!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        toggleButton.setText("UNFOLLOW");
                        App.getLogged().follow(artist);

                        if(App.demo) {
                            JOptionPane.showMessageDialog(getParent(), "You just followed this author!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }
            });

            toggleButton.setBackground(Color.white);
            toggleButton.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
            toggleButton.setFont(new Font("Monospaced", Font.PLAIN, 15));
            constraints.insets = new Insets(120, 150, 0,0);
            this.add(toggleButton, constraints);
        }



        ArrayList<String> datos1 = new ArrayList<>();
        if(artist != null) {
            top3 = App.getInstance().getAdministrator().getTopSongs(new Date(), 3, artist);
            for (Song song : top3) {
                datos1.add(song.getTitle());
            }
        }

        if(datos1.size() == 0){
            datos1.add("There are no songs here yet!");
        }
        DefaultTableModel model1 = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model1.addColumn("TOP SONGS", datos1.toArray());
        songs = new JTable(model1);
        songs.setRowHeight(40);
        songs.setTableHeader(null);
        songs.setFont(new Font("Monospaced", Font.PLAIN, 20));
        songs.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        songs.setForeground(Color.WHITE);
        songs.addMouseListener(new PopClickListener());

        ArrayList<String> datos2 = new ArrayList<>();
        sortedAlbums = new TreeSet<>();

        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getAuthors().contains(artist)) {
                sortedAlbums.add(album);
            }
        }
        for (Album album : sortedAlbums) {
            datos2.add(album.getTitle());
        }
        if(datos2.size() == 0){
            datos2.add("There are no albums here yet!");
        }
        DefaultTableModel model2 = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model2.addColumn("ALBUMS AND SINGLES", datos2.toArray());
        albums = new JTable(model2);
        albums.setRowHeight(40);
        albums.setTableHeader(null);
        albums.setFont(new Font("Monospaced", Font.PLAIN, 20));
        albums.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        albums.setForeground(Color.WHITE);



        constraints.insets = new Insets(10, 0, 0,0);
        infoPanel.add(topsongs, constraints);

        constraints.insets = new Insets(40+60*datos1.size(), 0, 0, 0);
        infoPanel.add(albumSingles, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(70+60*datos1.size(), 0, 0,0);
        infoPanel.add(albums, constraints);


        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(40, 0, 0,0);
        infoPanel.add(songs, constraints);
        constraints.gridy = 0;
        constraints.insets = new Insets(200, 0, 0 , 0);

        infoScrollPane = new JScrollPane(infoPanel);
        this.add(infoScrollPane, constraints);



        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        constraints.insets = new Insets(10,0,0,10);
        if(!App.getInstance().isAdminLogged) {
            JLabel gobackIcon = new JLabel(new ImageIcon("img/go_back_opt.png"));
            gobackIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    midPanel.change("inicio");

                }
            });
            this.add(gobackIcon, constraints);
        }
    }

    /**
     * Class PopUpMenu. Pop-up menu used to display options on top of the songs in an author profile.
     */
    class PopUpMenu extends JPopupMenu {
        JMenuItem item1;
        JMenuItem item2;
        JMenuItem item3;

        public PopUpMenu(MouseEvent e) {
            JTable source = (JTable)e.getSource();
            int row = source.rowAtPoint( e.getPoint() );

            item1 = new JMenuItem("Add to queue");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MediaPlayer.getInstance().addToQueue(top3.get(row));

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "You just added this song to your queue!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            add(item1);
            item2 = new JMenuItem("Report song");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App.getInstance().getLogged().report(top3.get(row));

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "You just reported this song!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            add(item2);
            item3 = new JMenuItem("Add to playlist...");
            item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MediaPlayer.getInstance().addToQueue(top3.get(row));
                }
            });
            add(item3);
        }
    }
    class DoClickListener extends  MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                Album album = (Album) sortedAlbums.toArray()[row];
                midPanel.change(album);
            }
        }

    }
    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
            else if (e.getClickCount() == 2) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                MediaPlayer.getInstance().startPlaying(top3.get(row));
            }
        }

        /*public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }*/

        private void doPop(MouseEvent e) {
            PopUpMenu menu = new PopUpMenu(e);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
