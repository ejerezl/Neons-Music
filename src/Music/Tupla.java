package Music;

import java.io.Serializable;

/**
 * Class that describes the functionality of a Tuple
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public abstract class Tupla implements Serializable, Comparable<Tupla> {
    private int playback;

    /**
     * Constructor that given a number of playbacks initializes
     * the attribute 'playback' with that number
     * @param playback
     */
    public Tupla(int playback) {
        this.playback = playback;
    }

    /**
     * Getter of the playback
     * @return
     */
    public int getPlayback() { return playback; }

    /**
     * Method to compare one tuple with another.
     * @param t
     * @return a positive number if the actual tuple is bigger, 0 if there are equals
     * and a negative number if the tuple received by argument is bigger that the current one.
     */
    public int compareTo(Tupla t) {
        return t.playback - this.playback;
    }
}
