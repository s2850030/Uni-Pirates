package mines;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JLabel;

class Options extends JPanel {
    // creates radio buttons
    private JRadioButton easyOption = new JRadioButton("Easy");
    private JRadioButton mediumOption = new JRadioButton("Medium");
    private JRadioButton hardOption = new JRadioButton("Hard");
    private ButtonGroup gameoptions = new ButtonGroup();
    private JPanel controlPanel;
    private JTextField userTextField;
    private JFrame mines;
    private int baddies;

     // actionpanel holds buttons in a group
    private JPanel actionpanel = new JPanel();
    private JButton play = new JButton("Play");
    private JButton resume = new JButton("Resume")   ;

    private int gamelevel;
    private String user;
    private Player player = new Player("Player",10,baddies) ;

    // constructor
    public Options(JPanel controlPanelIn, JFrame minesIn, Player playerIn) {

        controlPanel = controlPanelIn;
        mines = minesIn;
        player =  playerIn;
        //set  grid layout and display sizes
        this.setSize(300,300);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //add radio buttons to a ButtonGroup

        gameoptions.add(easyOption);
        easyOption.setSelected(true);
        easyOption.setActionCommand("1");
        gameoptions.add(mediumOption);
        mediumOption.setActionCommand("2");
        gameoptions.add(hardOption);
        hardOption.setActionCommand("3");

        // Panel setting
        userTextField = new JTextField("Player 1");


        this.add(new JLabel("Please Choose your Game Level:"));
        this.add(easyOption);
        this.add(mediumOption);
        this.add(hardOption);
        this.add(new JLabel("Please Enter your name:"));
        this.add(userTextField);
        this.add(play)  ;
        play.addActionListener(new ActionListener(){

            //   on entering player name - the game is activated.
            public void actionPerformed(ActionEvent e){
                //  set the game level   -  trying to recover the selected level and store to
                String ss =  gameoptions.getSelection().getActionCommand();
                if (ss == "1"){
                    baddies = 10;
                    gamelevel = 1;}
                else if (ss == "2" )  {
                    baddies = 20;
                    gamelevel = 2;}
                else              {
                    baddies = 40;
                    gamelevel = 3;}

                // sets the players name
                user = userTextField.getText();

                // display the game panel
                controlPanel.setVisible(true);
                setVisible(false);
                mines.pack();
            }
        });


        this.add(resume) ;
        resume.addActionListener(new ResumeListener());
        resume.setVisible(false);

    
}

    // implement ItemListener interface
    class MyItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent ev) {
        boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
        AbstractButton button = (AbstractButton) ev.getItemSelectable();
        String command = button.getActionCommand();
        }
    }
    public int getBaddies(){
    return baddies;}


    public String getUser(){
    return user;
    }

    public Options getOptions(){
    return this;
    }


    public void restoreGame(){
        // see if there is a file on disk...  if so.. call PlayerRestore()
        player.pullFile();
        //player.p


    }

    class ResumeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        { restoreGame(); }
    }

}




