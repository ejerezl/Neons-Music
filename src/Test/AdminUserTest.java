package Test;

import App.App;
import Exceptions.LoginException;
import Users.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Class to test the functionality of AdminUser class.
 * Each method is implemented to test one method of the AdminUser
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AdminUserTest {
    public static final String path = "/home/esther/IdeaProjects/padsof/";
    public static final String musicPath = path + "music/";
    public static final String imagePath = path + "images/";

    private AdminUser admin;
    private AuthenticationController auth;
    private static final String mail = "esther@gmail.com";
    private static final String pass = "password";
    private ArrayList<String> answ = new ArrayList<>();
    User user;

    /**
     * Done before every test
     * @throws Exception
     */
    @org.junit.Before
    public void setUp() throws Exception {
        App.setAdministrator(new Administrator("Administrator", "administrator@gmail.com", "admin"));
        admin = AdminUser.getInstance();
        auth = AuthenticationController.getInstance();
        auth.readPasswords();

        ArrayList<String> answ = new ArrayList<>();
        answ.add("First");
        answ.add("Second");

        user = admin.register("Esther Jerez", "ejerez", mail, new Date(), answ, pass);
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @org.junit.After
    public void tearDown() throws Exception {

    }

    /**
     * Tester of method register()
     * Here we register a new user in the app. And then, try to log in
     * with that new user.
     */
    @org.junit.Test
    public void register() {
        User user;
        String mail = "alex@gmail.com";
        String pass = "password";
        Boolean flag = true;

        user = admin.register("Alex", "alexandgu", mail, new Date(), answ, pass);

        if (user == null) {
            flag = false;
        }
        try {
            assertTrue(auth.checkCredentials(mail, pass));
        } catch(LoginException exc) {
            assertTrue(false);
        }
        assertTrue(flag);
    }


    /**
     * Tester of login() method.
     * Here we try to login with a correct user and a incorrect user, to check in
     * both cases if it works as expected.
     */
    @org.junit.Test
    public void login() {
        Boolean flag = false;

        //Now we log in with that user created
        try {
            user = admin.login(mail, pass);

        } catch(LoginException exc) {
            exc.printStackTrace();
        }
        assertTrue(user.equals(App.getLogged()));
        try {
            user = admin.login(mail, "wrongpassword");
            assertTrue(false);
        } catch (LoginException exc) {
            assertTrue(true);
        }
        admin.logout(App.getLogged());
    }

    /**
     * Tester of logout()
     * Here we just log out with the current user that was logged in
     * and check the results expected.
     */
    @org.junit.Test
    public void logout() {
        admin.logout(App.getLogged());
        assertNull(App.getLogged());
    }

    /**
     * Tester of restorePswd()
     * With a registered user, we try to change the password giving the security answers.
     * Once the password is changed, we log in again and see if it works.
     */
    @org.junit.Test
    public void restorePswd() {

        ArrayList<String> answ = new ArrayList<>();
        answ.add("First");
        answ.add("Second");
        User user;

        user = admin.register("Alex andr", "andraca", "andraca@gmail.com", new Date(), answ, "password");
        assertTrue(admin.restorePswd("andraca@gmail.com", "newpsswd", answ));
        //Now we should be able to log in with that new password because the answers to the security questions are right.
        try {
            assertTrue(user.equals(admin.login("andraca@gmail.com", "newpsswd")));
        } catch(LoginException exc){
            exc.printStackTrace();
            assertTrue(false);
        }
        admin.logout(user);

    }

    /**
     * Tester of upgrade() method.
     * Here we upgrade the state of a free user and then check the final state of the user.
     */
    @org.junit.Test
    public void upgrade() {

        //Now we try to upgrade
        assertTrue(admin.upgrade(user, "4911488244312234"));

    }

    /**
     * Tester of downgrade() method.
     * Same as in the previous method, but now downgrading the user.
     */
    @org.junit.Test
    public void downgrade() {
        Boolean flag = false;
        State st = new Free();

        //Now we try to downgrade
        admin.downgrade(user);

        //Check
        if (user.getState() instanceof Free) {
            flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of changeBirthday() method
     * With a registered user, we try to change the birthday date as adminUser.
     * Once it is changed, we obtain the new date and check if it is changed.
     */
    @org.junit.Test
    public void changeBirthday() {
        Date newdate = new Date(99,2,3);
        Boolean flag = false;

        admin.changeBirthday(newdate, user);
        if(user.getBirthday() == newdate) {
            flag = true;
        }
        assertTrue(flag);
    }

    /**
     * Tester of changeProfilePicture() method
     * With a registered user, we try to change his/her profile picture as adminUser.
     * Once it is changed, we obtain the new profile picture and check if it is changed.
     */
    @org.junit.Test
    public void changeProfilePicture() {
        Boolean flag = false;
            admin.changeProfilePicture(musicPath, this.user);

            assertTrue((new File(imagePath + this.user.getId())).exists());
    }

    /**
     * Tester of changeArtisticName() method
     * With a registered user, we try to change his/her artistic name as adminUser.
     * Once it is changed, we obtain the new artistic name and check if it is changed.
     */
    @org.junit.Test
    public void changeArtisticName() {
        Boolean flag = false;

        admin.changeArtisticName("newartisticname", user);
        if (user.getArtisticname() == "newartisticname") {
            flag = true;
        }
        assertTrue(flag);

    }
}