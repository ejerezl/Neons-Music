package Users;

import App.App;
import Music.Playlist;
import Music.Report;
import Music.Song;
import Users.Notifications.FollowNotification;
import Users.Notifications.MailNotifier;
import Users.Notifications.Notification;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.imagePath;

/**
 * This class implements the behaviour of an object of type User.
 * Here there're implemented the method needed to carry out actions as
 * follow and unfollow another user, report a song,..
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class User implements Serializable{
    private String name;
    private ArrayList<Song> songs;
    private String artisticname;
    private String mail;
    private Date birthday;
    private String profilepath;
    private ArrayList<Playlist> playlists;
    private int id;
    private Boolean banned;
    private Date bannedDate;
    private Date registrationdate;
    private int reportscount = 0;
    private ArrayList<Notification> notifications;
    private ArrayList<User> following;
    private ArrayList<User> followers;
    private State state;
    private TwitterClient twitterClient;
    private transient MailNotifier mailNotifier;
    private boolean adult = false;

    public void removeSongFromPlaylist(Song song, Playlist playlist) {
        for(Playlist play:playlists) {
            if(playlist.equals(play)) {
                play.removeSong(song);
            }
        }
    }
    /**
     * Constructor of the object User, that with the given parameter instantiate it.
     * @param name
     * @param artisticName
     * @param mail
     * @param birthday
     */
    public User(String name, String artisticName, String mail, Date birthday) {
        AdminUser admin = AdminUser.getInstance();
        this.following = new ArrayList<User>();
        this.followers = new ArrayList<User>();
        this.playlists = new ArrayList<Playlist>();
        this.songs = new ArrayList<>();
        this.notifications = new ArrayList<Notification>();
        this.name = name;
        this.artisticname = artisticName;
        this.mail = mail;
        this.birthday = birthday;
        this.profilepath = "img/user_opt.png";
        this.id = admin.updateCounter(); // get and increase id
        this.banned = false;
        this.state = new Free();
        this.registrationdate = new Date(); //Takes today's day if no argument specified
        this.twitterClient = new TwitterClient();
        this.mailNotifier = new MailNotifier(mail);
    }

    /**
     * Method that add the song received as parameter to the list of songs uploaded by the user.
     * @param song
     */
    public void addSong(Song song) {
        songs.add(song);
    }

    /**
     * Method that deletes the song received as parameter from the list of songs uploaded by the user.
     * @param song
     */
    public void deleteSong(Song song) { songs.remove(song); }

    /**
     * Method to obtain the list of songs uploaded by the user
     * @return list of songs uploaded by the user.
     */
    public ArrayList<Song> getSongs() { return songs; }

    /**
     * Method that returns the number of playbacks that a user has received from the date
     * given as a parameter till the actual day.
     * @param date
     * @return the number of playbacks
     */
    public int getPlaybacks(Date date) {
        int plays = 0;

        for(Song song:songs) {
            plays += song.getPlaybacks().getPlaybacks(date);
        }
        return 0;
    }

    /**
     * Method that returns a list of playlist created by the user
     * @return playlist of the user
     */
    public ArrayList<Playlist> getPlaylists() { return (ArrayList<Playlist>) playlists.clone(); }


    /**
     * Method that add a playlist recieved by argument to the list of the user's playlists.
     * @param playlist
     */
    public void addPlaylist(Playlist playlist){ playlists.add(playlist); }

    /**
     * Method that deletes the playlist received as parameter from the list of playlists of the user.
     * @param playlist
     */
    public void deletePlaylist(Playlist playlist){ playlists.remove(playlist); }


    /**
     * Method that returns a list of Users that the actual user is following
     * @return people whom the user follows
     */
    public ArrayList<User> getFollowing() { return (ArrayList<User>)this.following.clone(); }

    /**
     * This method returns the Mail Notifier object used to send emails to this user
     * @return the MailNotifier instance
     */
    public MailNotifier getMailNotifier(){ return mailNotifier;}
    /**
     * Method that returns a list of Users that follow the actual user
     * @return the followers of the user
     */
    public ArrayList<User> getFollowers() { return (ArrayList<User>)this.followers.clone(); }

    /**
     * Getter of the mail of the user
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    public String getName() { return name; }
    /**
     * Getter of the artistic name of the user
     * @return the artistic name
     */
    public String getArtisticname() {
        return artisticname;
    }

    /**
     * Getter of the id number of the user
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of the state of  the user (Premium or free)
     * @return the state
     */
    public State getState(){
        return this.state;
    }

    /**
     * Setterof the state of the user
     * @param state
     */
    public void setState(State state){
        this.state = state;
    }

    /**
     *
     */
    public void setIsAdult(Boolean adult){
        this.adult = adult;
    }

    /**
     * Getter of the Birthday date of the user
     * @return birthday date
     */
    public Date getBirthday() { return this.birthday; }

    /**
     * Setter of the Birthday date of the user
     * @param birthday
     */
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    /**
     * This method returns if a user is adult or not
     * @return attribute adult (Boolean)
     */
    public Boolean getIsAdult() { return this.adult; };

    /**
     * Setter of the artistic name of a user.
     * @param artisticname
     */
    public void setArtisticname(String artisticname) {
        this.artisticname = artisticname;
    }

    /**
     * Method to report a song received as a parameter.
     * @param song
     * @return the report made.
     */
    public Report report(Song song){
        return App.getAdministrator().addReported(new Report(new Date(), this, song));
    }


    /**
     * Method to follow the user received as a parameter.
     * @param user
     */
    public void follow(User user){
        if(user.equals(this) || user.followers.contains(this)){
            return;
        }
        user.followers.add(this);
        user.addNotification(new FollowNotification(this));
        this.following.add(user);
    }

    /**
     * Method to unfollow received as  a parammeter.
     * @param user
     */
    public void unfollow(User user){
        this.following.remove(user);
    }

    /**
     * Method to add a notification received as a parameter to the current user.
     * @param not
     */
    public void addNotification(Notification not){ notifications.add(not); }


    /**
     * Method to delete a notification received as a parameter from the actual notifications.
     * @param not
     */
    public void deleteNotification(Notification not){ notifications.remove(not); }


    /**
     * Getter of the notifications of a user.
     * @return a list of the notifications
     */
    public ArrayList<Notification> getNotifications() { return notifications; }

    /**
     * Getter of the profile picture of a user
     * @return the profile picture
     */
    public String getProfilePath() { return this.profilepath; }

    /**
     * Setter of the profile picture of a user
     * @param path
     * @throws IOException
     */
    public void setProfilePicture(String path) throws IOException {
        Files.copy(Paths.get(path), Paths.get("users/profilepicture/"+id), StandardCopyOption.REPLACE_EXISTING);
        this.profilepath = "users/profilepicture/" + id;
    }

    /**
     * Getter of the number of reports the user has made
     * @return reportscount
     */
    public int getReportCount(){
        return reportscount;
    }

    /**
     * Method to increase the number of reports the user has made
     * @return the number of reports updated
     */
    public int increaseReportscount(){
        return ++reportscount;
    }

    /**
     * Seetter of the banned attribute of the user
     * @param ban
     */
    public void setBanned(Boolean ban){
        banned = ban;
        if(ban){
            bannedDate = new Date();
        }
    }

    /**
     * Getter of the banned attribute of the user
     * @return if the user is banned or not
     */
    public boolean getBanned(){
        return banned;
    }


    /**
     * Getter of the date when the user was banned
     * @return the date
     */
    public Date getBannedDate(){ return bannedDate;}

    public TwitterClient getTwitterClient(){return twitterClient;}

}
