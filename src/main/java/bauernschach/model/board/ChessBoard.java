package bauernschach.model.board;

import bauernschach.model.Bauernschach;
import bauernschach.model.board.ChessPiece.Color;
import java.util.ArrayList;
import java.util.List;

/** This class represents the playing field of the {@link Bauernschach} game. */
public final class ChessBoard {
  private final int numRows;
  private final int numCols;
  private final ChessPiece[][] board;
  private final List<ChessPiece> whiteChessPieces;
  private final List<ChessPiece> blackChessPieces;

  /** Array for ChessPiece
   * @return 2D Array of ChessPiece
   */
  public ChessPiece[][] GetCurrentBoard(){
	  return board;
  }

  /**
   * Contructs a chess board with the given dimension.
   *
   * @param numRows the number of rows of the chess board
   * @param numCols the number of columns of the chess board
   */
  public ChessBoard(int numRows, int numCols) {
    if (numRows == 0 || numCols == 0) {
      throw new IllegalArgumentException("Chess board dimensions cannot be 0.");
    }

    this.numRows = numRows;
    this.numCols = numCols;
    board = new ChessPiece[numRows][numCols];
    whiteChessPieces = new ArrayList<>();
    blackChessPieces = new ArrayList<>();

    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
        Coordinate coordinate = Coordinate.of(i, j);
        if (i == getStartRowByColor(Color.WHITE)) {
          ChessPiece piece = ChessPiece.create(Color.WHITE, whiteChessPieces.size(), coordinate);
          whiteChessPieces.add(piece);
          setPieceAt(piece, coordinate);
        } else if (i == getStartRowByColor(Color.BLACK)) {
          ChessPiece piece = ChessPiece.create(Color.BLACK, blackChessPieces.size(), coordinate);
          blackChessPieces.add(piece);
          setPieceAt(piece, coordinate);
        } else { // no chess initially
          setPieceAt(ChessPiece.NONE, coordinate);
        }
      }
    }
  }

  private ChessBoard(ChessBoard sourceBoard) {
    numRows = sourceBoard.getNumRows();
    numCols = sourceBoard.getNumColumns();
    board = new ChessPiece[numRows][numCols];
    List<ChessPiece> clonedWhitePieceList = new ArrayList<>();
    List<ChessPiece> clonedBlackPieceList = new ArrayList<>();

    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
        Coordinate coordinate = Coordinate.of(i, j);
        ChessPiece clonedPiece = sourceBoard.getPieceAt(coordinate).copyOf();
        setPieceAt(clonedPiece, coordinate);
        if (clonedPiece.getColor() == Color.WHITE) {
          clonedWhitePieceList.add(clonedPiece);
        } else if (clonedPiece.getColor() == Color.BLACK) {
          clonedBlackPieceList.add(clonedPiece);
        }
      }
    }

    whiteChessPieces = List.copyOf(clonedWhitePieceList);
    blackChessPieces = List.copyOf(clonedBlackPieceList);
  }

  /**
   * Creates a new deep copy of this chess board. The returned board will have a state equal to this
   * board's state, but with separate instances of chess pieces.
   *
   * @return a deep copy of this chess board.
   */
  public ChessBoard copyOf() {
    return new ChessBoard(this);
  }

  /**
   * Places the piece at the given position.
   *
   * @param piece the chess piece to set
   * @param coordinate the coordinate to set the chess piece at
   */
  private void setPieceAt(ChessPiece piece, Coordinate coordinate) {
    assert piece.isNone() || (piece.getCoordinate().equals(coordinate));
    board[coordinate.getRow()][coordinate.getColumn()] = piece;
  }

  /**
   * Gets the piece at the given position.
   *
   * @param coordinate coordinate to get the piece from
   * @return piece at the givne position
   */
  public ChessPiece getPieceAt(Coordinate coordinate) {
    return board[coordinate.getRow()][coordinate.getColumn()];
  }

  /**
   * Gets the number of rows of the board.
   *
   * @return number of rows
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * Gets the number of columns of the board.
   *
   * @return number of columns
   */
  public int getNumColumns() {
    return numCols;
  }

  /**
   * Returns whether the given coordinate is with the board's bounds.
   *
   * @param coordinate coordinate to check
   * @return true if the given coordinate is within the board's bounds. false otherwise
   */
  boolean isPositionWithinBounds(Coordinate coordinate) {
    final int row = coordinate.getRow();
    final int column = coordinate.getColumn();
    return (row >= 0) && (row < getNumRows()) && (column >= 0) && (column < getNumColumns());
  }

  /** Returns whether there is a piece placed at the given position. */
  boolean hasPieceAt(Coordinate coordinate) {
    return getPieceAt(coordinate) != ChessPiece.NONE;
  }

  /** Returns whether there is an opposing piece placed at the given position. */
  boolean hasOpposingPieceAt(Coordinate coordinate, Color color) {
    return hasPieceAt(coordinate) && getPieceAt(coordinate).getColor() != color;
  }

  /** Returns the starting row of the given color. */
  int getStartRowByColor(Color color) {
    return (color == Color.WHITE) ? 0 : (getNumRows() - 1);
  }

  /**
   * Returns the finishing row of the given player color.
   *
   * @param color color to get finishing row for
   * @return finishing row of the given player color on this chess board.
   */
  public int getFinishRowByColor(Color color) {
    return getStartRowByColor(color.getOpposingColor());
  }

  /** Returns the list of remaining chess pieces with the given color. */
  private List<ChessPiece> getPieceListByColor(Color color) {
    return (color == Color.WHITE) ? whiteChessPieces : blackChessPieces;
  }

  /**
   * Returns the remaining chess pieces with the given color.
   *
   * @param color color to get pieces of
   * @return the remaining chess pieces with the given color
   */
  public List<ChessPiece> getImmutablePieceListByColor(Color color) {
    List<ChessPiece> clonedList = new ArrayList<>();
    for (ChessPiece piece : getPieceListByColor(color)) {
      clonedList.add(piece.copyOf());
    }
    return List.copyOf(clonedList);
  }

  /**
   * Applies the given move to the given chess piece.
   *
   * @param piece the piece to move
   * @param move the move to make with the piece
   */
  public void applyMove(ChessPiece piece, Move move) {
    if (move.getMoveType() == Move.MoveType.CAPTURE) {
      ChessPiece capturedPiece = move.getCapturedPiece();
      removePiece(capturedPiece);
    }
    movePiece(piece, move.getNewCoordinate());
  }

  /** Moves the piece to the new position. */
  private void movePiece(ChessPiece piece, Coordinate newCoordinate) {
    assert !piece.getCoordinate().equals(newCoordinate);
    removePiece(piece);
    ChessPiece movedPiece = piece.withNewPosition(newCoordinate);
    setPieceAt(movedPiece, newCoordinate);
    getPieceListByColor(movedPiece.getColor()).add(movedPiece);
  }

  /** Removes the piece from the chess board. */
  private void removePiece(ChessPiece piece) {
    assert !piece.isNone();
    setPieceAt(ChessPiece.NONE, piece.getCoordinate());
    boolean wasRemoved = getPieceListByColor(piece.getColor()).remove(piece);
    assert wasRemoved;
  }

  /**
   * Update the possible moves of the chess pieces with the given color.
   *
   * @param color player color to sync possible moves for
   */
  public void updatePossibleMovesByColor(Color color) {
    for (ChessPiece piece : getPieceListByColor(color)) {
      piece.updatePossibleMoves(this);
    }
  }

  /**
   * Returns whether there is any possible move for the player of the given color.
   *
   * @param color player to check
   * @return true if there is a possible move for the player of the given color. false otherwise
   */
  public boolean hasPossibleMovesByColor(Color color) {
    for (ChessPiece piece : getImmutablePieceListByColor(color)) {
      if (piece.hasPossibleMoves()) {
        return true;
      }
    }
    return false;
  }
}
