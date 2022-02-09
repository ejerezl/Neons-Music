package Music;

import Users.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class of Album that inherit from Aggrupation and that defines object
 * of music type Album.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Album extends Aggrupation implements Serializable, Comparable<Album> {

    private ArrayList<User> authors;
    private String coverPath = null;
    private Boolean explicit = false;
    private Date creationDate;

    /**
     * Constructor of Album.
     * @param title
     */
    public Album(String title) {
        super(title);
        authors = new ArrayList<User>();
        this.creationDate = new Date();
    }
    /**
     * Constructor of Album that initializes the attributes and add references
     * to songs of the album.
     * @param title
     * @param songs
     */
    public Album(String title, Song... songs) {
        this(title);
        this.songs = new ArrayList<Song>();
        for (Song song : songs) {
            song.addReference(this);
            if (song.isExplicit()) {
                this.explicit = true;
            }

            for (User author : song.getAuthors()) {
                if (authors.contains(author) == false) {
                    authors.add(author);
                }
            }
            this.songs.add(song);
        }
        this.creationDate = new Date();
    }

    /**
     * Another constructor but creating the Album with a cover picture.
     * @param title
     * @param pathOfPicture
     * @param songs
     */
    public Album(String title, String pathOfPicture, Song... songs) {
        this(title, songs);
        this.coverPath = pathOfPicture;
    }

    /**
     * This method returns the number of playbacks that the album has since the date
     * given by argument till today.
     *
     * @param date
     * @return number of playbacks
     */
    public int getPlaybacks(Date date) {
        int playbacks = 0;
        for (Song song:songs) {
            playbacks += song.getPlaybacks().getPlaybacks(date);
        }
        return playbacks;
    }

    /**
     * Returns if an album is an album
     * @return true
     */
    public Boolean isAlbum() {
        return true;
    }

    /**
     * Return a list of the authors that made the album
     * @return the authors
     */
    public ArrayList<User> getAuthors() {
        return (ArrayList<User>)authors.clone();
    }

    public void addAuthor(User author){
        if(authors == null){
            authors = new ArrayList<>();
        }
        authors.add(author);
    }
    /**
     * This function add authors to a specific song of the album
     *
     * @param song
     * @param users
     */
    public void setAuthors(Song song, User... users) { //
        authors.clear();
        for (User u : song.getAuthors()) {
            authors.add(u);
        }
    }

    /**
     * Method to add a song to the album, used only when creating it
     * @param s
     */
    public void addSong(Song s) {
        songs.add(s);
    }


    /**
     * This method remove the song in the position given by argument
     * from the album.
     * @param s
     */
    public void removeSong(int s) {
        songs.get(s).deleteReference(this);
        songs.remove(s);
    }

    /**
     * This method indicates if an album is explicit or not
     * @return if it's explicit
     */
    public boolean getIsExplicit(){
        return this.explicit;
    }

    /**
     * Setter of the attribute 'explicit'
     * @param value
     * @return
     */

    public void setIsExplicit(Boolean value) { this.explicit = value; }

    public Date getCreationDate() { return creationDate; }

    public String toString() {
        return "Album with title:" +title;
    }

    public int compareTo (Album album) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Playback pl = new Playback();

        return pl.compare(format.format(this.getCreationDate()), format.format(album.getCreationDate()));
    }

    public String getAuthorsAsString(){
        return Album.getAuthorsAsString(authors);
    }

    public static String getAuthorsAsString(ArrayList<User> authors){
        String ret = "";
        for(User user: authors){
            ret += user.getArtisticname() + ", ";
        }
        return ret.substring(0, ret.length()-2);
    }
}
