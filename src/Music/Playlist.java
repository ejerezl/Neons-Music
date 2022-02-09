package Music;

import App.App;
import Users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class of Playlist that inherit from Aggrupation and that defines object
 * of music type Playlist.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Playlist extends Aggrupation implements Serializable {

    private int numberSongs;
    private ArrayList<Aggrupation> aggrupations;
    private User creator;

    /**
     * Constructor of playlist that receives the title of the Playlist and the initial songs
     * with it is composed by.
     * @param title
     * @param songs
     */
    public Playlist(String title, User user, Song ... songs) {
        super(title);
        this.creator = user;
        aggrupations = new ArrayList<Aggrupation>();
        for (Song song:songs){
            addSong(song);
        }
    }

    /**
     * Getter of the user who created the playlist
     * @return user
     */
    public User getUser() { return creator; }
    /**
     * Constructor of playlist that receives the title of the Playlist and the initial aggrupation
     * with it is composed by.
     * @param title
     * @param agr
     */
    public Playlist(String title, User user, Aggrupation agr) {
        super(title);
        this.creator = user;
        aggrupations = new ArrayList<Aggrupation>();
        this.aggrupations.add(agr);
    }


    /**
     * This method return the duration of the aggrupation by adding up
     * the songs's and agruppations's length.
     * @return the total length
     */
    public double getLength() {
        Double length = 0.0;

        for (Song song:getSongs()) {
            length += song.getLength();
        }

        for (Aggrupation agr:aggrupations) {
            for (Song song:agr.getSongs())
                length += song.getLength();
        }
        return length;
    }

    /**
     * Getter of the title attribute
     * @return the title of the Playlist
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns a list of aggrupations (albums and other playlists) that
     * compose the current playlist.
     * @return the array list of aggrupations
     */
    public ArrayList<Aggrupation> getAggrupations() { return aggrupations; }

    public void addAgrupation(Aggrupation agr) {
        for (Song song:agr.getSongs()) {
            numberSongs += 1;
        }
        aggrupations.add(agr);
    }

    /**
     * This method remove the aggrupation 'agr' received by argument from
     * the list of aggrupations.
     * @param agr
     */
    public void removeAgrupation(Aggrupation agr) {
        for (Song song:agr.getSongs()) {
            numberSongs -= 1;
        }
        aggrupations.remove(agr);
        if (numberSongs == 0) {
            deletePlaylist();
        }
    }

    /**
     * This method adds a song received by argument, 'song', to the playlist
     * @param song
     */
    public void addSong(Song song) {
        if(songs.contains(song)) { //We don't want songs to be repeated
            return;
        }
        for (Aggrupation agr:aggrupations) {
            if (agr.getRealSongs().contains(song)) {
                return;
            }
        }
        song.addReference(this);
        songs.add(song);
        numberSongs = numberSongs + 1;
    }

    /**
     * This method returns the artist that created the song in the position
     * given by argument.
     *
     * @param index
     * @return the list of artists(users)
     */
    public ArrayList<User> getArtist(int index) {  //devuelve el artista de la cancion en la posicion de index
        return songs.get(index).getAuthors();
    }

    /**
     * Returns the title of the song that is in the index position given by
     * the integer received by argument.
     * @param index
     * @return title of the song asked
     */
    public String getTitleSong(int index) { return songs.get(index).getTitle(); }

    /**
     * This method returns the length of the song that is in the index position
     * given by the integer received by argument.
     * @param index
     * @return length of the song asked
     */
    public double getLength(int index) { return songs.get(index).getLength(); }

    /**
     * Returns whether a playlist is a playlist or not
     * @return true
     */
    public Boolean isPlaylist() {
        return true;
    }

    /**
     * This method deletes the song 's' received by argument from the list
     * of songs that compose the playlist.
     * @param s
     */
    public void removeSong(Song s) {
        int i = 0;

        i = getSongIndex(s);
        if (i != -1) {
            songs.get(i).deleteReference(this);
            if (size() == 1) {
                deletePlaylist();
            } else {
                songs.remove(i);
            }
            numberSongs -= 1;
        }
    }

    /**
     * This method returns the number of songs that are in the playlist.
     * @return
     */
    public int size() {
        return numberSongs;
    }

    /**
     * This method deletes the current playlist
     */
    public void deletePlaylist() {
        getUser().deletePlaylist(this);
        songs.clear();
    }

    /**
     * Prints the current state of the playlist
     */
    public void ViewPlaylist() {
        if (size() == 0) {
            System.out.println("\nPlaylist is empty.");

        } else {

            System.out.println(getTitle());
            System.out.println(size());

            for (int i = 0; i < size(); i++) {

                System.out.print("\n#" + (i+1) + " ");
                System.out.print(""+ getArtist(i) + " - ");
                System.out.print("\""+ songs.get(i).getTitle()+ "\"" + ", ");
                System.out.print(this.getLength() + ", ");

            }
        }
    }

    /**
     * This method returns the song in the playlist that can be played.
     * @return the array of songs
     */
    public ArrayList<Song> getSongs() {
        HashSet<Song> songs = new HashSet<Song>();
        for (Aggrupation aggrupation : aggrupations) {
            for (Song song: aggrupation.getSongs()) {
                if ((App.getLogged() == null || App.getLogged().getIsAdult() == false)&&song.isExplicit()) {
                    continue;
                }
                songs.add(song);
            }
        }
        for (Song song: this.songs) {
            if ((App.getLogged() == null || App.getLogged().getIsAdult() == false)&&song.isExplicit()) {
                continue;
            }
            songs.add(song);
        }
        return new ArrayList<Song>(songs);
    }
}
