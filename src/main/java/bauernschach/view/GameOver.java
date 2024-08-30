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

/** This class represents the GameOver of the {@link Bauernschach} game. */
public class GameOver {

	/** members of the class ChessPane */
    private Stage winStage;
	/** scene */
    private Scene scene;
	/** winner */
    private String winner;
    
    /**
     * Contructs a ChessPane.
     *
     * @param winStage Stage of ChessPane
	 * @param winner winner
     */
	public GameOver(String winner, Stage winStage) {
		this.winner = winner;
		Initialize();
	}
	/** initialize GUI of GameOver Dialog */
	private void Initialize() {

		VBox vBox = new VBox();
		vBox.setStyle("-fx-background-color:white;");
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setMinWidth(250);
		vBox.setStyle("-fx-background-color:white;-fx-border-color: #D3D3FF; -fx-border-width:1;-fx-border-style: solid;-fx-start-margin:10px");
		
	    Text txtTitle = new Text("Game Over");
	    txtTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	    
	    Label lblPlayer = new Label("Winner : " + winner);
	    lblPlayer.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
	    lblPlayer.setStyle("-fx-foreground-color:red");
	    Label lblSpace = new Label("");
	    lblSpace.setMinHeight(50);	    

	    Button btnStartGame = new Button("Start New Game");
//	    btnStartGame.setStyle("-fx-background-color:red; -fx-foreground-color:white;");
	    btnStartGame.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	    btnStartGame.setMinWidth(140);
	    btnStartGame.setMinHeight(30);
	    btnStartGame.setCursor(Cursor.HAND);
	    btnStartGame.setOnAction((event)->{
	    	winStage.hide();
	    	ChessPane pane = new ChessPane(winStage);
	    	pane.ShowChessPane();
	    });
		vBox.setMinHeight(250);
		vBox.setFillWidth(true);
	    vBox.getChildren().add(txtTitle);
	    vBox.getChildren().add(lblPlayer);
	    vBox.getChildren().add(lblSpace);
	    vBox.getChildren().add(btnStartGame);
	    vBox.setAlignment(Pos.TOP_CENTER);
	    vBox.setSpacing(20);
	    
		
        scene = new Scene(vBox, 400, 250);
		winStage = new Stage();
		winStage.setResizable(false);
		winStage.initModality(Modality.APPLICATION_MODAL);
        winStage.setTitle("Pawn Chess");
        winStage.setScene(scene);
		winStage.setOnCloseRequest( event -> {System.out.println("Closing Stage");} );
	}
	/** Show GUI of GameOver Dialog */
	public void Show() {
        winStage.show();
	}
	
}
