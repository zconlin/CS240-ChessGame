package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    PieceType type;
    ChessGame.TeamColor pieceColor;

    boolean enemy;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
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

                // Add move to list of possible moves and keep looping
                if(validateMove(newRow, newCol, board, myPosition)){
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy){
                        break;
                    }
                }else{
                    // If blocked by your own piece or out of bounds, stop searching this direction
                    break;
                }
            }
        }
        // Return HashSet of valid moves for the rook
        return validMoves;
    }

    // Checks if move is on the board, if the space is occupied, and if the space is occupied by the opponent
    public boolean validateMove(int row, int col, ChessBoard board, ChessPosition myPosition){
        int boardSize = 8;
        enemy = false;

        if((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)){
            if(board.getPiece(new ChessPosition(row,col)) != null){
                // If the square is occupied, return the color of piece
                if(board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(row,col)).getTeamColor()){
                    enemy = true;
                    return true;
                }
            }
            // Check if that space is already occupied or if the space is occupied by the opponent
            return board.getPiece(new ChessPosition(row, col)) == null || enemy;
        }
        return false;
    }

    // Determine possible moves for a bishop
    private Set<ChessMove> checkBishop(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return possibleDirections(board, myPosition, bishopDirections, validMoves);
    }

    // Determine possible moves for a rook
    private Set<ChessMove> checkRook(ChessBoard board, ChessPosition myPosition){
        Set<ChessMove> validMoves = new HashSet<>();
        int[][] rookDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        return possibleDirections(board, myPosition, rookDirections, validMoves);
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
            case KING -> null;
            case QUEEN -> null;
            case BISHOP -> checkBishop(board, myPosition);
            case KNIGHT -> null;
            case ROOK -> checkRook(board, myPosition);
            case PAWN -> null;
        };
    }
}
