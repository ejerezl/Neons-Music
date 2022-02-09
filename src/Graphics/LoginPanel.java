package Graphics;


import App.App;
import Exceptions.LoginException;
import Users.AdminUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Class LoginPanel. Class that contains all the code to create the login screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class LoginPanel extends JPanel {
    private LoginWindow container;

    public Color startColor = Color.getHSBColor(0.750f, 0.54f, 0.1f);
    public Color endColor = Color.getHSBColor(0.780f, 0.5f, 0.4f);
    public int GradientFocus = 500;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, endColor, GradientFocus, h, startColor);;

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        //g2d.dispose();
    }

    public LoginPanel(final LoginWindow container) {
        this.container = container;
        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Pintgram-Regular.ttf"));
        }catch (Exception exc){
            font = Font.getFont("Arial");
        }

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel login = new JLabel("LOGIN");
        login.setForeground(Color.WHITE);
        login.setFont(font.deriveFont(Font.PLAIN, 40));

        JTextField userTextField = new JTextField("demo");
        userTextField.setColumns(15);
        JPasswordField passwordField = new JPasswordField("demo");
        passwordField.setColumns(15);

        JLabel email = new JLabel("Email");
        email.setForeground(Color.WHITE);
        JLabel passwrd = new JLabel("Password");
        passwrd.setForeground(Color.WHITE);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton forgottenPassword = new JButton("Forgotten password");
        JLabel forgottenpass = new JLabel("Have you forgotten your password?");

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(70,0,0,0);
        this.add(login, constraints);

        constraints.insets = new Insets(120,0,0,0);
        this.add(email, constraints);

        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.insets = new Insets(150,0,0,0);
        this.add(userTextField, constraints);

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(180,0,0,0);
        this.add(passwrd, constraints);

        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.insets = new Insets(210,0,0,0);
        this.add(passwordField, constraints);

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(250,0,200,90);
        this.add(loginButton, constraints);

        constraints.insets = new Insets(250,80,200,0);
        this.add(registerButton, constraints);

        constraints.insets = new Insets(300,0,150,0);
        this.add(forgottenpass, constraints);

        constraints.insets = new Insets(330,0,100,0);
        this.add(forgottenPassword, constraints);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userTextField.getText().isEmpty()){
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to login as anonymous","Warning",JOptionPane.WARNING_MESSAGE);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        container.succesFullLogin();
                        return;
                    }else{
                        return;
                    }
                }
                if (passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a password", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                App.demo = userTextField.getText().equals("demo");

                if(userTextField.getText().equals("admindemo")) {
                    App.adminDemo = true;
                    container.adminlogin();
                    return;
                }

                if(userTextField.getText().equals("admin")){

                    if(passwordField.getText().equals(App.getAdministrator().getPassword())){
                        container.adminlogin();
                    }else{
                        JOptionPane.showMessageDialog(getParent(), "Wrong password for administrator", "Authentication error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }

                try {
                    AdminUser.getInstance().login(userTextField.getText(), passwordField.getText());
                } catch (LoginException exc) {
                    JOptionPane.showMessageDialog(null, "Wrong username/password", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    exc.printStackTrace();
                    return;
                }
                container.succesFullLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter text in both fields", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(AdminUser.getInstance().userExists(userTextField.getText())){
                    JOptionPane.showMessageDialog(null, "Please There is already an user with this email, have you forgotten your password?", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                container.register(userTextField.getText(), passwordField.getText());
            }
        });

        forgottenPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.forgottenPassword();
            }
        });


    }

}
