import javax.swing.*;
import java.awt.*;

/********************************************************************************************
 *  Class Name:     Grid
 *  Description:
 *******************************************************************************************/
public class Grid{
    final private int RED = 0, GREEN = 1, BLUE = 2, YELLOW = 3;

    final private double SLEEP_TIMER = 501; //default is 501ms
    final private int NUM_OF_WALKERS = 4; /*only works as intended (four corners placement) for 1-4,
                                           but will start walkers walkers 5-infinity at xPos 0 and yPos 0*/
    final private int windowWidth = 600,
                        windowHeight = 600;

    private int width, height, winnerId, place;
    private boolean gameInProgress;
    private Walker walkers[];
    private boolean walkerGrid[][][]; //height, width, isClaimedBy[walkerId]
    private Display window;
    private Component[] panels;

    /********************************************************************************************
     *  Function Name:  Grid
     *  Return Type:    None(Constructor)
     *  Description:    Initializes the Grid class to the specified defaults.
     *                  Width = 10, Height = 10, winnerId = -1, place = 1,
     *                  gameInProgress = false, initializes (NUM_OF_WALKERS) walkers.
     ********************************************************************************************/
    public Grid(){
        this.winnerId = -1;
        this.width = 10;
        this.height = 10;
        Display window = new Display(windowWidth, windowHeight, height, width);
        panels = window.getContentPane().getComponents();
        loadVars();
        loadGrid();
    }

    /********************************************************************************************
     *  Function Name:  Grid
     *  Return Type:    None(Explicit Constructor)
     *  Description:    Initializes the Grid class to the specified defaults.
     *                  Width = (inputted width), Height = (inputted height), winnerId = -1,
     *                  place = 1, gameInProgress = false, initializes (NUM_OF_WALKERS) walkers.
     ********************************************************************************************/
    public Grid(int w, int h){
        this.winnerId = -1;
        if(w < 2)//width cannot be below 2
            this.width = 2;
        else
            this.width = w;
        if(h < 2)//height cannot be below 2
            this.height = 2;
        else
            this.height = h;
        Display window = new Display(windowWidth, windowHeight, height, width);
        panels = window.getContentPane().getComponents();
        loadVars();
        loadGrid();
    }

    /********************************************************************************************
     *  Function Name:  Grid
     *  Return Type:    None(Explicit Constructor)
     *  Description:    Initializes the Grid class to the specified defaults.
     *                  Width = (inputted width), Height = (inputted height), winnerId = -1,
     *                  place = 1, gameInProgress = false, initializes (NUM_OF_WALKERS) walkers.
     *
     *                  Post Game Without Dimension Definition
     ********************************************************************************************/
    public Grid(int w, int h, int id){
        this.winnerId = -1;
        if(w < 2)//width cannot be below 2
            this.width = 2;
        else
            this.width = w;
        if(h < 2)//height cannot be below 2
            this.height = 2;
        else
            this.height = h;
        Display window = new Display(windowWidth, windowHeight, height, width);
        panels = window.getContentPane().getComponents();
        loadVars();
        loadGrid();
    }

    /********************************************************************************************
     *  Function Name:  Grid
     *  Return Type:    None(Explicit Constructor)
     *  Description:    Initializes the Grid class to the specified defaults.
     *                  Width = (inputted width), Height = (inputted height), winnerId = -1,
     *                  place = 1, gameInProgress = false, initializes (NUM_OF_WALKERS) walkers.
     *
     *                  Post Game Without Dimension Definition
     ********************************************************************************************/
    public Grid(int id){
        this.winnerId = -1;
        this.width = 10;
        this.height = 10;
        Display window = new Display(windowWidth, windowHeight, height, width);
        panels = window.getContentPane().getComponents();
        loadVars();
        loadGrid();
    }

    /********************************************************************************************
     *  Function Name:  loadVars
     *  Return Type:    void
     *  Description:    Resets all grid and walker properties to default.
     ********************************************************************************************/
    private void loadVars(){
        this.place = 1;
        this.gameInProgress = false;
        this.walkers = new Walker[NUM_OF_WALKERS];
        for(int i = 0; i < NUM_OF_WALKERS; i++){
            walkers[i] = new Walker(i);
        }
    }

    /********************************************************************************************
     *  Function Name:  loadGrid
     *  Return Type:    void
     *  Description:    Creates a new grid with all values of grid set to false.
     ********************************************************************************************/
    private void loadGrid(){
        walkerGrid = new boolean[height][width][NUM_OF_WALKERS];
    }

