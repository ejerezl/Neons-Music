package Test;

import Music.SearchList;
import Music.Song;
import Music.Album;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;
/**
 * Class to test the functionality of the SearchList class.
 * Each method is implemented to test one method of the SearchList
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class SearchListTest {

    private static SearchList slist = null;
    private static Song song1, song2 = null;
    private static Album a1 = null;
    private static User u1 = null;


    /**
     * Done before every test method
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

       slist = new SearchList();
       u1 = new User("Esther Jerez", "ejerezl", "esther@gmail.com", new Date(99,9,9));
       song1 = new Song("hive.mp3",  musicPath+"hive.mp3", u1);
       song2 = new Song("chicle3.mp3", musicPath+"chicle3.mp3", u1);
       a1 = new Album("Hiving", song1, song2);

    }

    /**
     * Done after every test method
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tester of method addSong()
     * We add a song to the list and then we check if it was correctly added to it.
     */
    @Test
    public void addSong() {
        boolean flag = false;
        slist.addSong(song1);

        for(Song s: slist.getSongList()){
            if(s.equals(song1)){
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of method addAlbum()
     * We add an album to the list and then we check if it was correctly added to it.
     */
    @Test
    public void addAlbum() {
        boolean flag = false;
        slist.addAlbum(a1);

        for(Album a: slist.getAlbumList()){
            if(a.equals(a1)){
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of addAuthor()
     * We add an author to the list and then we check if it was correctly added to it.
     */
    @Test
    public void addAuthor() {
        boolean flag = false;
        slist.addAuthor(u1);

        for(User u: slist.getAuthorList()) {
            if (u.equals(u1)){
                flag = true;
            }
        }
        assertTrue(flag);
    }

}