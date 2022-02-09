package Graphics;

import App.App;
import Music.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Class AlbumPanel. Class that contains the information of the album window.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AlbumPanel extends JPanel {
    private Album album;
    JTable table;
    MidPanel midPanel;

    public AlbumPanel(Album album, MidPanel midPanel) {
        super();
        this.midPanel = midPanel;
        this.setLayout(new GridBagLayout());
        this.album = album;

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        GridBagConstraints c1 = new GridBagConstraints();

        ImageIcon cover =  new ImageIcon(album.get(0).getCoverPath());
        Image image = cover.getImage(); // transform it
        image = image.getScaledInstance(350, 350,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        JLabel icon = new JLabel(new ImageIcon(image));
        JLabel title = new JLabel(album.getTitle());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JLabel author = new JLabel(album.getAuthorsAsString());
        author.setForeground(Color.WHITE);
        author.setFont(new Font("Monospaced", Font.PLAIN, 18));
        JLabel size = new JLabel( album.size()+" songs");
        size.setForeground(Color.WHITE);
        size.setFont(new Font("Monospaced", Font.PLAIN, 18));
        JButton playButton = new JButton("Play");
        JLabel plusIcon = new JLabel(new ImageIcon("img/plus_opt.png"));
        plusIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu playl = new JPopupMenu("Add to playlist");
                for(int i = 0; i < App.getLogged().getPlaylists().size(); i++){
                    Playlist temp = App.getLogged().getPlaylists().get(i);
                    JMenuItem item = new JMenuItem(temp.getTitle());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            temp.addAgrupation(album);
                            Home.getInstance().updatePlaylists();
                        }
                    });
                    playl.add(item);
                }
                JMenuItem create = new JMenuItem("Create playlist");
                create.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = JOptionPane.showInputDialog(getParent(), "Please insert a name for the playlist", "New Playlist", JOptionPane.INFORMATION_MESSAGE);
                        if(album.size() == 0) {
                            Library.getInstance().createPlaylist(name, App.getLogged());

                        } else {
                            Library.getInstance().createPlaylist(name, App.getLogged(), album);
                        }
                        Home.getInstance().updatePlaylists();

                    }
                });
                playl.add(create);
                add(playl);
                playl.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        JLabel gobackIcon = new JLabel(new ImageIcon("img/go_back_opt.png"));

        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    MediaPlayer.getInstance().startPlaying(album, false, false);
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

        c1.insets = new Insets(0,380,50,0);
        this.add(title, c1);

        c1.insets = new Insets(30, 380, 20, 0);
        this.add(author, c1);

        c1.insets = new Insets(90, 380, 20,0);
        this.add(size, c1);

        c1.anchor = GridBagConstraints.CENTER;
        c1.insets = new Insets(60,0,0,0);
        this.add(playButton, c1);

        c1.insets = new Insets(60, 150,0,0);
        this.add(plusIcon, c1);

        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.insets = new Insets(10,0,0,10);

        gobackIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                midPanel.change("inicio");
                validate();
                repaint();
            }
        });
        this.add(gobackIcon, c1);

        c1.gridy = 1;
        c1.ipady = 400;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.BOTH;
        c1.insets = new Insets(0,0,0,0);

        ArrayList<String> datos = new ArrayList<>();
        for(Song song: album.getSongs()) {
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
     * Class PopClickListener. Pop-up menu used to display options on top of the album songs.
     */
    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
            else if (e.getClickCount() == 2) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                MediaPlayer.getInstance().startPlaying(album.getSongs().get(row));
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
            PopUpMenu menu = new PopUpMenu(e);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Class PopUpMenu. Pop-up menu used to display options on top of the album songs.
     */
    class PopUpMenu extends JPopupMenu {
        JMenuItem anItem;
        public PopUpMenu(MouseEvent e) {
            JTable source = (JTable)e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            JMenu playlist = new JMenu("Add to playlist");
            for(int i = 0; i < App.getLogged().getPlaylists().size(); i++){
                Playlist temp = App.getLogged().getPlaylists().get(i);
                JMenuItem item = new JMenuItem(temp.getTitle());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        temp.addSong(album.getSongs().get(row));
                    }
                });
                playlist.add(item);
            }
            JMenuItem create = new JMenuItem("Create playlist");
            create.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog(getParent(), "Please insert a name for the playlist", "New Playlist", JOptionPane.INFORMATION_MESSAGE);
                    if(album.getSongs().size() == 0) {
                        Library.getInstance().createPlaylist(name, App.getLogged());

                    } else {
                        Library.getInstance().createPlaylist(name, App.getLogged(), album.getSongs().get(row));
                    }
                }
            });
            playlist.add(create);
            add(playlist);

            anItem = new JMenuItem("Report");
            anItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    App.getInstance().getLogged().report(album.getSongs().get(row));

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "You just reported this song!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            add(anItem);

            anItem = new JMenuItem("Add to queue");
            anItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    MediaPlayer.getInstance().addToQueue(album.getSongs().get(row));

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "Song added! If you'd wanted to report the song, \n " +
                                        "you'll just have to click on report. You can also add it to one of your\n playlists if you already created " +
                                        "one! (Or you can create one from there)\n Let's go back by clicking the arrow!",
                                "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }

                }
            });
            add(anItem);
        }
    }
}
