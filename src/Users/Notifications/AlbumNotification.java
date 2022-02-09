package Users.Notifications;

import Music.Album;
import Users.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that describes the Album Notifications
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AlbumNotification extends Notification implements Serializable {

    private Album album;

    /**
     * Constructor of the class
     * @param album
     */
    public AlbumNotification(Album album) {
        this.album = album;
    }

    /**
     * Getter of the album
     * @return the album
     */
    public Album getAlbum() { return album; }

    /**
     * Method to get information about the Album notification
     * @return the resulting string
     */
    public String toString(){
        String result = "Check out new album " + album.getTitle() + " uploaded by ";
        ArrayList<User> authors = album.getAuthors();

        for (User author: authors){
            result +=  author.getArtisticname() + " and ";
        }
        result = result.substring(0, result.length() - 4);
        return result;
    }
}
