package uk.ac.soton.comp1206.scene;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {
    GamePane pane;
    private Game game;
    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    private Label score;
    private Label lives;
    private Label level;
    private Label multiplayer;
    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }
    //Accessor for the label score
    public Label getScore(){
        return score;
    }
    //Accessor for the label level
    public Label getLevel(){
        return level;
    }
    //Accessor for the label lives
    public Label getLives(){
        return lives;
    }
    //Accessor for the label multiplayer
    public Label getMultiplayer(){
        return multiplayer;
    }
    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        setupGame();

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);

        var board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setCenter(board);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);


    }
    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();
    }
    public void getLabels(){
        //Binding the labels with their accessors to the Game SimpleIntegerProperties accessors
        getScore().textProperty().bind(game.getScore().asString());
        getLevel().textProperty().bind(game.getLevel().asString());
        getLives().textProperty().bind(game.getLives().asString());
        getMultiplayer().textProperty().bind(game.getMultiplayer().asString());
    }
}
