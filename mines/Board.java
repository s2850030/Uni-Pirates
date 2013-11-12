// Board.java

package mines;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayDeque;
import java.util.Random;

import javax.swing.*;

public class Board extends JPanel {
    private final int GAMEWIDTH = 1460;
    private final int GAMEHEIGHT = 660;

    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 25;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private int[] field;
    private int[] surrounds= new int[8];
    private boolean inGame;
    private int mines_left;
    private Image[] img;
    private int pirates = 10;
    private int rows = 16;
    private int cols = 16;
    private int lifepoints = 10;
    private int all_cells;
    private JLabel[] statusbar;

    private String playername;
    private Options options;
    private Player player;

    //  functional requirement undo game logs...
    private ArrayDeque <int[]> undoarray = new ArrayDeque  <int[]>();
    private ArrayDeque <int[]> redoarray = new ArrayDeque  <int[]>();


    // constructor
    public Board(JLabel[] statusbar, Options options ,Mines mines) {
        this.statusbar = statusbar;
        this.options = options;
        mines.setSize(GAMEWIDTH, GAMEHEIGHT);
        playername = options.getUser();
        pirates  = options.getBaddies();
        player = new Player( playername, pirates,all_cells+2);

        img = new Image[NUM_IMAGES];
        //  Make the images available for drawing on screen -  shopping list
        for (int i = 0; i < NUM_IMAGES; i++) {

        //  for JPL remove   getClassLoader().
        img[i] =(new ImageIcon(this.getClass().getClassLoader().getResource("Archive/j" + (i) + ".png"))).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter());
        setPreferredSize(new Dimension(getHeight(), getWidth()));
        newGame();           // todo bug found...  settings not passing... on first game not fixed.

    }

