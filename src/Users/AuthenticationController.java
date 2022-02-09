package Users;

import Exceptions.LoginException;
import Exceptions.WrongUserException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class implements the AuthenticationControlller of the app, who is in charge
 * of controlling activities as checking the credentials of someone, checking the security
 * question of a user...
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class AuthenticationController implements Serializable{

    private static AuthenticationController instance = null;
    private static final String pass_file = "passwords";
    private static final String questions_file = "questions";
    private HashMap<String, String> passwords = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> questions = new HashMap<String, ArrayList<String>>();


    /**
     * Static constructor of authentication Controller that returns its instance
     * @return the instance of the authentication controller.
     */
    public static AuthenticationController getInstance() {
        if(instance == null)
            instance = new AuthenticationController();
        return instance;
    }
    /**
     * Set the instance of the class to be used by other classes
     */
    public static void setInstance(AuthenticationController authenticationController) {
        instance = authenticationController;
    }

    /**
     * Private constructor.
     */
    private AuthenticationController(){
    }

    /**
     * Method that returns a HashMap with pairs <mail, password> with password encoded.
     * @return passwords of the users registered.
     */
    public  HashMap<String, String> getPasswords() {
        return this.passwords;
    }

    /**
     * Method that returns a HashMap with pairs <mail, answer of questions> where the
     * answer of the questions are given encoded and in an arrayList.
     * @return answers of security questions for each registered user.
     */
    public HashMap<String, ArrayList<String>> getQuestions() {
        return this.questions;
    }

    /**
     * Function to read the passwords and answwers from security questions from a file
     */
    public void readPasswords() {
        passwords = (HashMap<String, String>)initialize(pass_file);
        questions = (HashMap<String, ArrayList<String>>) initialize(questions_file);
    }

    /**
     * Method to initialize a HashMap reading from a file
     * @param path
     * @return the resulting HashMap
     */
    private HashMap initialize(String path) {
        File file = new File(path);
        Object result;
        if(file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
                result = objectInputStream.readObject();
                objectInputStream.close();
                return (HashMap) result;
            } catch (IOException exc) {
                System.out.println("Error reading file");
            } catch (ClassNotFoundException exc) {
                System.out.println("Incompatible software");
            }
        }
        return new HashMap<>();
    }

    /**
     * Method to check the credentials of a person that is trying to access.
     * @param mail
     * @param password
     * @return true if the credentials are correct, false in other case.
     * @throws LoginException
     */
    public boolean checkCredentials(String mail, String password) throws LoginException {

        try {
            String encoded_hash = hash_encode(password);
            String result = passwords.get(mail);
            return encoded_hash.equals(result);
        } catch(Exception exc) {
            exc.printStackTrace();
            throw new WrongUserException("User not found");
        }
    }

    /**
     * Method that add a new registered user to the system, saving his password and
     * security answers.
     * @param mail
     * @param password
     * @param answers
     */
    public void addUser(String mail, String password, ArrayList<String> answers) {
        String result;
        String password_encoded_hash = null;
        ArrayList<String> encodedAnswers = new ArrayList<>();
        password_encoded_hash = hash_encode(password);
        passwords.put(mail, password_encoded_hash);
        answers.forEach(answer -> encodedAnswers.add(hash_encode(answer)));
        questions.put(mail, encodedAnswers);
    }

    /**
     * Method to change the password of the user which mail is given as a parameter.
     * @param mail
     * @param password
     */
    public void setPassword(String mail, String password) {
        if (passwords.containsKey(mail)) {
            passwords.replace(mail, hash_encode(password));
        }

    }

    /**
     * Method to check if the answers given are correct with the anser of the user
     * which mail is given as a parameter.
     * @param mail
     * @param answers
     * @return True if the answers are correct, false in other case.
     */
    public boolean checkQuestions(String mail, ArrayList<String> answers){
        ArrayList<String> trueAnswers;
        ArrayList<String> encodedAnswers = new ArrayList<>();
        answers.forEach(answer -> encodedAnswers.add(hash_encode(answer)));
        if(questions.containsKey(mail)) {
            trueAnswers = questions.get(mail);
            Iterator<String> trueanswers_iterator = trueAnswers.iterator();
            Iterator<String> givenanswers_iterator = encodedAnswers.iterator();
            while(trueanswers_iterator.hasNext() && givenanswers_iterator.hasNext()){
                if(trueanswers_iterator.next().equals(givenanswers_iterator.next()) == false){
                    return false;
                }
            }
            if(trueanswers_iterator.hasNext()){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    /**
     * Method to encode a message
     * @param message
     * @return the encoded message
     */
    public String hash_encode(String message){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }catch (java.security.NoSuchAlgorithmException exc){
            return message;
        }

    }

    /**
     * Method to save the passwords and security answers in a file.
     */
    public void savePasswords(){
        try{
            File password_file = new File(pass_file);
            File question_file = new File(questions_file);
            password_file.delete();
            question_file.delete();
            ObjectOutputStream pass_oos = new ObjectOutputStream(new FileOutputStream(pass_file));
            ObjectOutputStream questions_oos = new ObjectOutputStream(new FileOutputStream(questions_file));
            pass_oos.writeObject(passwords);
            pass_oos.close();
            questions_oos.writeObject(questions);
            questions_oos.close();
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }


    /**
     * Method that returns a user which mail is given as a parameter.
     * @param mail
     * @return the user
     * @throws LoginException
     */
    public User getUser(String mail) throws LoginException {
        return AdminUser.getInstance().login(mail, passwords.get(mail));
    }


}
