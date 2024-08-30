package bauernschach.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

import bauernschach.model.Bauernschach;
import bauernschach.model.board.ChessPiece;
import bauernschach.model.board.Coordinate;

/** This class represents the CellView of the {@link Bauernschach} game. */
public class CellView extends Button{

    /**
     * Is Occupied Piece
     */
	public boolean occupied;
    /**
     * Occupied Piece
     */
	public ChessPiece occupyingPiece;
    /**
     * Coordinate
     */
    public Coordinate coordinate;

    /**
     * Contructs a CellView with the given dimension.
     *
     * @param coordinate Coordinate
     */
    public CellView(Coordinate coordinate){
        this.coordinate = coordinate;
        if((coordinate.getRow()+coordinate.getColumn())%2==0) {
            this.setBackground(new Background( new BackgroundFill(WHITE,null,null)));;
        }
        else {
            this.setBackground(new Background( new BackgroundFill(Color.STEELBLUE,null,null)));;
        }
        occupyingPiece = null;
        occupied = false;
    }
    /**
     * Reset color of CellView of white and black
     */
    public void resetColor(){
        if((coordinate.getRow()+coordinate.getColumn())%2==0) {
            this.setBackground(new Background( new BackgroundFill(WHITE,null,null)));;
        }
        else {
            this.setBackground(new Background( new BackgroundFill(STEELBLUE,null,null)));;
        }
    }
    /**
     * set light green color for selected cell 
     */
    public void selectedCellColor() {
        this.setBackground(new Background( new BackgroundFill(LIGHTGREEN,null,null)));
    }
    /**
     * Returns the ChessPiece.
     *
     * @return ChessPiece
     */
    public ChessPiece getOccupyingPiece() {
        return occupyingPiece;
    }
    /**
     * Add chess piece in the CellView
     *
     * @param piece piece
     */
    public void addPiece(ChessPiece piece) {
        this.occupyingPiece = piece;
        if(!piece.isNone()) {
            this.occupied = true;
            this.setImage(occupyingPiece.getColor() == ChessPiece.Color.WHITE ? "white":"black");
        }
    }
    /**
     * Set image in the ImageView by the color.
     *
     * @param color player color
     */
    public void setImage(String color) {
    	if(color==null) {
            this.setGraphic(null);
            return;
    	}
	    ImageView img = new ImageView(""+color+".png");
        this.setGraphic(img);
    }
}
