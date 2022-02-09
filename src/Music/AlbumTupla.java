package Music;

import java.io.Serializable;

/**
 * Specific tuple composed of an integer and an album. It inherits from tuple.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AlbumTupla extends Tupla implements Serializable {
    private Album album;

    /**
     * Constructor that initializes the fields of the AlbumTuple with the values given
     * as parameters.
     * @param playbacks
     * @param album
     */
    public AlbumTupla(int playbacks, Album album) {
        super(playbacks);
        this.album = album;
    }

    /**
     * Getter of the album
     * @return the album.
     */
    public Album getAlbum() {
        return album;
    }
}
