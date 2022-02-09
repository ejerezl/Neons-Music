package Users;

import Music.Playback;

import java.io.Serializable;
import java.util.Date;

/**
 * This class inherit from State and describes the free type of
 * account.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Free extends State implements Serializable {
    private Playback playbacks;


    /**
     * Constructor of the Free type.
     */
    public Free() {
        this.playbacks = new Playback();
    }

    /**
     * Method to increase the playbacks a user does
     */
    public void increasePlaybacks(){
        playbacks.increaseValue();
    }

    /**
     * Getter of the playbacks a user has made
     * @return playbacks
     */
    public int getPlaybacks(){
        return playbacks.getPlaybacks(new Date());
    }

    /**
     * Method to know if a free user can play a song
     * @return a boolean that indicates that
     */
    public boolean play(){
        if (getPlaybacks() < 100) {
            return true;
        }
        return false;
    }

    /**
     * Method to renew the premium state. In the case of free users
     * this method does nothing.
     */
    public void renewPremium(){
        return;
    }
    
}
