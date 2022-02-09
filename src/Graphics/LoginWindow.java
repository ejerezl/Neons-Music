package Graphics;

import App.App;
import Music.*;
import Users.AdminUser;
import Users.Administrator;
import Users.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Manifest;

/**
 * Class LoginWindow. Class in charge of implementing the methods to make the login screen work.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class LoginWindow extends JFrame {
    private LoginRightPanel loginRightPanel;
    private JPanel loginLeftPanel;
    private App app;

    public LoginWindow() {
        super();
        Container container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        if(App.getInstance()== null){
            try{
                app = null;
                if(Files.exists(Files.createTempFile(App.configPath, ""))){
                    app = App.loadData();
                }
                if(app == null){
                    app = new App();
                }
            }catch (IOException exc){
                app = new App();
            }
        }else{
            app = App.getInstance();
        }

        this.addWindowListener(new SaveDataOnClose(app));

        loginLeftPanel = new LoginLeftPanel(this);
        loginRightPanel = new LoginRightPanel(this,true, null, null);


        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        this.add(loginLeftPanel, constraints);

        constraints.gridx = 1;
        this.add(loginRightPanel, constraints);
        //container.add(loginLeftPanel);
        //container.add(loginRightPanel);

        this.setUndecorated(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        JOptionPane.showMessageDialog(getParent(), "We have prepared a demo! You can login with user demo password demo to check out the user experience.\n or log in with user admindemo and password admindemo to check out the administrator experience", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void succesFullLogin(){
        this.dispose();

        new HomeFrame(app);
        if(App.demo){
            JOptionPane.showMessageDialog(getParent(), "Welcome to Neons! Here we will show you around the app to get you comfortable with it.\n" +
                    " First click on the settings icon", "Demo", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void forgottenPassword() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        this.remove(loginRightPanel);
        loginRightPanel = new LoginRightPanel(this);

        this.add(loginRightPanel, constraints);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void register(String mail, String password){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        this.remove(loginRightPanel);

        loginRightPanel = new LoginRightPanel(this, false, mail, password);

        this.add(loginRightPanel, constraints);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void adminlogin(){
        this.dispose();
        app.isAdminLogged = true;
        new AdminFrame(app);
        if(app.adminDemo) {
            JOptionPane.showMessageDialog(getParent(), "Welcome to Neons! Here we will show you what the Administrator of the App can do\n" +
                    " First try to change between UPLOADED and REPORTED songs", "Demo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void login(){
        //this.getContentPane().remove(loginRightPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        this.remove(loginRightPanel);
        loginRightPanel = new LoginRightPanel(this, true, null, null);

        //this.getContentPane().add(loginRightPanel);
        this.add(loginRightPanel, constraints);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void questions(String name, String artisticName, String birthday){
        loginRightPanel.change(name, artisticName, birthday);
    }
    public static void main(String[] args) throws FileNotFoundException {
        new LoginWindow();
    }
}
