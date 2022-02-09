package Graphics;

import App.App;
import Music.Playlist;
import Music.Song;
import Users.Administrator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Class InicioPanel. Class that implements the home screen panel, which is the one in the middle.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class InicioPanel extends JPanel {
    private MidPanel midPanel;
    public InicioPanel(MidPanel midPanel){
        super();
        this.midPanel = midPanel;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        String[] searching = {"By song", "By album", "By author"};
        JComboBox search = new JComboBox(searching);
        search.setPreferredSize(new Dimension(110,40));
        JTextField searchText = new JTextField();
        searchText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                midPanel.change("search");
            }
        });
        searchText.setPreferredSize(new Dimension(250, 40));
        JLabel top10week = new JLabel(new ImageIcon("img/top10weekly_opt.jpg"));
        top10week.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7); // number represents number of days
                Date yesterday = cal.getTime();
                ArrayList<Song> total = App.getInstance().lib.getSongs();
                ArrayList<Song> songs = new ArrayList<>(); // top songs suddenly stopped to work we are taking random songs
                for(int i = 0; i < total.size(); i += 3){
                    songs.add(total.get(i));
                }
                Song array[] = new Song[songs.size()];
                array = songs.toArray(array);
                Playlist playlist = new Playlist("Weekly top 10", App.getLogged(), array);
                midPanel.change(playlist);
            }
        });
        JLabel top10daily = new JLabel(new ImageIcon("img/top10daily_opt.jpg"));
        top10daily.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1); // number represents number of days
                Date yesterday = cal.getTime();
                ArrayList<Song> total = App.getInstance().lib.getSongs();
                ArrayList<Song> songs = new ArrayList<>(); // top songs suddenly stopped to work we are taking random songs
                for(int i = 1; i < total.size(); i += 3){
                    songs.add(total.get(i));
                }
                Song array[] = new Song[songs.size()];
                array = songs.toArray(array);
                Playlist playlist = new Playlist("Daily top 10", App.getLogged(), array);
                midPanel.change(playlist);
            }
        });
        JLabel top10artists = new JLabel(new ImageIcon("img/top10monthly_opt.jpg"));
        top10artists.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1); // number represents number of days
                Date yesterday = cal.getTime();
                ArrayList<Song> total = App.getInstance().lib.getSongs();
                ArrayList<Song> songs = new ArrayList<>(); // top songs suddenly stopped to work we are taking random songs
                for(int i = 0; i < total.size(); i += 3){
                    songs.add(total.get(i));
                }
                Song array[] = new Song[songs.size()];
                array = songs.toArray(array);
                Playlist playlist = new Playlist("Daily top 10", App.getLogged(), array);
                midPanel.change(playlist);
            }
        });
        JLabel day = new JLabel("Here is our selection of the top");
        day.setForeground(Color.WHITE);
        JLabel day2 = new JLabel("most listened songs in Neons");
        day2.setForeground(Color.WHITE);
        JLabel day3 = new JLabel("during the day!");
        day3.setForeground(Color.WHITE);

        JLabel week = new JLabel("Here is our selection of the top");
        week.setForeground(Color.WHITE);
        JLabel week2 = new JLabel("most listened songs in Neons");
        week2.setForeground(Color.WHITE);
        JLabel week3 = new JLabel("during the week!");
        week3.setForeground(Color.WHITE);

        JLabel artists = new JLabel("Here is our selection of the top");
        artists.setForeground(Color.WHITE);
        JLabel artists2 = new JLabel("most listened artists in Neons");
        artists2.setForeground(Color.WHITE);
        JLabel artists3 = new JLabel("during the week!");
        artists3.setForeground(Color.WHITE);

        c1.weightx = 0.2;
        c1.weighty = 0.2;
        c1.gridx = 1;
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.insets = new Insets(70,0,0,30);

        this.add(search, c1);

        c1.insets = new Insets(70, 10, 0,150);
        c1.weightx = 0.6;
        c1.weighty = 0.6;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.fill = GridBagConstraints.HORIZONTAL;

        this.add(searchText, c1);

        c1.weightx = 1;
        c1.weighty = 1;
        c1.gridx = 1;
        c1.gridy = 1;
        c1.anchor = GridBagConstraints.FIRST_LINE_END;
        c1.fill = GridBagConstraints.NONE;
        c1.insets = new Insets(0,0,0,60);

        this.add(top10week, c1);

        c1.insets = new Insets(0,0,0,0);
        c1.anchor = GridBagConstraints.LINE_END;

        c1.insets = new Insets(0,0,0,38);
        this.add(week, c1);
        c1.insets = new Insets(30,0,0,43);
        this.add(week2, c1);
        c1.insets = new Insets(60,0,0,80);
        this.add(week3, c1);

        c1.insets = new Insets(0,0,0,0);
        c1.gridx = 1;
        c1.anchor = GridBagConstraints.PAGE_START;

        this.add(top10daily, c1);

        c1.insets = new Insets(0,0,0,0);
        c1.anchor = GridBagConstraints.CENTER;

        this.add(day, c1);
        c1.insets = new Insets(30,0,0,0);
        this.add(day2, c1);
        c1.insets = new Insets(60,0,0,0);
        this.add(day3, c1);

        c1.insets = new Insets(0,60,0,0);
        c1.gridx = 1;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;

        this.add(top10artists, c1);

        c1.insets = new Insets(0,0,0,0);
        c1.anchor = GridBagConstraints.LINE_START;

        c1.insets = new Insets(0,38,0,0);
        this.add(artists, c1);
        c1.insets = new Insets(30,43,0,0);
        this.add(artists2, c1);
        c1.insets = new Insets(60,80,0,0);
        this.add(artists3, c1);

    }

}
