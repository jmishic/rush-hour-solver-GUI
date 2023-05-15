
package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.*;

/**
 * model class that represents the board
 */
public class JamModel {
    public static String LOADED = "loaded";
    public static String LOAD_FAILED = "loadFailed";
    public static String HINT_PREFIX = "Hint:";
    public static char BLANK = '.';


    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private JamConfig currentConfig;
    private int moves;
    private String originalFilename;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */

    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }


    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */

    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * loads a new board from a given file name
     * @param filename
     */
    public void loadBoardFromFile(String filename) {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            // helper lists
            String[] sList;
            String[] carList;
            char carLetter;
            List<String> aList;
            ArrayList<Character> keyList = new ArrayList<>();
            // reads in first line
            sList = br.readLine().split(" ");
            // reads second line and creates number of cars
            int numCars = Integer.parseInt(br.readLine());
            // creates a hash map for each car
            HashMap<Character, int[]> cars = new HashMap<>();
            // reads rest of lines to an ArrayList
            aList = new ArrayList<>(br.lines().toList());
            // loops through rest of lines and adds each car to map
            for (int i = 0; i < numCars; i++){
                // creates int list that has cars position on grid
                int[] carPos = new int[4];
                // separates string into list
                carList = aList.get(i).split(" ");
                // letter of car is the first element in list
                carLetter = carList[0].charAt(0);
                keyList.add(carLetter);
                // loops through rest of list and adds int to carPos array
                for(int j = 1; j < carList.length; j++){
                    carPos[j-1] = Integer.parseInt(carList[j]);
                }
                // key is cars letter and value is array of the cars position
                cars.put(carLetter, carPos);
            }
            // uses first line to create the size of the grid
            int row = Integer.parseInt(sList[0]);
            int col = Integer.parseInt(sList[1]);
            char[][] puzzleGrid = new char[row][col];

            //make our start config
            currentConfig = new JamConfig(puzzleGrid, row, col, cars, keyList);
            originalFilename = filename;
            moves = 0;
            announce("Loaded: " + filename);
        } catch (FileNotFoundException e) {
            announce("Failed to load: " + filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * when user selects hint button, the computer performs next move for them
     */
    public void getHint() {
        if (gameOver()) {
            announce("Puzzle Already Solved!");
        } else {
            // creates new solver and finds path
            Solver solver = new Solver(currentConfig);
            List<Configuration> path = solver.solve(false);

            if (path == null) {
                announce("No solution!");
            }
            else {
                // gets next move in the path and performs it
                currentConfig = (JamConfig) path.get(1);
                announce("Next Step!");
                moves++;
            }
        }
    }

    /**
     * resets the board to its original state
     */
    public void resetBoard() {
        // calls load method with original filename
        loadBoardFromFile(originalFilename);
        moves = 0;
        announce("Reset the Puzzle!");
    }

    /**
     * method user uses to select a car on the board
     * @param row row car is at
     * @param col column car is at
     * @return boolean if selection was valid or not
     */
    public boolean select(int row, int col){
        // if the board is blank it is an invalid selection, otherwise it is valid and can be moved
        if (currentConfig.getBoard()[row][col] != BLANK){
            return true;
        }
        else {
            announce("Invalid Selection!");
            return false;
        }
    }

    /**
     * moves car to a new location on the board
     * @param row row car is located at
     * @param col column car is located at
     * @param nextRow row car is being moved to
     * @param nextCol colum car is being moved to
     * @return boolean on if car can be moved or not
     */
    public boolean moveCar(int row, int col, int nextRow, int nextCol){
        int colDif = JamConfig.getCOL();
        int rowDif = JamConfig.getROW();
        int closestCol = -1;
        int closestRow = -1;
        // creates array list of car locations
        ArrayList<Integer> rowNums = new ArrayList<>();
        ArrayList<Integer> colNums = new ArrayList<>();
        // current car
        char car = currentConfig.getBoard()[row][col];
        // adds car locations to arrayList
        for(int i = 0; i < JamConfig.getROW(); i++){
            for(int j = 0; j < JamConfig.getCOL(); j++){
                if(currentConfig.getBoard()[i][j] == car){
                    rowNums.add(i);
                    colNums.add(j);
                }
            }
        }
        for(int i = 0; i < colNums.size(); i++){
            if(i == 0){
                if(colNums.get(i) == colNums.get(i + 1)){
                    break;
                }
            }
            if(Math.abs(nextCol - colNums.get(i)) < colDif){
                colDif = nextCol - colNums.get(i);
                closestCol = i;
            }
        }
        for(int i = 0; i < rowNums.size(); i++){
            if(i == 0){
                if(rowNums.get(i) == rowNums.get(i + 1)){
                    break;
                }
            }
            if(Math.abs(nextRow - rowNums.get(i)) < rowDif){
                rowDif = nextRow - rowNums.get(i);
                closestRow = i;
            }
        }
        if(closestCol == -1 && closestRow == -1){
            announce("Invalid location");
            return false;
        }
        // loops through col
        else if(closestCol != -1){
            int j = colNums.get(closestCol);
            int additional;
            if (colDif < 0){
                additional = -1;
            }
            else {
                additional = 1;
            }
            try{while(j != colNums.get(closestCol) + colDif){
                if(currentConfig.getBoard()[rowNums.get(0)][j + additional] != BLANK){
                    announce("Invalid location");
                    return false;
                }
                if (colDif < 0){
                    j--;
                }
                else {
                    j++;
                }
            }}catch (IndexOutOfBoundsException iobe){
                announce("Invalid location");
                return false;
            }
            for(int i = 0; i < rowNums.size(); i++){
                currentConfig.getBoard()[rowNums.get(i)][colNums.get(i)] = BLANK;
            }
            for(int i = 0; i < rowNums.size(); i++){
                currentConfig.getBoard()[rowNums.get(i)][colNums.get(i) + colDif] = car;
            }
            moves++;
            alertObservers("Model Changed");
            announce("Car Moved to (" + nextRow + ", " + nextCol + ")");
            return true;
        }
        // loops through row
        else {
            int j = rowNums.get(closestRow);
            int additional;
            if (rowDif < 0){
                additional = -1;
            }
            else {
                additional = 1;
            }
            try{while(j != rowNums.get(closestRow) + rowDif){
                if(currentConfig.getBoard()[j + additional][colNums.get(0)] != BLANK){
                    announce("Invalid location");
                    return false;
                }
                if (rowDif < 0){
                    j--;
                }
                else {
                    j++;
                }
            }} catch(IndexOutOfBoundsException iobe){
                announce("Invalid location");
                return false;
            }
            for(int i = 0; i < rowNums.size(); i++){
                currentConfig.getBoard()[rowNums.get(i)][colNums.get(i)] = BLANK;
            }
            for(int i = 0; i < rowNums.size(); i++){
                currentConfig.getBoard()[rowNums.get(i) + rowDif][colNums.get(i)] = car;
            }
            moves++;
            alertObservers("Model Changed");
            announce("Car Moved to (" + nextRow + ", " + nextCol + ")");
            return true;
        }
    }

    /**
     * returns number of moves it takes user to solve the puzzle
     * @return int
     */
    public int getMoves(){
        return this.moves;
    }

    /**
     * tells user if game is over or not
     * @return boolean
     */
    public boolean gameOver(){
        return currentConfig.isSolution();
    }

    /**
     * Announce to observers the model has changed;
     */
    private void announce( String message ) {
        for ( var obs : this.observers ) {
            obs.update( this, message );
        }
    }

    /**
     * prints out the board
     * @return String
     */
    @Override
    public String toString() {
        return currentConfig.toString();
    }

    /**
     * gets number of rows on the board
     * @return int
     */
    public int getRow(){
        return JamConfig.getROW();
    }

    /**
     * gets number of columns on the board
     * @return int
     */
    public int getCol(){
        return JamConfig.getCOL();
    }

    /**
     * gets the character on the board at specified location
     * @param row y value
     * @param col x value
     * @return character
     */
    public char getCar(int row, int col){
        return currentConfig.getBoard()[row][col];
    }

    /**
     * gets the models current configuration
     * @return JamConfig
     */
    public JamConfig getCurrentConfig() {
        return currentConfig;
    }
}
