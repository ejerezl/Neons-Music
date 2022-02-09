package Graphics;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class TextField. Class to empty a text field when clicked.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class TextField extends JTextField {
    public String initialText;

    public TextField(String initialText){
        super(initialText);
        this.initialText = initialText;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(getText().equals(initialText)){
                    setText("");
                }
            }
        });
    }

}
