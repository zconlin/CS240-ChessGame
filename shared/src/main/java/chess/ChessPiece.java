package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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
        return switch(type) {
            case KING -> moveKing(board, myPosition);
            case QUEEN -> moveQueen(board, myPosition);
            case BISHOP -> moveBishop(board, myPosition);
            case KNIGHT -> moveKnight(board, myPosition);
            case ROOK -> moveRook(board, myPosition);
            case PAWN -> movePawn(board, myPosition);
        };
    }

    private Set<ChessMove> possibleDirections(ChessBoard board, ChessPosition myPosition, int[][] possDirections, Set<ChessMove> validMoves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int boardSize = 8;

        for (int[] dir : possDirections) {
            for (int i = 1; i < boardSize; i++) {
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];

                if(validateMove(newRow, newCol, board, myPosition)) {
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                    if(enemy) break;
                } else break;
            }
        }
        return validMoves;
    }

    public boolean validateMove(int row, int col, ChessBoard board, ChessPosition myPosition) {
        enemy = false;
        int boardSize = 8;

        if ((row > 0 && row <= boardSize) && (col > 0 && col <= boardSize)) {
            if (board.getPiece(new ChessPosition(row, col)) != null) {
                if (board.getPiece(myPosition).getTeamColor() != board.getPiece(new ChessPosition(row, col)).getTeamColor()) {
                    enemy = true;
                    return true;
                }
            }
            return board.getPiece(new ChessPosition(row, col)) == null || enemy;
        }
        return false;
    }

    private Set<ChessMove>moveQueen(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int[][] possDirections = { {1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, -1}, {-1, -1}, {-1, 1} };
        return possibleDirections(board, myPosition, possDirections, validMoves);
    }

    private Set<ChessMove>moveRook(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int[][] possDirections = { {0, 1}, {0, -1}, {-1, 0}, {1, 0} };
        return possibleDirections(board, myPosition, possDirections, validMoves);
    }

    private Set<ChessMove>moveBishop(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        int[][] possDirections = { {1, 1}, {1, -1}, {-1, -1}, {-1, 1} };
        return possibleDirections(board, myPosition, possDirections, validMoves);
    }

    private Set<ChessMove>moveKing(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (validateMove(newRow, newCol, board, myPosition)) {
                    ChessPosition validPosition = new ChessPosition(newRow, newCol);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));

                    if(enemy) break;
                }
            }
        }
        return validMoves;
    }

    private Set<ChessMove>moveKnight(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();

        int[][] possDirections = { {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2} };
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int[] dir: possDirections){
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (validateMove(newRow, newCol, board, myPosition)) {
                ChessPosition validPosition = new ChessPosition(newRow, newCol);
                validMoves.add(new ChessMove(myPosition, validPosition, null));
            }
        }
        return validMoves;
    }

    private Set<ChessMove>movePawn(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> validMoves = new HashSet<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int player = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        ChessPosition validPosition = new ChessPosition(row + player, col);

        if (promotionRow(row) && validatePawn(row + player, col, board)) {
            promotionMoves(validMoves, myPosition, validPosition);
        } else if (initialRow(row)) {
            for (int i = 1; i <= 2; i++) {
                if(validatePawn((row + i * player), col, board)) {
                    validPosition = new ChessPosition((row + i * player), col);
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                } else break;
            }
        } else if (validatePawn((row + player), col, board)) {
            validMoves.add(new ChessMove(myPosition, validPosition, null));
        }
        diagonalPawnMoves(validMoves, row, col, player, myPosition, board);

        return validMoves;
    }

    private boolean promotionRow(int row) {
        return (pieceColor == ChessGame.TeamColor.WHITE && row == 7 || pieceColor == ChessGame.TeamColor.BLACK && row == 2);
    }

    private boolean validatePawn(int row, int col, ChessBoard board) {
        int boardSize = 8;
        return (row > 0 && row <= boardSize) && (col > 0 && col < boardSize) && (board.getPiece(new ChessPosition(row, col)) == null);
    }

    private void promotionMoves(Set<ChessMove> validMoves, ChessPosition myPosition, ChessPosition validPosition) {
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.ROOK));
        validMoves.add(new ChessMove(myPosition, validPosition, PieceType.KNIGHT));
    }

    private boolean initialRow(int row) {
        return (pieceColor == ChessGame.TeamColor.WHITE && row == 2) ||
                (pieceColor == ChessGame.TeamColor.BLACK && row == 7);
    }

    private void diagonalPawnMoves(Set<ChessMove> validMoves, int row, int col, int player, ChessPosition myPosition, ChessBoard board) {
        int[][] possDirections = {{1, 1}, {1, -1}};

        for (int[] dir : possDirections) {
            int newRow = row + player * dir[0];
            int newCol = row + player * dir[1];

            ChessPosition validPosition = new ChessPosition(newRow, newCol);

            if(enemyChecker(newRow, newCol, board, myPosition)) {
                if(promotionRow(row)) {
                    promotionMoves(validMoves, myPosition, validPosition);
                } else {
                    validMoves.add(new ChessMove(myPosition, validPosition, null));
                }
            }
        }
    }

    private boolean enemyChecker(int row, int col, ChessBoard board, ChessPosition myPosition) {
        int boardSize = 8;
        boolean enemy = false;
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            ChessPiece pieceAtPosition = board.getPiece(new ChessPosition(row, col));
            if (pieceAtPosition != null) {
                enemy = pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor();
            }
        }
        return enemy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return enemy == that.enemy && type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor, enemy);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", pieceColor=" + pieceColor +
                ", enemy=" + enemy +
                '}';
    }
}
