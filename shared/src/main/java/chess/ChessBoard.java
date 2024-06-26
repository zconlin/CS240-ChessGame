package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    // Constructor for copy
    public ChessBoard(ChessBoard original) {
        this.board = original.copy();
    }

    // Method to perform copy
    private ChessPiece[][] copy() {
        ChessPiece[][] copy = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    copy[i][j] = board[i][j].copy();
                }
            }
        }
        return copy;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(board[position.getRow()-1][position.getColumn()-1] == null) {
            board[position.getRow()-1][position.getColumn()-1] = piece;
        } else{
            throw new RuntimeException("Position on board already taken");
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        initializePieces(1, ChessGame.TeamColor.WHITE);
        initializePawns(2, ChessGame.TeamColor.WHITE);
        initializePieces(8, ChessGame.TeamColor.BLACK);
        initializePawns(7, ChessGame.TeamColor.BLACK);
    }

    private void initializePieces(int row, ChessGame.TeamColor color) {
        addPiece(row, 1, color, ChessPiece.PieceType.ROOK);
        addPiece(row, 8, color, ChessPiece.PieceType.ROOK);
        addPiece(row, 2, color, ChessPiece.PieceType.KNIGHT);
        addPiece(row, 7, color, ChessPiece.PieceType.KNIGHT);
        addPiece(row, 3, color, ChessPiece.PieceType.BISHOP);
        addPiece(row, 6, color, ChessPiece.PieceType.BISHOP);
        addPiece(row, 4, color, ChessPiece.PieceType.QUEEN);
        addPiece(row, 5, color, ChessPiece.PieceType.KING);
    }

    private void initializePawns(int row, ChessGame.TeamColor color) {
        for (int col = 1; col <= 8; col++) {
            addPiece(row, col, color, ChessPiece.PieceType.PAWN);
        }
    }

    private void addPiece(int row, int col, ChessGame.TeamColor color, ChessPiece.PieceType type) {
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = new ChessPiece(color, type);
        addPiece(position, piece);
    }

    public void movePiece(ChessPosition startPosition, ChessPosition nextPosition, ChessPiece piece){
        board[nextPosition.getRow()-1][nextPosition.getColumn()-1] = piece;
        board[startPosition.getRow()-1][startPosition.getColumn()-1] = null;
    }

    public static class ChessBoardTA implements JsonDeserializer<ChessBoard> {

        @Override
        public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var gson = new GsonBuilder()
                    .registerTypeAdapter(ChessPiece.class, new ChessPiece.ChessPieceTA())
                    .create();
            return gson.fromJson(jsonElement, ChessBoard.class);
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}