package Music;

import Users.User;
import java.io.Serializable;

/**
 * Specific tuple composed of an integer and a user. It inherits from tuple.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class UserTupla extends Tupla implements Serializable {

    private User user;

    /**
     * Constructor that initializes the fields of the UserTuple with the values given
     * as parameters.
     * @param playbacks
     * @param user
     */
    public UserTupla(int playbacks, User user) {
        super(playbacks);
        this.user = user;
    }

    /**
     * Getter of the user
     * @return the user.
     */
    public User getUser() {
        return user;
    }


}
