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
    private int N;
    private int[][] board;

    private BDDFactory factory;
    private BDD True;
    private BDD False;
    private BDD bdd;

    public void initializeGame(int size) {
        N = size;
        board = new int[N][N];
        buildBDD();

        // Run update invalid (Not really needed in this case, but good practice)
        updateInvalid();
    }

    public void buildBDD() {
        //Factory
        factory = JFactory.init(2000000, 200000); // set buffer etc.

        //64 fields => 64 variables
        factory.setVarNum(N * N);

        //For clarity
        False = factory.zero();
        True = factory.one();

        //Initalize our bdd to true
        bdd = True;

        //Add the rules to the bdd
        createRules();
        createEightRule();
    }

    private void createRules() {
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                createCellRule(x, y);
            }
        }
    }

    private void createCellRule(int x, int y) {
        BDD restFalseBdd = True;

        // All other y's must be false
        for (int yy = 0; yy < N; yy++) {
            if (y != yy) {
                restFalseBdd = restFalseBdd.and(factory.nithVar(place(x, yy)));
            }
        }

        // All other x's must be false
        for (int xx = 0; xx < N; xx++) {
            if (x != xx) {
                restFalseBdd = restFalseBdd.and(factory.nithVar(place(xx, y)));
            }
        }

        // All other y+xx-x must be false
        for (int xx = 0; xx < N; xx++) {
            if (x != xx) {
                if ((y + xx - x < N) && (y + xx - x > 0)) {
                    restFalseBdd = restFalseBdd.and(factory.nithVar(place(xx, y + xx - x)));
                }
            }
        }

        // All other y-xx+xx must be false
        for (int xx = 0; xx < N; xx++) {
            if (x != xx) {
                if ((y - xx + x < N) && (y - xx + x > 0)) {
                    restFalseBdd = restFalseBdd.and(this.factory.nithVar(place(xx, y - xx + x)));
                }
            }
        }

        // Either the x,y is false
        BDD subBdd = factory.nithVar(place(x, y));

        // Or (if the x,y is true) the rest is false
        subBdd = subBdd.or(restFalseBdd);

        // subBdd must be true
        bdd = bdd.and(subBdd);
    }

    private int place(int column, int row) {
        return row * N + column;
    }

    // All rows should have a queen
    private void createEightRule() {
        for (int y = 0; y < N; y++) {
            BDD subBdd = False;

            for (int x = 0; x < N; x++) {
                subBdd = subBdd.or(factory.ithVar(place(x, y)));
            }

            //sub_bdd must be true
            bdd = bdd.and(subBdd);
        }
    }

    public int[][] getGameBoard() {
        return board;
    }

    public boolean insertQueen(int x, int y) {

        if (board[x][y] == -1 || board[x][y] == 1) {
            return true;
        }

        // Set a queen in graphic
        board[x][y] = 1;

        //Set a queen in the bdd
        bdd = bdd.restrict(factory.ithVar(place(x, y)));

        updateInvalid();

        placeDefiniteQueens();

        return true;
    }

    private void updateInvalid() {
        // For each square, check if placing a queen there makes it invalid
        // If so, make the square be -1
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (placeInvalid(x, y)) {
                    board[x][y] = -1;
                }
            }
        }
    }

    private boolean placeInvalid(int x, int y) {
        return bdd.restrict(factory.ithVar(place(x, y))).isZero();
    }

    private void placeDefiniteQueens() {
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (board[x][y] == 0) {
                    if (bdd.restrict(factory.nithVar(place(x, y))).isZero()) {
                        insertQueen(x, y);
                    }
                }
            }
        }
    }
}
