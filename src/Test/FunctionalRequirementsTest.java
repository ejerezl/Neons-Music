package Test;

import App.App;
import Exceptions.LoginException;
import Music.*;
import Users.AdminUser;
import Users.Administrator;
import Users.Notifications.FollowNotification;
import Users.Notifications.Notification;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.imagePath;
import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is a Tester for all functional requirements that our
 * app must fulfill.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class FunctionalRequirementsTest {
    public static Administrator administrator;
    private static User u1, u2, u3, u4 = null;
    private static Song s1, s2, s3, s4, s5, s6 = null;
    private static Album a1, a2, a3 = null;
    private static Library lib;
    private static SearchOptions sop;
    private static Search s;
    private static AdminUser admin = AdminUser.getInstance();
    private static MediaPlayer media;
    private static ArrayList<String> answers;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        App.setAdministrator(new Administrator("admin", "admin@gmail.com", "123abc.."));
        administrator = App.getAdministrator();
        answers = new ArrayList<String>();
        answers.add("first");
        answers.add("second");
        lib = Library.getInstance();

        User eva = AdminUser.getInstance().register("Eva Lacaba", "evalanic", "eva@gmail.com", new Date(2015, 02, 07), answers,"password");
        AdminUser.getInstance().login("eva@gmail.com", "password");
        User dani = AdminUser.getInstance().register("Dani Martin", "El canto del loco", "elcantodelloco@yahoo.es", new Date(77,2,19), answers, "psswrd");
        User riri = AdminUser.getInstance().register("Robyn Rihanna", "Rihanna", "rihanna@gmail.com", new Date(88,2,20), answers, "passw");
        User esther = AdminUser.getInstance().register("Esther Jerez", "Locurote", "locurotemaximo@gmail.com", new Date(99,9,9), answers, "pasitoapasito");
        u1 = dani;
        u2 = esther;
        u3 = riri;
        u4 = eva;
        s1 = new Song("Diamonds", musicPath + "diamonds.mp3", u3);
        s1.setIsexplicit(true);
        s2 = new Song("Don't dial me", musicPath + "chicle3.mp3", u2,u4);
        s3 = new Song("Work", musicPath + "work.mp3", u3);
        s4 = new Song("Umbrella", musicPath + "umbrella.mp3", imagePath + "front.jpg", u3);
        s5 = new Song("Locura maxima", musicPath + "hive.mp3", musicPath + "front.jpg", u2, u4);
        s6 = new Song("Eres tonto", musicPath + "erestonto.mp3", imagePath + "front.jpg", u1);
        a1 = new Album("Best of Rihanna", s1,s3);
        a2 = new Album("I'm the best", s2,s5);
        a3 = new Album("Eres tonto", s6);

        lib.saveAlbum(a1);
        lib.saveAlbum(a2);
        lib.saveSong(s4);
        lib.saveAlbum(a3);

        media = MediaPlayer.getInstance();
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        App.getInstance().users.logout(App.getLogged());
    }

    /**
     * Tester of requirement number 1.1
     * "User can search for songs with different criteria"
     * To check it, we search by different criteria and see if the results are the expected ones.
     * @throws Exception
     */
    @Test
    public void functionalRequirement11() throws Exception{
        App.getInstance().users.logout(App.getLogged());
        User user = App.getInstance().users.login("elcantodelloco@yahoo.es", "psswrd");
        assertTrue(searchByAlbum());
        assertTrue(searchByAuthor());
        assertTrue(searchBySong());
    }

    /**
     * Tester of searchByAlbum
     * @return if the test passed
     */
    private Boolean searchByAlbum() {
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

        return flag;
    }

    /**
     * Tester of searchBySong
     * @return if the test passed
     */
    private Boolean searchBySong() {
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

        return flag;
    }

    /**
     * Tester of searchByAuthor
     * @return if the test passed
     */
    private Boolean searchByAuthor() {
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
        return flag;
    }

    /**
     * Tester of requirement number 2.2
     * "A registered user can follow other users who gets notified when doing it"
     * So we simulate the follow between two users and check the notifications.
     */
    @Test
    public void functionalRequirement22() {
        Boolean flag = false;

        assertTrue(follow(u1, u2));
        for (Notification not:u2.getNotifications()) {
            if (not instanceof FollowNotification) {
                flag = true;
            }
        }
        assertTrue(flag);

    }

    /**
     * Test of user1 follows user2
     * @param user1
     * @param user2
     * @return if the test passed
     */
    private Boolean follow(User user1, User user2) {
        Boolean flag = false;
        user1.follow(user2);

        for (User user:user1.getFollowing()) {
            if (user.equals(user2)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Tester of requirement number 2.3
     * "A registered user has the possibility of adding songs to a queue"
     * So we use the mediaPlayer to add a song to the queue and check if the song
     * is in the queue.
     */
    @Test
    public void functionalRequirement23() {

        Boolean flag = false;

        media.addToQueue(s1);
        for(Song song:media.getQueue()) {
            if (song.equals(s1)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of requirement number 2.4
     * "A registered user has the possibility of creating playlist"
     * So we create a playlist and then check if the creator has the playlist.
     */
    @Test
    public void functionalRequirement24() {
        Playlist p;
        Boolean flag = false;
        p = lib.createPlaylist("miplaylist",  u1, s1, s2);

        for (Playlist playlist:u1.getPlaylists()) {
            if(playlist.equals(p)) {
                flag = true;
            }
        }
        assertTrue(flag);

    }

    /**
     * Tester of requirement number 2.5
     * "A registered user has the possibility of deleting a playist"
     * First we create a playlist, and now deleting song by song, the playlist is
     * deleted when the playlist is empty.
     */
    @Test
    public void functionalRequirement25() {
        Playlist p;
        Boolean flag = true;

        p = lib.createPlaylist("Playlist", u1, s1, s2);
        int size = p.getSongs().size();
        for( int i = 0; i < size; i++) {
            lib.removeSongFromPlaylist(p.getSongs().get(0), p);
        }

        for(Playlist playlist:u1.getPlaylists()) {
            if (playlist.equals(p)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of requirement number 2.6
     * "The registered user is able to listen to music catalogued as explicit content as long as
     *  he/she is over the legal age. Otherwise this content is hidden"
     *
     *  So we make a search using a user that is under the legal age and a user that is over the legal age.
     *  The results must be different because there are explicit songs.
     */
    @Test
    public void functionalRequirement26() {

        ArrayList<Song> results;
        Boolean flag = true;
        User user;
        App.getInstance().users.logout(App.getLogged());
        //Now, if u4, that  is under age, try to find that song it will not appear
        try {
            user = AdminUser.getInstance().login("eva@gmail.com", "password");
        } catch (LoginException exc) {
            assertTrue(false);
            return;
        }
        results = s.searchSong(lib, "dia");
        for(Song song:results) {
            if (song.equals(s1)) {
                flag = false;
            }
        }
        assertTrue(flag);

        //Nevertheless, if u1, that is over age, search it, it must appear
        AdminUser.getInstance().logout(user);
        try {
            user = AdminUser.getInstance().login("elcantodelloco@yahoo.es", "psswrd");
        } catch (LoginException exc) {
            assertTrue(false);
            return;
        }
        flag = false;
        results = s.searchSong(lib, "dia");
        for(Song song:results) {
            if (song.equals(s1)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of requirement number 2.7
     * "A registered free user has a limited number of playbacks."
     * So we increment the playbacks over the limit and then check that the free user
     * can not play anymore until the playbacks reinitialize.
     */
    @Test
    public void functionalRequirement27() {
        //Limited number of playbacks
        for (int i = 0; i<101; i++) { //Our maximum is 100
            u1.getState().increasePlaybacks();
        }
        assertFalse(u1.getState().play());
    }

    /**
     * Tester of requirement number 2.8
     * "A registered user can report songs"
     * So we make a report from a free user and check if the report was correctly made.
     */
    @Test
    public void functionalRequirement28() {
        Boolean flag = false;
        Report report = u1.report(s3);

        for(Report item: App.getInstance().administrator.getReported()){
            if (item.equals(report))
                flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of requirement number 2.9
     * "A registered user can upload single songs or albums"
     *
     * So what we do is add songs and a new album with a free user as author and then
     * check if it appears in the library.
     */
    @Test
    public void functionalRequirement29() {
        Boolean flag = false;
        //We are going to save in library directly a song and album to avoid the
        //Validation process.
        lib.saveSong(s4); //Song uploaded by author/s

        for (Song song:u3.getSongs()) {
            if (song.equals(s4)) {
                flag = true;
            }
        }
        assertTrue(flag);
        lib.saveAlbum(a2);
        flag = false;
        for(Album alb:lib.getAlbums()) {
            if (alb.equals(a2)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of requirement number 2.10
     * "A registered free user has the possibility of uploading a new profile picture"
     *
     * So we take a free user and change the profile picture, checking after if the changes where successful.
     */
    @Test
    public void functionalRequirement210(){
        try {
            u1.setProfilePicture(musicPath + "cd.jpg");
        } catch(IOException exc){
            exc.getStackTrace();
        }
        File file = new File(imagePath+u1.getId());
        assertTrue(file.exists());
    }

    /**
     * Tester of requirement number 2.11
     * "A registered free user has the possibility of change his/her birthday date"
     *
     * So we take a free user and change the birthday date, checking after if the changes where successful.
     */
    @Test
    public void functionalRequirement211(){
        User u = null;
        App.getInstance().users.logout(App.getLogged());
        u1.setBirthday(new Date());
        assertTrue(u1.getBirthday().equals(new Date()));

        u2.setArtisticname("Estherer");
        assertTrue(u2.getArtisticname().equals("Estherer"));

        AdminUser.getInstance().restorePswd("locurotemaximo@gmail.com", "newone", answers);
        try {
            u = AdminUser.getInstance().login("locurotemaximo@gmail.com", "newone");
        } catch(LoginException exc) {
            assertTrue(false);
        }
    }

    /**
     * Tester of requirement number 3.2
     * "A premium user will have unlimited playbacks"
     * So what we do is increment the number of playbacks over the limit and check
     * that function play() will always returns true.
     */
    @Test
    public void functionalRequirement32() {
        AdminUser.getInstance().upgrade(u3, "1234567891234567");
        for (int i = 0; i<101; i++) { //Our maximum is 100
            u3.getState().increasePlaybacks();
        }
        assertTrue(u3.getState().play());
    }

    /**
     * Tester of requirement number 4.1
     * "The administrator will be the one in charge of reviewing the songs uploaded by authors.
     * He can validate or delete this uploading"
     *
     * So what we do is add new uploads and as administrator, solve the uploads in different ways.
     */
    @Test
    public void functionalRequirement41() {
        Boolean flag = true;

        //First we remove all library
        lib.removeAllAlbums();
        lib.removeAllPlaylist();
        lib.removeAllSongs();
        App.getAdministrator().removoeAllUploaded();

        lib.uploadSong(s5);
        lib.uploadSong(s6);
        for (Song song:lib.getSongs()) {
            if (song.equals(s5) || song.equals(s6)) {
                flag = false;
            }
        }
        assertTrue(flag);
        App.getAdministrator().solveUpload(s5, true, true); //First true indicates that the upload is solved
                                                                       // Second one that the song is explicit
        App.getAdministrator().solveUpload(s6, true, true);
        /*flag = false;
        for (Song song:lib.getSongs()) {
            if (song.equals(s5) || song.equals(s6)) {
                flag = true;
            }
        }
        assertTrue(flag);
        assertTrue(s5.isExplicit());
*/
        App.getAdministrator().solveUpload(s6, false, false); //First true indicates that the upload is solved,
        //but the song doens't remain. Second one that the song is not explicit
        flag = true;
        for (Song song:lib.getSongs()) {
            if (song.equals(s6)) {
                flag = false;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of getTopSongs()
     * @return if the method works properly
     */
    private Boolean checkTopSongs() {
        Boolean flag = false;
        s1.incrementPlaybacks();
        s2.incrementPlaybacks();
        s2.incrementPlaybacks();
        lib.saveSong(s1);
        lib.saveSong(s2);
        for (Song song:administrator.getTopSongs(new Date(), 1)) {
            if (song.equals(s2)) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * Tester of getTopAlbums()
     * @return if the method works properly
     */
    private Boolean checkTopAlbums() {
        Album a1;
        Album a2;
        Boolean flag = false;

        s3.incrementPlaybacks();
        s6.incrementPlaybacks();
        s6.incrementPlaybacks();
        s6.setChecked(true);
        s6.setChecked(true);


        for (Album alb:administrator.getTopAlbums(new Date(), 1)) {
            if (alb.equals(a3)) {
                flag = true;
            }
        }
        return flag;
    }
    /**
     * Tester of getTopAuthors()
     * @return if the method works properly
     */
    private Boolean checkTopAuthors() {
        Boolean flag = true;

        s6.incrementPlaybacks();
        s6.incrementPlaybacks();
        s6.incrementPlaybacks();
        s3.incrementPlaybacks();

        for (User user:administrator.getTopAuthors(new Date(), 1)) {
            if (!user.equals(u1)) {
                flag = false;
            }
        }

        return flag;
    }

    /**
     * Tester of requirement number 4.2
     * "The administrator is able to to check statics"
     * So we check that he/she can access to that statics with different methods.
     */
    @Test
    public void functionalRequirement42() {
        assertTrue(checkTopAlbums());
        assertTrue(checkTopAuthors());
        assertTrue(checkTopSongs());
    }

    /**
     * Tester of requirement number 4.3
     * "Adminstrator can mark a song as explicit"
     *
     * So we mark a song as explicit and then check the changes.
     */
    @Test
    public void functionalRequirement43() {
        s1.setIsexplicit(true);
        assertTrue(s1.isExplicit());
        s1.setIsexplicit(false);

        a1.setIsExplicit(true);
        assertTrue(a1.getIsExplicit());
        a1.setIsExplicit(false);
    }

    /**
     * Tester of requirement number 4.5
     * "The administrator can ban a user in case this user made more than 3 reports
     * that weren't right"
     * So what we do is to simulate that a user makes 4 reports, then the administrator solves it
     * remaining the song in the library, and then check if the user is banned.
     *//*
    @Test
    public void functionalRequirement45() {
        Report r1;
        Report r2;
        Report r3;
        Report r4;

        r1 = u1.report(s1);
        r2 = u1.report(s2);
        r3 = u1.report(s3);
        r4 = u1.report(s4);

        administrator.solveReport(r1, true, false);
        administrator.solveReport(r2, true, false);
        administrator.solveReport(r3, true, false);
        administrator.solveReport(r4, true, false);

        assertTrue(u1.getBanned()); //The user is banned by the Administrator (internally, in the Administrator class)
    }
*/
}
