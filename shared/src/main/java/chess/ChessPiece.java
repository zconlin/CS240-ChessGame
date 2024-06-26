package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    PieceType type;
    ChessGame.TeamColor pieceColor;

    boolean enemyPiece;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    // Copy method
    public ChessPiece copy() {
        return new ChessPiece(this.pieceColor, this.type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    private Set<ChessMove> possibleDirections(ChessBoard board, ChessPosition myPosition, int[][] availableDirections, Set<ChessMove> validMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int boardSize = 8;

        for(int[] direction: availableDirections){
            for(int i = 1; i < boardSize; i++){
                int newRow = row + i * direction[0];
                int newCol = col + i * direction[1];

                if(validateMove(newRow, newCol, board, myPosition)){
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemyPiece){
                        break;
                    }
                }else{
                    // If blocked by your own piece or out of bounds, stop searching this direction
                    break;
                }
            }
        }
        return validMoves;
    }

    // Checks if move is on the board, if the space is occupied, and if the space is occupied by the opponent
    public boolean validateMove(int row, int col, ChessBoard board, ChessPosition myPosition){
        int boardSize = 8;
        enemyPiece = false;

        if((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)){
            if(board.getPiece(new ChessPosition(row,col)) != null){
                // If the square is occupied, return the color of piece
                if(board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(row,col)).getTeamColor()){
                    enemyPiece = true;
                    return true;
                }
            }
            // Check if that space is already occupied or if the space is occupied by the opponent
            return board.getPiece(new ChessPosition(row, col)) == null || enemyPiece;
        }
        return false;
    }

    // Determine possible moves for a king
    private Set<ChessMove> checkKing(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> kingMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int newRow = row + i;
                int newCol = col + j;

                if(validateMove(newRow, newCol, board, myPosition)){
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    kingMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemyPiece){
                        break;
                    }
                }
            }
        }
        return kingMoves;
    }

    // Determine possible moves for a queen
    private Set<ChessMove> checkQueen(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> queenMoves = new HashSet<>();
        int[][] queenDirections = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
        return possibleDirections(board, myPosition, queenDirections, queenMoves);
    }

    // Determine possible moves for a bishop
    private Set<ChessMove> checkBishop(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> bishopMoves = new HashSet<>();
        int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return possibleDirections(board, myPosition, bishopDirections, bishopMoves);
    }

    // Determine possible moves for a knight
    private Set<ChessMove> checkKnight(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();

        // Possible directions the knight can move (L-shape)
        int[][] knightDirections = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for(int[] direction: knightDirections){
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if(validateMove(newRow, newCol, board, myPosition)){
                ChessPosition validPosition = new ChessPosition(newRow, newCol);
                validMoves.add(new ChessMove(myPosition, validPosition, null));
            }
        }
        return validMoves;
    }

    // Determine possible moves for a rook
    private Set<ChessMove> checkRook(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> rookMoves = new HashSet<>();
        int[][] rookDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        return possibleDirections(board, myPosition, rookDirections, rookMoves);
    }

    // Determine possible moves for a pawn
    private Set<ChessMove> checkPawn(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int player = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        addPawnMoves(validMoves, myPosition, player, row, col, board);

        // Can the pawn capture diagonally
        diagonalPawnMoves(validMoves, row, col, player, myPosition, board);

        return validMoves;
    }

    private void addPawnMoves(Set<ChessMove> validMoves, ChessPosition myPosition, int player, int row, int col, ChessBoard board) {
        ChessPosition validPosition = new ChessPosition(row + player, col);

        // If pawn is getting promoted, give promotion options
        if (promotionRow(row) && validatePawn(row + player, col, board)) {
            promotionMoves(validMoves, myPosition, validPosition);
        } else if (startingRow(row)) {
            // If it is the first move of the pawn, it can move two spaces
            for (int i = 1; i < 3; i++) {
                if (validatePawn(row + (player * i), col, board)) {
                    validPosition = new ChessPosition(row + (i * player), col);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                } else {
                    break;
                }
            }
        } else if (validatePawn(row + player, col, board)) { // Normal forward movement of pawn
            validMoves.add(new ChessMove(myPosition, validPosition, null));
        }
    }

    // Is the pawn in the starting row
    private boolean startingRow(int row){
        return (pieceColor == ChessGame.TeamColor.WHITE ? row == 2 : row == 7);
    }

    // Is the pawn in a position to be promoted
    private boolean promotionRow(int row){
        return (pieceColor == ChessGame.TeamColor.BLACK ? row == 2 : row == 7);
    }

    // Promotion options for the pawn
    private void promotionMoves(Set<ChessMove> validMoves, ChessPosition myPosition, ChessPosition validPosition) {
        Stream.of(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)
                .map(pieceType -> new ChessMove(myPosition, validPosition, pieceType))
                .forEach(validMoves::add);
    }

    private void diagonalPawnMoves(Set<ChessMove> validMoves, int row, int col, int player, ChessPosition myPosition, ChessBoard board) {
        // Define possible diagonal directions
        int[][] diagonalDirections = { { 1, 1 }, { 1, -1 } };

        // Calculate potential positions for diagonal moves
        Arrays.stream(diagonalDirections)
                .map(direction -> new ChessPosition(row + player * direction[0], col + player * direction[1]))
                .filter(potentialPosition -> enemyChecker(potentialPosition.getRow(), potentialPosition.getColumn(), board, myPosition))
                .forEach(potentialPosition -> {
                    // If the pawn is in a promotion row, add promotion options
                    if (promotionRow(row)) {
                        promotionMoves(validMoves, myPosition, potentialPosition);
                    } else {
                        // Otherwise, add the potential move to the valid moves
                        validMoves.add(new ChessMove(myPosition, potentialPosition, null));
                    }
                });
    }

    // Checks to see if the pawn can move to a new position
    private boolean validatePawn(int row, int col, ChessBoard board){
        int boardSize = 8;
        // Make sure the new position is on the board and not taken
        return (row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)
                && board.getPiece(new ChessPosition(row, col)) == null;
    }

    private boolean enemyChecker(int row, int col, ChessBoard board, ChessPosition myPosition) {
        int boardSize = 8;

        // Check if the position is within the board boundaries
        if (row >= 1 && row <= boardSize && col >= 1 && col <= boardSize) {
            ChessPiece targetPiece = board.getPiece(new ChessPosition(row, col));

            // Check if there is a piece at the given position
            if (targetPiece != null) {
                // Check if the piece at the position belongs to the opponent
                return targetPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor();
            }
        }
        return false;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case KING -> checkKing(board, myPosition);
            case QUEEN -> checkQueen(board, myPosition);
            case BISHOP -> checkBishop(board, myPosition);
            case KNIGHT -> checkKnight(board, myPosition);
            case ROOK -> checkRook(board, myPosition);
            case PAWN -> checkPawn(board, myPosition);
        };
    }

    public static class ChessPieceTA implements JsonDeserializer<ChessPiece> {

        @Override
        public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Gson().fromJson(jsonElement, ChessPiece.class);
        }
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}