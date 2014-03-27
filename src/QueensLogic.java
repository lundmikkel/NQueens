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
    // private int x = 0;
    // private int y = 0;
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
        this.N = n;
        // this.x = n;
        // this.y = n;
        this.board = new int[N][N];
        
        // BDD
        this.fact = JFactory.init(nodenum, cachesize);
        fact.setVarNum(N*N);
        this.True = fact.one();
        this.False = fact.zero();
        this.queen = True;

        // build BDD variable array X
        this.X = new BDD[N][N];
        for (int i = 0; i < N; i++)
            for(int j = 0; j < N; j++)
                X[i][j] = fact.ithVar(i*N+j);

        // build rule that every row should have a queen
        for (int i = 0; i < N; i++) {
            BDD e = False;
            for (int j = 0; j < N; j++) {
                e.orWith(X[i][j].id());
            }
            queen.andWith(e);
        }

        // build rules for each board space
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buildQueenBDD(i, j);
            }
        }
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

    private void buildQueenBDD(int i, int j) {
        BDD a = True, b = True, c = True, d = True;

        // column
        for (int l = 0; l < N; l++) {
            if (l != j) {
                BDD u = X[i][l].apply(X[i][j], BDDFactory.nand);
                a.andWith(u);
            }
        }

        // row
        for (int k = 0; k < N; k++) {
            if (k != i) {
                 BDD u = X[i][j].apply(X[k][j], BDDFactory.nand);
                 b.andWith(u);
            }
        }

        // diagonal /
        for (int k = 0; k < N; k++) {
            int l = k - i + j;
            if (l >= 0 && l < N) {
                if (k != i) {
                    BDD u = X[i][j].apply(X[k][l], BDDFactory.nand);
                    c.andWith(u);
                }
            }
        }

        // diagonal \
        for (int k = 0; k < N; k++) {
            int l = i + j - k;
            if (l >= 0 && l < N) {
                if (k != i) {
                    BDD u = X[i][j].apply(X[k][l], BDDFactory.nand);
                    d.andWith(u);
                }
            }
        }

        // combine
        c.andWith(d);
        b.andWith(c);
        a.andWith(b);
        queen.andWith(a);
    }

    // Print board state to console
    private void printBoard() {
        System.out.println("Board:");
        for(int j = 0; j < N; j++) {
            for(int i = 0; i < N; i++) {
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
