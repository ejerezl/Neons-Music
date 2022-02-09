package Music;

import Users.User;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {

    private Date reported;
    private Boolean solved = false;
    private User issuer;
    private Song song;

    public Report(Date reported, User issuer, Song song) {
        this.reported = reported;
        this.issuer = issuer;
        this.song = song;
        song.setChecked(false);
    }

    public Song getSong() { return song; }

    public User getIssuer(){
        return issuer;
    }
}
