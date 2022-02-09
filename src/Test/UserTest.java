package Test;

import App.App;
import Music.Playlist;
import Music.Report;
import Music.Song;
import Users.Administrator;
import Users.Notifications.Notification;
import Users.Notifications.SongNotificationDenied;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static Test.AdminUserTest.imagePath;
import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.assertTrue;


/**
 * Class to test the functionality of User class.
 * Each method is implemented to test one method of the User
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class UserTest {

    User u1 = null;
    User u2 = null;
    Song song = null;
    Playlist playlist = null;
    //private static final String musicPath = "C:\\Users\\alexa\\IdeaProjects\\padsof\\music\\";

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        App.setAdministrator(new Administrator("Administrator", "administrator@gmail.com", "password"));

        u1 = new User("Esther Jerez", "ejerez","esther@gmail.com", new Date(99,9,9));
        u2 = new User("Eva Lacaba", "evalacaba", "eva@gmail.com", new Date(99,1,7));
        song = new Song("hive.mp3", musicPath + "hive.mp3", u1);
        playlist = new Playlist("MyPlaylist",u1, song);
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Tester of addSong()
     * Here a user add a song to his/her list of songs. Then we check
     * that the addition was correctly done.
     */
    @Test
    public void addSong() {
        Boolean flag = false;

        u1.addSong(song);
        for (Song song:u1.getSongs()) {
            if (song.equals(song)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of deleteSong()
     * Here we add a song to the user references and then delete it. To test
     * if it works properly we search for the deleted song.
     */
    @Test
    public void deleteSong() {
        Boolean flag = true;

        u1.addSong(song);
        u1.deleteSong(song);

        for (Song song:u1.getSongs()) {
            if (song.equals(song)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of addPlaylist()
     * We add a playlist to the playlist references of a user, and then search for it to check
     * that the addition was correctly done.
     */
    @Test
    public void addPlaylist() {
        Boolean flag = false;

        u1.addPlaylist(playlist);
        for (Playlist play:u1.getPlaylists()) {
            if (play.equals(playlist)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of deletePlaylist()
     * Once a playlist is added to a user, then we delete it. To check if it works,
     * we search for the playlist in the references.
     */
    @Test
    public void deletePlaylist() {
        Boolean flag = true;

        u1.addPlaylist(playlist);
        u1.deletePlaylist(playlist);
        for (Playlist play:u1.getPlaylists()) {
            if (play.equals(playlist)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of report()
     * This method simulates a report by a user and then, to check the
     * functionality, we search in the list of reports for the report made.
     */
    @Test
    public void report() {
        Boolean flag = false;

        try {
            song = new Song("chicle3.mp3", musicPath+"chicle3.mp3", u2);
        } catch(FileNotFoundException exc) {
            assertTrue(false);
        }

        Report report = u1.report(song);

        for(Report item: App.getInstance().administrator.getReported()){
            if (item.equals(report))
                flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of follow()
     * This method simulates the follow to a user by another.
     * Then we check if the process was done correctly.
     */
    @Test
    public void follow() {
        Boolean flag = false;
        u1.follow(u2);

        for (User user:u1.getFollowing()) {
            if (user.equals(u2)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of unfollow()
     * Once a follow between two users is done, we make the unfollow and check
     * that now there's no relation between oth users.
     */
    @Test
    public void unfollow() {
        Boolean flag = true;
        u1.follow(u2);
        u1.unfollow(u2);

        for (User user:u1.getFollowing()) {
            if (user.equals(u2)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of addNotification()
     * Here we add a notification to a user, and then check in the list of notofications
     * of that user if our added notification is there.
     */
    @Test
    public void addNotification() {
        Boolean flag = false;

        SongNotificationDenied not = new SongNotificationDenied(song, "Your song has been eliminated");
        u1.addNotification(not);

        for (Notification nots:u1.getNotifications()) {
            if (nots.equals(not)) {
                flag = true;
            }
        }
        assertTrue(flag);

    }

    /**
     * Tester of deleteNotification(9
     * Once a notification is added to one user, we delete it. Now we check in the list of
     * notifications of that user that our deleted notification is not there.
     */
    @Test
    public void deleteNotification() {
        Boolean flag = true;

        SongNotificationDenied not = new SongNotificationDenied(song, "Your song has been eliminated");
        u1.addNotification(not);
        u1.deleteNotification(not);

        for (Notification nots:u1.getNotifications()) {
            if (nots.equals(not)) {
                flag = false;
            }
        }
        assertTrue(flag);

    }

    /**
     * Tester setProfilePicture()
     * With a user, we try to change his/her profile picture.
     * Once it is changed, we obtain the new profile picture and check if it is changed.
     *
     */
    @Test
    public void setProfilePicture() {
        try {
            u1.setProfilePicture("test");
        } catch(IOException exc){
            exc.getStackTrace();
        }
        File file = new File( imagePath + u1.getId());
        assertTrue(file.exists());
    }

    /**
     * Tester of increaseReportscount()
     * Here we increase the number of reports a user has made, and then check
     * if this number has changed as expected.
     */
    @Test
    public void increaseReportscount() {
        int initialReports = u1.getReportCount();

        u1.increaseReportscount();
        assertTrue(u1.getReportCount() == initialReports + 1);

    }
}