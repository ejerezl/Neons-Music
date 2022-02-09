package Graphics;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Class LoginRightPanel. Class made with the purpose of changing panels in the login screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class LoginRightPanel extends JPanel {
    private JPanel top;
    private JPanel center;
    private JPanel bottom;
    private JPanel right;
    private JPanel left;
    private LoginWindow container;
    private CardLayout cardLayout;
    private JPanel registerPanel;
    private SignUp questionsPanel;
    public Color StartColor = Color.getHSBColor(0.780f, 0.5f, 0.4f);
    public Color endColor = Color.getHSBColor(0.750f, 0.54f, 0.1f);
    public int GradientFocus = 500;

    public LoginRightPanel(LoginWindow container) {
        this.container = container;
        GridBagLayout grid = new GridBagLayout();
        this.setLayout(new BorderLayout());
        center = new ForgottenPasswordPanel(container);

        this.add(center);

        this.setPreferredSize(new Dimension(600, 600));
        this.setVisible(true);
    }

    public LoginRightPanel(LoginWindow container, boolean login, String email, String password)  {
        this.container = container;
        GridBagLayout grid = new GridBagLayout();
        this.setLayout(new BorderLayout());




        if(login){
            center = new LoginPanel(container);
        }else {
            cardLayout = new CardLayout();
            center = new JPanel(cardLayout);
            registerPanel = new RegisterPanel(container);
            questionsPanel  = new SignUp(container, email, password);
            center.add(registerPanel, "register");
            center.add(questionsPanel, "questions");
            cardLayout.show(center, "register");
        }

        this.add(center);


        this.setPreferredSize(new Dimension(600, 600));
        this.setMaximumSize(new Dimension(400,400));
        this.setVisible(true);
    }


    /**
     * Method with the purpose of changing panels in the register screen
     * @param name
     * @param artisticName
     * @param birthday
     */
    public void change(String name, String artisticName, String birthday){
        cardLayout.show(center, "questions");
        questionsPanel.updateInfo(name, artisticName, birthday);
    }
}
