package Music;

import App.App;
import Exceptions.MaxPlaybacksException;
import Users.Administrator;
import Users.TwitterClient;
import pads.musicPlayer.Mp3Player;
import pads.musicPlayer.exceptions.Mp3InvalidFileException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

/**
 * This class implements the functionality of MediaPlayer.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class MediaPlayer implements Serializable {
    private ArrayList<Song> playing;
    private BlockingQueue<Song> priorityQueue = new LinkedBlockingDeque<>();
    private int playbacks = 0;
    private long toSleep = 0;
    private Semaphore semaphore = new Semaphore(1);
    private Random random = new Random();
    private transient Mp3Player player;
    private int current;
    private ArrayList<Integer> played = new ArrayList<Integer>();
    private boolean random_playing = false;
    private boolean repeat = false;
    private static MediaPlayer instance = new MediaPlayer();
    private Thread backgroundPlayer;
    private boolean stop = false;
    private boolean isPlaying;

    private MediaPlayer() {

    }

    /** This class is implemented following Singleton pattern.
     *  This method returns the unique instance of the class
     * @return MediaPlayer instance
     */
    public static MediaPlayer getInstance(){
        return instance;
    }

    /**
     * Sets the instance to be used by the other classes
     * @param mediaPlayer
     */
    public static void setInstance(MediaPlayer mediaPlayer) { instance = mediaPlayer; }

    /**This method is used to start playing an aggrupation, method will get all the (playable songs) from
     * the aggrupation. It will start a new thread where the music will be played.     *
     * @param playing Aggrupation(album or playlist) to be played
     * @param random_playing true if you want to play randomly, false otherwise
     * @param repeat true if you want to play multiple times the same song from one aggrupation
     */

    public void startPlaying(Song song){
        this.playing = new ArrayList<>();
        this.playing.add(song);
        this.repeat = true;
        this.isPlaying = true;
        try{
            if(this.player != null){
                this.player.stop();
            }
            this.player = new Mp3Player();
        }catch (Exception exc){

        }
        if(this.backgroundPlayer != null){
            this.backgroundPlayer.stop();
        }
        this.backgroundPlayer = new Thread(MediaPlayer::run);
        this.backgroundPlayer.start();
    }
    public void startPlaying(Aggrupation playing, boolean random_playing, boolean repeat) {

        this.isPlaying = true;
        this.playing = playing.getSongs();
        this.random_playing = random_playing;
        this.repeat = repeat;
        try{
            if(this.player != null){
                this.player.stop();
            }
            this.player = new Mp3Player();
        }catch (Exception exc){

        }
        this.backgroundPlayer = new Thread(MediaPlayer::run);
        this.backgroundPlayer.start();
    }

    public void stopPlaying() {
        if(this.player == null){
            return;
        }
        this.isPlaying = false;
        this.player.stop();
        this.player = null;
        try {
            semaphore.acquire();
            this.stop = true;
            this.toSleep = 0;
            this.backgroundPlayer.interrupt();
        }catch (Exception exc){

        } finally {
            semaphore.release();
        }

    }
    /**
     * This method is used to add to a priority queue any song. If an aggrupation is playing, you can add a song from
     * outside this aggrupation to the queue. The song will be played next and after that the aggrupation will keep
     * playing.
     * @param song song to be played
     */
    public void addToQueue(Song song) {
        try {
            semaphore.acquire();
            priorityQueue.put(song);
            toSleep += Mp3Player.getDuration(song.getPath());
        } catch (Exception exc) {
            System.out.println("Invalid song");
        } finally {
            semaphore.release();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public LinkedList<Song> getQueue(){
        return new LinkedList<>(priorityQueue);
    }

    /**
     * Stops playing the current song and starts playing the next one in the queue, if no song is next in the queue
     * music will stop playing.
     * @return the song played, null otherwise
     */
    public Song playNext(){
        return playNext(true);
    }

    /** Stops playing the current song and starts playing the next one in the queue, if no song is next in the queue
     * music will stop playing.
     * This method has a boolean to differentiate if the method is being called from outside the class or from
     * the sleeping thread. If the method is called from outside the class the method will wake up the sleeping thread
     * to play the next song inmediately. Otherwise the sleeping thread wont interrupt himself
     * @param callingThread
     * @return
     */
    private Song playNext(boolean callingThread) {
        int next =0;
        Song nextSong;
        int counter = 1;
        if (priorityQueue.size() != 0) {
            nextSong = priorityQueue.poll();
        } else {
            if (random_playing) {
                List<Song> checked = new ArrayList<Song>();
                next = random.nextInt(playing.size());
                if (repeat == false) {
                    while(played.contains(next) && (checked.size() < playing.size())){
                        next = random.nextInt(playing.size());
                        checked.add(playing.get(next));
                    }
                    if (checked.size() == playing.size()) {
                        return null;
                    }
                }
            } else {
                next = (current + 1)%playing.size();
                if (repeat == false) {
                    while(played.contains(next) && counter < playing.size()){
                        next = (current+1)%playing.size();
                        counter++;
                    }
                    if (played.contains(next)) {
                        return null;
                    }
                }
            }
            played.add(next);
            nextSong = playing.get(next);
            current = next;
        }

        try {
            player.stop();
            try {
                player = new Mp3Player();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            player.add(nextSong.getPath());
            try {
                playbacks++;
                if ((playbacks > Administrator.getMAX_PLAYBACKS() && App.getLogged() == null) || (App.getLogged().getState().play() == false)) {
                    throw new MaxPlaybacksException();
                }
                App.getLogged().getState().increasePlaybacks();
                player.play();
                semaphore.acquire();
                toSleep = (long)Mp3Player.getDuration(nextSong.getPath());
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                semaphore.release();
            }
            if (callingThread) {
                backgroundPlayer.interrupt();
            }
        } catch (Mp3InvalidFileException exc) { // should never happen
            System.out.println("Wrong mp3 file");
        }
        TwitterClient.nowPlayingTweet(nextSong);
        return nextSong;
    }

    /**
     * This method will play the song being reproduced before
     */
    public void  playBefore() {
        int index = played.size()-2;
        if (index < 0) {
            index += played.size();
        }
        Song song = playing.get(played.get(index));
        priorityQueue.add(song);


        try {
            semaphore.acquire();
            toSleep += Mp3Player.getDuration(song.getPath());
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            semaphore.release();
        }
        playNext();
    }

    /**
     * Function used to keep updating the MP3Player queue.
     * We decide to use our own queue so we have the ability to have a priority and to keep track of played songs
     */
    private static void run() {
        Song current;
        MediaPlayer mediaPlayer = MediaPlayer.getInstance();
        Song start;
        double duration = 1;
        if (mediaPlayer.random_playing) {
            int random = mediaPlayer.random.nextInt(mediaPlayer.playing.size());
            start = mediaPlayer.playing.get(random);
            mediaPlayer.played.add(random);
        } else {
            start = mediaPlayer.playing.get(0);
            mediaPlayer.played.add(0);
        }
        try {
            mediaPlayer.player.add(start.getPath());
            mediaPlayer.player.play();
            TwitterClient.nowPlayingTweet(start);
            long length = (long)Mp3Player.getDuration(start.getPath());
            System.out.println(length);
            sleep(length*1000);
            if (MediaPlayer.getInstance().stop) {
                MediaPlayer.getInstance().stop = false;
                return;
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
        while (true) {
            current = MediaPlayer.getInstance().playNext(false);
            if (current == null) {
                mediaPlayer.player.stop();
                mediaPlayer.player = null;
                return;
            }
            try {
                duration = Mp3Player.getDuration(current.getPath())*1000;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sleep((long)duration);
            if (MediaPlayer.getInstance().stop) {
                MediaPlayer.getInstance().stop = false;
                return;
            }
        }
    }

    public static void sleep(long duration) {
        while (true) {
            try {
                Thread.sleep(duration);
                return;
            } catch (Exception exc){
                try {
                    MediaPlayer.getInstance().semaphore.acquire();
                    duration = MediaPlayer.getInstance().toSleep*1000;
                } catch (Exception exce){
                } finally {
                    MediaPlayer.getInstance().semaphore.release();
                }
            }
        }
    }

    public void setRandom(boolean random){
        this.random_playing = random;
    }

    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }

    public Song getCurrent(){
        if(playing != null){
            return playing.get(current);
        }
        return null;

    }
}
