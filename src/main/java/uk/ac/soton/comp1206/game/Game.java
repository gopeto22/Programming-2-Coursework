package uk.ac.soton.comp1206.game;

import java.util.ArrayList;
import java.util.Random;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {
    private SimpleIntegerProperty score = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty level = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty lives = new SimpleIntegerProperty(3);
    private SimpleIntegerProperty multiplayer = new SimpleIntegerProperty(1);
    private GamePiece currentPiece;
    private static final Logger logger = LogManager.getLogger(Game.class);

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     *
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        //Create a new grid model to represent the game state
        this.grid = new Grid(cols, rows);

    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
        nextPiece();
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
    }

    /**
     * Handle what should happen when a particular block is clicked
     *
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();
        grid.playPiece(currentPiece, x, y);
        afterPiece();
        nextPiece();
    }

    /**
     * Get the grid model inside this game representing the game state of the board
     *
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     *
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    //creation of Accessor method for the SimpleInteger property called score
    public SimpleIntegerProperty getScore(){return score;}
    //creation of Accessor method for the SimpleInteger property called level
    public SimpleIntegerProperty getLevel(){
        return level;
    }
    //creation of Accessor method for the SimpleInteger property called lives
    public SimpleIntegerProperty getLives(){
        return lives;
    }
    //creation of Accessor method for the SimpleInteger property called multiplayer
    public SimpleIntegerProperty getMultiplayer(){
        return multiplayer;

    }

    // creation of new piece using Random class with upperBound of 15 and returning it
    public GamePiece spawnPiece() {
        Random r = new Random();
        int randomPiece = r.nextInt(0, 15);
        GamePiece.createPiece(randomPiece);
        return GamePiece.createPiece(randomPiece);
    }

    public void nextPiece() {
        currentPiece = spawnPiece();
    }

    //Check if there's need of clearing lines
    public void afterPiece() {
        ArrayList<Integer> listOfRows = new ArrayList<>(rows);
        ArrayList<Integer> listOfCols = new ArrayList<>(cols);
        int oldScore = score.get();
        for (var c = 0; c < cols; c++) {
            var counterColumns = 0;
            for (var r = 0; r < rows; r++) {
                //Checking for Horizontal lines
                if (grid.get(c, r) == 0) break;
                counterColumns++;
            }
            if (counterColumns == rows) {
                listOfCols.add(c);
            }
        }
        for (var r = 0; r < rows; r++) {
            var counterRows = 0;
            for (var c = 0; c < cols; c++) {
                //Checking for Vertical lines
                if (grid.get(c,r) == 0) break;
                counterRows++;
            }
            if (counterRows == cols) {
                listOfRows.add(r);
            }
        }
            int lineCounter = listOfCols.size() + listOfRows.size();
            int blockCounter = 5 * lineCounter - (listOfCols.size() * listOfRows.size());
                if(lineCounter > 0){
            score(lineCounter, blockCounter);
                score.set(multiplayer.get() + 1);
            } else {
                multiplayer.set(1);
            }
        if(score.get() != 0 && score.get() > oldScore){
            if(score(lineCounter,blockCounter) % 1000 == 0){
                logger.info("increased level");
                level.set(level.get() + 1);
            }
        }
        //Clear Column
        for(Integer i : listOfCols) {
            clearColumn(i);
        }
        //Clear Row
        for(Integer i : listOfRows){
            clearRow(i);
        }
    }

    public void clearRow(int rowToClear) {
         for(int i = 0; i < cols; i++) {
            grid.set(i,rowToClear,0);
        }
    }

    public void clearColumn(int colToClear) {
        for(int i = 0; i < rows; i++) {
            grid.set(colToClear,i,0);
        }
    }
    public int score(int nOfLines,int nOfBlocks){
        return nOfLines * nOfBlocks * 10 * multiplayer.get();
    }
}



