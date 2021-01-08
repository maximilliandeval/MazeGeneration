# MazeGeneration
This program uses disjoint sets to randomly generate and display interesting maze with only one solution path.  I implemented the disjoint set data structure with weighted-union-by-rank and path compression.

The generated maze has the following properties:
* There is exactly one path between any two cells. This implies:
  * There is a way to solve the maze (get from entrance to exit)
  * There is a way to explore the whole maze (travel to/from every cell)
* But, it is still complex looking:
  * There are no wide-open spaces
  * There are no inaccessible parts of the maze that a person can just ignore by eye
  * There are just enough openings between cells to allow for a way in and out of every one, but there are still a lot of walls

After compiling all files, you can invoke the program with:
`java MazeGen <R> <C> | java MazeView`
where R and C are positive integers indicating the number of rows and columns.
