package Users;

import App.App;
import Music.*;
import Users.Notifications.SongNotificationDenied;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class implements the Administrator of the app, who is in charge
 * of controlling the reports  of songs, the process of uploading a new song
 * and validating it...
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Administrator implements Serializable {
    private static final int MAX_REPORT_COUNT = 3;
    private static int MINOR_AGE = 18;
    private String name;
    private String mail;
    private int id;
    private String password;
    private static int MAX_PLAYBAKS = 100;
    private ArrayList<Report> reported;
    private ArrayList<Song> uploaded;

    /**
     * Constructor of the Administrator that initializes it.
     * @param name
     * @param mail
     * @param password
     */
    public Administrator(String name, String mail, String password) {
        AdminUser admin = AdminUser.getInstance();

        this.name = name;
        this.mail = mail;
        this.id = admin.updateCounter(); // set and update id
        this.password = password;
        this.reported = new ArrayList<Report>();
        this.uploaded = new ArrayList<Song>();
    }

    private void setMinorAge(int minorAge) {
        Administrator.MINOR_AGE = minorAge;
    }

    public static int getMinorAge() {return MINOR_AGE;}

    /**
     * Getter of the songs that currently reported but its report is not solved.
     * @return the list of reported songs.
     */
    public ArrayList<Report> getReported() {
        return (ArrayList<Report>)reported.clone();
    }

    /**
     * Getter of the songs that have been recently uploaded but pending of validation.
     * @return the list of uploaded songs.
     */
    public ArrayList<Song> getUploaded() {
        return (ArrayList<Song>)uploaded.clone();
    }

    /**
     * Method to add a new report, given as a parameter.
     * @param report
     * @return the report added.
     */
    public Report addReported(Report report) {
        report.getSong().setChecked(false);
        reported.add(report);
        return report;
    }

    /**
     * Getter of the maximum number of playbacks a standard user can play.
     * @return
     */
    public static int getMAX_PLAYBACKS(){
        return MAX_PLAYBAKS;
    }

    public static void setMAX_PLAYBACKS(int newOne) { MAX_PLAYBAKS = newOne; }
    /**
     * Method to add a new song received as parameter to the uploaded songs list.
     * @param song
     */
    public void addUploaded(Song song) {
        song.setChecked(false);
        uploaded.add(song);
    }


    /**
     * Method that gets the top songs more listened on the date passed as a parameter, being
     * the parameter top the number of songs we want to get
     * @param date
     * @param top
     * @return top songs
     */
    public ArrayList<Song> getTopSongs(Date date, int top) {
        SortedSet<SongTupla> orderedSongs = new TreeSet<SongTupla>();
        ArrayList<Song> result = new ArrayList<>();
        int i = 0;
        for (Song song:Library.getInstance().getSongs()) {
            orderedSongs.add(new SongTupla(song.getPlaybacks().getPlaybacks(date), song));
        }
        for (SongTupla songTupla : orderedSongs.toArray(new SongTupla[orderedSongs.size()])) {
            result.add(songTupla.getSong());
            if (++i == top) {
                return result;
            }
        }
        return result;
    }

    /**
     * Method that gets the top songs more listened of one author, the given as a parameter, on the date passed as a parameter, being
     * the parameter top the number of songs we want to get
     * @param date
     * @param top
     * @param author
     * @return top songs
     */
    public ArrayList<Song> getTopSongs(Date date, int top, User author) {
        SortedSet<SongTupla> orderedSongs = new TreeSet<SongTupla>();
        ArrayList<Song> result = new ArrayList<>();
        int i = 0;
        for (Song song:author.getSongs()) {
            orderedSongs.add(new SongTupla(song.getPlaybacks().getPlaybacks(date), song));
        }
        for (SongTupla songTupla : orderedSongs.toArray(new SongTupla[orderedSongs.size()])) {
            result.add(songTupla.getSong());
            if (++i == top) {
                return result;
            }
        }
        return result;
    }

    /**
     * Method that gets the top albums more listened on the date passed as a parameter, being
     * the parameter top the number of songs we want to get
     * @param date
     * @param top
     * @return top albums
     */
    public ArrayList<Album> getTopAlbums(Date date, int top) {
        SortedSet<AlbumTupla> orderedAlbums= new TreeSet<AlbumTupla>();
        ArrayList<Album> result = new ArrayList<>();
        int i = 0;
        for (Album album:Library.getInstance().getAlbums()) {
            orderedAlbums.add(new AlbumTupla(album.getPlaybacks(date), album));
        }
        for (AlbumTupla albumTupla : orderedAlbums.toArray(new AlbumTupla[orderedAlbums.size()])) {
            result.add(albumTupla.getAlbum());
            if (++i == top) {
                return result;
            }
        }
        return result;
    }

    /**
     * Method that gets the top authors more listened on the date passed as a parameter, being
     * the parameter top the number of songs we want to get
     * @param date
     * @param top
     * @return top authors
     */
    public ArrayList<User> getTopAuthors(Date date, int top) {
        SortedSet<UserTupla> orderedUsers = new TreeSet<UserTupla>();
        ArrayList<User> result = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, User> entry: App.getInstance().users.getUsers().entrySet()) {
            orderedUsers.add(new UserTupla(entry.getValue().getPlaybacks(date), entry.getValue()));
        }
        for (UserTupla userTupla : orderedUsers.toArray(new UserTupla[orderedUsers.size()])) {
            result.add(userTupla.getUser());
            if (++i == top) {
                return result;
            }
        }
        return result;
    }
    /**
     * Method to solve a report. The parameter 'report' indicates te report to solve.
     * 'type' indicates the way in which the report is solved, if it is false, then the
     * song is deleted, if it's true, the song remains. Finally, 'explicit' tell us if
     * after solving the report, the song changes to explicit content or not.
     * @param report
     * @param type
     * @param explicit
     * @return if the report was solved correctly
     */
    public Boolean solveReport(Report report, Boolean type, Boolean explicit) {
        Library lib = Library.getInstance();
        Boolean flag = true;

        flag = authorsAreUnderAge(report.getSong().getAuthors());

        if(type && !(flag && explicit)) //Then the song must be deleted
        {
            report.getSong().setIsexplicit(explicit);
            report.getSong().setChecked(true);
            report.getIssuer().increaseReportscount();
            if(report.getIssuer().getReportCount() > MAX_REPORT_COUNT)
                report.getIssuer().setBanned(true);
            lib.deleteSong(report.getSong());
        }
        else {
            lib.deleteSong(report.getSong());
        }
        return reported.remove(report);
    }

    /**
     * Private function to know if any user of the list received is under age.
     * @param users
     * @return  true or false
     */
    private Boolean authorsAreUnderAge(ArrayList<User> users) {
        for (User user:users) {
            if (user.getIsAdult()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Method that solves an upload. 'song' indicates us the song from the list of
     * uploaded songs we want to solve. 'type' indicates if the songs is deleted or if it remains,
     * and 'explicit' indicates if the song has or not explicit content.
     * @param song
     * @param type
     * @param explicit
     * @return if the upload was solved successfully.
     */
    public Boolean solveUpload(Song song, Boolean type, Boolean explicit) {
        Library lib = Library.getInstance();
        Boolean flag = true;
        String emailBody = "Your song " + song.getTitle();

        flag = authorsAreUnderAge(song.getAuthors());

        if(type && !(flag && explicit)){ //If the song is ready to be uploaded

            emailBody += " has just been validated as ";
            emailBody += song.isExplicit() ? "explicit." : "not explicit.";
            emailBody += "<br><br>Song will be available when the whole album is checked.";
            song.setChecked(true);
            song.setIsexplicit(explicit);
            lib.saveSong(song);
        }
        else{ //If the song is not ready to be uploaded (copyright, inappropriate content,...) then we notice it to the authors.
            emailBody += "was rejected by the Administrator. <br><br> Please login to your app to get more information";
            for(User users:song.getAuthors()){
                users.addNotification(new SongNotificationDenied(song, "We couldn't upload your song "));
            }
        }
        emailBody += "<br><br>Regards,<br><br>The Neons Team";
        try {
            for(User user:song.getAuthors()){
                user.getMailNotifier().sendEmailNotification(emailBody);
            }
        }catch (Exception exc){
        }
        return uploaded.remove(song);
    }

    public void removoeAllUploaded(){
        uploaded = new ArrayList<Song>();
    }

    public void removoeAllReported(){
        reported = new ArrayList<Report>();
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
}
