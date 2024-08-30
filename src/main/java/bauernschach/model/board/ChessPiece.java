package bauernschach.model.board;

import bauernschach.model.Bauernschach;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** This class represents the chess piece of a {@link Bauernschach} game. */
public final class ChessPiece {
  /** Represents the color of the piece. */
  public enum Color {
    /** Color black. */
    BLACK {
      @Override
      public Color getOpposingColor() {
        return WHITE;
      }
    },
    /** Color white. */
    WHITE {
      @Override
      public Color getOpposingColor() {
        return BLACK;
      }
    };

    /**
     * Returns the color that opposes this color. For white, this is black. For black, this is
     * white.
     *
     * @return the opposing color of this color
     */
    public abstract Color getOpposingColor();
  }

  private final Color color;
  private final int id;
  private final Coordinate coordinate;
  /** possibleMoves */
  public final List<Move> possibleMoves;

  /** Placeholder chess piece that represents no real piece. */
  public static final ChessPiece NONE = new ChessPiece(null, -1, Coordinate.of(-1, -1), List.of());

  /**
   * Creates a new chess piece.
   *
   * @param color color of the chess piece
   * @param id the number of the chess piece. Per player this must be unique on the board
   * @param coordinate position of the chess piece on the board
   * @return a new chess piece with the given attributes
   */
  public static ChessPiece create(Color color, int id, Coordinate coordinate) {
    return new ChessPiece(color, id, coordinate, new ArrayList<>());
  }

  private ChessPiece(Color color, int id, Coordinate coordinate, List<Move> possibleMoves) {
    this.color = color;
    this.id = id;
    this.coordinate = coordinate;
    this.possibleMoves = possibleMoves;
  }

  /**
   * Returns a clone of this chess piece.
   *
   * @return a clone of this chess piece
   */
  public ChessPiece copyOf() {
    if (isNone()) {
      return this;
    }
    return new ChessPiece(color, id, coordinate, List.copyOf(possibleMoves));
  }

  /**
   * Creates a new instance of ChessPiece where Coordinate is as given and with empty possibile
   * moves, other members are as in this instance.
   *
   * @param coordinate the coordinate the returned copy should have
   * @return a copy of this chess piece with a different coordinate
   */
  public ChessPiece withNewPosition(Coordinate coordinate) {
    assert isValid();
    return new ChessPiece(color, id, coordinate, new ArrayList<>());
  }

  /**
   * Update the possible moves of this piece.
   *
   * @param ChessBoard1 chess board state to use as the reference for possible moves
   */
  public void updatePossibleMoves(ChessBoard ChessBoard1) {
    assert isValid();
    possibleMoves.clear();

    checkForwardMove(ChessBoard1);
    checkCaptureMove(ChessBoard1);
  }

  /**
   * GetPossibleMoves()
   * @return List of Move
   */
  public List<Move> GetPossibleMoves(){
	  return possibleMoves;
  }

  /**
   * Check ForwardMove
   *
   * @param ChessBoard1 chess board state to use as the reference for possible moves
   */
  private void checkForwardMove(ChessBoard ChessBoard1) {
    final int rowDirection = (color == Color.WHITE) ? 1 : -1;
    final int column = coordinate.getColumn();
    final int numberOfSteps;
    final int startRow = ChessBoard1.getStartRowByColor(color);
    if (coordinate.getRow() == startRow) {
      numberOfSteps = 2;
    } else {
      numberOfSteps = 1;
    }

    for (int forwardStep = 1; forwardStep <= numberOfSteps; ++forwardStep) {
      final int newRow = coordinate.getRow() + rowDirection * forwardStep;
      Coordinate newCoordinate = Coordinate.of(newRow, column);
      // check if the new position is within bounds and not occupied
      if (ChessBoard1.isPositionWithinBounds(newCoordinate)
          && !ChessBoard1.hasPieceAt(newCoordinate)) {
        possibleMoves.add(Move.newForwardMove(newCoordinate));
      } else {
        // if 1 step forward is not possible, then more steps is not possible either
        break;
      }
    }
  }

  /**
   * check CaptureMove
   *
   * @param ChessBoard1 chess board state to use as the reference for possible moves
   */
  private void checkCaptureMove(ChessBoard ChessBoard1) {
    final int rowDirection = (color == Color.WHITE) ? 1 : -1;
    final int opponentRow = coordinate.getRow() + rowDirection;

    // check the possiblity of capture move in right/left directions
    for (int columnDirection : List.of(-1, 1)) {
      final int opponentColumn = coordinate.getColumn() + columnDirection;
      final Coordinate opponentCoordinate = Coordinate.of(opponentRow, opponentColumn);
      if (ChessBoard1.isPositionWithinBounds(opponentCoordinate)
          && ChessBoard1.hasOpposingPieceAt(opponentCoordinate, color)) {
        ChessPiece capturedPiece = ChessBoard1.getPieceAt(opponentCoordinate);
        possibleMoves.add(Move.newCaptureMove(opponentCoordinate, capturedPiece));
      }
    }
  }

  /**
   * Returns whether the piece has any possible moves.
   *
   * @return true if this chess piece as at least one possible move. false otherwise
   */
  public boolean hasPossibleMoves() {
    return !possibleMoves.isEmpty();
  }

  /**
   * Returns the color of this chess piece.
   *
   * @return the color of this chess piece
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the ID of this chess piece. The ID is a number that is unique for this player's chess
   * pieces.
   *
   * @return the ID of this chess piece
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the coordinate of this chess piece.
   *
   * @return the coordinate of this chess piece
   */
  public Coordinate getCoordinate() {
    return coordinate;
  }

  private boolean isValid() {
    return (color != null)
        && (id >= 0)
        && (coordinate.getRow() >= 0)
        && (coordinate.getColumn() >= 0);
  }

  /**
   * Returns whether the piece is equal to the global dummy piece, instead of an actual playing
   * piece.
   *
   * @return true if this chess piece is no real piece (the placeholder 'NONE'). false if the chess
   *     piece is an actual piece
   */
  public boolean isNone() {
    return this == NONE;
  }

  /**
   * Gets the list of possible moves.
   *
   * @return the list of possible moves for this chess piece
   */
  public List<Move> getPossibleMoves() {
    return List.copyOf(possibleMoves);
  }

  /**
   * Gets the list of new coordinates after applying one of currently the possible moves.
   *
   * @return list of all the future coordinates of this chess piece that are reachable in a single
   *     move
   */
  public List<Coordinate> getPossibleMoveCoordinates() {
    List<Coordinate> coordList = new ArrayList<>();
    for (Move move : possibleMoves) {
      coordList.add(move.getNewCoordinate());
    }
    return coordList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChessPiece that = (ChessPiece) o;
    return color == that.color && id == that.id && coordinate.equals(that.coordinate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, id, coordinate);
  }

  @Override
  public String toString() {
    return "ChessPiece{" + "color=" + color + ", id=" + id + ", coordinate=" + coordinate + '}';
  }
  
}
