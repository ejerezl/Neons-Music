package Test;

import App.App;
import Exceptions.WrongUserException;
import Music.*;
import Users.Administrator;
import Users.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pads.musicPlayer.exceptions.Mp3InvalidFileException;

import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.musicPath;
import static org.junit.Assert.*;

/**
 * Class to test the functionality of Library class.
 * Each method is implemented to test one method of the Library
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class LibraryTest {
    Library lib;
    User u1;
    User u2;
    Song s1;
    Song s2;
    Song s3;
    Album album;
    Playlist playlist;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        App.setAdministrator(new Administrator("admin", "admin@gmail.com", "123abc.."));
        lib = Library.getInstance();
        u2 = new User("Esthe Jerez", "ejerezl", "esther@gmail.com", new Date(99,3,7));
        u1 = new User("Eva Lacaba", "evaln", "eva@gmail.com", new Date(99,3,7));
        s1 = new Song("hive.mp3", musicPath +"hive.mp3", u1);
        s2 = new Song("chicle3.mp3", musicPath +"chicle3.mp3", u2);
        s3 = new Song("np.mp3", musicPath +"np.mp3", u1);
        playlist = new Playlist("AnotherPlaylist", u1, s3);
        album = new Album("Album", s2);

    }

    /**
     * Done after every test
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
     * Tester of uploadSong()
     * This method upload a song and check if it's in the list of uploaded.
     */
    @Test
    public void uploadSong() {
        ArrayList<Song> uploaded;

        lib.uploadSong(s1);
        uploaded = App.getAdministrator().getUploaded();
        assertTrue(uploaded.get(0).equals(s1));
    }

    /**
     * Tester of deleteSong()
     * This method save a song in the Library and then delete it. To check that
     * it works correctly, it searches in the library the deleted song.
     */
    @Test
    public void deleteSong() {
        //First we add some songs
        lib.saveSong(s1);
        lib.saveSong(s2);
        //Now we delete one
        lib.deleteSong(s1);
        //Check if the resulting one is the one that we have not deleted
        assertTrue(lib.getSongs().get(0).equals(s2));
    }

    /**
     * Tester of saveSong()
     * This method check if save song works by searching in the library the song saved.
     */
    @Test
    public void saveSong() {
        Boolean flag = false;

        lib.saveSong(s3);
        for(Song song:lib.getSongs()){
            if(song.equals(s3))
                flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester createAlbum()
     * Here we create an album and check if the initialization has been as expected.
     */
    @Test
    public void createAlbum() throws Exception {
        Boolean flag = false;
        try {
            lib.createAlbum("Album", s2);
        } catch (WrongUserException exc) {
            exc.getStackTrace();
        } catch (Mp3InvalidFileException exc) {
            exc.getStackTrace();
        }
        for (Aggrupation agr:s2.getContained()) {
            if(agr.equals(album));
                flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of saveAlbum()
     * once an album is created, we save it in the library and then search for it in the library.
     */
    @Test
    public void saveAlbum() {
        Boolean flag = false;

        lib.saveAlbum(album);

        for (Album alb:lib.getAlbums()) {
            if (alb.equals(album)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of createPlaylist()
     * Here we create a playlist and then search for it to check if it works.
     */
    @Test
    public void createPlaylist() {
        Playlist play;
        Boolean flag = false;

        play = lib.createPlaylist("MiPlaylist", u1, s1, s2);

        for (Playlist playlist:u1.getPlaylists()) {
            if (playlist.equals(play)) {
                flag = true;
            }
        }

        assertTrue(flag);

    }

    /**
     * Tester  addSongsToPlaylist()
     * Starting from an existing playlist, we add new songs and then check if they are
     * in the playlist.
     */
    @Test
    public void addSongsToPlaylist() {
        Boolean flag = false;

        lib.createPlaylist("MiPlaylist", u1, s1, s2);
        lib.addSongsToPlaylist(lib.getPlaylists().get(0), s3);
        for (Song song:lib.getPlaylists().get(0).getSongs()) {
            if (song.equals(s3)) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester of removeSongFromPlaylist()
     * Starting from an existing playlist, we delete one song of the playlist and then search
     * for it in the playlist (it must not be there)
     */
    @Test
    public void removeSongFromPlaylist() {
        Boolean flag = true;

        lib.createPlaylist("MiPlaylist", u1, s1, s2);
        lib.addSongsToPlaylist(lib.getPlaylists().get(0), s3);
        lib.removeSongFromPlaylist(s3, lib.getPlaylists().get(0));
        for (Song song:lib.getPlaylists().get(0).getSongs()) {
            if (song.equals(s3)) {
                flag = false;
            }
        }
        assertTrue(flag);

    }

    /**
     * Tester of removeAlbum()
     * First we save an album in the library, and then we delete it. To see if it works, we search
     * for it in the library.
     */
    @Test
    public void removeAlbum() {
        Boolean flag = true;

        lib.saveAlbum(album);
        lib.removeAlbum(album);

        for (Album alb:lib.getAlbums()) {
            if(alb.equals(album))
                flag = false;
        }

        assertTrue(flag);
    }

    /**
     * Tester of removePlaylist()
     * First we create a playlist in the library, and then we delete it. To see if it works, we search
     * for it in the library.
     */
    @Test
    public void removePlaylist() {
        Boolean flag = true;
        Playlist play;

        lib.createPlaylist("MiPlaylist", u1, s1, s2);
        play = lib.getPlaylists().get(0);
        lib.removePlaylist(play);

        for (Playlist playlist:lib.getPlaylists()) {
            if (playlist.equals(play)) {
                flag = false;
            }
        }

        assertTrue(flag);

    }
}