    /********************************************************************************************
     *  Function Name:  startGame
     *  Return Type:    void
     *  Description:    Turns gameInProgress indicator to true, starts all walker threads.
     ********************************************************************************************/
    public void startGame(){
        this.gameInProgress = true;
        this.winnerId = -1;
        for(int i = 0; i < NUM_OF_WALKERS; i++){
            walkers[i].start();
        }
        for(int i = 0; i < NUM_OF_WALKERS; i++){
            try{walkers[i].join();}catch(InterruptedException e){}
        }
    }

    /********************************************************************************************
     *  Function Name:  startGame(int id)
     *  Return Type:    void
     *  Description:    Turns gameInProgress indicator to true, starts a single walker thread (winner).
     ********************************************************************************************/
    public void startGame(int id){
        this.gameInProgress = true;
        this.winnerId = -1;
        if(id < NUM_OF_WALKERS)
            walkers[id].start();

        try{walkers[id].join();}catch(InterruptedException e){}
    }

    /********************************************************************************************
     *  Function Name:  reset
     *  Return Type:    void
     *  Description:    Resets gameInProgress indicator to false and reloads all grid
     *                  values and properties, as well as walkers to their default values.
     *******************************************************************************************/
    public void reset(){
        if(!gameInProgress){
            for(int i = 0; i < height * width; i++)
                panels[i].setBackground(Color.black);
            loadVars();
            loadGrid();
            System.out.println("Game has been reset");
        } else {
            System.out.println("Game in progress, cannot reset.");
        }
    }

