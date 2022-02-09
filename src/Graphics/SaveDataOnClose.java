package Graphics;

import App.App;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class SaveDataOnClose. Class used to save all the data in the app when exiting.
 * @author Esther Jerez Lopez esther.jerezl@estudiante.uam.es
 * @author Alejandro Andraca Gutierrez alejandro.andraca@estudiante.uam.es
 * @author Eva Lacaba Nicolas eva.lacaba@estudiante.uam.es
 */
public class SaveDataOnClose extends WindowAdapter {
    private App app;

    public SaveDataOnClose(App app){
        this.app = app;
    }
    @Override
    public void windowClosing(WindowEvent e) {
        App.setLogged(null);
        app.saveData();
        System.exit(0);
    }
}
