package Graphics;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class LoginLeftPanel. Class that contains the title of the app.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class LoginLeftPanel extends JPanel {
    private JLabel message;
    private LoginWindow container;

    public Color StartColor = Color.getHSBColor(0.750f, 0.54f, 0.1f);
    public Color endColor = Color.getHSBColor(0.780f, 0.5f, 0.4f);
    public int GradientFocus = 500;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, StartColor, GradientFocus, h, endColor);;

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        //g2d.dispose();
    }

    public LoginLeftPanel(LoginWindow container) {
        this.container = container;
        this.setLayout(new GridLayout());
        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Pintgram-Regular.ttf"));
        }catch (Exception exc){
            font = Font.getFont("Arial");
        }


        message = new JLabel("Welcome to Neons");
        message.setFont(font.deriveFont(Font.PLAIN, 70));
        message.setForeground(Color.WHITE);
        message.setHorizontalAlignment(JLabel.CENTER);
        message.setVerticalAlignment(JLabel.CENTER);

        this.add(message);
        this.setPreferredSize(new Dimension(600, 600));
        this.setMinimumSize(new Dimension(500,500));
        this.setVisible(true);
    }
}
