package Test;

import Music.*;
import Users.User;
import Users.AdminUser;
import Music.Library;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;

/**
 * Class to test the functionality of the Search class.
 * Each method is implemented to test one method of the Search
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */

public class SearchTest {

    private static User u1, u2, u3, u4 = null;
    private static Song s1, s2, s3, s4, s5, s6 = null;
    private static Album a1, a2, a3 = null;
    private static Library lib;
    private static SearchOptions sop;
    private static Search s;
    private static AdminUser admin = AdminUser.getInstance();

    /**
     * Done before every test method
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("first");
        answers.add("second");
        lib = Library.getInstance();

        User eva = AdminUser.getInstance().register("Eva Lacaba", "evalanic", "eva@gmail.com", new Date(1999, 02, 07), answers,"password");
        AdminUser.getInstance().login("eva@gmail.com", "password");
        User dani = AdminUser.getInstance().register("Dani Martin", "El canto del loco", "elcantodelloco@yahoo.es", new Date(77,2,19), answers, "psswrd");
        User riri = AdminUser.getInstance().register("Robyn Rihanna", "Rihanna", "rihannagmail.com", new Date(88,2,20), answers, "passw");
        User esther = AdminUser.getInstance().register("Esther Jerez", "Locurote", "locurotemaximo@gmail.com", new Date(99,9,9), answers, "pasitoapasito");

        u1 = dani;
        u2 = esther;
        u3 = riri;
        u4 = eva;
        s1 = new Song("Diamonds", musicPath + "diamonds.mp3", u3);
        s2 = new Song("Don't dial me", musicPath + "chicle3.mp3", musicPath + "front.jpg", u2,u4);
        s3 = new Song("Work", musicPath + "work.mp3", u3);
        s4 = new Song("Umbrella", musicPath + "umbrella.mp3", musicPath + "front.jpg", u3);
        s5 = new Song("Locura maxima", musicPath + "hive.mp3", musicPath + "front.jpg", u2, u4);
        s6 = new Song("Eres tonto", musicPath + "erestonto.mp3", musicPath + "front.jpg", u1);
        a1 = new Album("Best of Rihanna", s1,s3);
        a2 = new Album("I'm the best", s2,s5);
        a3 = new Album("Eres tonto", s6);

        lib.saveAlbum(a1);
        lib.saveAlbum(a2);
        lib.saveSong(s4);
        lib.saveAlbum(a3);

    }

    /**
     * Done after every test method
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    //suponiendo que las canciones o se suben en un album siempre, aunque sea un sencillo.

    /**
     * Tester of method search()
     * Here we create a variable of search options so like this we get the results of the criteria we've chosen.
     * Once created, we invoke the search method and we check if the results are the ones expected.
     */
    @Test
    public void search() {
        SearchList results;
        int cont = 0;
        boolean flag = false;

        this.sop = new SearchOptions(true, true, true);
        results = s.search(this.lib, admin, "loc", this.sop);

        for (User u: results.getAuthorList()) {
            if ( u.equals(u1) || u.equals(u2)) {
                cont = cont + 1;
            }
        }

        for (Song s : results.getSongList()){
            if( s.equals(s5)) {
                cont = cont + 1;
            }
        }

        if (results.getAlbumList().size() != 0){
            cont = cont + 1;
        }

        if (cont == 3){
            flag = true;
        }

        assertTrue(flag);
    }

    /**
     * This is the tester of searchAlbum()
     * We invoke the method with a search string to match, and we check if the number of result albums
     * that should return is correct
     */
    @Test
    public void searchAlbum() {
        ArrayList<Album> results;
        int cont = 0;
        boolean flag = false;

        results = s.searchAlbum(lib, "best");

        for ( Album a: results){
            if( a.equals(a1) || a.equals(a2)) {
                cont = cont + 1;
            }
        }
        if ( cont == 2){
            flag = true;
        }

        assertTrue(flag);
    }

    /**
     * This is the tester of searchSong()
     * We invoke the method with a search string to match, and we check if the number of result songs
     * that should return is correct
     */
    @Test
    public void searchSong() {
        ArrayList<Song> results;
        int cont = 0;
        boolean flag = false;

        results = s.searchSong(lib, "dia");

        for (Song s: results){
            if (s.equals(s1) || s.equals(s2)) {
                cont = cont + 1;
            }
        }
        if (cont == 2){
            flag = true;
        }

        assertTrue(flag);
    }

    /**
     * This is the tester of searchAuthor()
     * We invoke the method with a search string to match, and we check if the number of result authors
     * that should return is correct
     */
    @Test
    public void searchAuthor() {
        ArrayList<User> results;
        int cont = 0;
        boolean flag = false;


        results = s.searchAuthor(admin, "loc");

        for (User u : results){
            if (u.equals(u1)|| u.equals(u2)) {
                cont = cont + 1;
            }
        }
        if ( cont == 2){
            flag = true;
        }
        assertTrue(flag);
    }
}