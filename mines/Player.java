package mines;


import com.sun.xml.internal.rngom.dt.builtin.BuiltinDatatypeLibrary;

import javax.swing.*;
import java.io.*;
import java.util.*  ;


public class Player {

    private String playerName;
    private int gameLevel,lifePoints;
     // pass in the last saved game
    private ArrayDeque <int[]> curGame = new ArrayDeque<int[]> () ;
    //  pass in the stats
    private String [] gameStats = new String[10] ;
    //  build a panel and label array to hold the stats
    private JPanel playerstatsPanel = new JPanel() ;
    private JLabel[] playerstatsLabel = new JLabel[10] ;
    private int gamecells;




    public Player(String pName, int gLevel,int gCells)       {
        playerName = pName;
        gameLevel=gLevel;
        lifePoints= 10;
        gamecells=gCells ;
        //pullFile();         //TODO :Recover the file from the disk based on the player name.gam

        for (int i = 0; i<10;i++) {
            gameStats[i]  =  "Easy             "  + 0  ;
        }
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setLifePoints(int points)   {
        lifePoints = points;
    }
    public void setGameLevel(int level)  {
        gameLevel = level;
    }




    //  read in player's data from the player.gam file
    //  functional constraint all games read from disk
    public void pullFile()    {

        //  this code is about putting files where they are supposed to be
        File gameDir = new File(System.getProperty("user.home") + "/Documents/Games/");

        // ok its not there so create it and the default file...
        if(!gameDir.exists()){
            gameDir.mkdir();
            // creates a default file  using the current random settings and default player name
            pushFile("Player 1") ;
        }
        // initialise a scanner to parse the ints into the undo array
        Scanner mScan = new Scanner(System.in);
        File playerInFile = new File(gameDir.getAbsolutePath() + "/" + playerName + ".gam");
        int [] holdInbound = new int[gamecells] ;
        if (! playerInFile.canRead()){
            // create from a default position    == copy default file   functional requirement  - to read from disk
            pushFile(playerName);
        }
        BufferedReader bufferedReader = null;
        String readIt = "";
        try{
            bufferedReader = new BufferedReader(new FileReader(playerInFile))  ;
            // set up the player containers to accept data
            curGame.clear();
            clearGamestats();


            // read in the game stats - 1st 10 lines of strings...
            for (int i = 0; i < 10 && readIt != null; i++)  {

                // read the next line of the file
                readIt = bufferedReader.readLine();

                 // add the line to the game stats file
                gameStats[i] = readIt;
            }
            while (readIt != null)     {
                // now for the undo array
                readIt = bufferedReader.readLine();
                int i = 0;
                // pass it to the scanner
                mScan = new Scanner(readIt);
                while (mScan.hasNextInt())  {
                    holdInbound[i]  = mScan.nextInt();
                    i++;  //   todo i can go out of bounds here / out of scope   check catch exceptions
                }
                // writes it to the undo object in player
                curGame.push(holdInbound.clone());
            }

        }
        catch(IOException e){
             System.out.println("Player File not found - copy default"); }
        catch(ArrayIndexOutOfBoundsException e)
            {System.out.print("Invalid undo array too long"); }
        catch(Exception e)  {    System.out.print("probably a data type exception here " + e.getMessage());
        }

    }







    //  saves the current game to disk  - functional requirement
    public void pushFile(String playerName){
        int [] holdOutbound = new int[gamecells] ;
        String sendout = "";
        this.playerName = playerName;


        File gameDir = new File(System.getProperty("user.home") + "/Documents/Games/");
        if(!gameDir.exists()){
            gameDir.mkdir();
        }

        File playerFile = new File(gameDir.getAbsolutePath() + "/" + playerName + ".gam");

        System.out.println(gameDir.getAbsolutePath());
        System.out.println(playerFile.getAbsolutePath());  // testing

        BufferedWriter bw = null;

        try{

            bw = new BufferedWriter(new FileWriter(playerFile));

            for (int i = 0 ;i<10;i++){
                bw.write(gameStats[i]);
                bw.newLine();
                System.out.println(gameStats[i]);
            }
            while (!curGame.isEmpty())  {
                holdOutbound = curGame.pop();
                for (int i = 0; i<holdOutbound.length;i++) {
                    sendout += " "  + holdOutbound[i] ;
                }
                bw.write(sendout);
            }
            bw.close();
            playerName = " Game Saved";
        }
        catch (IOException e) {e.getMessage();}
    }

    // builds the game stats panel to pass back to the options screen
    private void setGameStats(){
        Scanner myScan = new Scanner(gameStats[0]) ;

    }
    public ArrayDeque<int[]> getCurGame() {
        return curGame;

    }

    // allows the undo array to be passed in for save
    public void setCurGame(ArrayDeque<int[]> gameIn)  {
        curGame = gameIn.clone();
    }


    //  populate the stats panel from an external call
    public void setPlayerStats(String[]stats){
        // build the   gameStats array and   playerstatsPanel
        int i = 0;
        gameStats = stats;
        while ( i < stats.length || i < 10)  {
           playerstatsLabel[i] = new JLabel(stats[i])  ;
           playerstatsPanel.add(playerstatsLabel[i]) ;
        }
    }

   public void clearGamestats(){
       for (int i = 0; i<10; i++)  {
           gameStats[i]   = " " ;
       }
   }
}

