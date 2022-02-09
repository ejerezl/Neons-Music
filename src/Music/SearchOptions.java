package Music;

import java.io.Serializable;

/**
 * Class that only contains a constructor for the options available for searching. Depending on which options are chosen,
 * the search method will return song, album, or author results.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class SearchOptions implements Serializable {

    public final boolean sSongs;
    public final boolean sAlbums;
    public final boolean sAuthors;

    /**
     * Constructor for the search options.
     * @param sSongs
     * @param sAlbums
     * @param sAuthors
     */
    public SearchOptions(boolean sSongs, boolean sAlbums, boolean sAuthors) {
        this.sSongs = sSongs;
        this.sAlbums = sAlbums;
        this.sAuthors = sAuthors;
    }
}
