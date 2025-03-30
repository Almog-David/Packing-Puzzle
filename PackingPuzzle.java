import java.util.*;
import javax.swing.SwingUtilities;

public class PackingPuzzle {

    private static int[][][] PIECES = {
            { { 0, 0 } }, // Piece 1: Single square (1)
            { { 0, 0 }, { 1, 0 } }, // Piece 2: Double vertical (2)
            { { 0, 0 }, { 0, 1 }, { 0, 2 } }, // Piece 3: Triple horizontal (3)
            { { 0, 0 }, { 0, 1 }, { 1, 0 } }, // Piece 4: Triple L-shape (4)
            { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } }, // Piece 5: Quad horizontal (5)
            { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, // Piece 6: square (6)
            { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 2 } }, // Piece 7: Triple L-shape down (7)
            { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 1, 2 } }, // Piece 8: Triple L-shape up (8)
            { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 } }, // Piece 9: Quad T-shape (9)
            { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 2 } }, // Piece 10: Quad Z-shape (10)
            { { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 } } // Piece 11: Quad Z-shape rotated (11)
    };

    public static int[][] packingPuzzle(int[] pieces, int size) throws Exception {
        if (pieces == null || pieces.length == 0) {
            throw new Exception("no pieces!");
        }

        int[][] board = new int[size][size];

        if (solve(board, size, pieces, 0)) {
            return board;
        } else {
            return new int[size][size];
        }
    }

    private static boolean solve(int[][] board, int size, int[] pieces, int pieceIndex) {
        if (pieceIndex == pieces.length) {
            return true;
        }

        // Get the current piece type (1-11)
        int pieceType = pieces[pieceIndex];

        // Get the shape of the piece
        int[][] pieceShape = PIECES[pieceType - 1];

        // Try to place the piece at each position on the board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Try each of the 4 possible rotations
                for (int rotation = 0; rotation < 4; rotation++) {
                    if (canPlace(board, size, pieceShape, row, col, pieceType, rotation)) {
                        // Place the piece
                        place(board, pieceShape, row, col, pieceType, rotation);

                        // Recursively try to place the next piece
                        if (solve(board, size, pieces, pieceIndex + 1)) {
                            return true;
                        }

                        // If placing the next piece doesn't work, remove this piece
                        remove(board, pieceShape, row, col, rotation);
                    }
                }
            }
        }

        return false;
    }

    // Check if a piece can be placed at the given position with the given rotation
    private static boolean canPlace(int[][] board, int size, int[][] pieceShape, int row, int col,
            int pieceType, int rotation) {
        for (int[] point : pieceShape) {
            int newRow, newCol;

            // Apply rotation
            switch (rotation) {
                case 0: // No rotation
                    newRow = row + point[0];
                    newCol = col + point[1];
                    break;
                case 1: // 90 degrees clockwise
                    newRow = row + point[1];
                    newCol = col - point[0];
                    break;
                case 2: // 180 degrees
                    newRow = row - point[0];
                    newCol = col - point[1];
                    break;
                case 3: // 270 degrees clockwise
                    newRow = row - point[1];
                    newCol = col + point[0];
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotation");
            }

            // Check if the position is valid
            if (newRow < 0 || newRow >= size || newCol < 0 || newCol >= size
                    || board[newRow][newCol] != 0) {
                return false;
            }
        }

        return true;
    }

    // Place a piece on the board
    private static void place(int[][] board, int[][] pieceShape, int row, int col,
            int pieceType, int rotation) {
        for (int[] point : pieceShape) {
            int newRow, newCol;

            // Apply rotation
            switch (rotation) {
                case 0: // No rotation
                    newRow = row + point[0];
                    newCol = col + point[1];
                    break;
                case 1: // 90 degrees clockwise
                    newRow = row + point[1];
                    newCol = col - point[0];
                    break;
                case 2: // 180 degrees
                    newRow = row - point[0];
                    newCol = col - point[1];
                    break;
                case 3: // 270 degrees clockwise
                    newRow = row - point[1];
                    newCol = col + point[0];
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotation");
            }

            board[newRow][newCol] = pieceType;
        }
    }

    // Remove a piece from the board
    private static void remove(int[][] board, int[][] pieceShape, int row, int col, int rotation) {
        for (int[] point : pieceShape) {
            int newRow, newCol;

            // Apply rotation
            switch (rotation) {
                case 0: // No rotation
                    newRow = row + point[0];
                    newCol = col + point[1];
                    break;
                case 1: // 90 degrees clockwise
                    newRow = row + point[1];
                    newCol = col - point[0];
                    break;
                case 2: // 180 degrees
                    newRow = row - point[0];
                    newCol = col - point[1];
                    break;
                case 3: // 270 degrees clockwise
                    newRow = row - point[1];
                    newCol = col + point[0];
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotation");
            }

            board[newRow][newCol] = 0;
        }
    }

    public static void main(String[] args) {
        PackingPuzzleGUI.displayGUI(PIECES);
    }
}
