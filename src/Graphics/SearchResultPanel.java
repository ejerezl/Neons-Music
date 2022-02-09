package Graphics;

import App.App;
import Music.*;
import Users.AdminUser;
import Users.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class SearchResultPanel Class that implements the panel to show the results from a search.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class SearchResultPanel extends JPanel {

    ArrayList<String> datos;
    JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    GridBagConstraints c1;
    JComboBox search;
    JTextField searchText;
    ArrayList<Song> songs;
    ArrayList<Album> albums;
    ArrayList<User> authors;
    MidPanel mid;

    public SearchResultPanel(MidPanel mid) {
        super();
        this.setLayout(new GridBagLayout());

        this.mid = mid;

        c1 = new GridBagConstraints();
        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        String[] searching = {"By song", "By album", "By author"};
        search = new JComboBox(searching);
        search.setPreferredSize(new Dimension(110, 40));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getData();
            }
        });
        searchText = new JTextField();

        searchText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getData();
            }


        });
        searchText.setPreferredSize(new Dimension(250, 40));

        c1.weightx = 0.2;
        c1.weighty = 0.2;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.insets = new Insets(70, 0, 0, 30);

        this.add(search, c1);

        c1.insets = new Insets(70, 10, 0, 150);
        c1.weightx = 0.6;
        c1.weighty = 0.6;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.HORIZONTAL;

        this.add(searchText, c1);


        datos = new ArrayList<>();

        update();
        this.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));

        this.setVisible(true);
    }
    public void focus(){
        searchText.requestFocusInWindow();
    }
    public void update(){
        if(scrollPane != null){
            this.remove(scrollPane);
        }
        c1.gridy = 1;
        c1.ipady = 400;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.BOTH;
        c1.insets = new Insets(0,0,0,0);

        model = new DefaultTableModel() {

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
        scrollPane = new JScrollPane(table);
        this.add(scrollPane, c1);
        validate();
        repaint();
    }
    public void getData(){
        switch (search.getSelectedIndex()){
            case 0:
                songs = Search.searchSong(Library.getInstance(), searchText.getText());
                datos = songs.stream().map(song -> song.getTitle()).collect(Collectors.toCollection(ArrayList::new));
                break;
            case 1:
                albums = Search.searchAlbum(Library.getInstance(), searchText.getText());
                datos = albums.stream().map(album -> album.getTitle()).collect(Collectors.toCollection(ArrayList::new));
                break;
            default:
                authors = Search.searchAuthor(AdminUser.getInstance(), searchText.getText());
                datos = authors.stream().map( author -> author.getArtisticname()).collect(Collectors.toCollection(ArrayList::new));
        }
        update();
    }

    /**
     * Class PopClickListener. Pop-up menu used to display options on top of the album songs.
     */
    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
            else if(e.getClickCount() == 2) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                if(row < 0){
                    return;
                }
                if(search.getSelectedItem().equals("By song")) {
                    if(row < songs.size()){
                        MediaPlayer.getInstance().startPlaying(songs.get(row));
                        Home.getInstance().getPlayer().startPlaying();
                    }
                } else if(search.getSelectedItem().equals("By album")) {
                    if(row < albums.size()){
                        mid.change(albums.get(row));
                    }
                } else {
                    if(row < authors.size()){
                        mid.change(authors.get(row));
                    }

                }
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
            menu.addMouseListener(new MouseAdapter() {
                /*@Override
                public void mouseClicked(MouseEvent e){
                    System.out.println("hola");
                }*/
            });
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Class PopUpMenu. Pop-up menu used to display options on top of the album songs.
     */
    class PopUpMenu extends JPopupMenu {
        JMenuItem anItem;
        public PopUpMenu(MouseEvent e) {
            if(search.getSelectedItem().equals("By song") || search.getSelectedItem().equals("By album")) {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                JMenu playl = new JMenu("Add to playlist");
                for(int i = 0; i < App.getLogged().getPlaylists().size(); i++){
                    Playlist temp = App.getLogged().getPlaylists().get(i);
                    JMenuItem item = new JMenuItem(temp.getTitle());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            temp.addSong(songs.get(row));
                        }
                    });
                    playl.add(item);
                }
                JMenuItem create = new JMenuItem("Create playlist");
                if(search.getSelectedItem().equals("By song")) {
                    create.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String name = JOptionPane.showInputDialog(getParent(), "Please insert a name for the playlist", "New Playlist", JOptionPane.INFORMATION_MESSAGE);
                            if(songs.size() == 0) {
                                Library.getInstance().createPlaylist(name, App.getLogged());

                            } else {
                                Library.getInstance().createPlaylist(name, App.getLogged(), songs.get(row));
                            }
                        }
                    });
                    playl.add(create);
                    add(playl);
                    anItem = new JMenuItem("Report");
                    anItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            App.getInstance().getLogged().report(songs.get(row));

                            JOptionPane.showMessageDialog(getParent(), "You just reported this song!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    add(anItem);

                    anItem = new JMenuItem("Add to queue");
                    anItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            MediaPlayer.getInstance().addToQueue(songs.get(row));

                            JOptionPane.showMessageDialog(getParent(), "Song added! If you'd wanted to report the song, \n " +
                                            "you'll just have to click on report. You can also add it to one of your\n playlists if you already created " +
                                            "one! (Or you can create one from there)\n Let's go back by clicking the arrow!",
                                    "Demo", JOptionPane.INFORMATION_MESSAGE);

                        }
                    });
                    add(anItem);
                } else {
                    create.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String name = JOptionPane.showInputDialog(getParent(), "Please insert a name for the playlist", "New Playlist", JOptionPane.INFORMATION_MESSAGE);
                            if(albums.size() == 0) {
                                Library.getInstance().createPlaylist(name, App.getLogged());

                            } else {
                                Library.getInstance().createPlaylist(name, App.getLogged(), albums.get(row));
                            }
                        }
                    });
                    playl.add(create);
                    add(playl);
                }
            } else {
                JTable source = (JTable)e.getSource();
                int row = source.rowAtPoint( e.getPoint() );
                JMenu follow = new JMenu();
                if(App.getInstance().getLogged().getFollowing().contains(authors.get(row))) {
                    follow.setText("Unfollow");
                    follow.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            App.getInstance().getLogged().unfollow(authors.get(row));

                            JOptionPane.showMessageDialog(getParent(), "You just unfollowed this author!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                else {
                    follow.setText("Follow");
                    follow.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            App.getInstance().getLogged().follow(authors.get(row));

                            JOptionPane.showMessageDialog(getParent(), "You just followed this author!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                add(follow);
            }
        }
    }
}
