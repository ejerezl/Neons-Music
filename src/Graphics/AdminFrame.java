package Graphics;

import App.App;
import Users.User;

import javax.swing.*;
import java.awt.*;

/**
 * Class AdminFrame. Class that contains the panels that make the admin graphical interface.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */

public class AdminFrame extends JFrame {
    JPanel statistics;
    AdminUser_Pending adminuserPending;
    JPanel container;
    CardLayout cardLayout;
    App app;
    public AdminFrame(App app){
        super();
        this.addWindowListener(new SaveDataOnClose(app));
        this.app = app;
        cardLayout = new CardLayout();
        statistics = new Statistics(this);
        adminuserPending = new AdminUser_Pending(this);
        container = new JPanel(cardLayout);
        container.add(statistics, "statistics");
        container.add(adminuserPending, "admin");
        cardLayout.show(container, "admin");
        container.setPreferredSize(new Dimension(1300, 700));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(container);
        this.pack();
        setVisible(true);
    }

    /**
     * Method used to change between panels contained in the frame
     * @param name
     */
    public void change(String name){
        if(name.equals("user") || name.equals("songs")){
            if(name.equals("user")){
                adminuserPending.change(0);
            }else {
                adminuserPending.change(1);
            }
            cardLayout.show(container, "admin");
        }else if(name.equals("statistics")){
            cardLayout.show(container, name);
        }
    }



}
