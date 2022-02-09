package Users;

import App.App;
import Exceptions.BannedUserException;
import Exceptions.LoginException;
import Exceptions.WrongPasswordException;
import Exceptions.WrongUserException;
import Music.Playback;
import es.uam.eps.padsof.telecard.FailedInternetConnectionException;
import es.uam.eps.padsof.telecard.InvalidCardNumberException;
import es.uam.eps.padsof.telecard.OrderRejectedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This class implements the AdminUser of the app, who is in charge
 * of controlling activities made by an user as the log in, log out, changes
 * of password, profile picture, etc...
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AdminUser implements Serializable {
    private static AdminUser instance = new AdminUser();
    private HashMap<String, User> registeredUsers = new HashMap<String, User>();
    private AuthenticationController authenticationController = AuthenticationController.getInstance();
    private int counter = 0;
    private AdminUser(){ }

    /**
     * returns the instance of the AdminUser
     * @return instance of AdminUser
     */
    public static AdminUser getInstance(){
        return instance;
    }

    /**
     * Sets the instance of the AdminUser to be used by other classes
     * @return instance of AdminUser
     */
    public static void setInstance(AdminUser adminUser){
        instance = adminUser;
    }

    public AuthenticationController getAuthenticationController(){
        return authenticationController;
    }
    /**
     * This method develop the registration of a user. With the given parameter it creates a new user(if it's possible)
     * and returns it.
     * @param name
     * @param artisticName
     * @param mail
     * @param birthday
     * @param answers
     * @param psswd
     * @return the user created.
     */
    public User register(String name, String artisticName, String mail, Date birthday, ArrayList<String> answers, String psswd) {
        User user = new User(name, artisticName, mail, birthday);
        registeredUsers.put(mail, user);
        (AuthenticationController.getInstance()).addUser(mail, psswd, answers);
        String emailBody = "Thank you for registering at Neons Music. "+"<br> You will get notifications once your songs get validated"
                + "<br><br> Regards, <br>Neons Team";
        try {
            user.getMailNotifier().sendEmailNotification(emailBody);
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return user;
    }

    /**
     * Getter of the counter.
     * @return the counter
     */
    public int getCounter(){
        return counter;
    }

    /**
     * Update the counter of number id and returns it updated.
     * @return the new value of the counter.
     */
    public int updateCounter(){
        return ++counter;
    }

    /**
     * This method develop the log in process.
     * Given a mail and a password, the method tries to log in and search the user that is trying
     * to log in. If there's any problem this method will notice it.
     * @param mail
     * @param pswd
     * @return the user who wants to log in
     * @throws LoginException
     */
    public User login (String mail, String pswd) throws LoginException {
        AuthenticationController authenticationController = AuthenticationController.getInstance();
        if(registeredUsers.containsKey(mail) == false){
            throw new WrongUserException("User not found");
        }
        try{
            if(registeredUsers.get(mail).getBanned()){
                int[] d = Playback.differenceBetweenDates(registeredUsers.get(mail).getBannedDate(), new Date());
                if((d[2] < 0) || (d[2] == 0 && d[1] < 0)){
                    registeredUsers.get(mail).setBanned(false);
                }else{
                    throw new BannedUserException("Banned user");
                }
            }
            if(authenticationController.checkCredentials(mail,pswd)) {
                if(registeredUsers.get(mail).getState() instanceof Premium){
                    int[] d = Playback.differenceBetweenDates(
                            ((Premium) registeredUsers.get(mail).getState()).getSubscriptionDate(), new Date());
                    if((d[2] < 0) || (d[2] == 0 && d[1] < 0)){
                        registeredUsers.get(mail).setState(new Free());
                    }
                }
                if(registeredUsers.get(mail).getIsAdult() == false){
                    int[] d = Playback.differenceBetweenDates(registeredUsers.get(mail).getBirthday(), new Date());
                    if(d[2] < - Administrator.getMinorAge()){
                        registeredUsers.get(mail).setIsAdult(true);
                    }else if(d[2] == -Administrator.getMinorAge() && d[1] < 0){
                        registeredUsers.get(mail).setIsAdult(true);
                    }else if(d[2] == -Administrator.getMinorAge() && d[1] == 0 && d[0] <= 0){
                        registeredUsers.get(mail).setIsAdult(true);
                    }
                }
                /*top = App.getAdministrator().getTopAuthors(new Date(), 1);

                if(top != null && top.contains(App.setLogged(registeredUsers.get(mail)))) {
                    if(App.setLogged(registeredUsers.get(mail)).getState() instanceof Premium) {
                        ((Premium) App.setLogged(registeredUsers.get(mail)).getState()).setSubscriptionDate(new Date());
                    } else {
                        App.setLogged(registeredUsers.get(mail)).setState(new Premium(new Date(), "4911488391123456"));
                    }
                }*/
                return App.setLogged(registeredUsers.get(mail));
            }
        }catch (WrongUserException exc) {
            throw exc;
        }/*catch (OrderRejectedException exc) {
            exc.getStackTrace();
            return null;
        }*/
        throw new WrongPasswordException("wrong password");
    }

    /**
     * This method implements the log out procces by a user received as a parameter.
     * @param user
     */
    public void logout( User user)  {
          App.setLogged(null);
    }

    public boolean userExists(String user){
        return registeredUsers.containsKey(user);
    }
    /**
     * Process of restoring the password of the user which credentials are given as parameters,
     * with the new password.
     *
     * @param mail
     * @param newPassword
     * @param answers
     */
    public boolean restorePswd (String mail, String newPassword, ArrayList<String> answers){
        AuthenticationController authenticationController = AuthenticationController.getInstance();
        if(authenticationController.checkQuestions(mail, answers)){
                authenticationController.setPassword(mail, newPassword);
                return true;
        }
        return false;
    }

    /**
     * Process of upgrading the state of a user, given as parameter, with its cardNumber
     * to carry out the payment.
     * @param user
     * @param cardNumber
     * @return if the action was made successfully.
     */
    public boolean upgrade (User user, String cardNumber){
        try{
            user.setState(new Premium(new Date(), cardNumber));
            return true;
        }catch (InvalidCardNumberException exc){
            System.out.println("Invalidad card number");
        }catch (FailedInternetConnectionException exc){
            System.out.println("No internet connection");
        }catch (OrderRejectedException exc){
            System.out.println("Unknown exception");
        }
        return false;
    }

    /**
     * Process of downgrading a user given as parameter.
     * @param user
     */
    public void downgrade (User user){
        user.setState(new Free());
    }

    /**
     * Method that change the Birthday Date of the a user given as parameter.
     * @param newbirthday
     * @param user
     */
    public void changeBirthday (Date newbirthday, User user){
        user.setBirthday(newbirthday);
    }

    /**
     * Method that change the profile picture of the a user given as parameter.
     * @param path
     * @param user
     */
    public void changeProfilePicture (String path, User user){
        try{
            user.setProfilePicture (path);
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

    /**
     * Method that change the artistic name of the a user given as parameter.
     * @param artisticname
     * @param user
     */
    public void changeArtisticName (String artisticname, User user){
        user.setArtisticname (artisticname);
    }

    /**
     * Method that returns a copy of a map with <mail, User> elements
     * for that users that are registered.
     * @return the users registered
     */
    public HashMap<String, User> getUsers(){
        return (HashMap<String, User>) registeredUsers.clone();
    }
}
