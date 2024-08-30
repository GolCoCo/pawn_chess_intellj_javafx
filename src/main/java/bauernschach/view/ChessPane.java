package bauernschach.view;

import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import bauernschach.model.Bauernschach;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;

/** This class represents the ChessPane of the {@link Bauernschach} game. */
public class ChessPane {
	
	/** members of the class ChessPane */
    public Stage winStage;
    private Scene scene;
    private Bauernschach game;
	/** lblPlayer Label to show current status*/
    public Label lblPlayer;
    
    /**
     * Contructs a ChessPane.
     *
     * @param winStage Stage of ChessPane
     */
	public ChessPane(Stage winStage) {
		game = new Bauernschach();
		this.winStage = winStage;
		Initialize();
		winStage.setOnCloseRequest( event -> {System.out.println("Closing Stage");} );
	}
	
	/** initialize GUI with hBox and vBox, Button, Label */
	private void Initialize() {
		HBox hBox = new HBox();
		hBox.setFillHeight(true);
		hBox.setMinWidth(900);
		
		VBox vBox = new VBox();
		vBox.setStyle("-fx-background-color:white;");
		hBox.setFillHeight(true);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setMinWidth(250);
		vBox.setStyle("-fx-background-color:white;-fx-border-color: #D3D3FF; -fx-border-width:1;-fx-border-style: solid;-fx-start-margin:10px");
		
	    Text txtTitle = new Text("Pawn Chess");
	    txtTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	    
	    lblPlayer = new Label("Current Player: BLACK");
	    lblPlayer.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
	    
	    Label lblSpace = new Label("");
	    lblSpace.setMinHeight(550);
	    
	    ChessBoardView chessboard = new ChessBoardView(8,8,game, this);
	    Button btnPassGame = new Button("Pass");
	    btnPassGame.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	    btnPassGame.setMinWidth(140);
	    btnPassGame.setMinHeight(30);
	    btnPassGame.setCursor(Cursor.HAND);
	    btnPassGame.setOnAction((event)->{
	    	chessboard.handlePassCommand();
	    });
	        
	    vBox.getChildren().add(txtTitle);
	    vBox.getChildren().add(lblPlayer);
	    vBox.getChildren().add(lblSpace);
	    vBox.getChildren().add(btnPassGame);
	    vBox.setAlignment(Pos.TOP_CENTER);
	    vBox.setSpacing(20);
	    

		hBox.getChildren().add(chessboard);
		hBox.getChildren().add(vBox);
		
        scene = new Scene(hBox, 970, 722);
		winStage = new Stage();
		winStage.setResizable(false);
		winStage.initModality(Modality.APPLICATION_MODAL);
        winStage.setTitle("Pawn Chess");
        winStage.setScene(scene);
	}
	
	/** show chess pane */
	public void ShowChessPane() {
        winStage.showAndWait();
	}
	
}
