# rush-hour-solver-GUI
Solver for different scenarios of the puzzle game rush hour in Java with basic GUI in JavaFX

## solver algorithm
The Solver is based on a BFS algorithm where the program follows each path until the destination(solution) is reached. The rush hour solver in particular is a variation of this algorithm with backtracking tied in. The rush hour solver uses this backtracking to solve the rush hour puzzle more efficiently.

## text files
The text files are entered in a specific format.
- Each file begins with the size of the puzzle (ex 5X5 or 7X8)
- Next the number of total cars is listed
- Finally all the cars are listed with each represented by a letter (ex A 0 1 0 2 0 3)
- The main 2X1 car the is to exit is always represented by 'X'

## run configurations
Run configurations for running different text files representing different rush hour puzzles

### solver for a textfile puzzle
![rush hour textfile for puzzle configuration](rush-hour-textfile.png)

### text user interface configuration
![rush hour tui run configuration](rush-hour-tui.png)

### GUI configuration
![rush hour gui run configuration](rush-hour-gui.png)
