package bauernschach.model.board;

import bauernschach.model.Bauernschach;

/**
 * This class represents the move of a {@link ChessPiece} on a {@link Move} of a {@link
 * Bauernschach} game.
 */
public final class Move {
  /** Represents the types of moves. */
  public enum MoveType {
    /** A forward move. */
    FORWARD,
    /** A capture move. */
    CAPTURE
  }
  /** moveType. */
  final MoveType moveType;
  /** newCoordinate */
  public final Coordinate newCoordinate;
  /** capturedPiece. */
  final ChessPiece capturedPiece;

  /**
   * Contructs a new Move instance that moves the piece forward.
   *
   * @param newCoordinate coordinate after the successful move
   * @return forward move
   */
  public static Move newForwardMove(Coordinate newCoordinate) {
    return new Move(MoveType.FORWARD, newCoordinate, ChessPiece.NONE);
  }

  /**
   * Contructs a new Move instance that captures an opposing piece.
   *
   * @param newCoordinate coordinate after the successful move
   * @param chessPiece chess piece that is captured by this move
   * @return a new capture move with the given data
   */
  public static Move newCaptureMove(Coordinate newCoordinate, ChessPiece chessPiece) {
    return new Move(MoveType.CAPTURE, newCoordinate, chessPiece);
  }

  private Move(MoveType moveType, Coordinate newCoordinate, ChessPiece capturedPiece) {
    this.moveType = moveType;
    this.newCoordinate = newCoordinate;
    this.capturedPiece = capturedPiece;
  }

  /**
   * Returns the type of this move.
   *
   * @return the type of this move
   */
  public MoveType getMoveType() {
    return moveType;
  }

  /**
   * Returns the coordinate this move moves to.
   *
   * @return target coordinate of this move
   */
  public Coordinate getNewCoordinate() {
    return newCoordinate;
  }

  /**
   * Returns the opposing piece that is captured by the move.
   *
   * @return piece that will be captured by this move. {@link ChessPiece#NONE} if no piece will be
   *     captured.
   */
  public ChessPiece getCapturedPiece() {
    return capturedPiece;
  }
}
