package Users.Notifications;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import javax.mail.*;
import javax.mail.internet.*;
/**
 * Class used to write on behalf of the application. Currently only used to notify users about the result of their uploaded songs
 *
 * @author   Alejandro Andraca Gutierrez
 * @author   Eva Lacaba Nicolás
 * @author   Esther Jerez López
 * @since    05-03-2019
 */
public class MailNotifier implements Runnable{

    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;
    private String address;
    private boolean defaultNotifying;
    private Transport transport;
    private Semaphore semaphore;
    private Queue<String> message;

    /**
     * This method is used to send an email to the address stored in addres
     * The string message is added to a queue
     * @param message The message to be sent
     * @throws AddressException Exception in case of malformed address
     * @throws MessagingException Other exceptions with gmail servers
     */
    public void sendEmailNotification(String message) throws AddressException, MessagingException {
        if (defaultNotifying == false) {
            return;
        }
        try {
            semaphore.acquire();
            this.message.offer(message);
        } catch (Exception exc) {

        } finally {
            semaphore.release();
        }
        Thread networkSender = new Thread(this::run);
        networkSender.start();

    }

    /**
     * Only constructor of the class.
     *  Initializes the address parameter and gets all the properties
     *
     * @param address
     */
    public MailNotifier(String address) {
        this.defaultNotifying = true;
        this.address = address;
        this.semaphore = new Semaphore(1);
        this.message = new LinkedList<String>();
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
    }

    /**
     * Sets the boolean checked whether or not the user wants to receive email notifications
     * @param defaultNotifying
     */
    public void setDefaultNotifying(boolean defaultNotifying) {
        this.defaultNotifying = defaultNotifying;
    }

    /**
     * Runnable method only used to send the email
     * The string is retrieved from a queue. The queue is used so that the main thread is not stop during the
     * sending email as it can be slow.
     */
    public void run(){
        String content = null;
        try {
            semaphore.acquire();
            content = message.poll();
        }catch (Exception exc) {
        }finally {
            semaphore.release();
        }
        try{
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

            generateMailMessage.setSubject("Greetings from Neons...");
            generateMailMessage.setContent(content, "text/html");

            transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", "donotreplyneonsmusic@gmail.com", "!zd+;mKbMhqV&5df*py#");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        }catch (Exception exc){
            exc.printStackTrace();
        }finally {
            semaphore.release();
        }

    }
}
