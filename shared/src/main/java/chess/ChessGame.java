package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard gameBoard;
    ChessBoard boardCopy;

    ChessMove dangerPiece;

    TeamColor turn = TeamColor.WHITE;

    private boolean modifiedCopy;

    public ChessGame() {
        gameBoard = new ChessBoard();
        initializeBoard();
    }

    private void initializeBoard() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allValidMoves = new HashSet<>();
        ChessPiece piece = gameBoard.getPiece(startPosition);

        if (piece == null) {
            return allValidMoves;
        }

        TeamColor teamPiece = piece.getTeamColor();
        for (ChessMove move : gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition)) {
            boardCopy = new ChessBoard(gameBoard);
            boardCopy.movePiece(move.getStartPosition(), move.getEndPosition(), boardCopy.getPiece(move.getStartPosition()));
            modifiedCopy = true;
            if (!isInCheck(teamPiece)) {
                allValidMoves.add(move);
            }
            modifiedCopy = false;
        }

        return allValidMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece pieceAtStart = gameBoard.getPiece(startPosition);

        if (pieceAtStart == null) {
            throw new InvalidMoveException("Invalid move: There is no piece at the specified position.");
        }

        boolean isPieceMoveValid = validMoves(startPosition).contains(move);

        if (!isPieceMoveValid) {
            throw new InvalidMoveException("Invalid move: The chess piece cannot move to the specified position.");
        }

        if (getTeamTurn() != pieceAtStart.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: It is not your turn.");
        }

        boardCopy = new ChessBoard(gameBoard);
        boardCopy.movePiece(startPosition, endPosition, boardCopy.getPiece(startPosition));
        modifiedCopy = true;

        if (isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Invalid move: Will leave your king in check.");
        }

        if (pieceAtStart.getPieceType() == ChessPiece.PieceType.PAWN && (endPosition.getRow() == 8 && getTeamTurn() == TeamColor.WHITE || endPosition.getRow() == 1 && getTeamTurn() == TeamColor.BLACK)) {
            ChessPiece promotedPiece = new ChessPiece(getTeamTurn(), move.getPromotionPiece());
            gameBoard.movePiece(startPosition, endPosition, promotedPiece);
        } else {
            gameBoard.movePiece(startPosition, endPosition, pieceAtStart);
        }

        setTeamTurn((getTeamTurn() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boardCopy = modifiedCopy ? boardCopy : new ChessBoard(gameBoard);
        return isKingInDanger(boardCopy, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boardCopy = new ChessBoard(gameBoard);

        // Check if the team is in check
        if (!isInCheck(teamColor)) {
            return false;
        }

        // Check if any piece can block the check or capture the threatening piece
        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if(gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor){
                    for(ChessMove move : validMoves(position)){
                        boardCopy = new ChessBoard(gameBoard);
                        // Move the piece on the test board
                        boardCopy.movePiece(move.getStartPosition(), move.getEndPosition(), boardCopy.getPiece(move.getStartPosition()));
                        modifiedCopy = true;
                        if(!isInCheck(teamColor)){
                            // If at least one valid move is found that doesn't result in the king being checked,
                            // the team is not in checkmate
                            return false;
                        }
                    }
                }
            }
        }

        // If no valid moves can get the team out of check, it's checkmate
        return true;
    }

    private boolean isKingInDanger(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = findKing(board, teamColor);
        TeamColor opposingColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition opposingPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(opposingPosition);

                if (piece != null && piece.getTeamColor() == opposingColor) {
                    for (ChessMove check : piece.pieceMoves(board, opposingPosition)) {
                        if (check.getEndPosition().equals(kingPosition)) {
                            dangerPiece = check;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition position;

        for(int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                position = new ChessPosition(i, j);
                if(gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor){
                    for(ChessMove move : validMoves(position)){
                        boardCopy = new ChessBoard(gameBoard);
                        //move piece on testBoard
                        boardCopy.movePiece(move.getStartPosition(), move.getEndPosition(), boardCopy.getPiece(move.getStartPosition()));
                        modifiedCopy = true;
                        if(!isInCheck(teamColor)){
                            // If at least one valid move is found that doesn't result in the king being checked,
                            // the team is not in stalemate
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private ChessPosition findKing(ChessBoard board, TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", turn=" + turn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return modifiedCopy == chessGame.modifiedCopy && Objects.equals(gameBoard, chessGame.gameBoard) && Objects.equals(boardCopy, chessGame.boardCopy) && Objects.equals(dangerPiece, chessGame.dangerPiece) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, boardCopy, dangerPiece, turn, modifiedCopy);
    }
}
