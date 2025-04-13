package ce326.hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static final Scanner scanner = new Scanner(System.in);
    private static PawnType currentTurn;
    private static boolean playerIsBlack;
    private static int estimatedForwardMoves;
    private static Board board;
    private static final List<String> moveHistory = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("Welcome to Othello!\n");

        chooseColor();
        estimateForwardMoves();

        board = new Board();
        currentTurn = PawnType.BLACK;

        while (true) {
            if (isGameOver()) {
                printGameResult();
                break;
            }

            System.out.println("Player's " + (currentTurn == PawnType.BLACK ? "O" : "X") + " turn\n");

            board.findAndMarkAvailableMoves(currentTurn);
            board.printBoard();

            if (!hasAvailableMoves()) {
                System.out.println("No available moves!\n");
                switchPlayer();
                continue;
            }

            if (isHumanTurn()) {
                int[] move = getUserMove();
                handleMove(move[0], move[1]);
            } else {
                int[] move = getUserMove();
                handleMove(move[0], move[1]);
                // Step 3a: AI logic will go here
                // Example dummy move:
                // int aiRow = ..., aiCol = ...;
                // handleMove(aiRow, aiCol);
                //aiMove();
            }

            switchPlayer();
        }
    }

    private static void chooseColor() {
        while (true) {
            System.out.print("Choose color (black/white/b/w/B/W/O/X/o/x): ");
            String input = Game.scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "black":
                case "b":
                case "o":
                    playerIsBlack = true;
                    return;
                case "white":
                case "w":
                case "x":
                    playerIsBlack = false;
                    return;
                default:
                    System.out.println("Invalid color. Try again...");
            }
        }
    }

    private static void estimateForwardMoves() {
        while (true) {
            System.out.print("Estimate forward moves [1,9]: ");
            String input = scanner.nextLine().trim();
            if (input.length() == 1 && Character.isDigit(input.charAt(0))) {
                int num = input.charAt(0) - '0'; //convert ASCII to number
                if (num >= 1 && num <= 9) {
                    estimatedForwardMoves = num;
                    return;
                }
            }
            System.out.println("Invalid moves. Try again...");
        }
    }

    private static boolean isGameOver() {
        board.clearAvailableMoves(); // ensure no leftover stars

        // Check black
        board.findAndMarkAvailableMoves(PawnType.BLACK);
        boolean blackHasMoves = hasAvailableMoves();

        board.clearAvailableMoves();

        // Check white
        board.findAndMarkAvailableMoves(PawnType.WHITE);
        boolean whiteHasMoves = hasAvailableMoves();

        board.clearAvailableMoves();

        return !blackHasMoves && !whiteHasMoves;
    }

    private static void printGameResult() {
        int countWhite = 0;
        int countBlack = 0;

        Pawn[][] grid = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                PawnType type = grid[i][j].getType();
                if (type == PawnType.WHITE) countWhite++;
                else if (type == PawnType.BLACK) countBlack++;
            }
        }

        System.out.print("X:" + countWhite + "/O:" + countBlack + " ");
        if (countWhite > countBlack) {
            System.out.print("Player X won!");
        } else if (countBlack > countWhite) {
            System.out.print("Player O won!");
        } else {
            System.out.print("It’s a draw!");
        }
        System.out.print("\n\n");
    }

    private static void switchPlayer() {
        currentTurn = (currentTurn == PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;
    }

    private static boolean hasAvailableMoves() {
        Pawn[][] grid = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].getType() == PawnType.AVAILABLE_MOVE) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isHumanTurn() {
        return (playerIsBlack && currentTurn == PawnType.BLACK) ||
                (!playerIsBlack && currentTurn == PawnType.WHITE);
    }

    private static int[] getUserMove() {
        while (true) {
            System.out.print(currentTurn +": Enter your move (e.g. c2): ");
            String input = scanner.nextLine().trim().toLowerCase(); //TODO: Check if C2 is valid(lowercase)

            if (input.length() >= 2) {
                char colChar = input.charAt(0);
                char rowChar = input.charAt(1);

                int col = colChar - 'a'; // 'a' → 0
                int row = rowChar - '1'; // '1' → 0

                if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                    PawnType type = board.getBoard()[row][col].getType();
                    if (type == PawnType.AVAILABLE_MOVE) {
                        return new int[]{row, col};
                    }
                }
            }

            System.out.println("Invalid move. Try again!\n");
        }
    }

    private static void aiMove() {
        // TODO: AI logic will be implemented here
        // 1. Search all valid moves
        // 2. Pick best move (based on estimatedForwardMoves depth)
        // 3. Apply move + flip logic
    }

    private static void handleMove(int row, int col) {
        board.applyMove(row, col, currentTurn);

        // Add to history
        char colChar = (char) ('a' + col);
        char rowChar = (char) ('1' + row);
        moveHistory.add("" + colChar + rowChar);

        // Print move and board
        System.out.println("Player " + (currentTurn == PawnType.BLACK ? "O" : "X") +
                " played: " + moveHistory.get(moveHistory.size() - 1));
        System.out.println();

        board.clearAvailableMoves(); // clear stars
        board.printBoard();
        System.out.println();

        System.out.print("Moves history:");
        for (String moveStr : moveHistory) {
            System.out.print(" " + moveStr);
        }
        System.out.println("\n");
    }
}
