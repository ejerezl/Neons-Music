package Graphics;

import App.App;

import javax.swing.*;
import java.awt.*;

/**
 * Class HomeFrame. Class made to change between the different panels in the home screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class HomeFrame extends JFrame {
    private static HomeFrame instance;
    App app;
    CardLayout cardLayout;
    JPanel cardPanel;
    int contador = 1;
    UploadSong uploadSong;
    public HomeFrame(App app){
        super();
        this.app = app;
        HomeFrame.instance = this;
        this.addWindowListener(new SaveDataOnClose(app));
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new Home(app), "home");
        if(App.getLogged() != null){
            uploadSong = new UploadSong(app);
            cardPanel.add(uploadSong, "upload");
            cardPanel.add(new Settings(), "settings");
        }
        this.add(cardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1300,700));
        this.pack();
        this.setVisible(true);
        cardLayout.show(cardPanel, "Home");
        //cardLayout.show(this, "Home");
    }

    public static HomeFrame getInstance(){
        return instance;
    }

    public void change(String string){
        if(string.equals("home") || string.equals("upload") || string.equals("settings")){
            //repaint();
            cardLayout.show(cardPanel, string);
        }
        if(App.demo) {

            if (string.equals("upload")) {
                JOptionPane.showMessageDialog(getParent(), "This is the uploading window. Let's try...\n" +
                                "First of all select the song, or songs you want to upload from your computer.\n By clicking on each of them, you can" +
                                " type a name for each.\n You can also add a name for the album of those songs and finally set a cover fot it.\n Try it!",
                        "Demo", JOptionPane.INFORMATION_MESSAGE);
            } else if (string.equals("settings")) {
                JOptionPane.showMessageDialog(getParent(), "This is the settings window.\n Right now you're not a premium user. Click on" +
                        " the button \n Open with Paypal and enter a valid card number (16 digits).");
            }

            switch (contador) {
                case 1:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "You're back! Let's try and create a playlist.\n Click on the plus button " +
                                "and select a name for it.", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        contador++;
                    }
                    break;
                case 2:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "Use the search field to look for your favourite songs.\n" +
                                        "It will start playing by clicking on it. You can also\n left-click on a song and a pop-up menu will show up!",
                                "Demo", JOptionPane.INFORMATION_MESSAGE);
                        contador++;
                    }
                    break;
                case 3:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "Let's see now how the music player works. Select a song and press\n" +
                                "the play/pause button!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        contador++;
                    }
                    break;
                case 4:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "Why don't you start following someone you have interest in\n " +
                                "so you'll start receiving notifications if\n they upload something? Search for an author. Then \n" +
                                "you have two options: right click on it in the search results, or enter \n in his profile and click the 'Follow' button");
                        contador++;
                    }
                    break;
                case 5:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "Have you tried to link your account to Twitter?\n" +
                                "Click on the twitter icon to do it!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                        contador++;
                    }
                    break;
                case 6:
                    if (string.equals("home")) {
                        JOptionPane.showMessageDialog(getParent(), "Well, that was all! You're ready!", "Demo", JOptionPane.INFORMATION_MESSAGE);
                    }
            }
        }


    }

    public void washUpload(){
        cardPanel.remove(uploadSong);
        this.uploadSong = new UploadSong(app);
        cardPanel.add(uploadSong, "upload");


    }
}
