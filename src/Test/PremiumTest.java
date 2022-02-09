package Test;

import es.uam.eps.padsof.telecard.OrderRejectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import Users.Premium;

import static org.junit.Assert.*;

/**
 * Class to test the functionality of Premium class.
 * Each method is implemented to test one method of the Premium
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class PremiumTest {

    Premium p = null;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        p = new Premium(new Date(), "4659277360001204");
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Tester of setSubscriptionDate()
     * Here we change the subscription date and then check if the change has been successful.
     */
    @Test
    public void setSubscriptionDate() {
        Date date = new Date();
        boolean flag = false;
        p.setSubscriptionDate(date);

        if (p.getSubscriptionDate().equals(new Date())){
            flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of play()
     * play() method must always returns true for a preimum user
     */
    @Test
    public void play() {
        assertTrue(p.play());
    }

    /**
     * Tester of renewPremium()
     * Here we renew the premium subscription, so the date must updated to today's date.
     * We check  it.
     */
    @Test
    public void renewPremium() {
        boolean flag = true;
        try {
            p.renewPremium();
            if (p.getSubscriptionDate().equals(new Date())){
                flag = true;
            }
        } catch (OrderRejectedException e) {
            e.printStackTrace();
        }
        assertTrue(flag);
    }
}