package Users.Notifications;

import Users.User;

import java.io.Serializable;

/**
 * Class that describes the Follow Notifications
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class FollowNotification extends Notification implements Serializable {
    private User user;

    /**
     * Constructor of the Follow Notification
     * @param user
     */
    public FollowNotification (User user){
        this.user = user;
    }

    /**
     * Method to get information about the Following notification
     * @return the resulting string
     */
    public String toString(){
        return (user.getArtisticname() + " started following you!");

    }

    /**
     * Getter of the user
     * @return the user
     */
    public User getUser() { return user; }
}
