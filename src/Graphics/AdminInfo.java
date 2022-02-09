package Graphics;

import App.App;
import Music.*;
import Music.Song;
import Users.User;
import pads.musicPlayer.Mp3Player;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

/**
 * Class AdminInfo. Class containing all the songs information on the right side of the admin screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AdminInfo extends JPanel {
    BackGround backGround;
    JLabel titulo;
    JLabel songCover;
    JCheckBox explicitContent;
    JCheckBox validated;
    Song current;
    AdminUser_Pending adminUser_pending;
    JSlider slider;
    JLabel init;
    JLabel end;
    public AdminInfo(AdminUser_Pending adminUser_pending){
        super(new GridBagLayout());
        this.adminUser_pending =adminUser_pending;
        setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        GridBagConstraints constraints = new GridBagConstraints();


        if(MediaPlayer.getInstance().getCurrent() == null){
            songCover = new JLabel(new ImageIcon("img/music_opt.png"));
            titulo = new JLabel("Nothing playing");
        }else{
            songCover = new JLabel(new ImageIcon(MediaPlayer.getInstance().getCurrent().getCoverPath()));
            titulo = new JLabel(MediaPlayer.getInstance().getCurrent().getTitle()+" - "+MediaPlayer.getInstance().getCurrent().getAuthors().stream().map(author -> author.getArtisticname()).collect(Collectors.joining()));
        }

        JLabel playPauseImage = new JLabel(new ImageIcon("img/play_opt.png"));
        playPauseImage.addMouseListener(new MouseAdapter() {
            private boolean playing = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                if(playing){
                    MediaPlayer.getInstance().stopPlaying();
                    backGround.stop();
                }else{
                    MediaPlayer.getInstance().startPlaying(current);
                    backGround = new BackGround();
                    backGround.start();
                }
            }
        });
        constraints.insets = new Insets(30,0,0,285);

        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Monospaced", Font.PLAIN, 20));
        slider = new JSlider(JSlider.HORIZONTAL, 0,300,150);
        slider.setForeground(Color.WHITE);
        init = new JLabel("1:00");
        init.setForeground(Color.WHITE);
        end = new JLabel("3:00");
        end.setForeground(Color.WHITE);

        JLabel ok = new JLabel(new ImageIcon("img/ok_opt.png"));
        ok.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                adminUser_pending.confirm(validated.isSelected(), explicitContent.isSelected());
                adminUser_pending.update();
                songCover.setIcon(new ImageIcon("img/music_opt.png"));
                titulo.setText("Nothing playing");

                if (App.adminDemo) {

                    JOptionPane.showMessageDialog(getParent(), "Fantastic! Now try to change the maximum size of playbacks that free users have", "Demo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(40,0,0,0);
        constraints.anchor = GridBagConstraints.PAGE_START;
        this.add(songCover, constraints);

        constraints.insets = new Insets(170,0,0,0);
        this.add(playPauseImage, constraints);

        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(0, 0, 200, 0);
        this.add(ok, constraints);

        constraints.insets = new Insets(130,0,0,0);
        constraints.anchor = GridBagConstraints.PAGE_START;
        this.add(titulo, constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,50,200,60);
        this.add(slider, constraints);

        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0,10,200,0);
        this.add(init, constraints);

        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(0,0,200,10);
        this.add(end, constraints);

        validated = new JCheckBox("VALIDATED");
        explicitContent = new JCheckBox("EXPLICIT CONTENT");

        JTextField maxPlybacks= new JTextField();
        JLabel max = new JLabel("Change maximum playbacks for free users:");

        max.setForeground(Color.WHITE);
        max.setFont(new Font("Monospaced", Font.PLAIN, 20));

        validated.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        validated.setFont(new Font("Monospaced", Font.PLAIN, 25));
        validated.setForeground(Color.WHITE);
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(80,40,0,0);
        this.add(validated, constraints);

        explicitContent.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        explicitContent.setFont(new Font("Monospaced", Font.PLAIN, 25));
        explicitContent.setForeground(Color.WHITE);
        constraints.insets = new Insets(160,40,0,0);
        this.add(explicitContent, constraints);
        constraints.insets = new Insets(160,40,0,0);

        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(0,  0, 150, 0);
        this.add(max, constraints);
        constraints.insets = new Insets(0,  0, 100, 0);
        maxPlybacks.setColumns(10);
        this.add(maxPlybacks, constraints);

        JLabel ok2 = new JLabel(new ImageIcon("img/ok_opt.png"));
        ok2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                App.getInstance().getAdministrator().setMAX_PLAYBACKS(Integer.parseInt(maxPlybacks.getText()));

                if (App.adminDemo) {
                    JOptionPane.showMessageDialog(getParent(), "Great, you look so comfy with the app!" +
                            "Next thing to do is play a little bit with the left right corner foldable menue", "Demo", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        constraints.insets = new Insets(0,  190, 100, 0);
        this.add(ok2, constraints);

    }

    /**
     * Method that changes the information to the information of the current song playing.
     * @param song
     */
    public void changeContent(Song song){
        this.current = song;
        this.titulo.setText(song.getTitle());
        this.songCover.setIcon(new ImageIcon(new ImageIcon(song.getCoverPath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        revalidate();
        repaint();
    }

    public class BackGround extends Thread{
        public void run(){
            while(true){
                MediaPlayer media = MediaPlayer.getInstance();
                Song song = media.getCurrent();
                int fixduration = 0;
                int duration = 0;
                try{
                    duration  = (int) Mp3Player.getDuration(song.getPath());
                }catch (Exception exc){
                    exc.printStackTrace();
                }
                fixduration = duration;
                slider.setMaximum(duration);
                //autor.setText(Album.getAuthorsAsString(media.getCurrent().getAuthors()));
                titulo.setText(media.getCurrent().getTitle());
                ImageIcon profile =  new ImageIcon(media.getCurrent().getCoverPath());
                Image image = profile.getImage(); // transform it
                image = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                songCover.setIcon(new ImageIcon(image));
                end.setText(timeformat(duration));
                while(duration > 0){
                    duration--;
                    init.setText(timeformat(fixduration - duration));
                    slider.setValue(fixduration-duration);
                    try{
                        Thread.sleep(1000);
                    }catch (Exception exc){
                        System.out.println(exc);
                    }
                }

            }
        }
        public String timeformat(int totalSecs){
            int minutes = (totalSecs) / 60;
            int seconds = totalSecs % 60;

            return String.format("  %02d:%02d  ", minutes, seconds);
        }
    }

}
