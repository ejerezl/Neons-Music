package Users;

import es.uam.eps.padsof.telecard.OrderRejectedException;

import java.io.Serializable;

/**
 * Abstract class that describes the type of account a user has.
 * The State can be of differennt types
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public abstract class State implements Serializable {

    /**
     * Method to know if a user can play or not a song
     * @return a boolean that indicates that
     */
    public abstract boolean play();

    /**
     * Method to renew the premium state
     * @throws OrderRejectedException
     */
    public abstract void renewPremium () throws OrderRejectedException;

    /**
     * Method to increase the playbacks a user does.
     */
    public abstract void increasePlaybacks();
}
