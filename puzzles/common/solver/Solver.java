package puzzles.common.solver;

/**
 * Description: Solver Class which can solve with the generic Configuration
 * Solver is based on a BFS backtracking algorithm and can be used on any puzzle
 *
 * @Author Justin Mishic
 */

import java.util.*;

public class Solver {
    //predecessors is a map of key: seen Configs and value: their predecessor
    private Map<Configuration, Configuration> predecessors;
    private Queue<Configuration> queue;
    private Configuration startConfig;
    private Configuration endConfig;
    private int totalConfigs;
    private int uniqueConfigs;

    public Solver(Configuration start) {
        predecessors = new HashMap<>();
        queue = new LinkedList<>();
        startConfig = start;
        //endConfig = end;
    }//end of constructor

    public List<Configuration> solve(boolean prints) {
        //primes it with the start Config
        queue.add(startConfig);
        predecessors.put(startConfig, startConfig);
        totalConfigs++;
        uniqueConfigs++;

        //go through the queue
        while (!queue.isEmpty()) {
            //our current config to work on is the next on the queue
            Configuration currConfig = queue.poll();

            //check to see if we solved it
            if (currConfig.isSolution()) {
                if (prints) {
                    //print out facts
                    System.out.println("Total configs: " + totalConfigs);
                    System.out.println("Unique configs: " + uniqueConfigs);
                }

                endConfig = currConfig;

                return getPath(predecessors);
            }

            //get neighbors of current and loop through them
            for (Configuration config: currConfig.getNeighbors()) {
                //add one to the total configs
                totalConfigs++;

                //if it has not already been seen add it to the queue and add it the predecessors
                if (!predecessors.containsKey(config)) {
                    //add one to the unique configs
                    uniqueConfigs++;

                    predecessors.put(config, currConfig);
                    queue.add(config);
                }//end of if not in predecessors
            }//end of for each neighbor config
        }//end of queue while

        if (prints) {
            //print out facts
            System.out.println("Total configs: " + totalConfigs);
            System.out.println("Unique configs: " + uniqueConfigs);
        }

        //returns null if a path was never found
        return null;
    }//end of solve

    private List<Configuration> getPath(Map<Configuration, Configuration> predecessors) {
        //create empty path
        List<Configuration> path = new LinkedList<>();

        //if predecessors contain the end
        if (predecessors.containsKey(endConfig)) {
            //work our way back from the end of the path setting the current to the end or finish
            Configuration currConfig = endConfig;

            //while we haven't yet found the full path
            while (currConfig != startConfig) {
                //add current to the front of the linked list
                //  this makes current point to the previous head of the linked list and become the head
                //  hence how the path is built in reverse
                path.add(0, currConfig);

                //set current to old current's predecessor
                currConfig = predecessors.get(currConfig);
            }
            //finally add the start config to the head of the linked list because we were adding before we get the
            //  predecessor, so we got the start, but never added it to the path
            path.add(0, startConfig);
        }
        return path;
    }
}//end of class