    /********************************************************************************************
     *  Function Name:  updateSpaces
     *  Return Type:    void
     *  Description:    Interfaces with JFrame display to update positions of walked on spaces
     *******************************************************************************************/
    synchronized private void updateSpaces(int h, int w, int id){
        int timesClaimed = 0;
        for(int i = 0; i < NUM_OF_WALKERS; i++){
            if(this.walkerGrid[this.height - 1 - h][w][i])
                timesClaimed++;
        }
        if(timesClaimed == 1){
            switch(id){
                case RED: panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.red);
                    break;
                case GREEN: panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.green);
                    break;
                case BLUE: panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.blue);
                    break;
                case YELLOW: panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.yellow);
                    break;
                default: panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.gray);
                    break;
            }
        } else if(timesClaimed < NUM_OF_WALKERS)
            panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.pink);
        else //has been claimed by all walkers
            panels[((this.height - 1 - h) * this.width) + w].setBackground(Color.white);

    }

    /********************************************************************************************
     *  Function Name:  getGrid
     *  Return Type:    boolean[height][width][is claimed by (walkerId)]  //3d array of booleans
     *  Description:    Returns the current grid
     *******************************************************************************************/
    public boolean[][][] getGrid(){ return this.walkerGrid; }

    /********************************************************************************************
     *  Function Name:  setGrid
     *  Return Type:    boolean
     *  Description:    If the walker has already set this location then do nothing.
     *                  If walker has not set this location set location to true and
     *                  return true.
     *******************************************************************************************/
    private boolean setGrid(int h, int w, int id){
        if(walkerGrid[this.height - 1 - h][w][id])    //if already set to true
            return false;
        walkerGrid[this.height - 1 - h][w][id] = true;    //else
        return true;
    }

    /********************************************************************************************
     *  Function Name:  getActiveGame
     *  Return Type:    boolean
     *  Description:    Returns if game is active.
     *******************************************************************************************/
    public boolean getActiveGame(){ return this.gameInProgress; }

    /********************************************************************************************
     *  Function Name:  getWidth
     *  Return Type:    int
     *  Description:    Returns the width of the grid.
     *******************************************************************************************/
    public int getWidth(){return this.width;}

    /********************************************************************************************
     *  Function Name:  getHeight
     *  Return Type:    int
     *  Description:    Returns the height of the grid.
     *******************************************************************************************/
    public int getHeight(){return this.height;}

    /********************************************************************************************
     *  Function Name:  getWalkerXPos
     *  Return Type:    int
     *  Description:
     *******************************************************************************************/
    public int getWalkerXPos(int walkerNum){return walkers[walkerNum].getXPos();}

    /********************************************************************************************
     *  Function Name:  getWalkerYPos
     *  Return Type:    int
     *  Description:
     *******************************************************************************************/
    public int getWalkerYPos(int walkerNum){return walkers[walkerNum].getYPos();}


    /********************************************************************************************
     *  Function Name:  getWinner
     *  Return Type:    int
     *  Description:    returns the id of the winner, returns -1 if there is no
     *                  winner / game in progress.
     *******************************************************************************************/
    public int getWinner(){
        if(gameInProgress)//if not all walkers have finished return -1
            return -1;
        return winnerId; //if all walkers have finished return winner's id.
    }

    /********************************************************************************************
     *  Function Name:  getNumOfWalkers
     *  Return Type:    int
     *  Description:    Returns the number of walker pawns in the game.
     *******************************************************************************************/
    public int getNumOfWalkers(){return this.NUM_OF_WALKERS;}

    /********************************************************************************************
     *  Function Name:  submitWinner
     *  Return Type:    void
     *  Description:    First access will set the accessor to be the winner, all subsequent
     *                  accesses will award the pawn's placement in console output.
     *******************************************************************************************/
    private synchronized void submitWinner(int winner, int spaceCount){
        if(winnerId == -1) {    //if winner is not yet determined.
            winnerId = winner;
            System.out.print("Player " + winner);

            switch(winner){
                case 0: System.out.print("(red)");
                    break;
                case 1: System.out.print("(green)");
                    break;
                case 2: System.out.print("(blue)");
                    break;
                case 3: System.out.print("(yellow)");
                    break;
            }
            System.out.println(" is the winner with " + spaceCount + " spaces");
        } else {    //has not won but has finished.
            System.out.print("Player " + winner);

            switch(winner){
                case 0: System.out.print("(red)");
                    break;
                case 1: System.out.print("(green)");
                    break;
                case 2: System.out.print("(blue)");
                    break;
                case 3: System.out.print("(yellow)");
                    break;
            }

            System.out.println(" has now finished in place " + place + " with " + spaceCount + " spaces");
        }
        place++;    //goes up every time somebody has finished to determine rankings.
        if (place > NUM_OF_WALKERS) { //Place will be n+1 at the end if there are n walkers.
            this.gameInProgress = false;
            this.reset();
        }
    }

    /********************************************************************************************
     *  Class Name:     Walker
     *  Description:    Walker class, the pawn for the thread based walker game.
     *******************************************************************************************/
    private class Walker extends Thread {
        private int id, claimedSpaceCount, xPos, yPos;
        final private int MAX_SPACES = height * width;

        /********************************************************************************************
         *  Function Name:  Walker
         *  Return Type:    None(Constructor)
         *  Description:    Initializes the Walker class to the specified defaults.
         *                  id = (inputted id), claimedSpaceCount = 0, x any y position
         *                  of walker based on its unique id.
         ********************************************************************************************/
        public Walker(int i){
            this.id = i;    //uniquely identifies each walker class.
            this.claimedSpaceCount = 0; //to verify that winner is correct.
            switch(id){
                case 0: this.xPos = 0;  //starts top left
                        this.yPos = height - 1;
                    break;
                case 1: this.xPos = width - 1;  //starts top right
                        this.yPos = height - 1;
                    break;
                case 2: this.xPos = 0;  //starts bottom left
                        this.yPos = 0;
                    break;
                case 3: this.xPos = width - 1;  //starts bottom right
                        this.yPos = 0;
                    break;
                default: this.xPos = 0; //for cases 5 to infinity map to bottom left corner.
                         this.xPos = 0; //Probably won't look as cool in visual form.
                    break;
            }
        }

        /********************************************************************************************
         *  Function Name:  setId
         *  Return Type:    void
         *  Description:    Sets the id of the current walker to new inputted value.
         *******************************************************************************************/
        private void setId(int newId){
            this.id = newId;
        }

        /********************************************************************************************
         *  Function Name:  getXPos
         *  Return Type:    int
         *  Description:
         *******************************************************************************************/
        private int getXPos(){return this.xPos;}

        /********************************************************************************************
         *  Function Name:  getYPos
         *  Return Type:    int
         *  Description:
         *******************************************************************************************/
        private int getYPos(){return this.yPos;}

        /********************************************************************************************
         *  Function Name:  reset
         *  Return Type:    void
         *  Description:    Resets claimedSpaceCount to 0 as well as resets to the original
         *                  position based on the walker ID.
         *******************************************************************************************/
        private void reset(){
            this.claimedSpaceCount = 0;
            switch(id){
                case 0: this.xPos = 0;  //starts top left
                    this.yPos = height - 1;
                    break;
                case 1: this.xPos = width - 1;  //starts top right
                    this.yPos = height - 1;
                    break;
                case 2: this.xPos = 0;  //starts bottom left
                    this.yPos = 0;
                    break;
                case 3: this.xPos = width - 1; //starts bottom right
                    this.yPos = 0;
                    break;
                default: System.out.println(id + " does not have proper placement data.");
                    break;
            }
        }

        /********************************************************************************************
         *  Function Name:  move
         *  Return Type:    void
         *  Description:    Move to a new space that isn't out of bounds. If walker hasn't
         *                  been to space before add to claimedSpaceCount, otherwise do nothing.
         *                  Walker is equally likely to move in all 4 cardinal directions.
         *
         *                  Uses the arbitrary order of left, right, up, down in that
         *                  order for switches.
         *******************************************************************************************/
        private void move(){
            /*These random numbers are intended for switch statements in moving decision.*/
            int fourMoves = (int)(Math.random() * 4);   //0 - 3
            int threeMoves = (int)(Math.random() * 3);  //0 - 2
            int twoMoves = (int)(Math.random() * 2);    //0 - 1
            if(this.xPos + 1 == width) {    //is it on right side?
                if(this.yPos + 1 == height){    //is it top right?
                    switch(twoMoves){
                        case 0:
                            this.xPos--;
                            break;
                        case 1:
                            this.yPos--;
                            break;
                    }
                } else if(this.yPos == 0){  //is it bottom right?
                    switch(twoMoves){
                        case 0:
                            this.xPos--;
                            break;
                        case 1:
                            this.yPos++;
                            break;
                    }
                } else {    //just on right side, not touching top or bottom
                    switch(threeMoves){
                        case 0:
                            this.xPos--;
                            break;
                        case 1:
                            this.yPos++;
                            break;
                        case 2:
                            this.yPos--;
                            break;
                    }
                }
            } else if(this.xPos == 0){  //okay, well is it on left side?
                if(this.yPos + 1 == height){    //is it top left?
                    switch(twoMoves){
                        case 0:
                            this.xPos++;
                            break;
                        case 1:
                            this.yPos--;
                            break;
                    }
                } else if(this.yPos == 0){  //is it bottom left?
                    switch(twoMoves){
                        case 0:
                            this.xPos++;
                            break;
                        case 1:
                            this.yPos++;
                            break;
                    }
                } else {    //just on left side, not touching top or bottom
                    switch(threeMoves){
                        case 0:
                            this.xPos++;
                            break;
                        case 1:
                            this.yPos++;
                            break;
                        case 2:
                            this.yPos--;
                            break;
                    }
                }
            } else if(this.yPos == 0){  //is it on bottom?
                switch(threeMoves){
                    case 0:
                        this.xPos--;
                        break;
                    case 1:
                        this.xPos++;
                        break;
                    case 2:
                        this.yPos++;
                        break;
                }
            } else if(this.yPos + 1 == height){ // is it on top?
                switch(threeMoves){ //can't move up and isn't on the sides
                    case 0:
                        this.xPos--;
                        break;
                    case 1:
                        this.xPos++;
                        break;
                    case 2:
                        this.yPos--;
                        break;
                }
            } else {    //every move is fine in this section
                switch(fourMoves){  //left, right, up, down
                    case 0:
                        this.xPos--;
                        break;
                    case 1:
                        this.xPos++;
                        break;
                    case 2:
                        this.yPos++;
                        break;
                    case 3:
                        this.yPos--;
                        break;
                }
            }
        }

        /********************************************************************************************
         *  Function Name:  run
         *  Return Type:    void
         *  Description:    while the amount of spaces it has claimed is < MAX_SPACES
         *                  1.) sleep 2.) claim 3.) move
         *******************************************************************************************/
        public void run() {
            while(this.claimedSpaceCount < MAX_SPACES){//while you haven't traveled to every space.
                try { Thread.sleep((int)(Math.random() * SLEEP_TIMER)); } //random 0-(SLEEP_TIMER + 1) ms sleep
                catch (InterruptedException e) {}
                if(setGrid(this.yPos,this.xPos,this.id)) {    //claim
                    claimedSpaceCount++;    //has claimed new space, add to counter.
                    updateSpaces(this.yPos,this.xPos,this.id);/*inform JFrame application that new space has been
                                                                claimed, update graphics*/
                }
                move(); //move
            }
            submitWinner(this.id, this.claimedSpaceCount);  //if it has escaped loop then it has won.
        }
    }

    private class Display extends JFrame {
        public Display(int w, int h, int row, int col){
            this.setTitle("Walker: By Nolan R");
            this.setSize(w,h);//sets window to pixel width and height desired
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closing application stops program
            GridLayout grid = new GridLayout(row,col);//should create a layout for additions to JFrame to follow
            this.setLayout(grid);//applies this layout

            /*Used for adding each individual tile to JFrame*/
            for(int i = 0; i < row; i++){//height
                for(int j = 0; j < col; j++){//width
                    JPanel panel = new JPanel(grid);//creates a new panel to be added to JFrame
                    panel.setBackground(Color.black);//makes panels default color
                    this.add(panel);//adds newly created panel to JFrame
                }
            }

            this.setMinimumSize(new Dimension(400,400));
            this.setVisible(true);//after everything has been done, change the visibility to on
        }
    }
}
