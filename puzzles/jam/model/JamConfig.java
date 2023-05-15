package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Configuration of a jam puzzle
 * @Author Justin Mishic
 */
public class JamConfig implements Configuration {
    private char board[][];
    private static int ROW;
    private static int COL;
    private static final char FINALCAR = 'X';
    private static final char BLANK = '.';
    private HashMap<Character, int[]> cars;
    private static ArrayList<Character> kList;

    /**
     *
     * @param board characters representing the cars
     * @param ROW how many rows on the board
     * @param COL how many columns on the board
     * @param cars hash map of each car and its location
     * @param kList list of car 'titles'
     */
    public JamConfig(char[][] board, int ROW, int COL, HashMap<Character, int[]> cars, ArrayList<Character> kList){
        JamConfig.COL = COL;
        JamConfig.ROW = ROW;
        // creats an empty board
        this.board = copyBoard(board);
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COL; j++){
                this.board[i][j] = BLANK;
            }
        }
        // copies over hash map of cars
        this.cars = copy(cars);
        JamConfig.kList = kList;
        // loops through map and adds each car into its respective spot
        for (char key : cars.keySet()){
            int[] cords = cars.get(key);
            // if the rows are the same, add going across row
            if (cords[0] == cords[2]){
                for (int i = cords[1]; i <= cords[3]; i++){
                    this.board[cords[0]][i] = key;
                }
            }
            // if the columns are the same, add going down column
            else {
                for (int i = cords[0]; i <= cords[2]; i++){
                    this.board[i][cords[1]] = key;
                }
            }
        }
    }

    /**
     * second constructor for JamCOnfig
     * @param board board of cars
     * @param cars hashmap of cars to loop through
     */
    public JamConfig(char[][] board, HashMap<Character, int[]> cars) {
        // copies over board and cars
        this.board = copyBoard(board);
        this.cars = copy(cars);
    }

    /**
     * creates deep copy of hash map
     * @param original original hash map to be copied
     * @return hash map
     */
    public static HashMap<Character, int[]> copy(HashMap<Character, int[]> original) {
        HashMap<Character, int[]> copy = new HashMap<>();
        // loops through each car in the map
        for (Map.Entry<Character, int[]> entry : original.entrySet())
        {
            copy.put(entry.getKey(),
                    copyArray(entry.getValue()));
        }
        return copy;
    }

    /**
     * creates a deep copy of an array
     * @param intArray array to be copied
     * @return array of integers
     */
    public static int[] copyArray(int[] intArray){
        // creates new array
        int[] copyArray = new int[intArray.length];
        // loops through length and adds each individually
        for (int i=0; i<intArray.length; i++){
            copyArray[i] = intArray[i];}

        return copyArray;
    }

    /**
     * checks to see if current board is the solution
     * @return boolean
     */
    @Override
    public boolean isSolution() {
        // loops through column values
        for(int i = 0; i < ROW; i++){
            // checks to see if final column contains 'X'
            if(this.board[i][COL - 1] == FINALCAR){
                return true;
            }
        }
        return false;
    }

    /**
     * gets each possible move of each car that can be moved
     * @return list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        // array list of configs to be returned
        List<Configuration> configList = new ArrayList<>();
        //char[][] newBoard = copyBoard(this.board);
        // loop over the key values of the map
        for(char c : cars.keySet()){
            //char[][] newBoard = copyBoard(this.board);
            // for loop to increase the amount of spaces moved
            // if the rows match, move horizontally
            if (cars.get(c)[0] == cars.get(c)[2]){
                // move car right by 1
                if (cars.get(c)[1] + 1 < COL && cars.get(c)[3] + 1 < COL){
                    // next two statements make sure increases car doesn't run into another car
                    if (this.board[cars.get(c)[2]][cars.get(c)[3] + 1] == BLANK || this.board[cars.get(c)[2]][cars.get(c)[3] + 1] == c){
                        if(this.board[cars.get(c)[0]][cars.get(c)[1] + 1] == BLANK || this.board[cars.get(c)[0]][cars.get(c)[1] + 1] == c){
                            // updates board with new values
                            // makes spot where car originally was blank
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i] = BLANK;
                            }
                            // creates the new car in the new spot
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i + 1] = c;
                            }
                            HashMap<Character, int[]> newCars = copy(cars);
                            newCars.get(c)[1]++;
                            newCars.get(c)[3]++;
                            char[][] newBoard = copyBoard(this.board);
                            // adds new board to the config list
                            configList.add(new JamConfig(newBoard, newCars));
                            // makes the spot where new car was added blank
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i + 1] = BLANK;
                            }
                            // adds the car back where it was originally (resetting board)
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i] = c;
                            }
                        }
                    }
                }
                // move car left by 1
                if (cars.get(c)[1] - 1 >= 0 && cars.get(c)[3] - 1 >= 0){
                    if (this.board[cars.get(c)[0]][cars.get(c)[1] - 1] == BLANK || this.board[cars.get(c)[0]][cars.get(c)[1] - 1] == c){
                        if (this.board[cars.get(c)[2]][cars.get(c)[3] - 1] == BLANK ||  this.board[cars.get(c)[2]][cars.get(c)[3] - 1] == c){
                            // updates board with new values
                            // makes spot where car originally was blank
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i] = BLANK;
                            }
                            // adds the car in the new spot
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i - 1] = c;
                            }
                            HashMap<Character, int[]> newCars = copy(cars);
                            newCars.get(c)[1]--;
                            newCars.get(c)[3]--;
                            char[][] newBoard = copyBoard(this.board);
                            // adds the new config to the list
                            configList.add(new JamConfig(newBoard, newCars));
                            // makes the spot where new car was blank
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i - 1] = BLANK;
                            }
                            // adds the car back to original spot (resetting board)
                            for (int i = cars.get(c)[1]; i <= cars.get(c)[3]; i++){
                                this.board[cars.get(c)[0]][i] = c;
                            }
                        }
                    }
                }
            }
            // if the columns match, move vertically
            else {
                // move car up by 1
                if (cars.get(c)[0] + 1 < ROW && cars.get(c)[2] + 1 < ROW){
                    if (this.board[cars.get(c)[2] + 1][cars.get(c)[3]] == BLANK || this.board[cars.get(c)[2] + 1][cars.get(c)[3]] == c){
                        if (this.board[cars.get(c)[0] + 1][cars.get(c)[1]] == BLANK || this.board[cars.get(c)[0] + 1][cars.get(c)[1]] == c){
                            // updates board with new values
                            // rewrites the car with blank values
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i][cars.get(c)[1]] = BLANK;
                            }
                            // puts the car in a new spot on the board
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i + 1][cars.get(c)[1]] = c;
                            }
                            HashMap<Character, int[]> newCars = copy(cars);
                            newCars.get(c)[0]++;
                            newCars.get(c)[2]++;
                            char[][] newBoard = copyBoard(this.board);
                            // adds configuration to the list
                            configList.add(new JamConfig(newBoard, newCars));
                            // makes new car that was added blank
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i + 1][cars.get(c)[1]] = BLANK;
                            }
                            // adds car back to the original spot (resetting the board)
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i][cars.get(c)[1]] = c;
                            }
                        }
                    }
                }
                // move car down by 1
                if (cars.get(c)[0] - 1 >= 0 && cars.get(c)[2] - 1 >= 0){
                    if (this.board[cars.get(c)[2] - 1][cars.get(c)[3]] == BLANK || this.board[cars.get(c)[2] - 1][cars.get(c)[3]] == c){
                        if (this.board[cars.get(c)[0] - 1][cars.get(c)[1]] == BLANK || this.board[cars.get(c)[0] - 1][cars.get(c)[1]] == c){
                            // updates board with new values
                            // rewrites the car with blank spots
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i][cars.get(c)[1]] = BLANK;
                            }
                            // puts car in a new position on the board
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i - 1][cars.get(c)[1]] = c;
                            }
                            HashMap<Character, int[]> newCars = copy(cars);
                            newCars.get(c)[0]--;
                            newCars.get(c)[2]--;
                            char[][] newBoard = copyBoard(this.board);
                            // adds new configuration to the list
                            configList.add(new JamConfig(newBoard, newCars));
                            // makes the spot where the new car is blank
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i - 1][cars.get(c)[1]] = BLANK;
                            }
                            // moves car back to previous position (resetting the board)
                            for (int i = cars.get(c)[0]; i <= cars.get(c)[2]; i++){
                                this.board[i][cars.get(c)[1]] = c;
                            }
                        }
                    }
                }
            }

        }
        return configList;
    }

    /**
     * creates a deep copy of the board
     * @param board original board
     * @return copy of orignal board
     */
    private char[][] copyBoard (char[][] board) {
        if (board == null){
            return null;
        }
        final char[][] newBoard = new char[board.length][];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return newBoard;
    }


    /**
     * equals method for hash map
     * @param other JamConfig to be checked
     * @return true or false
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof JamConfig) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (((JamConfig) other).board[i][j] != this.board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return result;
    }

    /**
     * hash function that is very weird because IDEK
     * @return int replacing the normal hash
     */
    @Override
    public int hashCode() {
        int code = 0;
        int counter = 0;
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COL; j++){
                if (counter % 3 == 0){
                    code *= board[i][j];
                }
                else {
                    code += board[i][j];
                }
                counter++;
            }
        }
        return code;
    }

    /**
     * Overrides the toString
     * @return the board in String form
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                //appends the next char for how many columns we have
                result.append(board[i][j]).append(" ");
            }
            //goes to the next line after
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * gets the current board
     * @return board of characters
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * gets column
     * @return int
     */
    public static int getCOL() {
        return COL;
    }

    /**
     * gets row
     * @return int
     */
    public static int getROW() {
        return ROW;
    }
}
