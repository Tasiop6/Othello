package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Pawn[][] board;

    public Board() {
        board = new Pawn[8][8];

        // Fill all cells with EmptyPawn
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new EmptyPawn();
            }
        }

        // Initial setup
        board[3][3] = new PawnWhite(); // d4
        board[3][4] = new PawnBlack(); // e4
        board[4][3] = new PawnBlack(); // d5
        board[4][4] = new PawnWhite(); // e5
    }

    public Pawn[][] getBoard() {
        return board;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");

        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                Pawn p = board[i][j];
                if (p instanceof PawnWhite) {
                    System.out.print("X ");
                } else if (p instanceof PawnBlack) {
                    System.out.print("O ");
                } else if (p instanceof AvailableMove) {
                    System.out.print("* ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public void findAndMarkAvailableMoves(Board board, PawnType player) {
        clearAvailableMoves();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j].getType() == PawnType.EMPTY && isValidMove(board, i, j, player)) {
                    board.getBoard()[i][j] = new AvailableMove();
                }
            }
        }
    }

    public void clearAvailableMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getType() == PawnType.AVAILABLE_MOVE) {
                    board[i][j] = new EmptyPawn();
                }
            }
        }
    }

    public boolean isValidMove(Board board, int row, int col, PawnType player) {
        if (board.getBoard()[row][col].getType() != PawnType.EMPTY) {
            return false;
        }

        PawnType opponent = (player == PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;

        // LEFT ( Each block of code corresponds to the movement done in each direction to check if move is valid )
        int i = row, j = col - 1;                   // (Same logic in all so i added comments only on the first one)
        boolean foundOpponent = false;              // Flag to see if we found an opponent in between potential valid move and player pawn
        while (j >= 0) {                            // Until we reach the end of the board
            PawnType t = board.getBoard()[i][j].getType();     // Check pawn type in the current position
            if (t == opponent) {
                foundOpponent = true;               // At least one opponent found
            } else if (t == player) {
                if (foundOpponent) return true;     // Player pawn found with opponent in between and no spaces -> valid move
                break;                              // Player pawn found but no opponent in between -> invalid move
            } else {                                // Empty space -> invalid move
                break;
            }
            j--;
        }

        // RIGHT
        j = col + 1;
        foundOpponent = false;
        while (j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent) return true;
                break;
            } else {
                break;
            }
            j++;
        }

        // UP
        i = row - 1;
        j = col;
        foundOpponent = false;
        while (i >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent) return true;
                break;
            } else {    // Empty space->exit
                break;
            }
            i--;
        }

        // DOWN
        i = row + 1;
        foundOpponent = false;
        while (i < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent)
                    return true;
                break;
            } else {    // Empty space->exit
                break;
            }
            i++;
        }

        // UP-LEFT
        i = row - 1;
        j = col - 1;
        foundOpponent = false;
        while (i >= 0 && j >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent)
                    return true;
                break;
            } else {    // Empty space->exit
                break;
            }
            i--;
            j--;
        }

        // UP-RIGHT
        i = row - 1;
        j = col + 1;
        foundOpponent = false;
        while (i >= 0 && j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent) return true;
                break;
            } else {
                break;
            }
            i--;
            j++;
        }

        // DOWN-LEFT
        i = row + 1;
        j = col - 1;
        foundOpponent = false;
        while (i < 8 && j >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent) return true;
                break;
            } else {
                break;
            }
            i++;
            j--;
        }

        // DOWN-RIGHT
        i = row + 1;
        j = col + 1;
        foundOpponent = false;
        while (i < 8 && j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                foundOpponent = true;
            } else if (t == player) {
                if (foundOpponent) return true;
                break;
            } else {
                break;
            }
            i++;
            j++;
        }

        return false;
    }

    public void applyMove(Board board, int row, int col, PawnType player) {
        PawnType opponent = (player == PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;
        board.getBoard()[row][col] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();

        int i, j;

        // LEFT
        i = row; j = col - 1;
        List<int[]> toFlip = new ArrayList<>();
        while (j >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            j--;
        }

        // RIGHT
        j = col + 1;
        toFlip.clear();
        while (j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            j++;
        }

        // UP
        i = row - 1; j = col;
        toFlip.clear();
        while (i >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i--;
        }

        // DOWN
        i = row + 1;
        toFlip.clear();
        while (i < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i++;
        }

        // UP-LEFT
        i = row - 1; j = col - 1;
        toFlip.clear();
        while (i >= 0 && j >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i--; j--;
        }

        // UP-RIGHT
        i = row - 1; j = col + 1;
        toFlip.clear();
        while (i >= 0 && j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i--; j++;
        }

        // DOWN-LEFT
        i = row + 1; j = col - 1;
        toFlip.clear();
        while (i < 8 && j >= 0) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i++; j--;
        }

        // DOWN-RIGHT
        i = row + 1; j = col + 1;
        toFlip.clear();
        while (i < 8 && j < 8) {
            PawnType t = board.getBoard()[i][j].getType();
            if (t == opponent) {
                toFlip.add(new int[]{i, j});
            } else if (t == player) {
                for (int[] pos : toFlip)
                    board.getBoard()[pos[0]][pos[1]] = (player == PawnType.BLACK) ? new PawnBlack() : new PawnWhite();
                break;
            } else break;
            i++; j++;
        }
    }

    public Board copyBoard() {
        Board copy = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PawnType type = this.board[i][j].getType();
                switch (type) {
                    case BLACK: copy.board[i][j] = new PawnBlack(); break;
                    case WHITE: copy.board[i][j] = new PawnWhite(); break;
                    default: copy.board[i][j] = new EmptyPawn();
                }
            }
        }
        return copy;
    }

}
