
package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * PTUI class that creates a text version of the rush hour game
 */
public class JamPTUI implements Observer<JamModel, String> {
    private final JamModel model;
    private Scanner in;

    /**
     * constructor for PTUI
     * sets model and in
     */
    public JamPTUI() {
        model = new JamModel();
        model.addObserver(this);
        in = new Scanner( System.in );

    }

    /**
     * main method
     * @param args filename of the board to be loaded
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else {
            JamPTUI jptui = new JamPTUI();
            // loads board from filename
            jptui.loadFromFile(args[0]);
            // displays the board
            jptui.displayBoard();
            // starts the game
            jptui.gameStart();
        }
    }

    /**
     * method that runs the game until 'q' is entered
     */
    public void gameStart(){
        // options of task to perform
        System.out.println("""
                    h(int)              -- hint next move
                    l(oad) filename     -- load new puzzle file
                    s(elect) Row Col    -- select a car to be moved
                    q(uit)              -- quit the game
                    r(eset)             -- reset the current game""");

        // loops until quit is entered
        boolean quit = false;
        while(!quit) {
            // command user selected
            String command =  in.next();
            switch (command.toLowerCase()){
                // calls hint method
                case "h":
                    model.getHint();
                    break;
                // loads a new file with the filename
                case "l":
                    if (in.hasNext()) {
                        String filename = in.next();
                        loadFromFile(filename);
                    } else {
                        System.out.println("Please provide a filename");
                    }
                    break;
                // selects a car at specified index
                case "s":
                    int row = in.nextInt();
                    int col = in.nextInt();
                    if (model.select(row, col)){
                        // prompts user to move the car
                        System.out.println("Enter coordinates in which to move car to");
                        boolean movable = false;
                        // loops until valid selection is made
                        while(!movable){
                            in.next();
                            int nextRow = in.nextInt();
                            int nextCol = in.nextInt();
                            movable = model.moveCar(row, col, nextRow, nextCol);
                            if (!movable){
                                break;
                            }
                        }
                        break;
                    }
                    else {
                        System.out.println("Please enter valid integers of a car after 's'");
                    }
                    break;
                // quits from game
                case "q":
                    System.out.println("Exiting");
                    quit = true;
                    in = new Scanner(System.in);
                    break;
                // resets the board
                case "r":
                    model.resetBoard();
                    break;
                // reminds user of options again if invalid selection was made
                default:
                    System.out.println("""
                    h(int)              -- hint next move
                    l(oad) filename     -- load new puzzle file
                    s(elect) Row Col    -- select a car to be moved
                    q(uit)              -- quit the game
                    r(eset)             -- reset the current game""");

            }
        }
        in = new Scanner(System.in);
    }

    /**
     * loads a board from a specified filename
     * @param filename String
     */
    public void loadFromFile(String filename){
        model.loadBoardFromFile(filename);
    }

    /**
     * displays model for the user to interact with
     */
    public void displayBoard(){
        System.out.println(model);
    }

    /**
     * updates the model
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel jamModel, String message) {
        // displays the board
        displayBoard();
        // prints out message to user
        System.out.println(message);
        // displays board and ends game once puzzle is solved
        if (model.gameOver()) {
            displayBoard();

            System.out.println("You win! You solved the puzzle in " + model.getMoves() + " moves!");
        }
    }

}

