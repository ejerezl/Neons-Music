package Music;
import App.App;
import Exceptions.WrongUserException;
import Users.AdminUser;
import Users.Administrator;
import Users.Notifications.AlbumNotification;
import Users.Notifications.SongNotificationDenied;
import Users.User;
import pads.musicPlayer.exceptions.Mp3InvalidFileException;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static Test.AdminUserTest.musicPath;

/**
 * Class Library that implements the necessary methods to describe the structure of an object of type library.
 * Abstract class Aggrupation that group both Album and Playlist classes
 * and that defines the common methods and attributes between the two classes.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Library implements Serializable {
    private int maxid = 0;
    private static final String coverPath = musicPath + "covers/";
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Album> albums = new ArrayList<>();
    private Administrator admin;
    private static Library  instance = new Library();
    private static MediaPlayer mediaPlayer = MediaPlayer.getInstance();

    private Library() {
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    /**
     * Creates a new library object.
     * @return the object instantiated
     */
    public static Library getInstance() {
        instance.admin = App.getAdministrator();
        return instance;
    }
    /**
     * Sets the instance to be used by other classes
     */
    public static void setInstance(Library library) {
        instance = library;
    }

    /**
     * Instantiates a new song with the parameters given and uploads it. In case an error occur it throws an exception.
     * @param path
     *
    public void uploadSong(String path) throws InvalidDataException, UnsupportedTagException {
        try {
            Mp3File mp3File = new Mp3File(path);
            if (mp3File.hasId3v1Tag()) {
                ID3v1 id3v1 = mp3File.getId3v1Tag();
                String title = id3v1.getTitle();
                String album = id3v1.getAlbum();
                String author = id3v1.getArtist();
            } else if (mp3File.hasId3v2Tag()) {
                ID3v2 id3v2 = mp3File.getId3v2Tag();
                String title = id3v2.getTitle();
                String album = id3v2.getAlbum();
                String author = id3v2.getArtist();
            }
        } catch (IOException exc){

        }
    }*/

    /**
     * Instantiates a new song with the parameters given and uploads it. In case an error occur it throws an exception.
     * @param title
     * @param picturePath
     * @param songPath
     * @param users
     */
    public void uploadSong(String title,  String picturePath, String songPath, User... users) {
        try {
            Song song = new Song(title, songPath,picturePath,  users);
            uploadSong(song);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Instantiates a new song with the parameters given and uploads it. In case an error occur it throws an exception.
     * @param title
     * @param songPath
     * @param users
     */
    public void uploadSong(String title, String songPath, User... users) {
        try {
            Song song = new Song(title, songPath, users);
            uploadSong(song);

        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public ArrayList<Album> getAlbums(User user) {
        ArrayList<Album> result = new ArrayList<>();
        for (Album album:albums) {
            if (album.getAuthors().contains(user)) {
                result.add(album);
            }
        }
        return result;
    }

    /**
     * Method to upload a song. Once they're uploaded, they remain pending of approval
     * @param song
     */
    public void uploadSong(Song song) {

        //First of all we create the song
        try {
            if (song.hasCoverPath()) {
                Files.copy(Paths.get(song.getCoverPath()), Paths.get("music/covers/"+song.getId()), StandardCopyOption.REPLACE_EXISTING);
            }
            Files.copy(Paths.get(song.getPath()), Paths.get("music/"+song.getId()), StandardCopyOption.REPLACE_EXISTING);
            song.setPath("music/"+song.getId());
            System.out.println(song.getId());
            //Now we add it to the queue of songs uploaded but pending of approval
            admin.addUploaded(song);
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }

    /**
     * Method that returns the songs uploaded to the app.
     * @return array of songs
     */
    public ArrayList<Song> getSongs() {
        HashSet<Song> songs = new HashSet<Song>();
        for (Song song : this.songs) {
            if ((App.getLogged() == null || App.getLogged().getIsAdult() == false)&&song.isExplicit()) {
                continue;
            }
            songs.add(song);
        }
        return new ArrayList<>(songs);
    }

    /**
     * Method that returns the albums uploaded to the app.
     * @return array of albums
     */
    public ArrayList<Album> getAlbums() {
        HashSet<Album> albums = new HashSet<Album>();
        for (Album album : this.albums) {
            if ((App.getLogged() == null || App.getLogged().getIsAdult() == false)&& album.getIsExplicit()) {
                continue;
            }
            albums.add(album);
        }
        return new ArrayList<Album>(albums);
    }

    /**
     * Returns an id
     * @return an id
     */
    public int getId() { return maxid; }

    /**
     * Updates the id and returns it.
     * @return updated id
     */
    public int updateId() { return maxid++; }

    /**
     * Method that returns the playlists uploaded to the app.
     * @return array of playlists
     */
    public ArrayList<Playlist> getPlaylists() { return playlists; }

    /**
     * Method that deletes a song passed as an argument. It sends a notification of deletion to the authors of the song.
     * @param song
     */
    public void deleteSong(Song song) {
        for (User user:song.getAuthors()) { //Send a notification to all authors of the song
            user.addNotification(new SongNotificationDenied(song, "We have deleted your song."));
            user.deleteSong(song);
        }

        for (Aggrupation agr:song.getContained()) { //Now we remove from all albums and playlist the song
            song.deleteReference(agr);
            if(agr.isAlbum()) {
                agr.removeSong(song);
                if(agr.getSongs().size() == 0) {
                    removeAlbum((Album)agr);
                }
            } else {
                removeSongFromPlaylist(song, (Playlist)agr);
            }
        }
        songs.remove(song); //Now we delete it definitely from Library
    }

    /**
     * Method that adds a song to the array of songs uploaded in the library.
     * @param song
     */
    public void saveSong(Song song) {
        for (User user:song.getAuthors()) {
            user.addSong(song);
        }
        songs.add(song);

        for (Aggrupation agr:song.getContained()) {
            if (agr.isAlbum()) {
                saveAlbum((Album) agr);
            }
        } //We know that it will only be one album because the song can only be contained in an album.
    }

    /**
     * Method that creates an album, and lefts it pending of validation.
     * @param title
     * @param pathOfPicture
     * @param songs
     * @return album created
     */
    public Album createAlbum(String title, String pathOfPicture, Song ... songs) throws Mp3InvalidFileException, WrongUserException {
        Album album;
        Boolean flag = false;

        if (App.getLogged() == null) {//Users that are not registered (anonymous) cant upload content
            throw (new WrongUserException("User not registered"));
        }

        album = new Album(title, pathOfPicture);
        for (Song song:songs) {
            if (song.getLength() < 1800) {
                uploadSong(song);
                album.addSong(song);
                song.addReference(album);
                flag = true;
            }
        }
        if (flag) {
            return album;
        } else {
            throw(new Mp3InvalidFileException("Time of songs exceed the maximum"));
        }
    }

    public void clearsongs(){
        this.songs = new ArrayList<Song>();
    }

    /**
     * Method that creates an album, and lefts it pending of validation. (No coverpath)
     * @param title
     * @param songs
     * @return album created
     */
    public Album createAlbum(String title, Song ... songs) throws Mp3InvalidFileException, WrongUserException {
        Boolean flag = false;
        Album album;

        if (App.getLogged() == null) {//Users that are not registered (anonymous) cant upload content
            throw (new WrongUserException("User not registered"));
        }

        album = new Album(title);
        for (Song song:songs) {
            if (song.getLength() < 1800) { //Only upload songs that fit the maximum size
                uploadSong(song);
                album.addSong(song);
                flag = true;
            }
        }
        if (flag) {
            return album;
        } else {
            throw(new Mp3InvalidFileException("Time of songs exceed the maximum"));
        }
    }
    /*
    public Album createAlbum(String ... songs) throws Exception {
        HashSet<String> albumTitleSet = new HashSet<String>();
        ArrayList<Song> songArrayList = new ArrayList<Song>();
        String songTitle;
        String authorName;
        ArrayList<User> author;
        ID3v1 id3v1;
        ID3v2 id3v2 = null;
        for(String song : songs){
            Mp3File mp3File = new Mp3File(song);
            if (mp3File.hasId3v2Tag()) {
                id3v2 = new ID3v23Tag();
            } else if (mp3File.hasId3v1Tag()) {
                id3v1 = new ID3v1Tag();
            } else {
                throw new Exception();
            }
            authorName = id3v2.getArtist();
            songTitle = id3v2.getTitle();
            albumTitleSet.add(id3v2.getAlbum());
            author = Search.searchAuthor(AdminUser.getInstance(), authorName);
            songArrayList.add(new Song(songTitle, song, author.toArray(new User[author.size()])));
        }
        if (albumTitleSet.size() != 1) {
            throw new Exception();
        }
        String albumTitle = albumTitleSet.toArray(new String[1])[0];
        Album album = new Album(albumTitle, songArrayList.toArray(new Song[songArrayList.size()]));
        for (Song song : songArrayList) {
            uploadSong(song);
        }
        return album;
    }*/

    /**
     * Uploads an album to the library when it is validated (checked and accepted) and sends a notification
     * to the author's followers informing them of the new release.
     * @param album
     */
    public void saveAlbum(Album album) {
        Boolean flag = true;
        HashSet<User> users = new HashSet<User>();
        /*When uploading an album, to add it to the library we have to wait for the checking of each song
         so we see if every song is already checked before saving the album in the library*/
        for (Song song:album.getRealSongs()) {
            if(!song.getChecked())
                flag = false;
        }

        if (flag) {
            String emailBody = "Your album " + album.getTitle() + "has just been validated. <br> <br> It is now available to other users. Album has been marked as ";
            emailBody += album.getIsExplicit() ? "explicit." : "not explicit.";
            emailBody += "<br><br>Regards,<br><br>The Neons Team";
            albums.add(album);
            for (User author: album.getAuthors()) {
                try {
                    author.getMailNotifier().sendEmailNotification(emailBody);
                } catch (Exception exc){
                }
                author.getTwitterClient().newAlbumTweet(album);
                for (User follower: author.getFollowers()) {
                    users.add(follower);
                }
            }
            users.forEach(follower -> follower.addNotification(new AlbumNotification(album)));
        }
    }

    /**
     * Method that creates a playlist.
     * @param title
     * @param userCreator
     * @param songs
     */
    public Playlist createPlaylist(String title, User userCreator, Song ... songs) {
        Playlist playlist = new Playlist(title, userCreator, songs);
        playlists.add(playlist);
        userCreator.addPlaylist(playlist);
        return playlist;
    }

    public Playlist createPlaylist(String title, User userCreator, Aggrupation agr) {
        Playlist playlist = new Playlist(title, userCreator, agr);
        playlists.add(playlist);
        userCreator.addPlaylist(playlist);
        return playlist;
    }
    /**
     * Method that adds songs to a created playlist.
     * @param playlist
     * @param song
     */
    public void addSongsToPlaylist(Playlist playlist, Song song) {
        playlist.addSong(song);
    }

    /**
     * Method that removes songs from a created playlist.
     * @param song
     * @param playlist
     */
    public void removeSongFromPlaylist (Song song, Playlist playlist) {
        Playlist ply;

        for(int i = 0; i < playlists.size(); i++) {
            ply = playlists.get(i);
            if(ply.equals(playlist)) {
                ply.getUser().removeSongFromPlaylist(song, playlist);
                ply.removeSong(song);
                if(ply.songs.size() == 0) {
                    ply.getUser().deletePlaylist(playlist);
                    removePlaylist(ply);
                }
            }
        }
    }

    /**
     * This methods removes an agruppation from a playlist
     * @param agr
     * @param playlist
     */
    public void removeAggrupationFromPlaylist(Aggrupation agr, Playlist playlist) {
        for (Playlist ply:playlists) {
            if (playlist.equals(ply)) {
                ply.removeAgrupation(agr);
            }
        }
    }

    /**
     * Method that removes a playlist once it is empty.
     * @param playlist
     */
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        playlist.deletePlaylist();
    }


    /**
     * Method that removes an entire album.
     * @param album
     */
    public void removeAlbum(Album album) {
            albums.remove(album);
    }

    /**
     * Removes all albums overwriting the album array with a new one.
     */
    public void removeAllAlbums() {
        for(Album album:albums) {
            removeAlbum(album);
        }
    }

    /**
     * Removes all songs overwriting the album array with a new one.
     */
    public void removeAllSongs() {
        for (Song song:songs) {
            deleteSong(song);
        }
    }

    /**
     * Removes all playlists overwriting the album array with a new one.
     */
    public void removeAllPlaylist() {
        for (Playlist playlist:playlists) {
            removePlaylist(playlist);
        }

    }
}
