package Graphics;

import App.App;
import Music.MediaPlayer;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class AdminUser_Pending. Class that shows the left panel information of the admin user.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AdminUser_Pending extends JPanel{
    JPanel content = new JPanel(new GridBagLayout());
    AdminRightPanel info;
    JToggleButton toggleButton = new JToggleButton("REPORTED");
    JTable uploadedSongsTable;
    CardLayout cardLayout;
    JTable usersTable;
    JTable reportedSongsTable;
    JPanel intermediatepanel;
    AdminFrame adminframe;
    JComboBox cb;
    ArrayList<String> uploadedSongs;
    ArrayList<String> reportedSongs;
    ArrayList<String> users;
    DefaultTableModel uploadedSongsModel;
    DefaultTableModel usersModel;
    DefaultTableModel reportedSongsModel;
    JScrollPane uploadedSongsPane;
    JScrollPane usersScrollPane;
    JScrollPane reportedSongsScrollPane;

    public AdminUser_Pending(AdminFrame frame) {
        super();
        this.adminframe = frame;
        this.setLayout(new GridBagLayout());
        JLabel exitIcon = new JLabel(new ImageIcon("img/logout_opt.png"));
        exitIcon.addMouseListener(new openLogin());
        info = new AdminRightPanel(this);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediaPlayer.getInstance().stopPlaying();
                if (toggleButton.isSelected()){
                    toggleButton.setText("UPLOADED");
                    cardLayout.show(intermediatepanel, "0");

                    if (App.adminDemo) {
                        JOptionPane.showMessageDialog(getParent(), "Here you have the reported songs", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    toggleButton.setText("REPORTED");
                    cardLayout.show(intermediatepanel, "2");

                    if (App.adminDemo) {
                        JOptionPane.showMessageDialog(getParent(), "Here you have the uploaded songs\n" + "Now, the administrator can validate a song or mark it as explicit by using the bottoms in the right panel\n"
                                + "Try to solve one song by double clicking on it and selecting the options you want", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });
        toggleButton.setPreferredSize(new Dimension(200, 40));


        GridBagConstraints cpanels = new GridBagConstraints();

        uploadedSongsPane = null;
        usersScrollPane = null;
        reportedSongsScrollPane = null;


        cardLayout = new CardLayout();
        intermediatepanel = new JPanel(cardLayout);

        update(0);
        update(1);
        update(2);

        content.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        content.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        GridBagConstraints constraints = new GridBagConstraints();



        String options[] = {"SONGS", "USERS", "STATISTICS"};

        cb = new JComboBox(options);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(10, 10, 0, 0);

        cb.setBackground(Color.WHITE);
        cb.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MediaPlayer.getInstance().stopPlaying();
                change(cb.getSelectedIndex());
            }
        });

        content.add(cb, constraints);


        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);



        intermediatepanel.add(uploadedSongsPane, "0");
        intermediatepanel.add(usersScrollPane, "1");
        intermediatepanel.add(reportedSongsScrollPane, "2");
        cardLayout.show(intermediatepanel, "2");
        constraints.insets = new Insets(55, 0, 0, 0);

        content.add(intermediatepanel, constraints);
        constraints.fill = GridBagConstraints.VERTICAL;

        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10, 0, 0, 40);
        content.add(exitIcon, constraints);

        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(0, 0, 0, 0);
        content.add(toggleButton, constraints);

        cpanels.insets = new Insets(0, 0, 0, 0);
        cpanels.gridy = 0;
        cpanels.weightx = 1;
        cpanels.weighty = 1;
        cpanels.gridx = 1;
        cpanels.fill = GridBagConstraints.BOTH;
        this.add(info, cpanels);


        cpanels.ipadx = 200;
        cpanels.gridx = 0;
        this.add(content, cpanels);
        this.setMinimumSize(new Dimension(1300, 700));
        this.setVisible(true);
    }

    /**
     * Method to change between panels
     * @param i
     */
    public void change(int i){
        if(i == 2){
            adminframe.change("statistics");

            if (App.adminDemo) {
                JOptionPane.showMessageDialog(getParent(), "Here you can see some statistics about the songs, artists and albums most played" +
                        " by introducing a date with format dd/MM/yyyy you will obtain the list of the most played till the date introduced", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }

        }
        if(i == 0){
            info.change(true);
            toggleButton.setVisible(true);
            if (toggleButton.isSelected()){
                cardLayout.show(intermediatepanel, "0");

            }
            else{
                cardLayout.show(intermediatepanel, "2");
            }
        }else{
            info.change(false);
            toggleButton.setVisible(false);
            cardLayout.show(intermediatepanel, "1");

            if (App.adminDemo) {
                JOptionPane.showMessageDialog(getParent(), "Here you can see all the users registered in the app. By double clicking on one of them or moving the arrow keys you will se a more detailed information in the right panel",
                        "Demo", JOptionPane.INFORMATION_MESSAGE);
            }

        }
        cb.setSelectedIndex(i);
    }


    /**
     * Method to refresh the tables, in order to show the results
     * @param i
     */
    public void update(int i){
        switch (i){
            case 0:
                if(uploadedSongsPane != null){
                    intermediatepanel.remove(uploadedSongsPane);
                }
                uploadedSongs = App.getAdministrator().getUploaded().stream().map(song -> song.getTitle()).collect(Collectors.toCollection(ArrayList::new));
                uploadedSongsModel = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                uploadedSongsModel.addColumn("Songs", uploadedSongs.toArray());
                uploadedSongsTable = new JTable(uploadedSongsModel);
                uploadedSongsTable.setPreferredScrollableViewportSize(new Dimension(300, 70));
                uploadedSongsTable.setFillsViewportHeight(true);
                uploadedSongsTable.setRowHeight(60);
                uploadedSongsTable.setFont(new Font("Monospaced", Font.PLAIN, 20));
                uploadedSongsTable.setTableHeader(null);
                uploadedSongsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent event) {
                        info.changeContent(App.getAdministrator().getUploaded().get(uploadedSongsTable.getSelectedRow()));
                    }
                });
                uploadedSongsPane = new JScrollPane(uploadedSongsTable);
                uploadedSongsTable.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                uploadedSongsTable.setForeground(Color.WHITE);
                intermediatepanel.add(uploadedSongsPane, "0");
                break;
            case 1:
                if(usersScrollPane != null){
                    intermediatepanel.remove(usersScrollPane);
                }
                users = App.getInstance().users.getUsers().values().stream().map(user -> user.getName()).collect(Collectors.toCollection(ArrayList::new));
                usersModel = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                usersModel.addColumn("Users", users.toArray());
                usersTable = new JTable(usersModel);
                usersTable.setPreferredScrollableViewportSize(new Dimension(300, 70));
                usersTable.setFillsViewportHeight(true);
                usersTable.setRowHeight(60);
                usersTable.setFont(new Font("Monospaced", Font.PLAIN, 20));
                usersTable.setTableHeader(null);
                usersTable.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                usersTable.setForeground(Color.WHITE);
                usersScrollPane = new JScrollPane(usersTable);
                usersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent event) {
                        info.changeContent(App.getInstance().users.getUsers().get(uploadedSongsTable.getSelectedRow()));

                        if (App.adminDemo) {
                            JOptionPane.showMessageDialog(getParent(), "Great! Now you can make a user premium or free by selecting what yu want in the right top corner buttons. Try it!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        }

                    }
                });
                intermediatepanel.add(usersScrollPane, "1");
                break;
            case 2:
                if(reportedSongsScrollPane != null){
                    intermediatepanel.remove(reportedSongsScrollPane);
                }
                reportedSongs = App.getAdministrator().getReported().stream().map(report -> report.getSong().getTitle()).collect(Collectors.toCollection(ArrayList::new));
                reportedSongsModel = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                reportedSongsModel.addColumn("Songs", reportedSongs.toArray());
                reportedSongsTable = new JTable(reportedSongsModel);
                reportedSongsTable.setPreferredScrollableViewportSize(new Dimension(300, 70));
                reportedSongsTable.setFillsViewportHeight(true);
                reportedSongsTable.setRowHeight(60);
                reportedSongsTable.setFont(new Font("Monospaced", Font.PLAIN, 20));
                reportedSongsTable.setTableHeader(null);
                reportedSongsTable.setForeground(Color.WHITE);
                reportedSongsTable.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
                reportedSongsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent event) {
                        info.changeContent(App.getAdministrator().getReported().get(reportedSongsTable.getSelectedRow()).getSong());
                    }
                });
                reportedSongsScrollPane = new JScrollPane(reportedSongsTable);
                intermediatepanel.add(reportedSongsScrollPane, "2");
                break;
            default:

        }

        validate();
        repaint();
        cardLayout.show(intermediatepanel, "0");
    }

    /**
     *
     */
    public void update(){
        if(toggleButton.isSelected()){
            update(0);
        }else{
            update(2);
        }
    }

    /**
     *
     * @param type
     * @param explicit
     */
    public void confirm(boolean type, boolean explicit){
        if(toggleButton.isSelected()){
            App.getAdministrator().solveUpload(App.getAdministrator().getUploaded().get(uploadedSongsTable.getSelectedRow()), type, explicit);
        }else{
            App.getAdministrator().solveReport(App.getAdministrator().getReported().get(uploadedSongsTable.getSelectedRow()), type, explicit);
        }

    }
    private class openLogin extends MouseAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MediaPlayer.getInstance().stopPlaying();
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            App.getInstance().isAdminLogged = false;
            new LoginWindow();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            MediaPlayer.getInstance().stopPlaying();
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            App.getInstance().isAdminLogged = false;
            new LoginWindow();
        }
    }

}
