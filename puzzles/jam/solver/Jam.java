package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * main class for the rush hour game
 */
public class Jam {

    /**
     * main method that reads in file
     * @param args contains file name
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else {
            String filename = args[0];
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
                JamConfig startConfig = new JamConfig(puzzleGrid, row, col, cars, keyList);

                Solver solver = new Solver(startConfig);
                List<Configuration> path = solver.solve(true);

                if (path != null) {
                    for (int i = 0; i < path.size(); i++) {
                        System.out.println("Step " + i + ": \n" + path.get(i));
                    }
                } else {
                    System.out.println("No path found :(");
                }
            }
        }
    }
}