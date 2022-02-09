package Test;

import App.App;
import Music.Album;
import Music.Library;
import Music.Report;
import Music.Song;
import Users.Administrator;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the functionality of Administrator class.
 * Each method is implemented to test one method of the Administrator
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AdministratorTest {

    private static Administrator admin = null;
    private static Report report;
    private static User u1;
    private static User u2;
    private static Song song1;
    private static Song song2;
    private static Library lib;
    private static Album album;

    /**
     * Done before every test method
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        admin = new Administrator("Administrator", "administrator@gmail.com", "password");
        App.setAdministrator(admin);
        lib = Library.getInstance();

        //Creating the needed structures
        u1 = new User("Esther Jee", "estherto", "estherto@gmail.com", new Date(99,9,9));
        u2 = new User("alejanndritoandrakita", "alexx", "alexito@gmail.com", new Date(99,3,7));
        song1 = new Song("hive.mp3", musicPath + "hive.mp3", u1);
        song2 = new Song("chicle3.mp3", musicPath + "chicle3.mp3", u2);
        album = new Album("Album", song2);
        report = new Report(new Date(), u2, song1);
    }

    /**
     * Done after every test method
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        lib.removeAllAlbums();
        lib.removeAllPlaylist();
        lib.removeAllSongs();
        App.getAdministrator().removoeAllUploaded();
    }

    /**
     * Test of method getTopSong().
     * Here we will save in the library some songs with different value
     * of playbacks each one, and then get the top 1 (for example) to test
     * the functionality.
     */
    @Test
    public void getTopSongs() {
        Boolean flag = false;
        song1.incrementPlaybacks();
        song2.incrementPlaybacks();
        song2.incrementPlaybacks();
        lib.saveSong(song1);
        lib.saveSong(song2);
        for (Song song:admin.getTopSongs(new Date(), 1)) {
            if (song.equals(song2)) {
                flag = true;
            }
        }

        assertTrue(flag);
    }

    /**
     * Test of method getTopAlbums().
     * Here we will save in the library some albums with different value
     * of playbacks each one, and then get the top 1 (for example) to test
     * the functionality.
     */
    @Test
    public void getTopAlbums() {
        Album a1;
        Album a2;
        Boolean flag = false;

        song1.incrementPlaybacks();
        song2.incrementPlaybacks();
        song2.incrementPlaybacks();
        song1.setChecked(true);
        song2.setChecked(true);
        a1 = new Album("album1", song1, song2);
        a2 = new Album("album2", song1);

        lib.saveAlbum(a1);
        lib.saveAlbum(a2);

        for (Album alb:admin.getTopAlbums(new Date(), 1)) {
            System.out.println(album);
            if (alb.equals(a1)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Test of method getTopAuthors().
     * Here we will increment the playbacks of some songs created by different authors,
     * and then get the top 1 (for example) to test the functionality.
     */
    @Test
    public void getTopAuthors() {
        Boolean flag = true;
        int i;
        for(i = 0; i< 100; i++) {
            song2.incrementPlaybacks();
        }

        for (User user:admin.getTopAuthors(new Date(), 1)) {
            if (!user.equals(u2)) {
                flag = false;
            }
        }

        assertTrue(flag);
    }

    /**
     * Method to test addReported().
     * We simulate that a user make a report, and then check if the report
     * was done properly.
     */
    @Test
    public void addReported() {
        Boolean flag = false;
        //Adding a new report, it was created in the before method
        admin.addReported(report);
        for (Report item: admin.getReported()) {
            if (item.equals(report)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Test of addUploaded()
     * Here we upload a song created in the before method, and then check
     * if the song is in the list of uploaded.
     */
    @Test
    public void addUploaded() {
        Boolean flag = false;
        //Adding a new song uploaded
        admin.addUploaded(song2);
        for (Song item:admin.getUploaded()) {
            if (item.equals(song2)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Method to check solveReport()
     * Here we add a report and then, as administrator, we solve it in different ways
     * to check if it works properly.
     */
    @Test
    public void solveReport() {
        //First of all, we add the report
        admin.addReported(this.report);
        //Then we solve it but the song was not appropriate
        assertTrue(admin.solveReport(this.report, false, false));
        for (Report report : admin.getReported()) {
            assertTrue(!(report.equals(this.report)));
        }
        //We are going to add it again to check if it works too when a report is solved and the song is appropriate
        admin.addReported(this.report);
        assertTrue(admin.solveReport(this.report, true, false));
        for (Report report : admin.getReported()) {
            assertTrue(!(report.equals(this.report)));
        }

        //In both cases the report is eliminated to the list of reports.
    }

    /**
     * Method to test solveUpload()
     * Following the steps in the previous test, here we add an upload and then,
     * as administrator, we solve it.
     */
    @Test
    public void solveUpload() {
        //First we add the song to uploaded
        admin.addUploaded(song2);
        //Now we solve it
        assertTrue(admin.solveUpload(song2, true, false));
        for (Song song: admin.getUploaded()) {
            assertTrue(!(song.equals(song2)));
        }

    }

}
