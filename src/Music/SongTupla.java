package Music;

import java.io.Serializable;


/**
 * Specific tuple composed of an integer and a song. It inherits from tuple.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class SongTupla extends Tupla implements Serializable {
    private Song song;

    /**
     * Constructor that initializes the fields of the SongTuple with the values given
     * as parameters.
     * @param playbacks
     * @param song
     */
    public SongTupla(int playbacks, Song song) {
        super(playbacks);
        this.song = song;
    }

    /**
     * Getter of the song
     * @return the song.
     */
    public Song getSong() {
        return song;
    }
}
