
package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;

public class JamGUI extends Application  implements Observer<JamModel, String>  {

    /** The resources directory is located directly underneath the gui package */

    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static String WHITE = "#FFFFFF";
    private final static String LIGHT_GRAY = "#C0C0C0";
    private final static String DARK_GRAY = "#606060";
    private final static String BLACK = "#000000";
    private final static String LIGHT_PINK = "#FF99CC";
    private final static String MAGENTA = "#FF3399";
    private final static String PINK = "#FF66FF";
    private final static String PURPLE = "#990099";
    private final static String DARK_PURPLE = "#6600cc";
    private final static String LIGHT_PURPLE = "#B266FF";
    private final static String GRAY_BLUE = "#9999FF";
    private final static String BLUE = "#0000FF";
    private final static String LIGHT_BLUE = "#3399FF";
    private final static String CYAN = "#66FFFF";
    private final static String GREEN_BLUE = "#00CCCC";
    private final static String GREEN_GRAY = "#009999";
    private final static String SEA_GREEN = "#33FF99";
    private final static String LIME = "#66FF66";
    private final static String BARF_GREEN = "#006600";
    private final static String PASTEL_YELLOW = "#FFFF99";
    private final static String YELLOW = "#FFFF33";
    private final static String BROWN_YELLOW = "#999900";
    private final static String BIEGE = "#994C00";
    private final static String ORANGE = "#FF8000";
    private final static String CORAL_ORANGE = "#FFB266";
    private final static String BRICK = "#990000";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;
    private JamModel model;
    private Label message;
    private Label moves;
    private GridPane gridPane;
    private boolean moveCar;
    private int storedRow;
    private int storedCol;


    /**
     * method to initialize
     */
    public void init() {
        System.out.println("init: Initialize and connect to model!");
        this.model = new JamModel();
        model.addObserver(this);
        message = new Label();
        gridPane = new GridPane();
        moves = new Label();
        moveCar = false;
        storedRow = -1;
        storedCol = -1;
        String filename = getParameters().getRaw().get(0);

        model.loadBoardFromFile(filename);
    }

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        // create hbox for top row
        HBox hboxTop = new HBox();
        // create buttons for bottom row

        // load game button
        Button loadGame = new Button("Load");
        // what happens when button is clicked
        loadGame.setOnAction(event -> {
            //create a new FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/jam"));
            //open up a window for the user to interact with.
            File selectedFile = fileChooser.showOpenDialog(stage);
            // exception is handled here is file isn't loaded
            if (selectedFile == null){

            }
            else {
                model.loadBoardFromFile(String.valueOf(selectedFile));
            }
        });

        // reset game button
        Button resetGame = new Button("Reset");
        // what happens when button is clicked
        resetGame.setOnAction(event -> {
            model.resetBoard();
            System.out.println("Game Reset");
        });

        // hint button
        Button hint = new Button("Hint");
        // what happens when hint button is clicked
        hint.setOnAction(event -> {
            model.getHint();
        });

        for(int i = 0; i < model.getRow(); i++){
            for(int j = 0; j < model.getCol(); j++){
                Button carBtn = new Button();
                carBtn.setMinSize(ICON_SIZE, ICON_SIZE);
                carBtn.setMaxSize(ICON_SIZE, ICON_SIZE);
                int finalI = i;
                int finalJ = j;
                carBtn.setOnAction(event -> {
                    if(moveCar){
                        model.moveCar(storedRow, storedCol, finalI, finalJ);
                        moveCar = false;
                        System.out.println("moved to " + finalI + " " + finalJ);
                    }
                    else if(model.select(finalI, finalJ)){
                        storedCol = finalJ;
                        storedRow = finalI;
                        moveCar = true;
                        System.out.println("selected at " + finalI + " " + finalJ);
                    }
                });
                gridPane.add(carBtn, finalJ, finalI);
            }
        }

        // create all the panes
        FlowPane fp = new FlowPane();
        BorderPane bp = new BorderPane();

        // add text to the hbox
        Text movesText = new Text("Moves ");
        moves.setText(model.getMoves() + " ");
        message.setText(" Message: ");
        hboxTop.getChildren().add(movesText);
        hboxTop.getChildren().add(moves);
        hboxTop.getChildren().add(message);
        // centers hbox
        hboxTop.setAlignment(Pos.CENTER);

        /*Button button1 = new Button();
        button1.setStyle(
                "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                        "-fx-background-color: " + X_CAR_COLOR + ";" +
                        "-fx-font-weight: bold;");
        button1.setText("X");
        button1.setMinSize(ICON_SIZE, ICON_SIZE);
        button1.setMaxSize(ICON_SIZE, ICON_SIZE);*/

        // add buttons to flow pane
        fp.getChildren().add(resetGame);
        fp.getChildren().add(loadGame);
        fp.getChildren().add(hint);
        // centers flow pane
        fp.setAlignment(Pos.CENTER);

        // create the main border pane with hbox in the top
        // gridPane of the cars in the center
        // flow pane of buttons in the bottom
        bp.setTop(hboxTop);
        bp.setCenter(gridPane);
        bp.setBottom(fp);

        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        message.setText(msg);
        moves.setText(model.getMoves() + " ");
        if(gridPane.getChildren().size() > 0){

            gridPane.getChildren().clear();

            for (int i = 0; i < model.getCol(); i++) {
                for (int j = 0; j < model.getRow(); j++) {
                    String buttonColor = switch (model.getCurrentConfig().getBoard()[i][j]) {
                        case 'A' -> YELLOW;
                        case 'B' -> ORANGE;
                        case 'C' -> LIME;
                        case 'D' -> BRICK;
                        case 'E' -> CYAN;
                        case 'F' -> PURPLE;
                        case 'G' -> LIGHT_PINK;
                        case 'H' -> DARK_GRAY;
                        case 'I' -> CORAL_ORANGE;
                        case 'J' -> LIGHT_PURPLE;
                        case 'K' -> PASTEL_YELLOW;
                        case 'L' -> MAGENTA;
                        case 'M' -> LIGHT_GRAY;
                        case 'N' -> LIGHT_BLUE;
                        case 'O' -> DARK_PURPLE;
                        case 'P' -> PINK;
                        case 'Q' -> BARF_GREEN;
                        case 'R' -> BLUE;
                        case 'S' -> GRAY_BLUE;
                        case 'T' -> SEA_GREEN;
                        case 'U' -> BLACK;
                        case 'V' -> GREEN_BLUE;
                        case 'W' -> GREEN_GRAY;
                        case 'X' -> X_CAR_COLOR;
                        case 'Y' -> BIEGE;
                        case 'Z' -> BROWN_YELLOW;
                        default -> WHITE;
                    };
                    System.out.println(buttonColor);
                    Button btn = new Button(Character.toString(model.getCurrentConfig().getBoard()[i][j]));
                    btn.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";");
                    btn.setStyle("-fx-background-color: " + buttonColor + ";");
                    btn.setStyle("-fx-font-weight: bold;");
                    if(model.getCurrentConfig().getBoard()[i][j] != '.'){
                        btn.setText(Character.toString(model.getCurrentConfig().getBoard()[i][j]));
                    }
                    else{
                        btn.setText(" ");
                    }

                    btn.setMinSize(ICON_SIZE, ICON_SIZE);
                    btn.setMaxSize(ICON_SIZE, ICON_SIZE);
                    btn.setStyle("-fx-border-color: black;");
                    gridPane.add(btn, j, i);
                }}
        }
        if (model.gameOver()) { //checks if game is over.
            message.setText(" Message: You win!");
            System.out.println("You win. Good for you.");
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
