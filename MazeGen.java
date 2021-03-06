import java.io.*;
import java.util.Random;
import java.util.ArrayList;

/*
Uses DisjointSets data structure to randomly generate a maze where there is
exactly one path between any two cells.

Command line arguments indicate whether the maze wall data should be output
in a text file or in standard output stream.

The first line of output is:
maze <R> <C>
where R and C specify the number of rows and columns respectively

Each of the following lines consists of two integers, for example:
0 1
Both of these integers indicate whether the wall of a cell is present.
A nonzero integer indicates the wall is present; a zero indicates the wall is not present.
The first number refers to the right wall of the cell; the second number refers to the bottom wall of the cell.
There should be R*C lines that follow the first line.
The cells in the top row appear first, going left to right, followed by cells in the second row from the top from left to right,
and so on, ending with the bottom row.
*/

public class MazeGen {
    
    public static void main(String[] args) {
        // Parse command-line arguments for the width and height of maze
        if (args.length < 2) {
            System.err.println("missing number of rows and/or columns");
            System.exit(1);
            return;
        }
        int rows, cols;
        try {
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
            // Make sure dimensions are at least 1 x 1
            if (rows < 1 || cols < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("invalid arguments for maze size (number of rows or columns)");
            System.exit(1);
            return;
        }

        // MAZE GENERATION
        
        // Generate a rows x cols maze where none of the cells are connected (every cell
        // has 4 walls):
        DisjointSets<Integer> maze = new DisjointSets<Integer>();
        for (int i = 0; i < rows * cols; i++) {
            maze.insert(i);
        }

        // This ArrayList will contain the cells that DO have a wall on the right
        ArrayList<Integer> right = new ArrayList<Integer>();
        // This ArrayList will contain the cells that DO have a wall below it
        ArrayList<Integer> bottom = new ArrayList<Integer>();

        // Now each cell must be connected by removing walls randomly until all elements are
        // part of the same set:
        Random rand = new Random();
        while (maze.numSets() != 1) {
            // Choose random cell
            int cell = rand.nextInt(maze.numElements());
            // Randomly choose right wall (0) or bottom wall (1)
            int wall = rand.nextInt(2);

            // If targeted wall is on the right border or the bottom border, do not remove it
            if ( ((wall==0)&&((cell+1) % cols == 0)) || ((wall==1)&&(cell >= (maze.numElements()-cols))) ) {
                continue;
            }

            // Unite current cell with the cell to its right, if they are not already in the same set
            if (wall==0) {
                boolean passed = maze.union(cell, cell+1);
                if (passed) {
                    // Record that current cell should NOT have a wall to the right
                    right.add(cell);
                }
            // Unite current cell with the cell below it, if they are not already in the same set
            } else {
                boolean passed = maze.union(cell, cell+cols);
                if (passed) {
                    // Record that current cell should NOT have a wall below it
                    bottom.add(cell);
                }
            }
        }

        PrintStream out;
        // Check if the output wall data should go to a text file or standard out
        if (args.length < 3 || args[2].equals("-")) {
            out = System.out;
        } else {
            try {
                out = new PrintStream(args[2]);
            } catch (FileNotFoundException e) {
                System.err.println("error opening file " + args[2] + " for writing");
                System.exit(2);
                return;
            }
        }
        
        // Output the header line
        out.println("maze " + rows + " " + cols);
        
        // Output the wall data for every cell in the maze
        for (int cell = 0; cell < maze.numElements(); cell++) {
            // If the current cell does not have a right wall, print 0.  Otherwise print 1.
            if ((cell+1) % cols != 0) {
                if (right.contains(cell)) {
                    System.out.print("0");
                } else {
                    System.out.print("1");
                }
            } else {
                System.out.print("1");
            }

            // If the current cell does not have a bottom wall, print 0.  Otherwise print 1.
            if (cell < (maze.numElements()-cols)) {
                if (bottom.contains(cell)) {
                    System.out.println(" 0");
                } else {
                    System.out.println(" 1");
                }
            } else {
                System.out.println(" 1");
            }
        }

        // Close output stream if it's a file
        if (out != System.out)
            out.close();
    }

}
