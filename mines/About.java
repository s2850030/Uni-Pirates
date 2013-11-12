package mines;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;

public class About implements ActionListener {           //Displays the about window when About/Info menu item   selected
    private JFrame About = new JFrame("About The Pirates Cove") ;
    private JPanel APA= new JPanel();
    private JLabel imgLabel = new JLabel(new ImageIcon("Archive/mAbout.png"));
    private String [] hof = new String [18];
    private Options options;
    public About() {
        About.add(APA)  ;
         APA.setLayout(new GridLayout(1, 1));
        About.setSize(350, 610);

        About.add(imgLabel);
        About.setLocationRelativeTo(null);
      //  About.setVisible(false);





    }
    @Override
    public void actionPerformed(ActionEvent e) {
        About.setVisible(true);
    }

    //  saves the current HOF and user name to disk  - functional requirement
    public void saveHOF(){

        File gameDir = new File(System.getProperty("user.home") + "/Documents/Games/");
        if(!gameDir.exists()){
            gameDir.mkdir();
        }
        options = options.getOptions();
        File gameFile = new File(gameDir.getAbsolutePath() + "/Pirates.ini");


        BufferedWriter bw = null;

        try{

            bw = new BufferedWriter(new FileWriter(gameFile));

            for (int i = 0 ;i<18;i++){
                bw.write(hof[i]);
                bw.newLine();
                //System.out.println(hof[i]);
            }
            bw.write(options.getUser());
            bw.close();
        }
        catch (IOException e) {e.getMessage();}
    }
}
