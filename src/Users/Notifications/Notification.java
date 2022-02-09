package Users.Notifications;

import java.io.Serializable;

/**
 * Abstract class that encompasses all notifications a user can receive.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public abstract class Notification implements Serializable {

    /**
     * Method to see the information of a notification
     * @return the resulting string
     */
    public abstract String toString();
}
