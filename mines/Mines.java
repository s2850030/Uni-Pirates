//   Mines.java
package mines;

import java.awt.*;
import java.awt.event.*;
import java.io.File;


//   check if we need .*
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;


public class Mines extends JFrame {



    //   Status and Menu setup
    private JPanel statusbar;
    private JLabel[] statusLabelArr;
    // create status labels and add to array
    private JLabel minesLabel= new JLabel();
    private JLabel playerLabel = new JLabel();
    private JLabel lifePointsLabel = new JLabel();

    private JMenuBar minesMenu;      // The menu bar
    private JPanel statistics;       // Statistics Panel
    private JPanel hOF;              // Hall of Fame Panel
    private JPanel gamePanel;  // for board and status bar

    private JMenu gameMenu;          // Game menu    has Options Solve and Exit
    private JMenu moveMenu;          // Move menu    has Undo and Redo
    private JMenu aboutMenu;         // About menu   has info

    private JMenuItem optionsItem;   // Options item
    private JMenuItem pauseItem;     // Pause item
    private JMenuItem solveItem;     // Solve item
    private JMenuItem saveItem;      // Save item
    private JMenuItem exitItem;      // Exit item
    private JMenuItem undoItem;      // Undo item
    private JMenuItem redoItem;      // Redo item
    private JMenuItem infoItem;      // Info item

    private Board board;             
    private Player player;
    private Options options;




    public Mines() {
        setTitle("Avoid the Pirates");
        setLayout(new BorderLayout());

        // add to array
        statusLabelArr = new JLabel[]{minesLabel, playerLabel, lifePointsLabel};

        statusbar = new JPanel(new BorderLayout());
        statusbar.add(minesLabel, BorderLayout.WEST);
        statusbar.add(playerLabel, BorderLayout.CENTER);
        statusbar.add(lifePointsLabel, BorderLayout.EAST);

        // create the game panel
        gamePanel = new JPanel(new BorderLayout());

        // create the options panel
        options = new Options(gamePanel, this, player);

        // add options panel to JFrame
        add(options, BorderLayout.WEST);

        // create the board
        board = new Board(statusLabelArr, options, this);

        // add contents to game panel
        gamePanel.add(board, BorderLayout.NORTH);
        gamePanel.add(statusbar, BorderLayout.SOUTH);

        // add gamePanel panel to Mines JFrame
        add(gamePanel, BorderLayout.CENTER);

        // make gamePanel not visible
        gamePanel.setVisible(false);



        player = null;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // Menu Added
        buildMenuBar();
        setResizable(false);
        pack();
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new Mines();
    }
    
    private void buildMenuBar()
    {
        // Create the menu bar.
        minesMenu = new JMenuBar();

        //infoItem
        buildGameMenu();
        buildMoveMenu();
        buildAboutMenu();

        // Stats panels

        //
        minesMenu.add(gameMenu);
        minesMenu.add(moveMenu);
        minesMenu.add(aboutMenu);

        //
        setJMenuBar(minesMenu);
    }

    private void buildGameMenu()
    {
        //
        optionsItem = new JMenuItem("Options");
        optionsItem.setMnemonic(KeyEvent.VK_O);
        optionsItem.addActionListener(new PauseListener());
        //
        pauseItem = new JMenuItem("Pause");
        pauseItem.setMnemonic(KeyEvent.VK_P);
        pauseItem.addActionListener(new PauseListener());
        //
        solveItem = new JMenuItem("Solve");
        solveItem.setMnemonic(KeyEvent.VK_S);
        solveItem.addActionListener(new SolveListener());
        //
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_A);
        saveItem.addActionListener(new SaveListener());
        //
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(new ExitListener());

        //
        gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);

        //
        gameMenu.add(optionsItem);
        gameMenu.add(pauseItem);
        gameMenu.add(solveItem);
        gameMenu.add(saveItem);
        gameMenu.add(exitItem);
    }

    private void buildMoveMenu()
    {
        //
        undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.addActionListener(new UndoListener());
        //
        redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.addActionListener(new RedoListener());
        //
        moveMenu = new JMenu("Move");
        moveMenu.setMnemonic(KeyEvent.VK_M);
        //
        moveMenu.add(undoItem);
        moveMenu.add(redoItem);

    }


    private void buildAboutMenu()  {

    infoItem = new JMenuItem("Info");
    infoItem.setMnemonic(KeyEvent.VK_I);
    infoItem.addActionListener(new About());

    aboutMenu = new JMenu("About");
    aboutMenu.setMnemonic(KeyEvent.VK_A);


    aboutMenu.add(infoItem);

    }

    public void setBoard(Board board){
        this.board = board;
    }
   
class PauseListener implements ActionListener
{
  public void actionPerformed(ActionEvent e)
  {options.setVisible(true);
  gamePanel.setVisible(false);
  
  }
}


    // implements funcional requirement undo
    class UndoListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        { board.undoMove(); }
    }
    // implements funcional requirement redo
    class RedoListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        { board.redoMove(); }
    }

    class ExitListener implements ActionListener
    {
       public void actionPerformed(ActionEvent e)
        { System.exit(0); }
    }
    
    class SolveListener implements ActionListener
    {
      public void actionPerformed(ActionEvent e)
        { board.EndGame(true); }
    }
    class SaveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        { SaveGame(); }
    }
    public void setCurrentGame(){
        board.setGame();
    }
        





    public void SaveGame(){
        if (board.getInGame()) {

            board.saveGame();
        }

        else
            statusLabelArr[1] .setText("   Can not save");
    }
}


