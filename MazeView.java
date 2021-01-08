import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MazeView implements Runnable {

    private static class MazeDraw extends JComponent {
	private static final long serialVersionUID = 4374600290553516425L;
	private int rows = 0, cols = 0;     // dimensions
        private boolean[] right;      // right walls
        private boolean[] bot;        // bottom walls
        private String name = "MazeViewer"; // title of maze
        
        // Drawing constants
        private final int Dth = 4;  // thickness of lines
        private final int Dsz = 14;  // size of cell
        private final int Dmg = 10; // margin
        
        // Utility method to convert from (row,col) to [0..w*h]
        private int onedim(int row, int col)
        { return row * cols + col; }
    
        // Adjust outer borders
        private void fixCellBorders()
        {
            for (int i = 0; i < rows-1; i++)
                right[onedim(i, cols-1)] = true;
            right[onedim(rows-1, cols-1)] = false;
        
            for (int i = 0; i < cols; i++)
                bot[onedim(rows-1, i)] = true;
        }
    
        public String title()
        { return name; }

        public int getRows()
        { return rows; }

        public int getCols()
        { return cols; }
        
        public boolean getRight(int row, int col)
        {
            return right[onedim(row, col)];
        }
    
        public boolean getBot(int row, int col)
        {
            return bot[onedim(row, col)];
        }
        
        
        // Constructor
	// Creates a MazeDraw object to display a maze
        public MazeDraw(String filename) throws InputMismatchException
        {
            super();
            
            Scanner scan;
            boolean disk = true;
            
	    // If the parameter is "-", get input from stdin
            if (filename.equals("-")) {
                scan = new Scanner(System.in);
                filename = "stdin";
                disk = false;
	    // Otherwise, read input from the specified filepath
            } else {
                try {
                    scan = new Scanner(new File(filename));
                } catch (FileNotFoundException e) {
                    throw new InputMismatchException("could not open " + filename);
                }
            }
    	    // Make sure specified file is populated
            if (!scan.hasNextLine()) {
                if (disk) scan.close();
                throw new InputMismatchException(filename + " is empty");
            }
    	    // Get desired maze dimensions from first line of file
            String[] line = scan.nextLine().split("\\s+");
            if (line.length < 3 || !(line[0].equals("maze"))) {
                if (disk) scan.close();
                throw new InputMismatchException(filename + " contains invalid maze file format");
	    }
            try {
                rows = Integer.parseInt(line[1]);
                cols = Integer.parseInt(line[2]);
                if (rows < 1 || cols < 1) {
                    if (disk) scan.close();
                    throw new InputMismatchException(String.format("%s contains a maze with nonpositive dimension(s) %d x %d", filename, rows, cols));
                }
            } catch (NumberFormatException e) {
                if (disk) scan.close();
                throw new InputMismatchException(String.format("%s contains a maze with non-integer dimension(s) [\"%s\" x \"%s\"]", filename, line[1], line[2]));
            }
            
            this.name = String.format("%s: %s (%dx%d)", name, filename, rows, cols);

            right = new boolean[rows * cols];
            bot = new boolean[rows * cols];
            for(int i = 0; i < rows * cols; i++) {
                int r;
                int b;
    
                try {
                    r = scan.nextInt();
                    b = scan.nextInt();
                } catch (InputMismatchException e) {
                    if (disk) scan.close();
                    throw new InputMismatchException(String.format("%s contains bad cell description at row=%d, col=%d\n", filename, i / rows, i % rows));
                } catch (NoSuchElementException e) {
                    if (disk) scan.close();
                    throw new InputMismatchException(String.format("%s missing cell descriptions starting at row=%d, col=%d\n", filename, i / rows, i % rows));
                } 
    
                right[i] = (r != 0);
                bot[i] = (b != 0);
            }
     
            if (disk) scan.close();
                
            fixCellBorders();
            
            setBackground(Color.white);
            setOpaque(true);
        }
        
        
        // Draws the maze in the player window
        // Parameter g represents display context for GUI.
        public void paint(Graphics g) {
    
            super.paint(g);
            
            // For cell drawing, ignore margins
            g.translate(Dth + Dmg, Dth + Dmg);
    
            // Draw top line
            g.setColor(Color.black);
            g.fillRect(-Dth, -Dth, cols * Dsz + Dth, Dth);
            
            // Draw left line
            g.fillRect(-Dth, Dsz - Dth, Dth, (rows - 1) * Dsz + Dth);
            
	    // Draw walls
            for (int row = 0; row < rows; row++)
            {
                for (int col = 0; col < cols; col++)
                {
                    if (getRight(row, col))
                        g.fillRect(col * Dsz + Dsz - Dth, row * Dsz - Dth, Dth, Dsz + Dth); 
                    if (getBot(row, col))
                        g.fillRect(col * Dsz - Dth, row * Dsz + Dsz - Dth, Dsz + Dth, Dth);
                }
                
            }
        }    
        
    }

    private MazeDraw maze;
    private MazeView(MazeDraw maze) {
        this.maze = maze;
    }

    // Thread to create a window to display the maze.
    // Assumes a <tt>MazeDraw</tt> object has been initialized to contain the maze to display.
    public void run()
    {        
        if (maze == null)
            return;

        JFrame f = new JFrame(maze.title());
        f.setSize(Math.max(20 + 20 * maze.getCols(), 300), Math.max(20 + 20 * maze.getRows(), 300));
        f.setBackground(Color.white);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setContentPane(maze);
        f.setVisible(true);
        f.toFront();        
    }


    public static void main(String[] args)
    {
        String filename;
	// Check if input file was specified
        if (args.length < 1) {
            filename = "-";
        } else {
            filename = args[0];
        }
        // Draw the maze
        try {
            MazeDraw m = new MazeDraw(filename);
            SwingUtilities.invokeLater(new MazeView(m));
        }
        catch (InputMismatchException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
