package Music;


import Users.User;
import pads.musicPlayer.Mp3Player;


import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * This class describes the structure of an object of the music type Song.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Song implements Serializable {


    private String title;
    private Date uploaded;
    private ArrayList<User> authors;
    private ArrayList<Aggrupation> contained;
    private double length;
    private String path;
    private String coverPath;
    private Boolean isexplicit = false;
    private int id;
    private int likes = 0;
    private int dislikes = 0;
    private Playback playbacks;
    private boolean checked = true;


    /**
     * Constructor that receives a title, a path of the file, and the users that created the song.
     * @param title
     * @param path
     * @param users
     * @throws FileNotFoundException
     */
    public Song (String title, String path, User ... users) throws FileNotFoundException {
        this.title = title;
        this.playbacks = new Playback();
        this.authors = new ArrayList<User>();
        this.contained = new ArrayList<Aggrupation>();
        for ( User u : users) {
            authors.add(u);
        }
        try {
            this.length = Mp3Player.getDuration(path);
        } catch(FileNotFoundException exc) {
            throw(exc);
        }
        this.path = path;
        this.uploaded = new Date();
        this.id = Library.getInstance().updateId();
        this.coverPath = null;
    }

    /**
     * Another constructor that initializes the song with a cover picture.
     * @param title
     * @param path
     * @param coverpath
     * @param users
     * @throws FileNotFoundException
     */
    public Song (String title, String path, String coverpath,  User ... users) throws FileNotFoundException{
        this(title, path, users);
        if(coverpath == null){
            this.coverPath = "img/music_opt.png";
        }else{
            this.coverPath = coverpath;
        }
        this.uploaded = new Date();

    }

    public boolean hasCoverPath() {return coverPath != null;}
    /**
     * This method increments the playbacks of the song.
     */
    public void incrementPlaybacks() { playbacks.increaseValue(); }

    /**
     * This method returns the date when the song was uploaded.
     * @return the date
     */
    public Date getUploaded() { return uploaded; }

    /**
     * This method returns the path of the cover path of the song
     * @return the path of the cover picture
     */
    public String getCoverPath() { return coverPath; }

    /**
     * Here a reference can be added to the actual references of the song.
     * The reference added it that received by argument.
     * @param aggr
     */
    public void addReference(Aggrupation aggr) {
        if (!contained.contains(aggr)) {
            contained.add(aggr);
        }
    }

    /**
     * Method that deletes the aggrupation received by argument from the
     * actual references of the song.
     * @param aggr
     */
    public void deleteReference(Aggrupation aggr) { contained.remove(aggr); }


    /**
     * Returns an array list of the aggrupation where the song is contained.
     * @return the list of aggrupations
     */
    public ArrayList<Aggrupation> getContained() { return contained; }

    /**
     * Returns a Boolean that indicates if the song have been checked or it's being checked.
     *
     * @return true if it has already been checked, false if is being checked right now.
     */
    public Boolean getChecked() { return checked; }

    /**
     * Setter of the attribute 'checked'.
     * @param value
     */
    public void setChecked(Boolean value) { this.checked = value; }

    /**
     * Return the legth(duration) of the song
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns the path of the mp3 file of the song
     * @return the path
     */
    public String getPath(){
        return path;
    }

    /**
     * Setter of the attribute 'path'
     * @param path
     */
    public void setPath(String path) { this.path = path; }

    public void rateSong (int points) {

        if ( points == 0) {
            dislikes += 1;
        } else {
            likes += 1;
        }

    }
    /**
     * Method that returns a list of authors of the song
     * @return the list of authors
     */
    public ArrayList<User> getAuthors() {
        return (ArrayList<User>)authors.clone();
    }

    /**
     * Getter of the attribute 'Title'
     * @return the title of the song
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter of the title song.
     * @param title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Setter of the song's Authors.
     * @param users
     */
    public void setAuthors(User ... users) {
        for (User u : users) {
            authors.add(u);
        }
    }

    /**
     * Setter of the song's cover path.
     * @param coverPath
     */
    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }

    /**
     * Getter of the likes that the song has.
     * @return
     */
    public int getLikes() { return this.likes; }

    /**
     * Getter of the dislikes that the song has.
     * @return
     */
    public int getDislikes() { return this.dislikes; }

    /**
     * Getter of the structure playbacks that describes the number of playbacks this song
     * has per day.
     * @return
     */
    public Playback getPlaybacks() { return this.playbacks; }

    /**
     * This method returns if the song has explicit content or not.
     * @return isexplicit (boolean)
     */
    public boolean isExplicit() {
        return isexplicit;
    }

    /**
     * Setter of 'isExplicit' attribute from song.
     * @param explicit
     */
    public void setIsexplicit(boolean explicit) { this.isexplicit = explicit; }

    /**
     * Method to see the state of a song.
     * @return string with all the information
     */
    public String toString() {
        return "Title: "+ this.getTitle();
    }

    public int getId(){
        return this.id;
    }


}
