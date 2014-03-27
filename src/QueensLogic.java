/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import net.sf.javabdd.*;

public class QueensLogic {

    private int N = 0;
    private int x = 0;
    private int y = 0;
    private int[][] board;

    // BDD
    private int nodenum   = 2000000;
    private int cachesize =  200000;
    private BDDFactory fact;
    private BDD True;
    private BDD False;
    private BDD[][] X; // BDD variables
    private BDD queen; // n-queens BDD


    public QueensLogic() {
        // constructor
        // leave empty
    }

    public void initializeGame(int n) {
        this.x = n;
        this.y = n;
        this.board = new int[x][y];
        
        // BDD
        this.fact = JFactory.init(nodenum, cachesize);
        fact.setVarNum(n*n);
        this.True = fact.one();
        this.False = fact.zero();
        //this.queen = fact.universe(); // method does not exist ???

        this.X = new BDD[x][y];
        for(int j = 0; j < y; j++)
            for(int i = 0; i < x; i++)
                this.X[i][j] = fact.ithVar(i+j*n);

        buildQueenBDD();
    }

    public int[][] getGameBoard() {
        return board;
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;
        
        // put some logic here..
        
        printBoard();
      
        return true;
    }

    private void buildQueenBDD() {
        // rules
    }

    // Print board state to console
    private void printBoard() {
        System.out.println("Board:");
        for(int j = 0; j < y; j++) {
            for(int i = 0; i < x; i++) {
                if(board[i][j] < 0) {
                    System.out.print("x ");
                } else if(board[i][j] > 0) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
