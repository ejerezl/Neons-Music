package Music;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.*;

import Users.User;
import Users.AdminUser;
import App.App;

/**
 * This class implements the methods used to make a search in the library.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class Search implements Serializable {

    /**
     * This method can return, depending on what we pass as argument in searchOptions, a list with the albums,
     * authors, or songs, or a combination of them, that match with the search we've made.
     * @param lib
     * @param admin
     * @param search
     * @param searchOptions
     * @return list of results
     */
    public static SearchList search(Library lib, AdminUser admin, String search, SearchOptions searchOptions){

        SearchList returnedList = new SearchList();

        Collections.sort(lib.getAlbums(), new Comparator(){
            public int compare(Object o1, Object o2){
                Album a1 = (Album) o1;
                Album a2 = (Album) o2;

                return a1.getTitle().compareToIgnoreCase(a2.getTitle());

            }
        });

        if (searchOptions.sAlbums) {
            ArrayList<Album> returnedAlbums = searchAlbum(lib, search);
            for (int i = 0 ; i < returnedAlbums.size() ; i++){
                returnedList.addAlbum(returnedAlbums.get(i));
            }
        }

        if (searchOptions.sAuthors) {
            ArrayList<User> returnedAuthors = searchAuthor(admin, search);
            for (int i = 0 ; i < returnedAuthors.size() ; i++){
                returnedList.addAuthor(returnedAuthors.get(i));
            }
        }

        if (searchOptions.sSongs) {
            ArrayList<Song> returnedSongs = searchSong(lib, search);
            for (int i = 0 ; i < returnedSongs.size() ; i++){
                returnedList.addSong(returnedSongs.get(i));
            }
        }

        return returnedList;
    }

    /**
     * This method will search through the albums in the library and will return an array of albums that
     * match with the string passed as an argument. First it will be searched for a match at the beginning of the album title,
     * and then for the match anywhere within the title. In case the album is explicit and the user
     * is under age, the result will be discarded.
     * @param lib
     * @param search
     * @result array of albums matching the search key
     */
    public static ArrayList<Album> searchAlbum(Library lib, String search){

        Boolean explicit = (App.getLogged() != null) && (App.getLogged().getIsAdult());
        ArrayList<Album> returnedAlbums = new ArrayList<Album>();

        //We create the matching pattern with the string we're gonna search for and discarding the uppercase.

        Pattern inputSearch = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        /*
         * Steps through albumList array, testing each albumTitle string
         * against the pattern object.  Will add an album to the returnedList array
         * only if the pattern is in the albumTitle, and if the pattern is at the
         * beginning of the albumTitle.
         */

        for (int i = 0 ; i < lib.getAlbums().size(); i++){

            Matcher matcher = inputSearch.matcher(lib.getAlbums().get(i).getTitle().trim());

            if (matcher.find() && matcher.start() == 0) {
                if (explicit == false && lib.getAlbums().get(i).getIsExplicit() == true){
                    continue;
                }
                returnedAlbums.add(lib.getAlbums().get(i));
            }
        }

        /*
         * Will return an album to returnedList if the pattern is anywhere in the
         * albumTitle, but not at the beginning
         */

        for (int i = 0 ; i < lib.getAlbums().size() ; i++){
            Matcher matcher = inputSearch.matcher(lib.getAlbums().get(i).getTitle().trim());

            if (matcher.find() && matcher.start() != 0) {
                if (explicit == false && lib.getAlbums().get(i).getIsExplicit() == true ){
                    continue;
                }
                returnedAlbums.add(lib.getAlbums().get(i));
            }
        }

        return returnedAlbums;

    }

    /**
     * This method will search through the songs in the library and will return an array of songs that
     * match with the string passed as an argument. First it will be searched for a match at the beginning
     * of the song title, and then for the match anywhere within the title. In case the song is explicit and the user
     * is under age, the result will be discarded.
     * @param lib
     * @param search
     * @return array of songs matching the search key
     */
    public static ArrayList<Song> searchSong(Library lib, String search) {

        Boolean explicit = (App.getLogged() != null) && (App.getLogged().getIsAdult());
        ArrayList<Song> returnedSongs = new ArrayList<Song>();

        Pattern inputSearch = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        for (int i = 0 ; i < lib.getAlbums().size() ; i++) {
            for (int j = 0 ; j < lib.getAlbums().get(i).getSongs().size() ; j++){
                Matcher matcher = inputSearch.matcher(lib.getAlbums().get(i).getSongs().get(j).getTitle().trim());

                if (matcher.find() && matcher.start() == 0) {
                    if (explicit == false && lib.getAlbums().get(i).getSongs().get(j).isExplicit() == true ) {
                        continue;
                    }
                    returnedSongs.add(lib.getAlbums().get(i).getSongs().get(j));
                }
            }

        }

        for (int i = 0 ; i < lib.getAlbums().size() ; i++) {
            for (int j = 0 ; j < lib.getAlbums().get(i).getSongs().size() ; j++) {
                Matcher matcher = inputSearch.matcher(lib.getAlbums().get(i).getSongs().get(j).getTitle().trim());

                if (matcher.find() && matcher.start() != 0) {
                    if (explicit == false && lib.getAlbums().get(i).getSongs().get(j).isExplicit() == true ){
                        continue;
                    }
                    returnedSongs.add(lib.getAlbums().get(i).getSongs().get(j));
                }
            }
        }

        return returnedSongs;
    }

    /**
     * This method will search through the registered users saved in the administrator's class and will return
     * an array of users that match with the string passed as an argument. First it will be searched for a match
     * at the beginning of the user's artistic name, and then for the match anywhere within the artistic name.
     * @param admin
     * @param search
     * @return array of users matching the search key
     */

    public static ArrayList<User> searchAuthor(AdminUser admin, String search) {

        ArrayList<User> returnedAuthors = new ArrayList<User>();

        Pattern inputSearch = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        for (Map.Entry<String, User> entry: admin.getUsers().entrySet()) {
            User user = entry.getValue();
            Matcher matcher = inputSearch.matcher(user.getArtisticname().trim());

            if (matcher.find() && matcher.start() == 0) {
                returnedAuthors.add(user);
            }
        }

        for (Map.Entry<String, User> entry: admin.getUsers().entrySet()) {
            User user = entry.getValue();
            Matcher matcher = inputSearch.matcher(user.getArtisticname().trim());

            if (matcher.find() && matcher.start() != 0) {
                returnedAuthors.add(user);
            }
        }

        return returnedAuthors;

    }
}
