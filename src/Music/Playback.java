package Music;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Playback class describes like a calendar with the playbacks per day.
 * To implement it we have appealed to the use of HashMap, where the key is
 * a day and the value is the number of playbacks in that day.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Playback implements Serializable {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private HashMap<String,Integer> playbacks;

    /**
     * Constructor that initializes the HashMap.
     */
    public Playback(){
        playbacks = new HashMap<String, Integer>();
    }

    /**
     * Method that increase the number of playbacks for the day when the method is called.
     */
    public void increaseValue() {
        if (playbacks.containsKey(format.format(new Date()))) {
            playbacks.replace(format.format(new Date()), playbacks.get(format.format(new Date())) + 1);
        } else {
            playbacks.put(format.format(new Date()), 1);
        }
    }

    /**
     * Returns the number of playbacks from the date received by argument till today (included).
     * @param date
     * @return number of playbacks
     */
    public int getPlaybacks(Date date) {
        int plays = 0;
        for (Map.Entry<String, Integer> dato : playbacks.entrySet()) {
            if(compare(dato.getKey(), format.format(date)) >= 0){
                plays += dato.getValue();
            }
        }
        return plays;
    }

    /**
     *  This method compares String s1 and s2, being both String in a format of a date.
     *  If the date described in s1 is bigger(earlier) than the date described in s2, then it returns 1.
     *  If, instead it's smaller it will return -1. In case both strings (dates) are equals, it
     *  will return 0.
     *
     * @param s1
     * @param s2
     * @return integer that determines if a string is bigger than the other
     */
    public int compare(String s1, String s2) {
        String[] campos1;
        String[] campos2;

        campos1 = s1.split("/");
        campos2 = s2.split("/");

        if (Integer.parseInt(campos1[2]) > Integer.parseInt(campos2[2])) {
            return 1;
        } else if (Integer.parseInt(campos1[2]) < Integer.parseInt(campos2[2])) {
            return -1;
        } else {
            if (Integer.parseInt(campos1[1]) > Integer.parseInt(campos2[1])) {
                return 1;
            } else if (Integer.parseInt(campos1[1]) < Integer.parseInt(campos2[1])) {
                return -1;
            } else {
                if (Integer.parseInt(campos1[0]) > Integer.parseInt(campos2[0])) {
                    return 1;
                } else if (Integer.parseInt(campos1[0]) < Integer.parseInt(campos2[0])) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    /**
     * This method returns an integer array of dimension 3, where the first field is the difference
     * between the day of the first date received by argument and the second one. The second field is
     * the difference between the moths, and the third one between the years.
     *
     * @param date1
     * @param date2
     * @return the difference between date1 and date2
     */
    public static int[] differenceBetweenDates(Date date1, Date date2) {
        int[] result = new int[3];
        String[] campos1 = format.format(date1).split("/");
        String[] campos2 = format.format(date2).split("/");

        result[0] =  Integer.parseInt(campos1[0]) - Integer.parseInt(campos2[0]);
        result[1] =  Integer.parseInt(campos1[1]) - Integer.parseInt(campos2[1]);
        result[2] =  Integer.parseInt(campos1[2]) - Integer.parseInt(campos2[2]);
        return result;
    }
}
