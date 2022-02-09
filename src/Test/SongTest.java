package Test;

import Music.Aggrupation;
import Music.Album;
import Music.Playlist;
import Music.Song;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;

/**
 * Class to test the functionality of Song class.
 * Each method is implemented to test one method of the Song
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class SongTest {
    private static Song s1;
    private static User u1;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        u1 = new User("Eva Lacaba", "evaln", "eva@gmail.com", new Date(99,3,7));
        s1 = new Song("hive.mp3", musicPath + "hive.mp3", u1);
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tester of addReference()
     * Here we add a reference to a song and then check in the contained list of references.
     */
    @Test
    public void addReference(){
        Boolean flag = false;
        Album album = new Album("MyAlbum", s1);

        s1.addReference(album);
        for(Aggrupation agr:s1.getContained()) {
            if (agr.equals(album)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of deleteReference()
     * Here we add some references and then delete one of them. To check the functionality we search by
     * the deleted reference in the contained list.
     */
    @Test
    public void deleteReference(){
        Boolean flag = true;

        Album album = new Album("MyAlbum", s1);
        Playlist playlist = new Playlist("MyPlaylist", u1, s1);

        s1.addReference(album);
        s1.addReference(playlist);

        s1.deleteReference(album);
        for(Aggrupation agr:s1.getContained()) {
            if (agr.equals(album)) {
                flag = false;
            }
        }
        assertTrue(flag);

    }

    /**
     * Tester of incrementPlaybacks()
     * Here we increment the Playbacks of a song and then check if playbacks has changed
     * to the value expected.
     */
    @Test
    public void incrementPlaybacks(){
        for(int i = 1; i < 5; i++){
            s1.incrementPlaybacks();
            assertEquals(s1.getPlaybacks().getPlaybacks(new Date()), i);
        }
    }

    /**
     * Tester of rateSong()
     * This method simulate the rating of a Song and then checks the likes and dislikes of the
     * song.
     */
    @Test
    public void rateSong() {
        s1.rateSong(0);
        assertTrue(s1.getDislikes() == 1);

        s1.rateSong(5);
        assertTrue(s1.getLikes() == 1);
    }
}