    public void newGame() {

        Random random;
        int current_col;
        options = options.getOptions();
        int i = 0;
        int position = 0;
        int cell = 0;
        //  constructs the new game with variables passed in
        pirates = options.getBaddies()  ;
        random = new Random();
        inGame = true;
        mines_left = pirates;
        lifepoints = 10;
        statusbar[1] .setText("   "+options.getUser());
        all_cells = rows * cols;
        field = new int[all_cells+2];
        field[all_cells]  = lifepoints;
        field[all_cells+1]  = pirates;
        for (i = 0; i < all_cells; i++)
            field[i] = COVER_FOR_CELL;

        setStatusBar();
        clearMoveLog();

        // todo   refactor this code   its ugly -  out of time
        i = 0;
        while (i < pirates) {

            position = (int) (all_cells * random.nextDouble());

            if ((position < all_cells) &&
                (field[position] != COVERED_MINE_CELL)) {


                current_col = position % cols;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) { 
                    cell = position - 1 - cols;
                    if (cell >= 0)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;
                    cell = position - 1;
                    if (cell >= 0)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;

                    cell = position + cols - 1;
                    if (cell < all_cells)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;
                }

                cell = position - cols;
                if (cell >= 0)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                cell = position + cols;
                if (cell < all_cells)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;

                if (current_col < (cols - 1)) {
                    cell = position - cols + 1;
                    if (cell >= 0)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;
                    cell = position + cols + 1;
                    if (cell < all_cells)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;
                    cell = position + 1;
                    if (cell < all_cells)
                        if (field[cell] != COVERED_MINE_CELL)
                            field[cell] += 1;
                }
            }
        }
        // push field onto undo stack
        undoarray.push(field.clone());
    }

    private void setStatusBar() {
        statusbar[0].setText(Integer.toString(mines_left));
        statusbar[1].setText(playername);
        statusbar[2].setText(Integer.toString(lifepoints));

    }

    public void find_empty_cells(int j) {

        int current_col = j % cols;
        int cell;

        if (current_col > 0) { 
            cell = j - cols - 1;
            if (cell >= 0)
                setCell(cell,1);

            cell = j - 1;
            if (cell >= 0)
                setCell(cell,1);

            cell = j + cols - 1;
            if (cell < all_cells)
               setCell(cell,1);
        }

        cell = j - cols;
        if (cell >= 0)
           setCell(cell,1);

        cell = j + cols;
        if (cell < all_cells)
            setCell(cell,1);

        if (current_col < (cols - 1)) {
            cell = j - cols + 1;
            if (cell >= 0)
               setCell(cell,1);
            cell = j + cols + 1;
            if (cell < all_cells)
                setCell(cell,1);
            cell = j + 1;
            if (cell < all_cells)
               setCell(cell,1);
                }
      }

    public void paint(Graphics g) {

        int cell = 0;
        int uncover = 0;

        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {

                cell = field[(i * cols) + j];

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }


                } else {
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                    (i * CELL_SIZE), this);
            }
        }

        //  Mark Up  ---   status bar panel at base of screen
        statusbar[0] .setText("Pirates left "+ mines_left +"  ");
        statusbar[1] .setText("  "+options.getUser());
        statusbar[2] .setText(" Life Points "+lifepoints);


        //  Mark up   TODO   update end of game method
        if (uncover == 0 ) {
            inGame = false;

            String result = "";
            if (lifepoints >0 )
                statusbar[0].setText("Game won");
            else
                statusbar[0].setText("Game lost");


        }





    }


    class MinesAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            int curPos = 0;
            boolean rep = false;
            statusbar[1] .setText("   "+options.getUser());
  
            if (!inGame) {
                newGame();
                repaint();   }
            else  {
                // push field onto undo stack
                undoarray.push(field.clone());
                if(!redoarray.isEmpty()){
                    redoarray.clear();
                }
            }


            if ((x < cols * CELL_SIZE) && (y < rows * CELL_SIZE)) {
                curPos = (cRow * cols) + cCol;
                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[curPos] > MINE_CELL) {
                        rep = true;

                        if (field[curPos] <= COVERED_MINE_CELL) {
                            if (mines_left > 0) {
                                field[curPos] += MARK_FOR_CELL;
                                mines_left--;
                                statusbar[0].setText("Pirates left "+ mines_left +"  ");
                            } else
                                statusbar[0].setText("No marks left");
                        } else {

                            field[curPos] -= MARK_FOR_CELL;
                            mines_left++;
                            statusbar[0].setText("Pirates left "+ mines_left +"  ");
                        }
                    }

                } else {

                    if (field[curPos] > COVERED_MINE_CELL) {
                        return;
                    }

                    if ((field[curPos] > MINE_CELL) &&
                        (field[curPos] < MARKED_MINE_CELL)) {

                        field[curPos] -= COVER_FOR_CELL;
                        rep = true;

                        if (field[curPos] == MINE_CELL) {// you hit a mine
                            loseLifePoints();
                            mines_left -= 1;
                            statusbar[0].setText("Pirates left "+ mines_left +"  ");
                            if (lifepoints <= 0) {    // you died   update stats                   
                              inGame = false;

                          }
                        }
                        if (field[curPos] == EMPTY_CELL)
                            find_empty_cells(curPos);
                    }
                }

                if (rep)
                    repaint();
                if (inGame && mines_left==0)
                    inGame = false;
                if (!inGame)
                    EndGame(false);
                }
        }

    }
    //   Mark up code in here...    This is still untidy but I am out of time.


    // unit tested   works
    public void clearMoveLog()  {
        undoarray.clear();
        redoarray.clear();
    }
    // unit tested   works  functional requirement
    public void undoMove(){
      	// check if there are moves to undo
	    if(!undoarray.isEmpty()){
		// first take the field as is and add to the redo stack
		redoarray.push(field);
		// then make the field equal to the top of the undo stack
		field = undoarray.pop();

		// restore lifepoints and pirates
		lifepoints=field[all_cells]      ;
        pirates = field[all_cells+1] ;
		// repaint the board
		  repaint();

	}
}
    //  unit tested    works    functional requirement
    public void redoMove(){
        // check if there are moves to redo
        if(!redoarray.isEmpty()){
            // first take the field as is and add to the undo stack
            undoarray.push(field);
            // then make the field equal to the top of the redo stack
            field = redoarray.pop();
            // restore lifepoints and pirates
            lifepoints=field[all_cells]      ;
            pirates = field[all_cells+1] ;

            // repaint the board
            repaint();

        }
    }

    public boolean getInGame(){
        return inGame;
    }
    // unit tested     works    -  functional requirement
    public void saveGame(){
        String trySave = "    Saving Game" ;
        statusbar[1] .setText(trySave);
        // pass the undo array to the player
        player.setCurGame(undoarray) ;
        // check that we have the correct user name
        playername = options.getUser();
        // tell the player to dump out to disk
        player.pushFile(playername);
        // tell the user
        trySave= "   Game Saved";
        statusbar[1] .setText(trySave);
    }
    //Uncover all pirates on the board        unit tested   - works
    public void uncoverMines(){
        for (int i = 0; i < all_cells; i++)
            field[i] = field[i] == COVERED_MINE_CELL ? MINE_CELL:field[i];
    }
    //
    public void setLifepoints(int setTo) {
        lifepoints = setTo;
    }
    // Implements requirement different strength mine
    public void loseLifePoints(){
        Random random;
        random = new Random();
        int deduct =  (int) (5 * random.nextDouble());

        lifepoints -= deduct==0 ? 1:deduct;
        lifepoints = lifepoints< 0 ? 0:lifepoints;
        setStatusBar();
    }
    // not implemented out of time  trying to refactor bad code above (recursive...  this flags the cells but does not work correctly)
    // populates the surrounding cells array    top left, left, bottom left, above, below, top right, right, bottom right --
    public void SurroundingCells(int position){

        int current_col= position % cols;

        // clear position array
        for (int i=0;i<8;i++)
            surrounds[i] = -1;

        // get surrounding addresses   - no wrapping or out of bounds
        surrounds[0] = (current_col > 0)? position - 1 - cols:-1;
        surrounds[1] = (current_col > 0)? position - 1:-1;
        surrounds[2] = (current_col > 0)? position + cols - 1:-1;

        surrounds[3] = position - cols;
        surrounds[4] = position + cols;

        surrounds[5] = (current_col < (cols - 1))? position - cols + 1:-1;
        surrounds[6] = (current_col < (cols - 1))? position + cols + 1:-1;
        surrounds[7] = (current_col < (cols - 1))? position + 1:-1;

    }
    //  refactor to clean up some recursive code...  this is still ugly
    public void setCell(int curPos, int context){
        if (context==1 && field[curPos] > MINE_CELL ) {
            field[curPos] -= COVER_FOR_CELL;
            if (field[curPos] == EMPTY_CELL)
                find_empty_cells(curPos);}
    }

    public int getWidth(){
        return CELL_SIZE * cols;
    }

    public int getHeight(){
        return CELL_SIZE * rows;
    }

    public void setPlayername(String playername){
        this.playername = playername;
    }

    public void setPirates(int pirates){
        this.pirates = pirates;
    }
    public int getAll_cells  (){
        return all_cells;
    }
    public int getLifepoints(){
        return lifepoints;
    }

    public void EndGame( boolean solved) {

        // todo solve should not work from options pane...
        uncoverMines();
        setLifepoints(0);
        repaint();
        ImageIcon background;
        JPanel wpa = new JPanel();
        JFrame wimp = new JFrame();
        String heading = "Game Over";
        JLabel holdBack= new JLabel();

        //   wimped out...  asked for solve
        if (solved){
            background = (new ImageIcon(this.getClass().getClassLoader().getResource("Archive/mSolve.png")));
            // kicks the wimp out of the game on close
            wimp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            wimp.setSize(520, 320);
            heading = "Wimp !!!   to th' STOCKS wiv YE !!! LANDLUBBER!!" ;
        }
        else {
            // game ended naturally
            wimp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            wimp.setSize(520, 320);

            if (statusbar[0].getText()== "Game Won"){
                // game won
                background = new ImageIcon(this.getClass().getClassLoader().getResource("Archive/mWon.png"));
                wimp.setSize(800, 520);
                heading = "YARD MAKE A GOOD PIRATE !!  AVE UN ALE !";
                //todo  update the About HOF   -  not implemented

            }

            else {
                // game lost
                background = new ImageIcon(this.getClass().getClassLoader().getResource("Archive/mDead.png"));
                wimp.setSize(800, 520);
                heading = "YARD BEEN A GOO SHIPMATE FER DAVEY !";
            }
            //TODO update the player Stats    not implemented

        }
        //wpa.add (background) ;
        wimp.setTitle(heading);
        wimp.add(wpa);
        holdBack.setIcon(background);
        wpa.add(holdBack) ;



        wimp.setLocationRelativeTo(null);
        wimp.setVisible(true);
    }

    public void setGame(){
        this.undoarray.clear();
        this.undoarray = player.getCurGame().clone();
        undoMove();
    }
}
