package Graphics;

import App.App;
import Exceptions.LoginException;
import Users.AdminUser;
import Users.AuthenticationController;
import Users.Premium;
import es.uam.eps.padsof.telecard.OrderRejectedException;
import es.uam.eps.padsof.telecard.TeleChargeAndPaySystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class Settings. Class that implement the settings panel.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class Settings extends JPanel{
    ImageIcon profile;
    Image image;
    JLabel profilePicture;
    public Settings() {
        super();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        GridBagConstraints cpanels = new GridBagConstraints();

        //Size of the matrix
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        cpanels.weightx = 1;
        cpanels.weighty = 1;

        //Create the panels needed for this window
        JPanel settings = new JPanel(new GridBagLayout());
        JPanel author = new JPanel(new GridBagLayout());

        //Change the color of the Panel background
        settings.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        settings.setBorder(BorderFactory.createLineBorder(Color.getHSBColor(0.750f, 0.20f, 0.7f)));
        author.setBackground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        JLabel home = new JLabel(new ImageIcon("img/home.png"));
        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomeFrame.getInstance().change("home");
            }
        });
        profile =  new ImageIcon(App.getInstance().getLogged().getProfilePath());
        image = profile.getImage(); // transform it
        image = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        profilePicture = new JLabel (new ImageIcon(image));
        JLabel artisticName = new JLabel(App.getInstance().getLogged().getArtisticname());
        JLabel realName = new JLabel(App.getInstance().getLogged().getName());
        JLabel birthday = new JLabel("Birthday: "+dateFormat.format(App.getInstance().getLogged().getBirthday()));
        JLabel email = new JLabel(App.getInstance().getLogged().getMail());

        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(2,2,0,0);
        author.add(home, constraints);

        constraints.insets = new Insets(70,10,0,0);
        author.add(profilePicture, constraints);

        artisticName.setForeground(Color.WHITE);
        artisticName.setFont(new Font("Monospaced", Font.BOLD, 20));
        constraints.insets = new Insets(68, 150, 0,0);
        author.add(artisticName, constraints);

        realName.setForeground(Color.WHITE);
        realName.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(90, 150, 0,0);
        author.add(realName, constraints);

        birthday.setForeground(Color.WHITE);
        birthday.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(108, 150, 0,0);
        author.add(birthday, constraints);

        email.setForeground(Color.WHITE);
        email.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(126, 150, 0,0);
        author.add(email, constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;

        JLabel premium = new JLabel();
        JLabel joinPremium = new JLabel("Want to join the premium club?");
        JButton join = new JButton("Open with Paypal");
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String card;
                do{
                    card= JOptionPane.showInputDialog(getParent(), "Please insert a valid card number");
                }while(!AdminUser.getInstance().upgrade(App.getLogged(), card));
                premium.setText("Premium:    YES");
                validate();
                repaint();

                JOptionPane.showMessageDialog(getParent(), "Great! You're now a premium user! Now, you can change \n your password, " +
                        "your artistic name, or your birthdate by typing and then clicking in the tick button. Try it!", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JLabel newPass = new JLabel("New Password: ");
        TextField pass1 = new TextField("Type here...");
        TextField pass2 = new TextField("Repeat here...");
        JLabel newArtisticName = new JLabel("Change artistic name: ");
        TextField name1 = new TextField("Type here...");
        TextField name2 = new TextField("Repeat here...");
        JLabel newBirthday = new JLabel("Change birthday date: mm/dd/yyyy");
        TextField date1 = new TextField("Type here...");
        TextField date2 = new TextField("Repeat here...");
        JLabel ok1 = new JLabel(new ImageIcon("img/ok_opt.png"));
        ok1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(pass1.getText().isEmpty()){
                    return;
                }
                if(pass1.getText().equals(pass2.getText())){
                    AuthenticationController.getInstance().setPassword(App.getLogged().getMail(), pass1.getText());
                    JOptionPane.showMessageDialog(getParent(), "Password changed succesfully", "Congratulations", JOptionPane.OK_OPTION);
                }

                JOptionPane.showMessageDialog(getParent(), "Awesome! Let's try and change your profile picture.\n Click on 'Select from your computer'" +
                        " and upload a pic.", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JLabel ok2 = new JLabel(new ImageIcon("img/ok_opt.png"));
        ok2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(name1.getText().isEmpty()){
                    return;
                }
                if(name1.getText().equals(name2.getText())){
                    App.getLogged().setArtisticname(name1.getText());
                    artisticName.setText(name1.getText());
                    validate();
                    repaint();
                }
                JOptionPane.showMessageDialog(getParent(), "Awesome! Let's try and change your profile picture.\n Click on 'Select from your computer'" +
                        " and upload a pic.", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JLabel ok3 = new JLabel(new ImageIcon("img/ok_opt.png"));
        ok3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(date1.getText().isEmpty()){
                    return;
                }
                if(date1.getText().equals(date2.getText())){
                    App.getLogged().setBirthday(new Date(date1.getText()));
                    birthday.setText(date1.getText());
                    validate();
                    repaint();
                }
                JOptionPane.showMessageDialog(getParent(), "Awesome! Let's try and change your profile picture.\n Click on 'Select from your computer' or the folder" +
                        " and upload a pic.", "Demo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        if (App.getInstance().getLogged().getState() instanceof Premium) {
            premium.setText("Premium:    YES");
            constraints.insets = new Insets(20, 10, 0, 0);
            premium.setForeground(Color.WHITE);
            premium.setFont(new Font("Monospaced", Font.PLAIN, 20));
            settings.add(premium, constraints);
        } else {
            premium.setText("Premium:    NO");
            constraints.insets = new Insets(20, 10, 0, 0);
            premium.setForeground(Color.WHITE);
            premium.setFont(new Font("Monospaced", Font.PLAIN, 20));
            settings.add(premium, constraints);
            joinPremium.setForeground(Color.WHITE);
            joinPremium.setFont(new Font("Monospaced", Font.PLAIN, 20));
            constraints.insets = new Insets(20, 10, 0, 0);
            constraints.anchor = GridBagConstraints.PAGE_START;
            settings.add(joinPremium, constraints);

            join.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
            join.setFont(new Font("Monospaced", Font.PLAIN, 15));
            constraints.anchor = GridBagConstraints.FIRST_LINE_END;
            constraints.insets = new Insets(20, 0, 0, 200);
            settings.add(join, constraints);
        }





        newPass.setForeground(Color.WHITE);
        newPass.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(70, 10, 0, 0);
        settings.add(newPass, constraints);

        pass1.setColumns(20);
        pass1.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        pass1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(70, 0, 0, 0);
        settings.add(pass1, constraints);

        pass2.setColumns(20);
        pass2.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        pass2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(70, 0, 0, 100);
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        settings.add(pass2, constraints);


        newArtisticName.setForeground(Color.WHITE);
        newArtisticName.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(120, 10, 0, 0);
        settings.add(newArtisticName, constraints);

        name1.setColumns(20);
        name1.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        constraints.anchor = GridBagConstraints.PAGE_START;
        name1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(120, 0, 0, 0);
        settings.add(name1, constraints);

        name2.setColumns(20);
        name2.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        name2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(120, 0, 0, 100);
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        settings.add(name2, constraints);

        newBirthday.setForeground(Color.WHITE);
        newBirthday.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(170, 10, 0, 0);
        settings.add(newBirthday, constraints);

        date1.setColumns(20);
        date1.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        constraints.anchor = GridBagConstraints.PAGE_START;
        date1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(170, 0, 0, 0);
        settings.add(date1, constraints);

        date2.setColumns(20);
        date2.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        date2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.insets = new Insets(170, 0, 0, 100);
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        settings.add(date2, constraints);

        constraints.insets = new Insets(170, 0, 0, 15);
        settings.add(ok3, constraints);

        constraints.insets = new Insets(120, 0, 0, 15);
        settings.add(ok2, constraints);

        constraints.insets = new Insets(70, 0, 0, 15);
        settings.add(ok1, constraints);

        JLabel uploadSong = new JLabel("Upload song/s: ");
        JButton upload = new JButton("Click here...");
        upload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HomeFrame.getInstance().change("upload");

            }
        });
        JLabel changeProfilePicture = new JLabel("Change profile picture: ");
        JLabel folder = new JLabel(new ImageIcon("img/folder_opt.png"));
        folder.addMouseListener(new ChangePicture());
        JLabel folderText = new JLabel("Select from your computer ...");
        folderText.addMouseListener(new ChangePicture());

        uploadSong.setForeground(Color.WHITE);
        uploadSong.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(220, 10, 0, 0);
        settings.add(uploadSong, constraints);

        upload.setForeground(Color.getHSBColor(0.750f, 0.54f, 0.1f));
        upload.setFont(new Font("Monospaced", Font.PLAIN, 15));
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(220, 0, 0, 0);
        settings.add(upload, constraints);

        changeProfilePicture.setForeground(Color.WHITE);
        changeProfilePicture.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(270, 10, 0, 0);
        settings.add(changeProfilePicture, constraints);

        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(270, 0, 0, 400);
        settings.add(folder, constraints);

        folderText.setForeground(Color.WHITE);
        folderText.setFont(new Font("Monospaced", Font.PLAIN, 20));
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(270, 40, 0, 0);
        settings.add(folderText, constraints);


        author.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        cpanels.fill = GridBagConstraints.BOTH;
        cpanels.gridx = 0;
        cpanels.gridy = 0;
        this.add(author, cpanels);

        cpanels.ipady = 300;
        cpanels.gridy = 1;
        this.add(settings, cpanels);
        this.setMinimumSize(new Dimension(1300, 700));
        this.setVisible(true);


    }

    /**
     * Class ChangePicture. Allows the users to change their profile pictures.
     */
    public class ChangePicture extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(getParent());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try{
                    App.getLogged().setProfilePicture(file.getPath());
                }catch (IOException exc){
                    exc.printStackTrace();
                }
            } else {
                System.out.println("fail");
            }
            JOptionPane.showMessageDialog(getParent(), "There you go! To finish with this window, why don't you try\n and upload " +
                    "a song? Click in the button!\n (Or you can go back to the home menu by clicking the \n home button of the top left corner", "Demo", JOptionPane.INFORMATION_MESSAGE);

            profile =  new ImageIcon(App.getInstance().getLogged().getProfilePath());
            image = profile.getImage(); // transform it
            image = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            profilePicture.setIcon(new ImageIcon(image));
            validate();
            repaint();
        }
    }

}