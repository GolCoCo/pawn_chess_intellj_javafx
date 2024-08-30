package bauernschach.view;

import bauernschach.model.Bauernschach;
import bauernschach.model.GameState;
import bauernschach.model.Bauernschach.OperationStatus;
import bauernschach.model.board.ChessBoard;
import bauernschach.model.board.ChessPiece;
import bauernschach.model.board.Coordinate;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


/** This class represents the playing field of the {@link Bauernschach} game. */
public final class ChessBoardView extends GridPane {

	/** PrintMode. */
  public enum PrintMode {
	  /** A PIECE SELECT. */
	    PIECE_SELECT,
	  /** A MOVE SELECT. */
	    MOVE_SELECT,
	  /** PLAIN. */
	    PLAIN
	  }
  private final int numRows;
  private final int numCols;
  private final CellView[][] board;
  private static final char ROW_START_CHAR = 'A';
  private static final int COLUMN_DISPLAY_OFFSET = 1;
  private static final String SELECT_PIECE_MESSAGE = "Please select a chess piece.";
  private static final String SELECT_MOVE_MESSAGE = "Please select a move.";
  private Bauernschach game;
  private ChessPane mainpane;
	/** PrintMode. */
  public PrintMode printMode;

  /**
   * Contructs a chess board with the given dimension.
   *
   * @param numRows the number of rows of the chess board
   * @param numCols the number of columns of the chess board
   * @param game Bauernschach Game Instance
   * @param mainpane MainPain as ChessPane
   */
  public ChessBoardView(int numRows, int numCols, Bauernschach game, ChessPane mainpane) {
    if (numRows == 0 || numCols == 0) {
      throw new IllegalArgumentException("Chess board dimensions cannot be 0.");
    }
    this.mainpane = mainpane;
    this.game = game;
	printMode = PrintMode.PIECE_SELECT;
    this.numRows = numRows;
    this.numCols = numCols;
    board = new CellView[numRows][numCols];
    
    for (int row = 0; row < 8; row++) {
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        rc.setMinHeight(90);
        rc.setMaxHeight(90);
        rc.setFillHeight(true);
        this.getRowConstraints().add(rc);
    }

    for (int col = 0; col < 8; col++) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setMinWidth(90);
        cc.setMaxWidth(90);
        cc.setFillWidth(true);
        this.getColumnConstraints().add(cc);
    }
    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
    	CellView cell;
        Coordinate coordinate = Coordinate.of(i, j);
        ChessPiece piece = game.getGameState().getChessBoard().GetCurrentBoard()[i][j];
        cell = new CellView(coordinate);
        cell.addPiece(piece);
        board[coordinate.getRow()][coordinate.getColumn()] = cell;
        cell.setMinSize(90,90);
        cell.setOnMouseClicked(mouseEvent -> {
			CellView clickedCell = (CellView) mouseEvent.getSource();
			System.out.print(""+ clickedCell.coordinate.getRow() + ":" + clickedCell.coordinate.getColumn()+"\n");

			if(printMode == PrintMode.PIECE_SELECT) {
				handleSelectCommand(clickedCell);
			}else if(printMode == PrintMode.MOVE_SELECT) {
				handleMoveCommand(clickedCell);
			}else {

			}
			printCurrentChessBoardView();
		});
        this.add(cell, j, i);
      }
    }
    this.setGridLinesVisible(true);
  }
  
  /**
   * Handler when select the piece
   */
  private void handleSelectCommand(CellView clickedCell) {
	    if(clickedCell.getOccupyingPiece().getColor() != game.getGameState().getCurrentRound()) {
	    	return;
	    }
	    if(printMode == PrintMode.PIECE_SELECT) {
	    	if(clickedCell.getOccupyingPiece().isNone()) return;
	    }

	    if (!isGameRunning()) {
	      return;
	    }
	    
	    int id =clickedCell.getOccupyingPiece().getId();
	    
	    OperationStatus status = game.selectPieceById(id);
	    if (status == OperationStatus.SUCCESS) {
		    clickedCell.selectedCellColor();
		    printMode = PrintMode.MOVE_SELECT;

	    }else if(status == OperationStatus.SAME_FAIL) {
	    	if(OperationStatus.SUCCESS==game.deselectPiece()) {
	    		RefreshPossibleCells();	
	  	        printMode = PrintMode.PIECE_SELECT;
	    	}
	    } 
	    else {
	      displayError("Error");
	    }
  }
  
  /**
   * Handler when move the piece
   */
  private void handleMoveCommand(CellView clickedCell) {

    if (!isGameRunning()) {
      return;
    }
	
	ChessPiece selectedChessPiece = game.getGameState().GetSelectedChessPiece();

	if(selectedChessPiece.getCoordinate().equals(clickedCell.getOccupyingPiece().getCoordinate())) {
    	if(OperationStatus.SUCCESS==game.deselectPiece()) {
    		RefreshPossibleCells();	
  	        printMode = PrintMode.PIECE_SELECT;
  	        return;
    	}
	}
	
	int id = selectedChessPiece.getPossibleMoveCoordinates().indexOf(clickedCell.coordinate);
	if(id==-1) return;
	
    OperationStatus operationStatus = game.move(id);
    if (operationStatus == OperationStatus.FAIL) {
      return;
    }
    
    UpdateChessBoardCells();	
	
	if (game.getGameState().isGameRunning()) {
	    printMode = PrintMode.PIECE_SELECT;
	} else {
		printMode = PrintMode.PLAIN;
	}
    printCurrentChessBoardView();

    if (!game.getGameState().isGameRunning()) {
      game = null;
    }
  }
  
  /**
   * Handler when click Pass Button
   */
  public void handlePassCommand() {
    if (!isGameRunning()) {
      return;
    }

    game.pass();
    printMode = PrintMode.PIECE_SELECT;
    printCurrentChessBoardView();
  }
  
  /**
   * Update All CellViews in the ChessBoard with Updated GameState
   */
  private void UpdateChessBoardCells() {
	this.setGridLinesVisible(false);
	this.getChildren().clear();
    for (int i = 0; i < numRows; ++i) {
      for (int j = 0; j < numCols; ++j) {
    	CellView cell;
        Coordinate coordinate = Coordinate.of(i, j);
        ChessPiece piece = game.getGameState().getChessBoard().GetCurrentBoard()[i][j];
        cell = new CellView(coordinate);
        cell.addPiece(piece);
        board[coordinate.getRow()][coordinate.getColumn()] = cell;
        cell.setMinSize(90,90);
		  cell.setOnMouseClicked(mouseEvent -> {
			  CellView clickedCell = (CellView) mouseEvent.getSource();
			  System.out.print(""+ clickedCell.coordinate.getRow() + ":" + clickedCell.coordinate.getColumn()+"\n");

			  if(printMode == PrintMode.PIECE_SELECT) {
				  handleSelectCommand(clickedCell);
			  }else if(printMode == PrintMode.MOVE_SELECT) {
				  handleMoveCommand(clickedCell);
			  }else {

			  }
			  printCurrentChessBoardView();
		  });
        this.add(cell, j, i);
      }
    }
    
    this.setGridLinesVisible(true);
  }
  
  /**
   * Refresh CellView Button with Original Color
   */
  private void RefreshPossibleCells() {
	  
    for (int i = 0; i < numRows; ++i) {
        for (int j = 0; j < numCols; ++j) {
          board[i][j].resetColor();
      }
    }
    
  } 
  /**
   * print Current ChessBoard View
   */
  private void printCurrentChessBoardView() {

	    if(game==null) return;
	    final GameState gameState = game.getGameState();
	    final GameState.GameStatus gameStatus = gameState.getStatus();
	    final ChessBoard ChessBoardView = gameState.getChessBoard();

	    final int numRows = ChessBoardView.getNumRows();
	    final int numColumns = ChessBoardView.getNumColumns();

	    // print column index (1~8)
	    StringBuilder line = new StringBuilder("  ");
	    for (int colNum = 0; colNum < numColumns; ++colNum) {
	      line.append(" ").append(colNum + COLUMN_DISPLAY_OFFSET);
	    }
	    System.out.println(line);

	    // print each row with row index (A~Z)
	    for (int rowNum = 0; rowNum < numRows; ++rowNum) {
	      line = new StringBuilder(" ");
	      line.append((char) (rowNum + ROW_START_CHAR));
	      for (int colNum = 0; colNum < numColumns; ++colNum) {
	        Coordinate coord = Coordinate.of(rowNum, colNum);
	        line.append(" ");
	        line.append(chessPieceToString(coord, gameState));
	      }
	      System.out.println(line);
	    }

	    if (gameStatus == GameState.GameStatus.ONGOING) {
	      System.out.println("Current round: " + gameState.getCurrentRound());
	    } else if (gameStatus == GameState.GameStatus.WHITE_WON) {
//	    	mainpane.winStage.hide();
	    	GameOver pane = new GameOver("WHITE", mainpane.winStage);
	    	pane.Show();
	      System.out.println("Player WHITE wins!");
	    } else if (gameStatus == GameState.GameStatus.BLACK_WON) {
	    	mainpane.winStage.hide();
	    	GameOver pane = new GameOver("BLACK", mainpane.winStage);
	    	pane.Show();
	      System.out.println("Player BLACK wins!");
	    } else { // gameStatus == GameStatus.DRAW
	      System.out.println("No possible move left. Draw!");
	    }

	    if (printMode == PrintMode.PIECE_SELECT) {
	      System.out.println(SELECT_PIECE_MESSAGE);
	    } else if (printMode == PrintMode.MOVE_SELECT) {
	      System.out.println(SELECT_MOVE_MESSAGE);
	    }
	    mainpane.lblPlayer.setText("Current Player : " + game.getGameState().getCurrentRound());
	  }

	  private String pieceColorToString(ChessPiece.Color color) {
	    return (color == ChessPiece.Color.WHITE) ? "W" : "B";
	  }

	  private String chessPieceToString(Coordinate coord, GameState gameState) {
	    ChessPiece piece = gameState.getChessBoard().getPieceAt(coord);
	    if (printMode == PrintMode.PIECE_SELECT) {
	      if (piece.isNone()) {
	        return ".";
	      } else if (piece.getColor() == gameState.getCurrentRound()) {
	        if (piece.hasPossibleMoves()) {
	          return String.valueOf(piece.getId());
	        } else {
	          return pieceColorToString(piece.getColor());
	        }
	      } else { // piece.getColor() == currentRound
	        return pieceColorToString(piece.getColor());
	      }
	    } else if (printMode == PrintMode.MOVE_SELECT) {
	      if (piece.equals(gameState.getSelectedPiece())) {
	        return "*";
	      } else if (gameState.getSelectedPiece().getPossibleMoveCoordinates().contains(coord)) {
	        final int i = gameState.getSelectedPiece().getPossibleMoveCoordinates().indexOf(coord);
	        return String.valueOf(i);
	      } else if (!piece.isNone()) {
	        return pieceColorToString(piece.getColor());
	      } else {
	        return ".";
	      }
	    } else {
	      if (piece.isNone()) {
	        return ".";
	      } else {
	        return pieceColorToString(piece.getColor());
	      }
	    }
	  }

	  private void displayError(String message) {
	    System.out.println("Error! " + message);
	  }

	  private boolean isGameRunning() {
	    return game != null;
	  }
}
