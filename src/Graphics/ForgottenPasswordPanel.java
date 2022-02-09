package Graphics;

import Users.AdminUser;
import Users.AuthenticationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Class ForgottenPasswordPanel. Class that contains all the information to create the forgotten password window.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class ForgottenPasswordPanel extends JPanel {
    LoginWindow container;

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
    public ForgottenPasswordPanel(LoginWindow container) {
        JLabel jLabel;
        TextField mail;
        TextField answer1;
        TextField answer2;
        TextField answer3;
        JPasswordField password;
        JPasswordField password2;
        JButton jButton;
        JButton back;
        this.container = container;

        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Pintgram-Regular.ttf"));
        }catch (Exception exc){
            font = Font.getFont("Arial");
        }

        GridBagConstraints constraints = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        jLabel = new JLabel("Restore password");
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(font.deriveFont(Font.PLAIN, 40));

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(50,0,0,0);

        this.add(jLabel, constraints);

        jLabel = new JLabel("MAIL");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(100,0,0,0);
        this.add(jLabel, constraints);

        mail = new TextField("Your mail here");
        mail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mail.getText().equals("Your mail here")){
                    mail.setText("");
                }
            }
        });
        constraints.insets = new Insets(130, 0,0,0);
        this.add(mail, constraints);

        jLabel = new JLabel("NAME OF YOUR BEST FRIEND OF CHILDHOOD");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(160,0,0,0);
        this.add(jLabel, constraints);

        answer1 = new TextField("Answer here");
        answer1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(answer1.getText().equals("Answer here")){
                    answer1.setText("");
                }
            }
        });
        constraints.insets = new Insets(190,0,0,0);
        this.add(answer1, constraints);

        jLabel = new JLabel("NAME OF YOUR FAVOURITE TEACHER SO FAR");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(220, 0,0,0);
        this.add(jLabel, constraints);

        answer2 = new TextField("Answer here");
        answer2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(answer2.getText().equals("Answer here")){
                    answer2.setText("");
                }
            }
        });
        constraints.insets = new Insets(250, 0,0,0);
        this.add(answer2, constraints);


        jLabel = new JLabel("FAVOURITE/LUCKY NUMBER");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(280, 0,0,0);
        this.add(jLabel, constraints);

        answer3 = new TextField("Answer here");
        answer3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(answer3.getText().equals("Answer here")){
                    answer3.setText("");
                }
            }
        });
        constraints.insets = new Insets(310, 0,0,0);
        this.add(answer3, constraints);

        jLabel = new JLabel("NEW PASSWORD");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(340, 0,0,0);
        this.add(jLabel, constraints);

        password = new JPasswordField("Type here");
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(password.getText().equals("Type here")){
                    password.setText("");
                }
            }
        });
        constraints.insets = new Insets(370, 0,0,0);
        this.add(password, constraints);

        jLabel = new JLabel("REPEAT PASSWORD");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(400, 0,0,0);
        this.add(jLabel, constraints);

        password2 = new JPasswordField("Type here");
        password2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(password2.getText().equals("Type here")){
                    password2.setText("");
                }
            }
        });
        constraints.insets = new Insets(430, 0,0,0);
        this.add(password2, constraints);

        jButton = new JButton("Restore Password");
        constraints.insets = new Insets(490,0,0,0);
        this.add(jButton, constraints);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> answers = new ArrayList<>();
                answers.add(answer1.getText());
                answers.add(answer2.getText());
                answers.add(answer3.getText());

                if(mail.getText().isEmpty() || answer1.getText().isEmpty() || answer2.getText().isEmpty() || answer3.getText().isEmpty() || password.getText().isEmpty() || password2.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter text in both fields", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if(!AdminUser.getInstance().userExists(mail.getText())){
                    JOptionPane.showMessageDialog(null, "Sorry, there is no user registered with that mail", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if (!password.getText().equals(password2.getText())) {
                    JOptionPane.showMessageDialog(null, "Sorry, passwords don't match", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!AdminUser.getInstance().restorePswd(mail.getText(), password.getText(), answers)) {
                    JOptionPane.showMessageDialog(null, "Sorry, answers were not correct", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Now you can log in with your new password", "Changes done: ", JOptionPane.INFORMATION_MESSAGE);
                container.login();
            }
        });

        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        back = new JButton("BACK");
        constraints.insets = new Insets(10,10,0,0);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.login();
            }
        });
        this.add(back, constraints);

    }
}
