package ce326.hw2;

import java.util.Scanner;

public class Game {
    private static final Scanner scanner = new Scanner(System.in);
    private static PawnType currentTurn;
    private static boolean playerIsBlack;
    private static int estimatedForwardMoves;
    private static Board board;

    public static void main(String[] args) {

        System.out.println("Welcome to Othello!\n");

        chooseColor();
        estimateForwardMoves();

        // Main game loop
        while (true) {
            if (isGameOver()) {
                printGameResult();
                break;
            }

            System.out.println("Player's " + (currentTurn == PawnType.BLACK ? "O" : "X") + " turn\n");

            // Mark moves
            board.findAndMarkAvailableMoves(currentTurn);
            board.printBoard();

            // Check if no * exists on board
            if (!hasAvailableMoves()) {
                System.out.println("No available moves!\n");
                switchPlayer();
                continue;
            }

            // Placeholder for actual turn handling
            break; // TEMP: exit here until we add game turns
        }

//        System.out.println("You chose " + (playerIsBlack ? "black (O)" : "white (X)") + ".");
//        System.out.println("Search depth: " + estimatedForwardMoves);
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
        // TODO: real move checking later. For now simulate with always false.
        return false;
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
}
