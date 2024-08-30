package bauernschach.model;

import bauernschach.model.board.ChessPiece;
import bauernschach.model.board.Move;

/** Represents a Bauernschach game. This class implements the game logic. */
public class Bauernschach {
  /** Represent the status of the previous game operation. */
  public enum OperationStatus {
    /** Signals that the operation was executed successfully. */
    SUCCESS,
    /** Signals that the operation failed. */
    FAIL,
    /** Same that the operation failed. */
    SAME_FAIL
  }

  static final int DEFAULT_NUM_ROWS = 8;
  static final int DEFAULT_NUM_COLS = 8;

  // TODO: Make it possible that views observe the game state for updates.
  private GameState gameState;

  /** Contructs a Bauernschach game instance with the prespecified board dimensions. */
  public Bauernschach() {
    gameState = new GameState(DEFAULT_NUM_ROWS, DEFAULT_NUM_COLS);
  }

  /**
   * Contructs a Bauernschach game instance with the given board dimensions.
   *
   * @param numRows the number of rows of the chess board
   * @param numCols the number of columns of the chess board
   */
  public Bauernschach(int numRows, int numCols) {
    gameState = new GameState(numRows, numCols);
  }

  /**
   * Gets the current GameState.
   *
   * @return the current GameState
   */
  public final GameState getGameState() {
    return gameState;
  }

  /**
   * Selects the chess piece by its ID.
   *
   * @param id the chess piece ID
   * @return OperationStatus.SUCCESS if the piece with the given ID exists and is selected;
   *     OperationStatus.FAIL if the game is not running, another piece has already been selected,
   *     or the given ID does not match any moveable piece
   */
  public OperationStatus selectPieceById(int id) {
    if (!gameState.isGameRunning() || gameState.hasSelectedPiece()) {
      return OperationStatus.FAIL;
    }

    for (ChessPiece piece : gameState.getPieceListAtCurrentRound()) {
      if (!piece.hasPossibleMoves()) {
        continue;
      }
      if (piece.getId() == id) {
        gameState.selectPiece(piece);
        assert gameState.isGameRunning();
        return OperationStatus.SUCCESS;
      }
    }
    return OperationStatus.FAIL;
  }

  /**
   * Deselects the chess piece.
   *
   * @return OperationStatus.SUCCESS if a piece is selected; OperationStatus.FAIL if the game is not
   *     running, or no piece has been selected
   */
  public OperationStatus deselectPiece() {
    if (!gameState.isGameRunning() || !gameState.hasSelectedPiece()) {
      return OperationStatus.FAIL;
    }
    gameState.deselectPiece();
    assert gameState.isGameRunning();
    return OperationStatus.SUCCESS;
  }

  private void startNewRound() {
    int count = 0;
    do {
      gameState.newRound();
      ++count;
    } while ((count < 2) && !gameState.currentRoundHasPossibleMoves());

    if (!gameState.currentRoundHasPossibleMoves()) {
      gameState = gameState.with(GameState.GameStatus.DRAW);
    }
  }

  private void checkWinningConditions(Move move) {
    final int finishRow =
        gameState.getChessBoard().getFinishRowByColor(gameState.getCurrentRound());

    // reach finish row or no opposing piece left
    if ((move.getNewCoordinate().getRow() == finishRow)
        || gameState.getOpposingPieceListAtCurrentRound().isEmpty()) {
      GameState.GameStatus status =
          (gameState.getCurrentRound() == ChessPiece.Color.WHITE)
              ? GameState.GameStatus.WHITE_WON
              : GameState.GameStatus.BLACK_WON;
      gameState = gameState.with(status);
    }
  }

  /**
   * Applies the move, checks winning conditions, and updates the game status. If the game is still
   * running, i.e. no player has won and there exists possible moves, starts a new round.
   *
   * @param id the move ID
   * @return OperationStatus.SUCCESS if the selected move is applied; OperationStatus.FAIL if the
   *     game is not running, or the move with the given ID does not exist
   */
  public OperationStatus move(int id) {
    if (!gameState.isGameRunning()) {
      return OperationStatus.FAIL;
    }
    if (id < 0 || id >= gameState.getSelectedPiece().getPossibleMoves().size()) {
      return OperationStatus.FAIL;
    }
    Move move = gameState.getSelectedPiece().getPossibleMoves().get(id);
    gameState.applyMove(move);

    checkWinningConditions(move);

    if (gameState.isGameRunning()) {
      startNewRound();
    }

    return OperationStatus.SUCCESS;
  }

  /**
   * Pass the current round and starts a new round.
   *
   * @return OperationStatus.FAIL if the game is not running; OperationStatus.SUCCESS otherwise
   */
  public OperationStatus pass() {
    if (!gameState.isGameRunning()) {
      return OperationStatus.FAIL;
    }
    startNewRound();
    assert !gameState.hasSelectedPiece();
    assert gameState.isGameRunning();
    return OperationStatus.SUCCESS;
  }
}
