package Test;

import Music.Playback;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import Users.Free;

import static org.junit.Assert.*;

/**
 * Class to test the functionality of Free class.
 * Each method is implemented to test one method of the Free
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class FreeTest {

    private static Free free;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        free = new Free();
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{

    }

    /**
     * Tester of increasePlayback()
     * Here we increase the playbacks of a free user and then check
     * if the result is the expected
     */
    @Test
    public void increasePlayback() {

        boolean flag = false;

        free.increasePlaybacks();

        if (free.getPlaybacks() == 1){
            flag = true;
        }

        assertTrue(flag);
    }

    /**
     * Tester of getPlaybacks()
     * Check iff the method works correctly calling to getPlaybacks() from user and from class playbacks.
     */
    @Test
    public void getPlaybacks() {

        Playback playbacks = new Playback();
        boolean flag = false;

        if (free.getPlaybacks() == playbacks.getPlaybacks(new Date())) {
            flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of play()
     * As we know, a free user as a limit of playbacks, so we check the function play when
     * the number of playbacks less than the limit and when is upper.
     */
    @Test
    public void play() {
        boolean flag = true;

        assertTrue(free.play());

        for (int i = 0; i <  101; i++) {
            free.increasePlaybacks(); //We exceed the limit
        }

        assertFalse(free.play());
    }

    }
