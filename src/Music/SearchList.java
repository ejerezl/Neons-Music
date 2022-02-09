package Music;

import java.io.Serializable;
import java.util.ArrayList;
import Users.User;


/**
 * This class implements the methods used for the list returned in the global search method in class search.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */

public class SearchList implements Serializable {
    private ArrayList<Song> songList = new ArrayList<Song>();
    private ArrayList<Album> albumList = new ArrayList<Album>();
    private ArrayList<User> authorList = new ArrayList<User>();

    /**
     * This method adds a song to a list of songs which will be inside a global result list.
     * @param song
     */
    public void addSong(Song song){
        songList.add(song);
    }

    /**
     * Adds an album to a list of albums which will be inside a global list of results.
     * @param album
     */
    public void addAlbum(Album album) {
        albumList.add(album);
    }

    /**
     * Adds an author to a list of authors which will be inside a global list of results.
     * @param author
     */
    public void addAuthor(User author) {
        authorList.add(author);
    }

    /**
     * Method that returns the sub-list of songs in the global list of results.
     * @return list of songs
     */
    public ArrayList<Song> getSongList() {
        return songList;
    }

    /**
     * Method that returns the sub-list of albums in the global list of results.
     * @return list of albums
     */
    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Method that returns the sub-list of authors in the global list of results.
     * @return list of authors
     */
    public ArrayList<User> getAuthorList() {
        return authorList;
    }

}
