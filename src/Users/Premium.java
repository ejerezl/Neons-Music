package Users;

import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.telecard.TeleChargeAndPaySystem;

import java.io.Serializable;
import java.util.Date;

/**
 * This class inherit from State and describes the premium type of
 * account.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Premium extends State implements Serializable {

    Date subscriptionDate;
    private static double amount;
    private static final String subject = "Neons music";
    String creditCard;

    /**
     * Constructor of the Premium type, that initializes it wiht the parameter received.
     * @param subscriptionDate
     * @param creditCard
     * @throws OrderRejectedException
     */
    public Premium(Date subscriptionDate, String creditCard) throws OrderRejectedException {
        this.creditCard = creditCard;
        this.subscriptionDate = subscriptionDate;
        TeleChargeAndPaySystem.charge(this.creditCard, subject, amount);
    }


    /**
     * Setter of the subscription date of a premium user
     * @param subscriptionDate
     */
    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }


    /**
     * Getter of the subscription date
     * @return the subscription date
     */
    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    /**
     * Method to know if a user can play a song. In the case of premium user it always returns
     * true, because they dont have a limit number of playbacks
     * @return true
     */
    public boolean play(){
        return true;
    }

    /**
     * Method to renew the premium state for a user
     * @throws OrderRejectedException
     */
    public void renewPremium() throws OrderRejectedException{
        if(true){
            TeleChargeAndPaySystem.charge(this.creditCard, this.subject, this.amount);
        }
        subscriptionDate = new Date();
    }

    /**
     * Method to increase the playbacks a user does. In the case of the premium user
     * this method does nothing.
     */
    public void increasePlaybacks(){
        return;
    }
}
