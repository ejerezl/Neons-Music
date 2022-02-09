package Graphics;

import App.App;
import Music.Album;
import Music.MediaPlayer;
import Music.Song;
import Users.User;

import Music.Album;
import Music.Song;
import Users.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class Statistics. Class that implements the statistics panel of the administrator screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class Statistics extends JPanel {
    AdminFrame frame;
    JPanel st1;
    JPanel st2;
    JPanel st3;
    JTable table1;
    JTable table2;
    JTable table3;
    JButton b1;
    JButton b2;
    JButton b3;


    public Statistics(AdminFrame frame) {
        super();
        this.frame = frame;
        this.setLayout(new GridBagLayout());

        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        st1 = new JPanel(new GridBagLayout());
        st2 = new JPanel(new GridBagLayout());
        st3 = new JPanel(new GridBagLayout());
        JLabel top1 = new JLabel("TOP 10 SINGERS/BANDS");
        JLabel top2 = new JLabel("TOP 10 SONGS");
        JLabel top3 = new JLabel("TOP 10 ALBUMS");
        JLabel exitIcon = new JLabel(new ImageIcon("img/logout_opt.png"));
        exitIcon.addMouseListener(new openLogin());
        JTextField date1 = new JTextField("");
        JTextField date2 = new JTextField("");
        JTextField date3 = new JTextField("");
        b1 = new JButton("OK");
        b2 = new JButton("OK");
        b3 = new JButton("OK");

        b1.setPreferredSize(new Dimension(60, 30));
        b1.setBackground(Color.WHITE);
        b1.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                remove(st1);
                st1.remove(table1);
                ArrayList<String> datos = new ArrayList<>();
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(date1.getText());
                } catch(Exception exc) {
                    exc.printStackTrace();
                }

                for(User user: App.getInstance().administrator.getTopAuthors(date,10)) {
                    datos.add(user.getArtisticname());
                }
                DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model.addColumn("title", datos.toArray());
                table1 = new JTable(model);
                table1.setRowHeight(60);
                table1.setFont(new Font("Monospaced", Font.PLAIN, 20));
                table1.setTableHeader(null);
                table1.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                table1.setForeground(Color.white);

                GridBagConstraints constraints = new GridBagConstraints();

                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.weightx = 0.1;
                constraints.weighty = 0.1;
                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                constraints.fill = GridBagConstraints.BOTH;
                constraints.insets = new Insets(55, 0, 0, 0);
                st1.add(table1, constraints);

                GridBagConstraints cpanels = new GridBagConstraints();

                cpanels.anchor = GridBagConstraints.FIRST_LINE_START;
                cpanels.gridy = 0;
                cpanels.weightx = 0.3;
                cpanels.weighty = 0.3;
                cpanels.gridx = 0;
                cpanels.fill = GridBagConstraints.BOTH;
                add(st1, cpanels);
                validate();
                repaint();
                JOptionPane.showMessageDialog(getParent(), "Great! Try all you want", "Demo", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(st2);
                st2.remove(table2);
                ArrayList<String> datos = new ArrayList<>();
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(date2.getText());
                } catch(Exception exc) {
                    exc.printStackTrace();
                }

                for(Song song: App.getInstance().administrator.getTopSongs(date,10)) {
                    datos.add(song.getTitle());
                }
                DefaultTableModel model = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model.addColumn("title", datos.toArray());
                table2 = new JTable(model);
                table2.setRowHeight(60);
                table2.setFont(new Font("Monospaced", Font.PLAIN, 20));
                table2.setTableHeader(null);
                table2.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                table2.setForeground(Color.white);

                GridBagConstraints constraints = new GridBagConstraints();


                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.weightx = 0.1;
                constraints.weighty = 0.1;
                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                constraints.fill = GridBagConstraints.BOTH;
                constraints.insets = new Insets(55, 0, 0, 0);
                st2.add(table2, constraints);

                GridBagConstraints cpanels = new GridBagConstraints();

                cpanels.anchor = GridBagConstraints.FIRST_LINE_START;
                cpanels.gridy = 0;
                cpanels.weightx = 0.3;
                cpanels.weighty = 0.3;
                cpanels.gridx = 1;
                cpanels.fill = GridBagConstraints.BOTH;
                add(st2, cpanels);
                validate();
                repaint();
                JOptionPane.showMessageDialog(getParent(), "Great! Try all you want", "Demo", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        b2.setPreferredSize(new Dimension(60, 30));
        b2.setBackground(Color.WHITE);
        b2.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                remove(st3);
                st3.remove(table3);
                ArrayList<String> datos = new ArrayList<>();
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(date3.getText());
                } catch(Exception exc) {
                    exc.printStackTrace();
                }

                for(Album album: App.getInstance().administrator.getTopAlbums(date,10)) {
                    datos.add(album.getTitle());
                }
                DefaultTableModel model = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model.addColumn("title", datos.toArray());
                table3 = new JTable(model);
                table3.setRowHeight(60);
                table3.setFont(new Font("Monospaced", Font.PLAIN, 20));
                table3.setTableHeader(null);
                table3.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                table3.setForeground(Color.white);

                GridBagConstraints constraints = new GridBagConstraints();


                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.weightx = 0.1;
                constraints.weighty = 0.1;
                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                constraints.fill = GridBagConstraints.BOTH;
                constraints.insets = new Insets(55, 0, 0, 0);
                st3.add(table3, constraints);

                GridBagConstraints cpanels = new GridBagConstraints();

                cpanels.anchor = GridBagConstraints.FIRST_LINE_START;
                cpanels.gridy = 0;
                cpanels.weightx = 0.3;
                cpanels.weighty = 0.3;
                cpanels.gridx = 2;
                cpanels.fill = GridBagConstraints.BOTH;
                add(st3, cpanels);

                validate();
                repaint();

                JOptionPane.showMessageDialog(getParent(), "Great! Try all you want", "Demo", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        b3.setPreferredSize(new Dimension(60, 30));
        b3.setBackground(Color.WHITE);
        b3.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        //Settings of panels
        st1.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        st2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        st2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        st1.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        st2.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        st3.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        date1.setMinimumSize(new Dimension(90,25));
        date1.setBackground(Color.white);
        date1.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        date1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        date2.setMinimumSize(new Dimension(90,25));
        date2.setBackground(Color.white);
        date2.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        date2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        date3.setMinimumSize(new Dimension(90,25));
        date3.setBackground(Color.white);
        date3.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        date3.setFont(new Font("Monospaced", Font.PLAIN, 15));

        //Creating the three tables
        ArrayList<String> datos1 = new ArrayList<>();
        DefaultTableModel model1 = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model1.addColumn("title", datos1.toArray());
        table1 = new JTable(model1);

        ArrayList<String> datos3 = new ArrayList<>();
        DefaultTableModel model3 = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model3.addColumn("title", datos3.toArray());
        table3 = new JTable(model3);

        ArrayList<String> datos2 = new ArrayList<>();
        DefaultTableModel model2 = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model2.addColumn("title", datos2.toArray());
        table2 = new JTable(model2);

        //Settings of tables
        table1.setRowHeight(60);
        table1.setFont(new Font("Monospaced", Font.PLAIN, 20));
        table1.setTableHeader(null);
        table1.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        table1.setForeground(Color.white);
        table2.setRowHeight(60);
        table2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        table2.setTableHeader(null);
        table2.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        table2.setForeground(Color.white);
        table3.setRowHeight(60);
        table3.setFont(new Font("Monospaced", Font.PLAIN, 20));
        table3.setTableHeader(null);
        table3.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        table3.setForeground(Color.white);

        top1.setFont(new Font("Monospaced", Font.PLAIN, 20));
        top1.setForeground(Color.white);
        top2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        top2.setForeground(Color.white);
        top3.setFont(new Font("Monospaced", Font.PLAIN, 20));
        top3.setForeground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();

        //Combo Box
        String options[] = {"SONGS", "USERS", "STATISTICS"};

        JComboBox cb = new JComboBox(options);
        cb.setSelectedItem("STATISTICS");
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cb.getSelectedIndex() == 0){
                    frame.change("user");
                }else if(cb.getSelectedIndex() == 1){
                    frame.change("songs");
                }
            }
        });
        cb.setPreferredSize(new Dimension(100,30));
        cb.setFont(new Font("Arial", Font.BOLD, 11));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(10, 10, 0, 0);

        cb.setBackground(Color.WHITE);
        cb.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        st1.add(cb, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        constraints.insets = new Insets(10,0, 0, 10);
        st3.add(exitIcon, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10, 0, 0, 0);
        st2.add(top1, constraints);
        st1.add(top2, constraints);
        st3.add(top3, constraints);

        constraints.insets = new Insets(34, 0, 0, 0);
        date1.setColumns(10);
        date2.setColumns(10);
        date3.setColumns(10);
        st2.add(date1, constraints);
        st1.add(date2, constraints);
        st3.add(date3, constraints);

        constraints.insets = new Insets(34, 230, 0, 0);
        st2.add(b1, constraints);
        st1.add(b2, constraints);
        st3.add(b3, constraints);

        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(55, 0, 0, 0);
        st2.add(table1, constraints);
        st1.add(table2, constraints);
        st3.add(table3, constraints);

        GridBagConstraints cpanels = new GridBagConstraints();

        cpanels.anchor = GridBagConstraints.FIRST_LINE_START;
        cpanels.gridy = 0;
        cpanels.weightx = 0.3;
        cpanels.weighty = 0.3;
        cpanels.gridx = 0;
        cpanels.fill = GridBagConstraints.BOTH;
        this.add(st1, cpanels);

        cpanels.gridx = 1;
        this.add(st2, cpanels);

        cpanels.gridx = 2;
        this.add(st3, cpanels);

        this.setMinimumSize(new Dimension(1300, 700));
        this.setVisible(true);

    }

    /**
     * Private class openLogin. Used when logout to load the login screen.
     */
    private class openLogin extends MouseAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            new LoginWindow();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            new LoginWindow();
        }
    }

}
