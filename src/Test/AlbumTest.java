package Test;

import Music.Album;
import Music.Song;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;

/**
 * Class to test the functionality of Album class.
 * Each method is implemented to test one method of the Album
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AlbumTest {
    Album album;
    User u1;
    Song s1;
    Song s2;
    Song s3;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        u1 = new User("Esther Jerez", "ejerezl", "esther@gmail.com", new Date(99,9,9));
        s1 = new Song("hive.mp3", musicPath + "hive.mp3", u1);
        s2 = new Song("chicle3.mp3", musicPath + "chicle3.mp3", u1);
        s3 = new Song("np.mp3", musicPath + "np.mp3", u1);
        s3.setChecked(false);
        album =  new Album("MiAlbum", s1, s2, s3);
    }

    /**
     * Done after every test method
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tester of getPlaybacks()
     * Here we just increment the playbacks of the songs from an album and
     * then check if the album playbacks are the expected.
     */
    @Test
    public void getPlaybacks() {
        s1.incrementPlaybacks();
        s2.incrementPlaybacks();

        assertTrue(album.getPlaybacks(new Date()) == 2);
    }

    /**
     * Tester of getSongIndex()
     * Just checking the index of different songs from an album.
     */
    @Test
    public void getSongIndex() {
        assertTrue(album.getSongIndex(s1) == 0);
        assertTrue(album.getSongIndex(s2) == 1);
    }

    /**
     * Tester of removeSong()
     * Starting from a created album, we delete one song and then
     * see if there's in the songs that belong to that album.
     */
    @Test
    public void removeSong() {
        Boolean flag = true;
        //We remove it from the album
        album.removeSong(s1);

        //then we search it, it must not be there
        for (Song song:album.getSongs()) {
            if (song.equals(s1)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of getRealSongs()
     * This method returns all the songs from an album, without
     * taking into consideration if the song is playable. So from
     * an album composed by songs playable and not, we check if it
     * returns all songs.
     */
    @Test
    public void getRealSongs() {
        ArrayList<Song> songs;

        songs = album.getRealSongs();
        //Although s3 is not checked song, this mehotd must return it
        assertTrue(songs.get(0).equals(s1));
        assertTrue(songs.get(1).equals(s2));
        assertTrue(songs.get(2).equals(s3));

    }

    /**
     * Tester of getSongs()
     * This method returns the songs that are in an album and that can be
     * played. So from an album composed by songs playable and not, we check if it
     * returns only the playable ones.
     */
    @Test
    public void getSongs() {
        ArrayList<Song> songs;

        songs = album.getSongs();

        //This method must only return songs that are checked
        assertTrue(songs.size() == 2);
        assertTrue(songs.get(0).equals(s1));
        assertTrue(songs.get(1).equals(s2));

    }

    /**
     * Tester of removeSong()
     * Here we remove a song from an album and then check that
     * it is no more in the songs that belong to that album.
     */
    @Test
    public void removeSong1() {
        ArrayList<Song> songs;

        album.removeSong(s1);
        songs = album.getRealSongs();
        assertTrue(songs.get(0).equals(s2));
        assertTrue(songs.size() == 2);
    }
}