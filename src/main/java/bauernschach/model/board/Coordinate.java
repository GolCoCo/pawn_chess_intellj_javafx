package bauernschach.model.board;

import java.util.Objects;

/** Coordinate on a {@link Coordinate}. */
public final class Coordinate implements Comparable<Coordinate> {
  private final int row;
  private final int column;

  private Coordinate(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /**
   * Creates a Coordinate instance with the given position.
   *
   * @param row row of the coordinate
   * @param column column of the coordinate
   * @return coordinate of the given row and column
   */
  public static Coordinate of(int row, int column) {
    return new Coordinate(row, column);
  }

  /**
   * Returns the row coordinate.
   *
   * @return row coordinate
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column coordinate.
   *
   * @return column coordinate
   */
  public int getColumn() {
    return column;
  }

  @Override
  public int compareTo(Coordinate coordinate) {
    int rowDiff = getRow() - coordinate.getRow();
    if (rowDiff != 0) {
      return rowDiff;
    }
    return getColumn() - coordinate.getColumn();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return row == that.row && column == that.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }

  @Override
  public String toString() {
    return "Coordinate{" + "row=" + row + ", column=" + column + '}';
  }
}
