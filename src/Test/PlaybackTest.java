package Test;

import Music.Playback;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class to test the functionality of Playback class.
 * Each method is implemented to test one method of the Playback
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class PlaybackTest {
    Playback playback;
    String fecha1;
    String fecha2;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        playback = new Playback();
        fecha1 = "09/1/2019";
        fecha2 = "10/01/2000";
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tester of increaseValue()
     * Here we call to the method increaseValue() and then check if the value of the playback
     * has changed to the value expected.
     */
    @Test
    public void increaseValue() {
        int initialPlaybacks = playback.getPlaybacks(new Date());

        playback.increaseValue();
        playback.increaseValue();

        assertTrue(playback.getPlaybacks(new Date()) == initialPlaybacks + 2);
    }

    /**
     * Tester of compare()
     * This methd compares strings that define a date each one, so we check the values returned
     * with different combinations of dates.
     */
    @Test
    public void compare() {

        assertTrue(playback.compare(fecha1, fecha2) == 1);
        assertTrue(playback.compare(fecha1, fecha1) == 0);
        assertTrue(playback.compare(fecha2, fecha1) == -1);
    }

    /**
     * Tester of differenceBetweenDates()
     * The method we want to test returns an array with the difference between days, months, and years
     * with two dates. To check if it works we see the results with different combinations of dates.
     */
    @Test
    public void differenceBetweenDates() {
        int[] result = new int[3];
        Date d1 = new Date();
        // Create a calendar object with today date. Calendar is in java.util pakage.
        Calendar calendar = Calendar.getInstance();
        // Move calendar to yesterday
        calendar.add(Calendar.DATE, 1);
        // Get current date of calendar which point to the yesterday now
        Date d2 = calendar.getTime();

        result[0] = 0;
        result[1] = 0;
        result[2] = 0;

        assertTrue(playback.differenceBetweenDates(d1, d1)[0] == result[0]);
        assertTrue(playback.differenceBetweenDates(d1, d1)[1] == result[1]);
        assertTrue(playback.differenceBetweenDates(d1, d1)[2] == result[2]);
        result[0] = -1;
        //Take into consideration that the test is made for two dates that belong to the same month
        //If tomorrow's day is a new month it will not work.
        assertTrue(playback.differenceBetweenDates(d1, d2)[0] == result[0]);
        assertTrue(playback.differenceBetweenDates(d1, d2)[1] == result[1]);
        assertTrue(playback.differenceBetweenDates(d1, d2)[2] == result[2]);

    }
}