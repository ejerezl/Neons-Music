package Users.Notifications;

import Music.Song;

import java.io.Serializable;


/**
 * Class that describes the Song Notifications
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class SongNotificationDenied extends Notification implements Serializable {

        private Song song;
        private String message;

    /**
     * Constructor of the Song Notification denied.
     * @param song
     * @param message
     */
    public SongNotificationDenied (Song song, String message) {
        this.song = song;
        this.message = message;
    }

    /**
     * GGetter of the song
     * @return the song
     */
    public Song getSong() {
        return song;
    }

    /**
     * Getter of the message
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Method to get information about the Song notification denied
     * @return the resulting string
     */
    public String toString(){
            return (message + song.getTitle());
        }
}
