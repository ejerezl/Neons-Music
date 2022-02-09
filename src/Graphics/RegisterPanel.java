package Graphics;

import Users.AdminUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Class RegisterPanel. Class that implements the register panel in the login screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class RegisterPanel extends JPanel {
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
    public RegisterPanel(LoginWindow container) {
        JLabel jLabel;
        TextField name;
        TextField artisticName;
        TextField birthdate;
        JButton jButton;
        JButton cancel;
        this.container = container;
        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Pintgram-Regular.ttf"));
        }catch (Exception exc){
            font = Font.getFont("Arial");
        }

        GridBagConstraints constraints = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        jLabel = new JLabel("Sign up");
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(font.deriveFont(Font.PLAIN, 40));

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(70,0,0,0);

        this.add(jLabel, constraints);

        jLabel = new JLabel("NAME");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(120,0,0,0);
        this.add(jLabel, constraints);

        name = new TextField("Your name here");
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(name.getText().equals("Your name here")){
                    name.setText("");
                }
            }
        });
        constraints.insets = new Insets(150, 0,0,0);
        this.add(name, constraints);

        jLabel = new JLabel("ARTISTIC NAME");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(180,0,0,0);
        this.add(jLabel, constraints);

        artisticName = new TextField("Your artistic name here");
        artisticName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(artisticName.getText().equals("Your artistic name here")){
                    artisticName.setText("");
                }
            }
        });
        constraints.insets = new Insets(210,0,0,0);
        this.add(artisticName, constraints);

        jLabel = new JLabel("BIRTHDATE");
        jLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jLabel.setForeground(Color.WHITE);
        constraints.insets = new Insets(240, 0,0,0);
        this.add(jLabel, constraints);

        birthdate = new TextField("mm/dd/yyyy");
        birthdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(birthdate.getText().equals("mm/dd/yyyy")){
                    birthdate.setText("");
                }
            }
        });
        constraints.insets = new Insets(270, 0,0,0);
        this.add(birthdate, constraints);

        jButton = new JButton("Register");
        constraints.insets = new Insets(310,0,0,100);
        this.add(jButton, constraints);

        cancel = new JButton("Cancel");
        constraints.insets = new Insets(310, 100,0,0);
        this.add(cancel, constraints);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name.getText().isEmpty() || artisticName.getText().isEmpty() || birthdate.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter text in both fields", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    new Date(birthdate.getText());
                }catch (IllegalArgumentException exc){
                    JOptionPane.showMessageDialog(null, "Please enter your birthdate in mm/dd/yyyy format", "Authentication error:", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                container.questions(name.getText(), artisticName.getText(), birthdate.getText());
            }
        });
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(10,10,0,0);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.login();
            }
        });
    }
}
