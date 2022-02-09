package Graphics;

import App.App;
import Music.Library;
import Music.MediaPlayer;
import Music.Playlist;
import Music.Song;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Class PlaylistPanel. Class that contains all the code to create the playlist window in the home screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class PlaylistPanel extends JPanel {
    private Playlist playlist;
    JTable table;
    MidPanel midPanel;

    public PlaylistPanel(Playlist playlist, MidPanel midPanel) {
        super();
        this.setLayout(new GridBagLayout());
        this.playlist = playlist;
        this.midPanel = midPanel;

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        GridBagConstraints c1 = new GridBagConstraints();

        JLabel icon = new JLabel(new ImageIcon("img/music_opt.png"));
        JLabel title = new JLabel(playlist.getTitle());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JLabel author = new JLabel(App.getInstance().getLogged() == null ? "Neons" : App.getInstance().getLogged().getArtisticname());
        author.setForeground(Color.WHITE);
        author.setFont(new Font("Monospaced", Font.PLAIN, 18));
        JLabel size = new JLabel( "songs");
        size.setForeground(Color.WHITE);
        size.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JButton playButton = new JButton("Play");
        JLabel gobackIcon = new JLabel(new ImageIcon("img/go_back_opt.png"));

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    MediaPlayer.getInstance().startPlaying(playlist, false, false);
                    Home.getInstance().getPlayer().startPlaying();
                }
            }
        });


        c1.weightx = 0.2;
        c1.weighty = 0.2;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.LINE_START;
        c1.insets = new Insets(0,10,0,0);
        this.add(icon, c1);

        c1.insets = new Insets(0,100,50,0);
        this.add(title, c1);

        c1.insets = new Insets(30, 100, 20, 0);
        this.add(author, c1);

        c1.insets = new Insets(60, 100, 0,0);
        this.add(size, c1);

        c1.anchor = GridBagConstraints.CENTER;
        this.add(playButton, c1);

        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.insets = new Insets(10,0,0,10);
        gobackIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                midPanel.change("inicio");

            }
        });
        this.add(gobackIcon, c1);

        c1.gridy = 1;
        c1.ipady = 400;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.BOTH;
        c1.insets = new Insets(0,0,0,0);

        ArrayList<String> datos = new ArrayList<>();
        for (Song song:playlist.getSongs()) {
            datos.add(song.getTitle());
        }

        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Songs", datos.toArray());
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);
        table.setFont(new Font("Monospaced", Font.PLAIN, 20));
        table.setTableHeader(null);
        table.setForeground(Color.WHITE);
        table.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        table.addMouseListener(new PopClickListener());
        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane, c1);

        this.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));


    }

    /**
     * Class PopClickListener. Class that plays a song from a playlist when double clik.
     */
    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
            else if (e.getClickCount() == 2) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                MediaPlayer.getInstance().startPlaying(playlist.getSongs().get(row));
                Home.getInstance().getPlayer().startPlaying();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            PopUpMenu menu = new PopUpMenu(e);/*
            menu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    System.out.println("hola");
                }
            });*/
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Class PopUpMenu. Displays options to add songs to a playlist or create the playlist, report a song
     * or delete it from the playlist when you right click.
     */
    class PopUpMenu extends JPopupMenu {
        JMenuItem anItem;
        public PopUpMenu(MouseEvent e) {
            JTable source = (JTable)e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            if(App.getLogged() != null) {
                JMenu playl = new JMenu("Add to playlist");
                for (int i = 0; i < App.getLogged().getPlaylists().size(); i++) {
                    Playlist temp = App.getLogged().getPlaylists().get(i);
                    JMenuItem item = new JMenuItem(temp.getTitle());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            temp.addSong(playlist.getSongs().get(row));
                        }
                    });
                    playl.add(item);
                }
                JMenuItem create = new JMenuItem("Create playlist");
                create.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = JOptionPane.showInputDialog(getParent(), "Please insert a name for the playlist", "New Playlist", JOptionPane.INFORMATION_MESSAGE);
                        if (playlist.getSongs().size() == 0) {
                            Library.getInstance().createPlaylist(name, App.getLogged());

                        } else {
                            Library.getInstance().createPlaylist(name, App.getLogged(), playlist.getSongs().get(row));
                        }
                    }
                });
                playl.add(create);
                add(playl);
            }
            anItem = new JMenuItem("Report");
            anItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App.getInstance().getLogged().report(playlist.getSongs().get(row));
                    JOptionPane.showMessageDialog(getParent(), "You just reported this song!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            add(anItem);
            if (App.demo) {
                JOptionPane.showMessageDialog(getParent(), "You just reported this song!", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }
            add(anItem);

            anItem = new JMenuItem("Delete");
            anItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App.getInstance().getLib().removeSongFromPlaylist(playlist.getSongs().get(row), playlist);

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "You just deleted this song from your playlist!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            add(anItem);

            anItem = new JMenuItem("Add to queue");
            anItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    MediaPlayer.getInstance().addToQueue(playlist.getSongs().get(row));

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "Song added! If you'd wanted to report the song, \n " +
                                        "you'll just have to click on report. You can also add it to one of your\n playlists if you already created " +
                                        "one! (Or you can create one from there)\n Let's go back by clicking the arrow! (If you want \n" +
                                        "to delete the song from the playlist, just click on Delete.",
                                "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            add(anItem);
        }
    }
}
