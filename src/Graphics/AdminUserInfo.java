package Graphics;

import App.App;
import Music.Album;
import Music.Song;
import Users.AdminUser;
import Users.Administrator;
import Users.Premium;
import Users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class AdminUserInfo. Class containing all the user information on the right side of the admin screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class AdminUserInfo extends JPanel {
    User user;
    AuthorPanel authorPanel;

    public AdminUserInfo(User user) {
        super();
        this.setLayout(new GridBagLayout());
        authorPanel = new AuthorPanel(user, null);
        JRadioButton rb1, rb2;
        JButton b;
        rb1 = new JRadioButton("PREMIUM");
        rb2 = new JRadioButton("FREE");

        rb1.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        rb1.setForeground(Color.WHITE);
        rb2.setForeground(Color.WHITE);
        rb2.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        rb1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        rb2.setFont(new Font("Monospaced", Font.PLAIN, 15));

        ButtonGroup bg = new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);
        b = new JButton("SAVE");

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rb1.isSelected()){
                    AdminUser.getInstance().upgrade(user, "0000000000000000");
                }
                if(rb2.isSelected()){
                    if(user.getState() instanceof Premium){
                        AdminUser.getInstance().downgrade(user);
                    }
                }

                if (App.adminDemo) {
                    JOptionPane.showMessageDialog(getParent(), "Well.. That's all! Hope you enjoyed it!", "Demo", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagConstraints cpanel = new GridBagConstraints();


        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(40, 0, 0, 200);
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        authorPanel.add(rb1, constraints);
        constraints.insets = new Insets(40, 0, 0, 100);
        authorPanel.add(rb2, constraints);
        constraints.insets = new Insets(40, 0, 0, 10);
        authorPanel.add(b, constraints);

        cpanel.gridx = 0;
        cpanel.gridy = 0;
        cpanel.weightx = 0.1;
        cpanel.weighty = 0.1;
        cpanel.fill = GridBagConstraints.BOTH;

        this.add(authorPanel, cpanel);
        this.setBackground(Color.PINK);
    }

}
