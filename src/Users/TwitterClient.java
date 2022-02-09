package Users;

import App.App;
import Music.Album;
import Music.Song;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Class used to send tweets while playing songs or when a new album is uploaded
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
import java.io.Serializable;

public class TwitterClient implements Serializable {
    private static final String CONSUMER_KEY = "L3XQUFYjKwcYWJNnSBxVUykXE";
    private static final String CONSUMER_KEY_SECRET = "9bMxImCAp67c1pPYmrCdOrlxpQ1VmmnJ93cLb8Pcq4PldPYpYl";
    private  Twitter twitter = new TwitterFactory().getInstance();
    private RequestToken requestToken;
    private AccessToken accessToken = null;
    private boolean defaultTwitting = false;

    public TwitterClient(){}

    /**
     * This method queries the Twitter API and retrieves an URL for the user to log in
     * @return An URL where the user whould log in in order to get a OTP
     * @throws TwitterException
     */
    public String getLoginURL() throws TwitterException {
        if(accessToken != null){
            return "You are already logged in!";
        }
        if(twitter.getConfiguration().getOAuthConsumerKey() == null || twitter.getConfiguration().getOAuthConsumerSecret() == null){
            twitter.setOAuthConsumer(CONSUMER_KEY,CONSUMER_KEY_SECRET);
        }
        requestToken = twitter.getOAuthRequestToken();
        return requestToken.getAuthorizationURL();
    }

    /**
     * After the user has logged in in the URL get from getLoginURL the User introduces the pin in this method
     * and is finally logged
     * The accestoken that we got from Twitter is saved so that the user only logs in once
     * @param pin
     * @throws TwitterException
     */
    public boolean login(String pin) throws TwitterException{
        if(accessToken != null){
            return true;
        }
        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        defaultTwitting = true;
        return  accessToken != null;
    }

    /**
     * Send a tweets specified in the text parameter
     * @param text
     */
    public void tweet(String text) {
        if(text.length() > 240){
            return;
        }
        try {
            twitter.updateStatus(text);
        } catch (Exception exc){
        }
    }

    /**
     *
     * @param song
     */
    public static void nowPlayingTweet(Song song) {
        if(App.getLogged() == null || App.getLogged().getTwitterClient().accessToken == null || App.getLogged().getTwitterClient().defaultTwitting == false) {
            return;
        }
        App.getLogged().getTwitterClient().tweet("Currently playing " +song.getTitle() + " by "+ song.getAuthors().get(0).getArtisticname() + " #Neons");
    }
    public void newAlbumTweet(Album album) {
        if(App.getLogged() == null || App.getLogged().getTwitterClient().accessToken == null || defaultTwitting == false) {
            return;
        }
        App.getLogged().getTwitterClient().tweet("Check out my new album "+album.getTitle()+ " now available in " + "#Neons");
    }

    public void setDefaultTwitting(boolean defaultTwitting){
        this.defaultTwitting = defaultTwitting;
    }

    public boolean isLogged(){
        return accessToken != null;
    }
}
