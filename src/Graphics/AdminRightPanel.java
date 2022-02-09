package Graphics;

import Music.Song;
import Users.AdminUser;
import Users.User;

import javax.swing.*;
import java.awt.*;

/**
 * Class AdminRightPanel. Class made to change between right panels (for users and songs) in an admin screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AdminRightPanel extends JPanel {
    AdminInfo adminInfo;
    AdminUserInfo adminUserInfo;
    CardLayout cardLayout;

    public AdminRightPanel(AdminUser_Pending adminUser_pending){
        super();
        adminUserInfo = null;
        adminInfo = new AdminInfo(adminUser_pending);
        cardLayout = new CardLayout();

        this.setLayout(cardLayout);
        this.add(adminInfo, "admininfo");
    }

    /**
     * Method to change between panels
     * @param song
     */
    public void changeContent(Song song){
        cardLayout.show(this, "admininfo");
        adminInfo.changeContent(song);
    }

    /**
     * Method to change between panels
     * @param type
     */
    public void change(boolean type){
        if(type){
            cardLayout.show(this, "admininfo");
        }else{
            cardLayout.show(this, "adminuserinfo");
        }
    }

    /**
     * Method to change between panels
     * @param user
     */
    public void changeContent(User user){
        if(adminUserInfo != null){
            this.remove(adminUserInfo);
        }
        adminUserInfo = new AdminUserInfo(user);
        this.add(adminUserInfo, "adminuserinfo");
        cardLayout.show(this, "adminuserinfo");
    }
}
