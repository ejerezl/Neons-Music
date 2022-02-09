package Test;

import Music.Aggrupation;
import Music.Album;
import Music.Playlist;
import Music.Song;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;
/**
 * Class to test the functionality of Playlist class.
 * Each method is implemented to test one method of the Playlist
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class PlaylistTest {
    User u1;
    Song s1;
    Song s2;
    Song s3;
    Album album;
    Playlist p2;
    Playlist p;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        u1 = new User("Eva Lacaba", "evaln", "eva@gmail.com", new Date(99,3,7));
        s1 = new Song("hive.mp3", musicPath + "hive.mp3", u1);
        s2 = new Song("chicle3.mp3", musicPath+ "chicle3.mp3", u1);
        s3 = new Song("np.mp3", musicPath + "np.mp3", u1);
        p2 = new Playlist("AnotherPlaylist", u1, s3);
        album = new Album("Album", s2);

        p = new Playlist("MiPlaylist", u1, s1, s2);
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tester of removeSong()
     * Here we remove a song from an existing playlist and then check if the song still remains
     * in the playlist or not.
     */
    @Test
    public void removeSong() {
        ArrayList<Song> songs;

        p.removeSong(s1);
        songs = p.getSongs();
        assertTrue(songs.size() == 1);
        assertTrue(songs.get(0).equals(s2));
    }

    /**
     * Tester of addSong()
     * Here we add a song to an existing playlist and then check if the song still remains
     * in the playlist or not.
     */
    @Test
    public void addSong() {
        Song s3 = null;
        ArrayList<Song> songs;
        Boolean flag = false;

        try{
            s3 = new Song("np.mp3", musicPath +"np.mp3", u1);
        } catch(FileNotFoundException exc){
            assertTrue(false);
        }

        p.addSong(s3);
        songs = p.getSongs();
        for(Song song:songs) {
            if(song.equals(s3))
                flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of size()
     * Just compare if the method returns the number of songs the playlist have.
     */
    @Test
    public void size() {
        assertTrue(p.size() == 2);
    }

    /**
     * Tester of getArtist()
     * Check if the artists of a specific song of a playlist are the correct ones.
     */
    @Test
    public void getArtist() {
        ArrayList<User> users;
        users = p.getArtist(0);

        assertTrue(users.get(0).equals(u1));
    }

    /**
     * tester of getTitleSong()
     * Check if the title of a specific song of a playlist is the correct one.
     */
    @Test
    public void getTitleSong() {
        String title = p.getTitleSong(0);

        assertTrue(title.equals("hive.mp3"));
    }

    /**
     * Tester of getLength()
     * Check if the length of a specific song of the playlist and the length of the whole
     * playlist are the correct ones.
     */
    @Test
    public void getLength() {
        assertTrue(p.getLength(0) == s1.getLength());
        assertTrue(p.getLength() == s1.getLength() + s2.getLength());
    }

    /**
     * Tester of isPlaylist()
     * Must return always true for playlists so we check
     */
    public void isPlaylist() {
        assertTrue(p.isPlaylist());
    }

    /**
     * Tester of isAlbum()
     * Must return always false for playlists so we check
     */
    public void isAlbum() {
        assertFalse(p.isAlbum());
    }

    /**
     * tester of addAgrupation()
     * We add an album and annother playlist to one, and check if the aggrupations are in the
     * playlist where we added in.
     */
    public void addAgrupation() {
        ArrayList<Aggrupation> agr = null;

        p.addAgrupation(p2);
        p.addAgrupation(album);

        agr = p.getAggrupations();

        assertTrue(agr.get(0).equals(p2));
        assertTrue(agr.get(1).equals(album));
        assertTrue(p.size() == 4);
    }

    /**
     * Tester of removeAgrupation()
     * Here we add and then remove some aggrupations from a playlist.
     * To check if it works properly we search for the deleted aggrupations in the playlist.
     */
    public void removeAgrupation() {
        ArrayList<Aggrupation> agr = null;

        p.addAgrupation(p2);
        p.addAgrupation(album);

        p.removeAgrupation(p2);

        agr = p.getAggrupations();

        assertTrue(agr.get(0).equals(album));
        assertTrue(agr.size() == 1);

    }

    /**
     * Tester of removeSong()
     * Here we add and then remove some song from a playlist.
     * To check if it works properly we search for the deleted song in the playlist.
     */
    @Test
    public void removeSong1() {
        ArrayList<Song> songs;
        p.removeSong(s2);
        songs = p.getSongs();
        assertTrue(p.size() == 1);
        assertTrue(songs.get(0).equals(s1));
    }

    /**
     * Tester of deletePlaylist()
     * Here we delete song by song from the playlist and then, when the playlist is empty,
     * it is eliminated automatically.
     */
    @Test
    public void deletePlaylist() {
        Boolean flag = true;

        p.deletePlaylist();
        for(Song song:p.getSongs()) {
            if(song.equals(s1) || song.equals(s2)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }
}