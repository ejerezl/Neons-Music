package Graphics;

import App.App;
import Music.Album;
import Music.Playlist;
import Users.User;

import javax.swing.*;
import java.awt.*;

/**
 * Class MidPanel. Class made with the purpose of changing the middle panels in the home screen between them.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class MidPanel extends JPanel {
    CardLayout cardLayout = new CardLayout();
    InicioPanel inicio;
    AlbumPanel albumPanel;
    PlaylistPanel playlistPanel;
    SearchResultPanel searchPanel;
    AuthorPanel authorPanel;
    public MidPanel(){
        super();
        this.setLayout(cardLayout);
        inicio = new InicioPanel(this);
        albumPanel = null;
        playlistPanel = null;
        searchPanel = new SearchResultPanel(this);
        authorPanel = null;
        this.add(inicio, "inicio");
        this.add(searchPanel, "search");
        cardLayout.show(this, "inicio");
    }

    public void change(String string){
        if(string.equals("album") || string.equals("inicio") || string.equals("search") || string.equals("playlist")){
            cardLayout.show(this, string);

            if (App.demo) {
                if (string.equals("search")) {
                    JOptionPane.showMessageDialog(getParent(), "Here there will be your search results. Left click in \na song to listen to it," +
                            "right click to open a menu", "Demo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        if(string.equals("search")){
            searchPanel.focus();
        }
    }

    public void change(Playlist playlist){
        //cardLayout.removeLayoutComponent(playlistPanel);
        if(playlistPanel != null){
            this.remove(playlistPanel);
        }
        playlistPanel = new PlaylistPanel(playlist, this);
        this.add(playlistPanel, "playlist");
        cardLayout.show(this, "playlist");

        if (App.demo) {
            JOptionPane.showMessageDialog(getParent(), "This is a playlist window. Left click on a song to play it,\n" +
                    "right click to open a menu");
        }
    }

    public void change(Album album){
        if(albumPanel != null){
            this.remove(albumPanel);
        }
        albumPanel = new AlbumPanel(album, this);
        this.add(albumPanel, "album");
        cardLayout.show(this, "album");

        if (App.demo) {
            JOptionPane.showMessageDialog(getParent(), "This is an album window. You can click the plus \n button to add" +
                    " it to one of your playlists.\n Left click on a song to play it, right click to open a menu");
        }

    }

    public void change(User user){
        if(authorPanel != null){
            this.remove(authorPanel);
        }
        authorPanel = new AuthorPanel(user, this);
        this.add(authorPanel, "author");
        cardLayout.show(this, "author");

        if(App.demo) {
            JOptionPane.showMessageDialog(getParent(), "This is an author window. You can follow the author\n " +
                    "by clicking in the 'Follow' button. Left click on an album to open the\n album window, or right click to " +
                    "display an option menu");
        }
    }



}
