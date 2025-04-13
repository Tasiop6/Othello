package ce326.hw2;

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

    public void findAndMarkAvailableMoves(PawnType player) {
        clearAvailableMoves();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getType() == PawnType.EMPTY && isValidMove(i, j, player)) {
                    board[i][j] = new AvailableMove();
                }
            }
        }
    }

    private void clearAvailableMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getType() == PawnType.AVAILABLE_MOVE) {
                    board[i][j] = new EmptyPawn();
                }
            }
        }
    }

    private boolean isValidMove(int row, int col, PawnType player) {
        if (board[row][col].getType() != PawnType.EMPTY) {
            return false;
        }

        PawnType opponent = (player == PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;

        // LEFT ( Each block of code corresponds to the movement done in each direction to check if move is valid )
        int i = row, j = col - 1;                   // (Same logic in all so i added comments only on the first one)
        boolean foundOpponent = false;              // Flag to see if we found an opponent in between potential valid move and player pawn
        while (j >= 0) {                            // Until we reach the end of the board
            PawnType t = board[i][j].getType();     // Check pawn type in the current position
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
            PawnType t = board[i][j].getType();
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
}
