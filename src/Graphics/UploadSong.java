package Graphics;

import App.App;
import Exceptions.WrongUserException;
import Music.Album;
import Music.Song;
import pads.musicPlayer.exceptions.Mp3InvalidFileException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class UploadSong. Class that implements the panel that allows a registered user to upload a song.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class UploadSong extends JPanel{

    JPanel selectSongs;
    HashMap<Integer,String> songNames = new HashMap<Integer, String>();
    ArrayList<String> songFiles = new ArrayList<String>();
    String album;
    String coverPath = "";
    JTextField nameSongTF;
    UploadSong reference;
    JTextField albumNameTF;
    JComboBox cb;
    String[] start;
    private int selectedIndex = -1;

    UploadSong(App app) {
        super();
        reference= this;
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        selectSongs = new JPanel(new GridBagLayout());
        selectSongs.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));

        GridBagConstraints cpanel = new GridBagConstraints();

        JLabel home = new JLabel(new ImageIcon("img/home.png"));
        cpanel.weightx = 0.5;
        cpanel.weighty = 0.5;
        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomeFrame.getInstance().washUpload();
                HomeFrame.getInstance().change("home");
            }

        });
        cpanel.gridx = 0;
        cpanel.gridy = 0;
        cpanel.anchor = GridBagConstraints.FIRST_LINE_START;
        cpanel.insets = new Insets(10,10,0,0);

        top.add(home, cpanel);

        JLabel uploadSongText = new JLabel("Upload song/s...");
        uploadSongText.setForeground(Color.WHITE);
        uploadSongText.setFont(new Font("Monospaced", Font.PLAIN, 40));
        cpanel.insets = new Insets(60,120,0,0);
        top.add(uploadSongText, cpanel);

        JLabel artisticName = new JLabel(App.getInstance().getLogged().getArtisticname());
        artisticName.setForeground(Color.WHITE);
        cpanel.anchor = GridBagConstraints.FIRST_LINE_END;
        cpanel.insets = new Insets(30,0, 0,60);
        cpanel.weightx = 1;
        cpanel.weighty = 1;

        top.add(artisticName, cpanel);

        JLabel profilePicture;
        if(App.getLogged().getProfilePath().isEmpty() == false){
            ImageIcon profile =  new ImageIcon(App.getInstance().getLogged().getProfilePath());
            Image image = profile.getImage(); // transform it
            image = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            profilePicture = new JLabel (new ImageIcon(image));
        }else{
            profilePicture = new JLabel(new ImageIcon("img/user_opt.png"));
        }
        cpanel.anchor = GridBagConstraints.FIRST_LINE_END;
        cpanel.insets = new Insets(30,0,0,160);

        top.add(profilePicture, cpanel);

        JButton finish = new JButton("Finish");
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Album album;
                songNames.put(selectedIndex, nameSongTF.getText());
                if(songNames.get(0).isEmpty()){
                    songNames.remove(0);
                }
                if(songNames.containsValue("") || songNames.values().size() != songFiles.size()){
                    JOptionPane.showMessageDialog(getParent(), "Please fill a name for every song", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Song[] songs = new Song[songFiles.size()];
                for(int i = 0;i < songFiles.size(); i++){
                    try {
                        songs[i] = new Song(songNames.get(i), songFiles.get(i), coverPath, App.getLogged());
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                if(coverPath.isEmpty()){
                    try {
                        if(albumNameTF.getText().isEmpty()){
                            album = App.getInstance().lib.createAlbum(songs[0].getTitle(), coverPath, songs);
                        }else{
                            album = App.getInstance().lib.createAlbum(albumNameTF.getText(), coverPath, songs);
                        }
                        album.addAuthor(App.getLogged());
                        for(Song song: songs){
                            song.addReference(album);
                        }
                    } catch (Mp3InvalidFileException ex) {
                        ex.printStackTrace();
                    } catch (WrongUserException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    try {
                        if(!albumNameTF.getText().isEmpty()){
                            album = App.getInstance().lib.createAlbum(albumNameTF.getText(), songs);
                        }else{
                            album = App.getInstance().lib.createAlbum(songs[0].getTitle(), songs);
                        }
                        album.addAuthor(App.getLogged());
                        for(Song song: songs){
                            song.addReference(album);
                        }
                    } catch (Mp3InvalidFileException ex) {
                        ex.printStackTrace();
                    } catch (WrongUserException ex) {
                        ex.printStackTrace();
                    }
                }
                HomeFrame.getInstance().washUpload();
                HomeFrame.getInstance().change("home");
            }
        });
        finish.setPreferredSize(new Dimension(100,40));
        cpanel.anchor = GridBagConstraints.PAGE_START;
        cpanel.insets = new Insets(60,20,0,0);
        cpanel.weightx = 0;
        cpanel.weighty = 0;

        top.add(finish, cpanel);

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        this.add(top, constraints);
        top.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));

        GridBagConstraints middlePanel = new GridBagConstraints();
        middlePanel.gridx = 0;
        middlePanel.gridy = 0;
        middlePanel.weightx = 0.2;
        middlePanel.weightx = 0.2;

        JLabel folderIconS = new JLabel(new ImageIcon("img/folder_opt.png"));
        folderIconS.addMouseListener(new MoveSong());
        middlePanel.anchor = GridBagConstraints.PAGE_START;
        middlePanel.insets = new Insets(0,0,130,330);
        selectSongs.add(folderIconS, middlePanel);

        JLabel select = new JLabel("Select from your computer...");
        select.addMouseListener(new MoveSong());
        select.setForeground(Color.WHITE);
        select.setFont(new Font("Monospaced", Font.PLAIN, 20));
        middlePanel.insets = new Insets(0, 60, 130, 0);
        selectSongs.add(select, middlePanel);

        /*JLabel icon = new JLabel(new ImageIcon("img/music_opt.png"));
        middlePanel.anchor = GridBagConstraints.LINE_START;
        middlePanel.insets = new Insets(15,30,0,0);
        selectSongs.add(icon, middlePanel);*/



        constraints.gridy = 1;

        this.add(selectSongs, constraints);
        selectSongs.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        this.add(selectSongs, constraints);

        cpanel.weightx = 0.2;
        cpanel.weighty = 0.2;

        JLabel nameFile = new JLabel("Name of the file selected: ");
        nameFile.setForeground(Color.WHITE);
        nameFile.setFont(new Font("Monospaced", Font.PLAIN, 20));
        cpanel.anchor = GridBagConstraints.PAGE_START;
        cpanel.insets = new Insets(20,0,0,0);
        bottom.add(nameFile, cpanel);
        cpanel.anchor = GridBagConstraints.FIRST_LINE_START;

        start = new String[1];
        start[0] = "No songs yet";
        cb = new JComboBox(start);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)cb.getSelectedItem();
                if(select.equals("No songs yet")){
                    return;
                }
                if(selectedIndex != -1){
                    songNames.put(selectedIndex, nameSongTF.getText());
                }

                selectedIndex = cb.getSelectedIndex();
                nameSongTF.setText(songNames.get(cb.getSelectedIndex()));
            }
        });
        cpanel.insets = new Insets(20,10,0,0);
        bottom.add(cb, cpanel);
        JLabel nameFileS = new JLabel("");
        cpanel.insets = new Insets(20,320,0,0);

        bottom.add(nameFileS, cpanel);

        JLabel nameSong = new JLabel("Type name of the song: ");
        nameSong.setForeground(Color.WHITE);
        nameSong.setFont(new Font("Monospaced", Font.PLAIN, 20));
        cpanel.insets = new Insets(100,20,0,0);
        bottom.add(nameSong, cpanel);

        nameSongTF = new JTextField(nameFileS.getText());
        nameSongTF.setColumns(10);
        cpanel.insets = new Insets(100, 320 , 0, 0);
        bottom.add(nameSongTF, cpanel);

        JLabel albumName = new JLabel("Include it in an album?");
        albumName.setForeground(Color.WHITE);
        albumName.setFont(new Font("Monospaced", Font.PLAIN, 20));
        cpanel.insets = new Insets(200, 20,0,0);
        bottom.add(albumName, cpanel);

        albumNameTF = new JTextField("Type here...");
        albumNameTF.setColumns(10);
        albumName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(albumNameTF.getText().equals("Type here...")){
                    albumNameTF.setText("");
                }
            }
        });
        cpanel.insets = new Insets(200,300,0,0);
        bottom.add(albumNameTF, cpanel);

        JLabel ifyou = new JLabel("If you leave it blank, the song will be uploaded in an album with the song's name");
        ifyou.setForeground(Color.WHITE);
        ifyou.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cpanel.insets = new Insets(200, 440, 0,0);
        bottom.add(ifyou, cpanel);

        JLabel selectPicture = new JLabel("Select picture for the songs: ");
        selectPicture.setForeground(Color.WHITE);
        selectPicture.setFont(new Font("Monospaced", Font.PLAIN, 20));
        cpanel.insets = new Insets(300, 20,0,0);
        bottom.add(selectPicture, cpanel);

        JLabel folderIcon = new JLabel(new ImageIcon("img/folder_opt.png"));
        folderIcon.addMouseListener(new MovePicture());
        cpanel.insets = new Insets(300, 450, 0, 0);
        bottom.add(folderIcon, cpanel);

        JLabel selectFromComputer = new JLabel("Select from your computer...");
        selectFromComputer.addMouseListener(new MovePicture());
        selectFromComputer.setForeground(Color.WHITE);
        selectFromComputer.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cpanel.insets = new Insets(300, 500, 0,0);
        bottom.add(selectFromComputer, cpanel);

        constraints.gridy = 2;

        bottom.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        this.add(bottom, constraints);

        this.add(bottom, constraints);
        this.setVisible(true);
        this.setVisible(true);


    }

    /**
     * Class MoveSong. Made with the purpose of allowing a user to upload a song to the app.
     */
    public class MoveSong extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(getParent());
            if(songFiles.isEmpty()){
                cb.removeItemAt(0);
            }
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                cb.addItem(file.getName());
                songFiles.add(file.getPath());
                addSong(file.getPath());
                revalidate();
                repaint();
            }
            if(App.demo){
                JOptionPane.showMessageDialog(getParent(), "Now give a name to your song", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    /**
     * Class MovePicture. Made with the purpose of allowing a user to upload a cover for their song.
     */
    public class MovePicture extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(getParent());
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                coverPath = file.getPath();
            }
            if(App.demo){
                JOptionPane.showMessageDialog(getParent(), "That's it! You can now click finish, and leave the rest to us.\n " +
                        "We'll notify you when your song gets validated.\n To go back, click on the home button.");
            }
        }

    }
    void addSong(String path){
        GridBagConstraints middlePanel = new GridBagConstraints();
        middlePanel.gridx = 0;
        middlePanel.gridy = 0;
        middlePanel.weightx = 0.2;
        middlePanel.weightx = 0.2;
        JLabel icon2 = new JLabel(new ImageIcon(new ImageIcon("img/music_opt.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        middlePanel.anchor = GridBagConstraints.LINE_START;
        middlePanel.insets = new Insets(15,30,0,0);
        middlePanel.gridx = songFiles.indexOf(path)+1;
        selectSongs.add(icon2, middlePanel);
        JLabel name = new JLabel(path.substring(path.lastIndexOf("\\")+1, path.length()));
        middlePanel.gridy = 1;
        selectSongs.add(name, middlePanel);
    }

}
