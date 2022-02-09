package Test;

import App.App;
import Exceptions.WrongUserException;
import Music.Album;
import Music.Playlist;
import Music.Song;
import Users.AdminUser;
import Users.Administrator;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pads.musicPlayer.exceptions.Mp3InvalidFileException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;

public class AppTest {
    App app;
    AdminUser users;
    User eva;
    ArrayList<Song> songs;
    Playlist playlist;
    Album album;
    User acdc;

    @Before
    public void setUp() throws Exception {
        app = new App();
        songs = new ArrayList<Song>();
        users = AdminUser.getInstance();
        App.setAdministrator(new Administrator("Administrator", "administrator@gmail.com", "password"));
        Administrator administrator = App.getAdministrator();
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("firstanswer");
        answers.add("secondanswer");
        eva = users.register("Eva", "evalanic", "evalanic@gmail.com", new Date(1999, 02,07), answers, "password");
        eva.getMailNotifier().setDefaultNotifying(false);
        acdc = users.register("AC_DC", "AC-DC",
                "evalanic@hotmail.com", new Date(1999, 02, 07), answers, "password");
        acdc.getMailNotifier().setDefaultNotifying(false);
        app.getAdminUser().login("evalanic@hotmail.com", "password");
        songs.add(new Song("Shoot to Thrill", musicPath+"1.mp3", acdc));
        songs.add(new Song("Rock and Roll dammnation", musicPath+"2.mp3", acdc));
        songs.add(new Song("Guns for Hire", musicPath+"3.mp3", acdc));
        try {
            album = app.getLib().createAlbum("Iron Man2", songs.toArray(new Song[songs.size()]));
        } catch (WrongUserException exc) {
            exc.getStackTrace();
        } catch (Mp3InvalidFileException exc) {
            exc.getStackTrace();
        }
        for(Song song: administrator.getUploaded()){
            administrator.solveUpload(song, true, true);
        }
        playlist = app.getLib().createPlaylist("Ac-dc", eva, songs.toArray(new Song[songs.size()]));
        app.saveData();
    }
    /**
     * This method check the data saved in SaveData and reads it from a file
     */
    @Test
    public void loadData(){
        Boolean flag = true;
        App read = App.loadData();
        for (Map.Entry<String, User> entry: read.getAdminUser().getUsers().entrySet()) {
            if (eva.equals(entry.getValue()) == false && acdc.equals(entry.getValue()) == false) {
                flag = false;
            }
        }
        assertTrue(flag);
        flag =true;
        for (Song song :read.getLib().getSongs()) {
            if (songs.contains(song) == false) {
                flag = false;
            }
        }
        assertTrue(flag);
        flag =true;
        for (Album readAlbum: read.getLib().getAlbums()) {
            if (readAlbum.equals(album) == false) {
                flag = false;
            }
        }
        assertTrue(flag);
        flag =true;
        for (Playlist readPlaylist: read.getLib().getPlaylists()) {
            if (playlist.equals(readPlaylist) == false) {
                flag = false;
            }
        }
        assertTrue(flag);
    }
}