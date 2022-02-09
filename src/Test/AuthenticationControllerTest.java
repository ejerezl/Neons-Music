package Test;

import Exceptions.LoginException;
import Users.AuthenticationController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * Class to test the functionality of AuthenticationController class.
 * Each method is implemented to test one method of the AuthenticationController
 * class.
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class
AuthenticationControllerTest {

    private AuthenticationController auth;

    /**
     * Done before every test
     * @throws Exception
     */
    @Before
    public void setUp() {
        auth = AuthenticationController.getInstance();
        auth.readPasswords();
        ArrayList<String> answers = new ArrayList<>();
        answers.add("firstAnswer");

        //Now we add an user
        auth.addUser("esther@gmail.com","psswrd", answers);
        auth.savePasswords(); //Save the data
    }

    /**
     * Done after every test
     * @throws Exception
     */
    @After
    public void tearDown() {
        /*Delete passwords and questions files in case we have saved*/
        File password = new File("passwords");
        password.delete(); //We don't care what it returns because either file doesn't exist or was properly deleted are valid for us

        File questions = new File("questions");
        questions.delete();
    }

    /**
     * Tester of readEmptyPasswords()
     */
    @Test
    public void readEmptyPasswords() {
        File password = new File("passwords");
        password.delete(); //We don't care what it returns because either file doesn't exist or was properly deleted are valid for us

        File questions = new File("questions");
        questions.delete();

        auth.readPasswords();
        //Now it should be empty so we check
        assertTrue((auth.getPasswords()).isEmpty());
        assertTrue((auth.getQuestions()).isEmpty());
    }

    /**
     * Tester of readPasswords.
     * From a file where the passwords where saved, we read it and check if it has been done correctly.
     */
    @Test
    public void readPasswords() {
        auth.savePasswords(); //Now the files of passwords and questions should have changed
        auth.readPasswords();//Reading data, it shouldn't be empty

        //Check if entries passwords and questions are working correctly
        for (Map.Entry<String, String> item:auth.getPasswords().entrySet()) {
            //mail and password should be the same as introduced
            assertEquals("esther@gmail.com", item.getKey());
            assertEquals(auth.hash_encode("psswrd"), item.getValue());
            //But it also should notice that another mail and password are not the ame
            assertNotSame("alex@gmail.com", item.getKey());
            assertNotSame(auth.hash_encode("bye"), item.getValue());
        }
        for (Map.Entry<String, ArrayList<String>> item:auth.getQuestions().entrySet()) {
            assertEquals("esther@gmail.com", item.getKey());
            assertNotSame("alex@gmail.com", item.getKey());
            for (String answer:item.getValue()) {
                assertEquals(auth.hash_encode("firstAnswer"), answer);
                assertNotSame(auth.hash_encode("anotherAnswer"), answer);
            }
        }
    }

    /**
     * Check in our files if the credentials of a user are correct.
     * If are correct, they should be in our files and data.
     */
    @Test
    public void checkCredentials() {
        try {
        //Now we just check if credentials of the user created are okey(they should)
        assertTrue(auth.checkCredentials("esther@gmail.com", "psswrd"));
        //Check too if credentials of a non-user are true or false
        assertFalse(auth.checkCredentials("esther@gmail.com", "anotherPsswrd"));
            } catch (LoginException exc){
            exc.getStackTrace();
        }

    }

    /**
     * Tester of addUser()
     * Here we add a new user in our data, and then check if it was saved as expected.
     */
    @Test
    public void addUser() {
        ArrayList<String> answers = new ArrayList<>();
        answers.add("firstOne");
        String mail = "eva@gmail.com";
        Boolean flag = false;
        /* Taking into consideration the before method, there's already a user.
         * What we are going to do is add another one and check if it is added properly
         */
        auth.addUser(mail,"passw", answers);
        /*We only want to check if data of this second user added is okey*/
        for(Map.Entry<String, String> item:auth.getPasswords().entrySet()) {
            //mail and password should be the same as introduced
            if(mail.equals(item.getKey())){
                flag = true;
                assertEquals(auth.hash_encode("passw"), item.getValue());
            }
        }
        assertTrue(flag);
        flag = false;
        for(Map.Entry<String, ArrayList<String>> item:auth.getQuestions().entrySet()) {
            if(mail.equals(item.getKey())){
                flag = true;
                for(String answer:item.getValue()){
                    assertEquals(auth.hash_encode("firstOne"), answer);
                    assertNotSame(auth.hash_encode("anotherAnswer"), answer);
                }
            }
        }
        assertTrue(flag);
    }

    /**
     * Tester setPassword().
     * With a saved user, we change the password callling to the method and ten check
     * if in our data the updates have changed.
     */
    @Test
    public void setPassword() {
        //Now we change the password
        auth.setPassword("esther@gmail.com", "newpsswrd");

        //So now new password would be the correct one, and not the previous one.
        try {
            assertTrue(auth.checkCredentials("esther@gmail.com", "newpsswrd"));
            assertFalse(auth.checkCredentials("esther@gmail.com", "psswrd"));
        } catch(LoginException exc){
            exc.getStackTrace();
        }

    }

    /**
     * Tester of checkQuestions()
     * Here we check if with a correct and incorrect answers the method answer as expected.
     */
    @Test
    public void checkQuestions() {
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> answers2 = new ArrayList<>();
        answers2.add("one");
        answers2.add("two");
        answers.add("firstAnswer");
        answers.add("secondAnswer");

        //We add another user with more that one answer to the question
        auth.addUser("alex@gmail.com","psswrd", answers);

        assertTrue(auth.checkQuestions("alex@gmail.com", answers));
        assertFalse(auth.checkQuestions("alex@gmail.com", answers2));

    }

}