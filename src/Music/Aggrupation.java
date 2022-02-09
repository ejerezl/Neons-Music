package Music;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract class Aggrupation that group both Album and Playlist classes
 * and that defines the common methods and attributes between the two classes.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public abstract class Aggrupation implements Serializable {
    protected String title;
    protected ArrayList<Song> songs;

    /**
     * Constructor with a title of the aggrupation as a parameter.
     * It also initializes the array of songs.
     * @param title
     */
    public Aggrupation(String title) {
        this.title = title;
        this.songs = new ArrayList<Song>();
    }

    /*
    public void add(Song song){
        songs.add(song);
    }*/

    /**
     * Method that given an integer, returns the song in that index in the
     * array songs that can be played (It means, if a song is in review it
     * will temporarily disappear).
     * @param index
     * @return Song that is in the position given by index
     */
    public Song get(int index){
        return getSongs().get(index);
    }

    /**
     * This method return the duration of the aggrupation by adding up
     * the songs's length.
     * @return the total length
     */
    public double getLength() {
        Double length = 0.0;
        for (Song song:getSongs()) {
            length += song.getLength();
        }
        return length;
    }

    /**
     * Given a song, this method returns the position in the array of playable songs
     * of that one.
     * @param s
     * @return index of the song given
     */
    public int getSongIndex(Song s) {
        int i = 0;
        Boolean flag = false;
        //First of all we find the index of the song
        for (Song song:songs) {
            if (song.equals(s)) {
                flag = true;
                break;
            }
            i += 1;
        }
        if (flag)
            return i;
        else
            return -1;
    }

    /**
     * Returns the title of the aggrupation
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns all the song that the aggrupation has, without taking
     * into consideration that songs that can not be played because of reports.
     * @return the array of songs
     */
    public ArrayList<Song> getRealSongs() { return songs; }

    /**
     * This method returns the song in the aggrupation that can be played.
     * @return the array of songs
     */
    public ArrayList<Song> getSongs() {
        ArrayList<Song> list = new ArrayList<>();
        for (Song s:songs) { //We only return songs that are not reported, it means,songs that can be played.
            if(s.getChecked())
                list.add(s);
        }
        return list;
    }

    /**
     * Setter of the aggrupation title. It is changed by the String received by argument.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the size of the playable songs.
     * @return
     */
    public int size(){
        return getSongs().size();
    }

    /**
     * Returns whether an aggrupation is an album or not
     * @return true if it's an album, false if not
     */
    public Boolean isAlbum() {
        return false;
    }

    /**
     * Returns whether an aggrupation is a  playlist or not
     * @return true if it's a playlist, false if not
     */
    public Boolean isPlaylist() {
        return false;
    }

    /**
     * Remove a song given as argument from the aggrupation.
     * @param s
     */
    public void removeSong(Song s) {
        s.deleteReference(this);
        songs.remove(s);
    }
}
