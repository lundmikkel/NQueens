N-Queens Problem with BDD
=========================
by Mikkel Riise Lund, Jens Dahl Møllerhøj and Jonas Busk

This java program uses JavaBDD to solve the N-Queens problem. A GUI lets the
user see how her decisions affects the remaining solutions. A so called "interactive configurator"

This project was build for an AI class at the IT-university:
"Intelligent Systems Programming 2014"

The following is a 'Project report'. (We have to make one for our course, if you
want to keep your sanity, I would stop reading now.)

Our solution is inspired by a solver for the N-Queens problem implemented by John Whaley found here: http://javabdd.sourceforge.net/xref/NQueens.html

Project Report
==============

### Exercise Goals

Learning to use a BDD library in order to solve a specific problem. Our professor provided
a nice Java GUI implementation. We spend a lot of time finding out how to compile the jar,
and how to mark a cell on the game board as invalid (set the cells int value to -1).
The JavaBDD library turned out to be surprisingly straightforward to use.

We have not provided any tests, because we believe it is the idiomatic Java style.
Also, our methods are long, and use a lot of nested for-loops, so this is definitely idiomatic Java.

### Methods
A description of our methods

#### buildBDD()
Set up the BDD, Factory and all the other library initialization code.
Also apply the rules and update the board accordingly, removing squares that will result in no possible solution.
#### createRules()
Make sure that no queen can take another one.
#### createCellRule(int x,int y)
If there is a queen in this cell, make sure that she cannot be taken.
#### place(int x, int y)
Given a square coordinate, give the variable number in the BDD (0 - N*N)
#### createEightRule()
Make sure that there is a queen in every row.
#### updateInvalid()
Update the GUI to show what cells that you can no longer place a queen in.
#### placeInvalid(int x, int y)
Check if the BDD is solvable after a queen has been placed here.
#### placeDefiniteQueens()
Places queens when it is definite that they must be in a certain square.

### Implementation Idea
Our solutions consist of two basic rules. First, make sure that there is a queen in every row of the board.
Second, for every cell in the board, make sure that either there is no queen in that cell, or (if there is a queen) that
there is no queen in any of the cells that the queen would be able to take.

The first rule is defined in the createEightRule() method. For every row, the following must hold:
There must be one of the cells in that row that contains a queen.

The second rule is defined in the createRules() method. For every cell, the following must hold:
* If there is no queen in the cell, skip the rest.
* If there is a queen, there can not be any queens in the cells in the same row.
* If there is a queen, there can not be any queens in the cells in the same column.
* If there is a queen, there can not be any queens in the cells in the same diagonals.

As an example, this is the part that makes use that  there can not be any queens in the same row:
````
//All other x's must be false
for (int xx = 0; xx < N; xx++) {
  if (x != xx) {
    restFalseBdd = rest_false_bdd.and(factory.nithVar(place(xx,y)));
  }
}
````
When those two rules are applied, the BDD can be used to detect what cells are valid.
To check if a cell is valid, we create a new BDD where that cell has a queen in it (is set to True).
If and only if the new BDD is satisfiable, then the cell is valid.
Whenever a queen is placed in a cell, we update the BDD by setting that cell to True.
Then, we check all other cells to see if they are still valid. If they are not, we mark them invalid.

When a queen is placed on a board, we iterate through all the empty squares, and try to set the square to false.
If this reveals no solution, the square must be occupied by a queen in all remaining solutions.
This allows us to set a queen in the square, and recursively call the insertQueen method to update all constraints.

### How to compile and run the code
You need the javabdd-1.0b2.jar from JavaBDDs website to be in the same folder as the project. Then run the following command:
````
javac -cp "javabdd-1.0b2.jar:." *.java && java -cp "javabdd-1.0b2.jar:." ShowBoard
````

### Some thoughts
The BDD turns out to be a very easy way to allow for interactive restrictions.
However, it is seldom clear why a given cell is removed from the space of solutions.
It would be interesting to see a tool that allowed the user to see why the a given cell can no longer contain a queen.
A nice part of using a BDD library is that a lot of optimisations have already been provided.
The interface is as responsive as you could expect from a swing program.

### Conclusion
We succeeded to build a working configurator for the N-Queens problem that satisfies our requirements using the provided tools.
It even enables us to put down queens for the user, if the position is definitely in all solutions.

### License
DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE: www.wtfpl.net
