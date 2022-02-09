package Graphics;

import Users.AdminUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Class SignUp. Class that implements the register questions panel for the login screen.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class SignUp extends JPanel{

    public Color startColor = Color.getHSBColor(0.750f, 0.54f, 0.1f);
    public Color endColor = Color.getHSBColor(0.780f, 0.5f, 0.4f);
    public int GradientFocus = 500;

    private String mail;
    private String password;
    private String name;
    private String artisticName;
    private String birthday;
    private LoginWindow container;
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

    public SignUp(LoginWindow container, String mail, String password)

    {
        super();
        this.container = container;
        this.mail = mail;
        this.password = password;
        this.setLayout(new GridBagLayout());

        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Pintgram-Regular.ttf"));
        }catch (Exception exc){
            font = Font.getFont("Arial");
        }

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0,0,20,0);

        JLabel signUp = new JLabel("Sign Up");
        signUp.setFont(font.deriveFont(Font.PLAIN, 40));
        signUp.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 0;

        this.add(signUp, c);

        JLabel question1 = new JLabel("Name of your best friend from childhood");
        c.gridy = 1;
        question1.setForeground(Color.WHITE);

        this.add(question1, c);

        JTextField t1 = new JTextField();

        c.gridy = 2;

        t1.setColumns(15);
        this.add(t1, c);

        JLabel question2 = new JLabel("Name of your favourite teacher so far");
        c.gridy = 3;
        question2.setForeground(Color.WHITE);

        this.add(question2, c);

        JTextField t2 = new JTextField();

        c.gridy = 4;
        t2.setColumns(15);
        this.add(t2, c);

        JLabel question3 = new JLabel("Favourite number/Lucky number");
        c.gridy = 5;
        question3.setForeground(Color.WHITE);

        this.add(question3, c);

        JTextField t3 = new JTextField();

        c.gridy = 6;
        t3.setColumns(15);
        this.add(t3, c);

        c.gridy = 7;
        c.fill = GridBagConstraints.NONE;

        c.insets = new Insets(50,0,0,100);
        JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> answers = new ArrayList<>(Arrays.asList(t1.getText(), t2.getText(),t3.getText()));
                answers.stream().map(string -> string.toLowerCase()).collect(Collectors.toCollection(ArrayList::new));
                AdminUser.getInstance().register(name, artisticName, mail, new Date(birthday), answers, password);
                container.login();
            }
        });
        this.add(next, c);

        c.insets = new Insets(50,100,0,0);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.login();
            }
        });
        this.add(cancel, c);

        this.setMinimumSize(new Dimension(500,500));
        this.setVisible(true);

    }

    public void updateInfo(String name, String artisticName, String birthday){
        this.name = name;
        this.artisticName = artisticName;
        this.birthday = birthday;

    }


}

