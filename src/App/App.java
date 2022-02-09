package App;


import Music.*;
import Users.AdminUser;
import Users.Administrator;
import Users.AuthenticationController;
import Users.User;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import static Test.AdminUserTest.musicPath;

public class App implements Serializable {
    public boolean isAdminLogged = false;
    public Administrator admin;
    public final Library lib;
    public final AdminUser users;
    public Administrator administrator;
    private AuthenticationController authenticationController;
    public User logged;
    public static String configPath = "neons.config";
    public static App app;
    public static boolean demo = false;
    public static boolean adminDemo = false;

    public static void main(String[] args) throws Exception{
        /*administrator = new Administrator("Administrator", "administrator@gmail.com", "password");
        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<Song> songs = new ArrayList<Song>();
        answers.add("firstanswer");
        answers.add("secondanswer");
        User eva = users.register("Eva", "evalanic", "evalanic@gmail.com", new Date(115, 02,07), answers, "password");
        System.out.println("User eva registered");
        users.register("AC_DC", "AC-DC",
                "alexandgu@hotmail.com", new Date(99, 02, 07), answers, "password");
        System.out.println("User AC_DC registered");
        System.out.println("Logging in with AC_DC user");
        User registered = users.login("alexandgu@hotmail.com" ,"password");
        registered.getMailNotifier().setDefaultNotifying(false);
        eva.getMailNotifier().setDefaultNotifying(false);
        System.out.println("Email notifications switched off");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you want to login with your twitter account?");
        Boolean flag = true;
        while (flag){
            System.out.println("Please enter 'Y' or 'N'");
            String line = br.readLine();
            if(line.equals("Y")){
                System.out.println(registered.getTwitterClient().getLoginURL());
                System.out.println("Please introduce copy and paste the link above in a web browser. After logging in with your twitter accouont pleease introduce the pin provided here");
                registered.getTwitterClient().login(br.readLine());
                break;
            }else if(line.equals("N")){
                break;
            }
        }
        System.out.println("Eva now follows AC_DC");
        eva.follow(registered);
        System.out.println("AC_DC uploads Shoot to Thrill");
        songs.add(new Song("Shoot to Thrill", musicPath+"1.mp3", registered));
        System.out.println("AC_DC uploads Rock and Roll dammnation");
        songs.add(new Song("Rock and Roll dammnation", musicPath+"2.mp3", registered));
        System.out.println("AC_DC uploads Guns for Hire");
        songs.add(new Song("Guns for Hire", musicPath+"3.mp3", registered));
        Album album = lib.createAlbum("Iron Man2", songs.toArray(new Song[songs.size()]));
        administrator.solveUpload(administrator.getUploaded().get(0), true, false);
        administrator.solveUpload(administrator.getUploaded().get(0), true, true);
        administrator.solveUpload(administrator.getUploaded().get(0), true, true);

        System.out.println("Administrator just validated the three songs marking them as explicit");
        songs.clear();
        System.out.println("AC_DC uploads War Machine");
        songs.add(new Song("War Machine", musicPath+"14.mp3", registered));
        System.out.println("AC_DC uploads Highway to Hell");
        songs.add(new Song("Highway to Hell", musicPath+"15.mp3", registered));
        Album album2 = lib.createAlbum("Iron Man", songs.toArray(new Song[songs.size()]));
        System.out.println("Administrator decides that War Machine is not uploaded");
        administrator.solveUpload(administrator.getUploaded().get(0), false, true);
        System.out.println("Administrator decides that Highway to hell is uploaded as not explicit");
        administrator.solveUpload(administrator.getUploaded().get(0), true, false);
        System.out.println("Eva notifications:");
        eva.getNotifications().forEach(notification -> System.out.println(notification));
        MediaPlayer.getInstance().startPlaying(album, false, true);
        System.out.println("We start listening an album with the first three songs");
        Thread.sleep(1000);
        System.out.println("We add highway to hell to the queue");
        MediaPlayer.getInstance().addToQueue(album2.get(0));
        System.out.println("Play next");
        MediaPlayer.getInstance().playNext();
        Thread.sleep(1000);
        System.out.println("Play next");
        MediaPlayer.getInstance().playNext();
        Thread.sleep(1000);
        System.out.println("Play before");
        MediaPlayer.getInstance().playBefore();
        Thread.sleep(1000);
        System.out.println("Stop playing");
        MediaPlayer.getInstance().stopPlaying();

        System.out.println("We try to search for songs and we see that only songs in an album");
        Search.searchSong(Library.getInstance(), "Shoot").forEach(element -> System.out.println(element.getTitle()));
        Search.searchSong(Library.getInstance(), "Rock").forEach(element -> System.out.println(element.getTitle()));
        Search.searchSong(Library.getInstance(), "Guns").forEach(element -> System.out.println(element.getTitle()));


        System.out.println("Login out of the adult user");
        AdminUser.getInstance().logout(logged);
        User loggedin = AdminUser.getInstance().login("evalanic@gmail.com", "password");
        System.out.println("If we try to search for songs in the library we will only find the one that is not explicit");
        Search.searchSong(Library.getInstance(), "Shoot").forEach(element -> System.out.println(element.getTitle()));
        Search.searchSong(Library.getInstance(), "Rock").forEach(element -> System.out.println(element.getTitle()));
        Search.searchSong(Library.getInstance(), "Guns").forEach(element -> System.out.println(element.getTitle()));*/

    }

    public static App getInstance(){
        return app;
    }
    public App(){
        App.app = this;
        lib = Library.getInstance();
        users = AdminUser.getInstance();
        administrator = new Administrator("Juan", "Juan", "123abc..");
        authenticationController = AuthenticationController.getInstance();
    }
    public static void setAdministrator(Administrator admin){
        getInstance().administrator= admin;
    }
    public static Administrator getAdministrator() { return getInstance().administrator; }
    public static User setLogged(User user) {
        getInstance().logged = user;
        return getInstance().logged;
    }

    public static User getLogged() {
        return getInstance().logged;
    }

    public void saveData(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(configPath));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static App loadData(){
        ObjectInputStream ois = null;
        App app = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(App.configPath));
            app = (App)ois.readObject();
            Library.setInstance(app.getLib());
            AdminUser.setInstance(app.getAdminUser());
            AuthenticationController.setInstance(app.getAdminUser().getAuthenticationController());
            MediaPlayer.setInstance(app.getLib().getMediaPlayer());
            App.app = app;
            ois.close();
            return app;
        }catch (IOException exc){
            System.out.println("Exception while trying to open file with last configuration");
        }catch (ClassNotFoundException exc){
            System.out.println("Corrupted file");
        }
        return app;
    }

    public AdminUser getAdminUser(){
        return users;
    }
    public Library getLib(){
        return lib;
    }
}
