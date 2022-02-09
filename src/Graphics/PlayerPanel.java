package Graphics;

import App.App;
import Music.Album;
import Music.MediaPlayer;
import Music.Song;
import pads.musicPlayer.Mp3Player;
import twitter4j.TwitterException;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Class PlayerPanel. Class that implements the player panel, the one in charge of playing/stopping/repeating a song.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class PlayerPanel extends JPanel {
    private GridBagLayout layout;
    private GridBagConstraints c1;
    private JLabel init;
    private JLabel end;
    private JLabel titulo;
    private JLabel songCover;
    private JLabel autor;
    private JSlider slider;
    private Thread backgroundInfoUpdater;
    public PlayerPanel(){
        super();
        layout = new GridBagLayout();
        this.setLayout(layout);
        c1 = new GridBagConstraints();
        this.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        c1.weightx = 0.5;
        c1.weighty = 0.5;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.insets = new Insets(0,30,0,0);
        c1.anchor = GridBagConstraints.LINE_START;

        if(MediaPlayer.getInstance().getCurrent() == null){
            songCover = new JLabel(new ImageIcon("img/music_opt.png"));
            titulo = new JLabel("Nothing playing");
            autor = new JLabel("");
        }else{
            songCover = new JLabel(new ImageIcon(MediaPlayer.getInstance().getCurrent().getCoverPath()));
            titulo = new JLabel(MediaPlayer.getInstance().getCurrent().getTitle());
            autor = new JLabel(MediaPlayer.getInstance().getCurrent().getAuthors().stream().map(author -> author.getArtisticname()).collect(Collectors.joining()));
        }
        titulo.setForeground(Color.WHITE);
        autor.setForeground(Color.WHITE);
        JLabel previousImage = new JLabel(new ImageIcon("img/previous_opt.png"));
        previousImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(MediaPlayer.getInstance().isPlaying()){
                    MediaPlayer.getInstance().playBefore();
                    backgroundInfoUpdater.stop();
                    backgroundInfoUpdater = new BackGroundUpdater();
                    backgroundInfoUpdater.start();
                }
            }
        });
        JLabel nextImage = new JLabel(new ImageIcon("img/next_opt.png"));
        nextImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(MediaPlayer.getInstance().isPlaying()){
                    MediaPlayer.getInstance().playNext();
                    backgroundInfoUpdater.stop();
                    backgroundInfoUpdater = new BackGroundUpdater();
                    backgroundInfoUpdater.start();
                }
            }
        });
        JLabel playPauseImage = new JLabel(new ImageIcon("img/play_opt.png"));
        playPauseImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(MediaPlayer.getInstance().isPlaying()){
                    MediaPlayer.getInstance().stopPlaying();
                    backgroundInfoUpdater.stop();

                    if (App.demo) {
                        JOptionPane.showMessageDialog(getParent(), "There you go! You already know how the\n" +
                                "repeat and random buttons work... so let's\n add a song to the queue! Search for a song, click \n" +
                                "the right button and select 'Add to queue'", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        JLabel thumbsUp = new JLabel(new ImageIcon("img/thumbsup_opt.png"));
        JLabel thumbsDown = new JLabel(new ImageIcon("img/thumbsdown_opt.png"));
        JLabel twitterIcon = new JLabel(new ImageIcon("img/twitter_opt.png"));
        twitterIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!App.getLogged().getTwitterClient().isLogged()){
                    String input;
                    try{
                        input = showMessageDialogWithLink();
                        if(App.getLogged().getTwitterClient().login(input) ){
                            JOptionPane.showMessageDialog(getParent(), "Succesfully logged in!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(getParent(), "Ooops! Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (Exception exc){
                        exc.printStackTrace();
                    }
                }
            }
            public String showMessageDialogWithLink() throws TwitterException{
                JLabel label = new JLabel();
                Font font = label.getFont();

                // create some css from the label's font
                StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
                style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
                style.append("font-size:" + font.getSize() + "pt;");

                // html content
                JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
                        + "Please login to this account so you can tweet and insert the pin code <a href=\" "
                        + App.getLogged().getTwitterClient().getLoginURL()
                        + " \">a link</a> "
                        + "</body></html>");

                // handle link events
                ep.addHyperlinkListener(new HyperlinkListener()
                {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent e)
                    {
                        try {
                            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                                java.awt.Desktop.getDesktop().browse(java.net.URI.create(e.getURL().toString())); // roll your own link launcher or use Desktop if J6+
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }

                    }
                });
                ep.setEditable(false);
                ep.setBackground(label.getBackground());

                // show
                return JOptionPane.showInputDialog(null, ep);
            }
        });
        twitterIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                App.getLogged().getTwitterClient().tweet("Que pasaaaaa");
            }
        });
        JLabel repeatImage = new JLabel(new ImageIcon("img/repeat_opt.png"));
        repeatImage.addMouseListener(new MouseAdapter() {
            public boolean set = true;
            @Override
            public void mouseClicked(MouseEvent e) {
                MediaPlayer.getInstance().setRepeat(true);
                if(set){
                    repeatImage.setIcon(new ImageIcon("img/repeat_true_opt.png"));
                }else{
                    repeatImage.setIcon(new ImageIcon("img/repeat_opt.png"));
                }
                set = !set;
            }
        });
        JLabel shuffleImage = new JLabel(new ImageIcon("img/shuffle_opt.png"));
        shuffleImage.addMouseListener(new MouseAdapter() {
            public boolean set = true;
            @Override
            public void mouseClicked(MouseEvent e) {
                MediaPlayer.getInstance().setRandom(true);
                if(set){
                    shuffleImage.setIcon(new ImageIcon("img/shuffle_true_opt.png"));
                }else{
                    shuffleImage.setIcon(new ImageIcon("img/shuffle_opt.png"));
                }
                set = !set;
            }
        });
        repeatImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MediaPlayer.getInstance().setRepeat(true);
            }
        });
        slider = new JSlider(JSlider.HORIZONTAL, 0,300,150);
        slider.setForeground(Color.WHITE);

        init = new JLabel("0:00");
        init.setForeground(Color.WHITE);
        end = new JLabel("3:00");
        end.setForeground(Color.WHITE);

        this.add(songCover, c1);

        c1.insets = new Insets(0,120,0,0);
        c1.anchor = GridBagConstraints.LINE_START;
        this.add(titulo, c1);

        c1.insets = new Insets(50,120,0,0);
        this.add(autor, c1);

        c1.gridx = 1;
        c1.anchor = GridBagConstraints.PAGE_START;
        c1.insets = new Insets(20,0,0,380);
        this.add(previousImage, c1);

        c1.insets = new Insets(20,0,0,285);
        this.add(playPauseImage, c1);

        c1.insets = new Insets(20,0,0,190);
        this.add(nextImage, c1);

        c1.insets = new Insets(20,0,0,450);
        this.add(shuffleImage, c1);

        c1.insets = new Insets(20,0,0,120);
        this.add(repeatImage, c1);

        c1.anchor = GridBagConstraints.CENTER;
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.insets = new Insets(30,30,0,315);
        this.add(slider, c1);

        c1.anchor = GridBagConstraints.LINE_START;
        c1.fill = GridBagConstraints.NONE;
        c1.insets = new Insets(30,0,0,50);
        this.add(init, c1);

        c1.anchor = GridBagConstraints.LINE_END;
        c1.insets = new Insets(30,50,0,285);
        this.add(end, c1);

        c1.gridx = 2;
        c1.anchor = GridBagConstraints.LINE_START;
        c1.insets = new Insets(0,0,0,0);
        this.add(thumbsUp, c1);
        c1.insets = new Insets(0,50,0,0);
        this.add(thumbsDown, c1);
        c1.insets = new Insets(0, 100, 0 , 0);
        this.add(twitterIcon, c1);

        this.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        this.setMinimumSize(new Dimension(1300, 100));



    }

    /**
     * Method to start playing a song.
     */
    public void startPlaying(){
        if(backgroundInfoUpdater != null){
            backgroundInfoUpdater.stop();
           // MediaPlayer.getInstance().stopPlaying();
        }
        backgroundInfoUpdater = new BackGroundUpdater();
        backgroundInfoUpdater.start();

    }

    /**
     * Class BackGroundUpdater. Class that is actually a thread that runs and updates cover, slider, title... while
     * the user listens to the songs.
     */
    public class BackGroundUpdater extends Thread{
        public void run(){
            while(true){
                MediaPlayer media = MediaPlayer.getInstance();
                Song song = media.getCurrent();
                int fixduration = 0;
                int duration = 0;
                try{
                    duration  = (int)Mp3Player.getDuration(song.getPath());
                }catch (Exception exc){
                    exc.printStackTrace();
                }
                fixduration = duration;
                slider.setMaximum(duration);
                autor.setText(Album.getAuthorsAsString(media.getCurrent().getAuthors()));
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
