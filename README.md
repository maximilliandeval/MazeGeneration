# MazeGeneration
This program uses disjoint sets to randomly generate an interesting maze with only 1 solution.

The maze has the following properties:
* There is exactly one path between any two cells. This implies:
  * There is a way to solve the maze (get from entrance to exit)
  * There is a way to explore the whole maze (travel to/from every cell)
* But, it is still complex looking:
  * There are no wide-open spaces
  * There are no inaccessible parts of the maze that a person can just ignore by eye
  * There are just enough openings between cells to allow for a way in and out of every one, but there are still a lot of walls

After compiling all files, invoke the program with:
`java MazeGen <R> <C> <FILENAME.txt>`
R and C should be positive integers indicating the number of rows and columns. The third optional argument is the name of a file in which to store the maze output. If there is no third argument, or the third argument is a single hyphen, the maze output will be printed to standard output.
