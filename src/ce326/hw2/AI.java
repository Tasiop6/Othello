package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class AI{
    private static final int[][] EVAL_TABLE = {
            {500, -20, 10,  5,  5, 10, -20, 500},
            {-20, -50, -2, -2, -2, -2, -50, -20},
            { 10,  -2,  1,  1,  1,  1,  -2,  10},
            {  5,  -2,  1,  0,  0,  1,  -2,   5},
            {  5,  -2,  1,  0,  0,  1,  -2,   5},
            { 10,  -2,  1,  1,  1,  1,  -2,  10},
            {-20, -50, -2, -2, -2, -2, -50, -20},
            {500, -20, 10,  5,  5, 10, -20, 500}
    };

    public static int[] aiMove(Board board, int depth, PawnType aiPlayer) {
        // Copy of the board so we don't mutate the original
        Board copy = board.copyBoard();

        // All valid moves for the AI player
        List<int[]> moves = getAllValidMoves(copy, aiPlayer);

        int bestScore = Integer.MIN_VALUE; // Init to lowest possible value
        int[] bestMove = null;

        // For every possible move use minimax to evaluate
        for (int[] move : moves) {
            // Reset board copy for each move
            copy = board.copyBoard();

            copy.applyMove(copy, move[0], move[1], aiPlayer);

            int score = minimax(copy, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiPlayer);

            // If move better than the previous best, save
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove; // Return the best move found
    }

    private static int minimax(Board board, int depth, int alpha, int beta, boolean maximizing, PawnType aiPlayer) {
        // If max depth or game is over exit
        if (depth == 0 || isGameOverStatic(board)) {
            return evaluateBoard(board, aiPlayer); // Return static evaluation
        }

        // Determine whose turn it is
        PawnType current = maximizing ? aiPlayer : getOpponent(aiPlayer);

        // All valid moves for current player
        List<int[]> moves = getAllValidMoves(board, current);

        // No available moves - pass turn
        if (moves.isEmpty()) {
            return minimax(board, depth - 1, alpha, beta, !maximizing, aiPlayer);
        }

        // Maximizing player (AI)
        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;

            for (int[] move : moves) {
                Board copy = board.copyBoard();
                copy.applyMove(copy, move[0], move[1], current);

                // Recursively evaluate next state
                int eval = minimax(copy, depth - 1, alpha, beta, false, aiPlayer);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                // Alpha-Beta Pruning
                if (beta <= alpha) break;
            }
            return maxEval;

            // Minimizing player (opponent)
        } else {
            int minEval = Integer.MAX_VALUE;

            for (int[] move : moves) {
                Board copy = board.copyBoard();
                copy.applyMove(copy, move[0], move[1], current);

                // Recursively evaluate next state
                int eval = minimax(copy, depth - 1, alpha, beta, true, aiPlayer);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                // Alpha-Beta Pruning
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private static int evaluateBoard(Board board, PawnType aiPlayer) {
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                PawnType t = board.getBoard()[i][j].getType();

                // Found AI pawn, add eval value
                if (t == aiPlayer) {
                    score += EVAL_TABLE[i][j];
                }
                // Found opponent's pawn, subtract EVAL value
                else if (t != PawnType.EMPTY && t != PawnType.AVAILABLE_MOVE) {
                    score -= EVAL_TABLE[i][j];
                }
            }
        }

        return score; // Higher score = better for AI
    }

    private static List<int[]> getAllValidMoves(Board board, PawnType player) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j].getType() == PawnType.EMPTY) {
                    boolean valid = board.isValidMove(board,i, j, player);
                    if (valid) {
                        moves.add(new int[]{i, j});
                    }
                }
            }
        }
        return moves;
    }

    private static boolean isGameOverStatic(Board board) {
        for (PawnType p : new PawnType[]{PawnType.BLACK, PawnType.WHITE}) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board.getBoard()[i][j].getType() == PawnType.EMPTY &&
                            board.isValidMove(board, i, j, p)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static PawnType getOpponent(PawnType player) {
        return (player == PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;
    }
}
